package info.androidhive.uplus;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.text.format.Time;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.fragments.Chats;

public class UserProfile extends AppCompatActivity {
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> Money=new ArrayList<String>();
    ArrayList<String> TransactionTime=new ArrayList<String>();
    ArrayList<String> Status=new ArrayList<String>();
    private TextView textView1,textView2,userPhone,userMobile;
    final Context context = this;
    private Button btnSend;
    private ImageButton btnCloseProfileDialog;
    private GestureDetectorCompat gestureObject;

    EditText editView,txtAmountToSend,txtFromPhone;
    public Toolbar contactToolbar;
    ImageView imgContact;
    RelativeLayout relativeLayout;
    String name;
    String receiverPhone;
    ProgressDialog progress;
    TransactionDB transactionDB;
    public static Activity fa;
    ImageButton myChatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Bundle bundle = getIntent().getExtras();
        String uname = bundle.getString("user");
        String phones = bundle.getString("phone");
        final String states = bundle.getString("state");
        fa=this;
        recyclerView=(RecyclerView)findViewById(R.id.recyclerTransaction);
        adapter=new TransactionAdapter(getApplicationContext(),Money,TransactionTime,Status);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        textView1 = (TextView)findViewById(R.id.username) ;
        userMobile  =(TextView)findViewById(R.id.txtMobile);
        userPhone = (TextView)findViewById(R.id.userPhone);
        myChatButton = (ImageButton) findViewById(R.id.personChat);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        myChatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               Intent i = new Intent(UserProfile.this, Activity2.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
               // Toast.makeText(getApplicationContext(),"We are going to help you",Toast.LENGTH_LONG).show();
            }
        });
        if(bundle.getString("user") != null){
            name = bundle.getString("user");

            textView1.setText(uname);
            userPhone.setText(phones);
            userMobile.setText(states);
        }
        receiverPhone=removeSpaceInPhone(userPhone.getText().toString());
        transactionDB=new TransactionDB(this);
        transactionDB.RecreateTable();
        getData();
        try
        {
            contactToolbar= (Toolbar) findViewById(R.id.contactToolbar);
            setSupportActionBar(contactToolbar);
            contactToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        relativeLayout=(RelativeLayout)findViewById(R.id.layoutImage);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        contactToolbar.setBackgroundColor(color1);
        TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(name), color1);
//        imgContact.setImageDrawable(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            relativeLayout.setBackground(drawable);
        }
        if(states.equals("TIGO") || states.equals("MTN")){
            btnSend = (Button)findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // custom dialog

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.send_money_dialog);
                    // Custom Android Allert Dialog Title
                    dialog.setTitle("Send Instant Money");
                    //show numbers
                    String number = SharedPrefManager.getInstance(getApplicationContext()).getPhone().toString();
                    final EditText edit  = (EditText)dialog.findViewById(R.id.txtFromPhone);
                    edit.setText(number);
                    edit.setEnabled(true);
                    txtAmountToSend=(EditText)dialog.findViewById(R.id.txtAmountToSend);
                    final String Amount=txtAmountToSend.getText().toString();
//                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.sendMoneyDialogCancel);
                    Button dialogButtonOk = (Button) dialog.findViewById(R.id.sendMoneyDialogWithdraw);
//                btnCloseProfileDialog=(ImageButton)dialog.findViewById(R.id.btnCloseProfileDialog);
//                btnCloseProfileDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }

//                });
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM  HH:mm");
                            String strDate = sdf.format(c.getTime());
                            String BankId="";
                            TransactionTime.add(strDate);
                            Status.add("PENDING");
                            Money.add(txtAmountToSend.getText().toString());
                            adapter.notifyDataSetChanged();
                            if(states.equals("TIGO"))
                            {
                              BankId="2";
                            }
                            else if(states.equals("MTN"))
                            {
                                BankId="1";
                            }
                            //Toast.makeText(getApplicationContext(),SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString(),Toast.LENGTH_LONG).show();
                            String amountTosend=txtAmountToSend.getText().toString();
                            if(txtAmountToSend.getText().toString()!="" && txtAmountToSend.getText().toString()!=null)
                            {
                                if(amountTosend!="0" && returnlenght(txtAmountToSend.getText().toString())>0)
                                {
                                    if(edit.getText().toString()!="" && edit.getText().length()==10)
                                    {
                                        String senderId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                                        String senderName="sam";
                                        String senderPhone=edit.getText().toString();
                                        String receiverName=textView1.getText().toString();
                                        receiverPhone=removeSpaceInPhone(userPhone.getText().toString());
                                        String senderBank=validateMyPhone(senderPhone);

                                        if(isNetworkAvailable())
                                        {
                                            String finalData=amountTosend+"\n"+senderId+"\n"+senderName+"\n"+senderPhone+"\n"+receiverName+
                                                    "\n"+receiverPhone+"\n"+senderBank+"\n"+BankId;
                                            //Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_LONG).show();
                                            //ADD TO QUE REQUEST
                                            dialog.dismiss();
                                            progress = new ProgressDialog(UserProfile.this);
                                            progress.setTitle("Sending Money");
                                            progress.setCancelable(false);
                                            progress.setMessage("Please wait...");
                                            progress.show();
                                            Intent intent=getIntent();
                                            DirectTransFer directTransFer=new DirectTransFer(getApplicationContext(),progress,receiverPhone,amountTosend,dialog,intent);
                                            directTransFer.execute("transfer",amountTosend,senderId,senderName,senderPhone,senderBank,receiverName,receiverPhone,BankId);
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Enter Valid Phone",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Please Enter Valid Amount",Toast.LENGTH_LONG).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Enter Valid Amount",Toast.LENGTH_LONG).show();
                                txtAmountToSend.setFocusable(true);
                                txtAmountToSend.setText("");
                            }


                        }
                    });
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogMoney; //style id
                    dialog.show();

                }

            });
        }else{
            btnSend = (Button)findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.send_money_fail_dialog);
                    // Custom Android Allert Dialog Title
                    dialog.setTitle("Send Instant Money");

//                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.sendMoneyDialogCancel);
                    Button dialogButtonOk = (Button) dialog.findViewById(R.id.btnFail);
//                btnCloseProfileDialog=(ImageButton)dialog.findViewById(R.id.btnCloseProfileDialog);
//                btnCloseProfileDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogMoney; //style id
                    dialog.show();

                }

            });
        }



    }


    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocity){
            if(event2.getX() > event1.getX()){




            }
            else
            if(event2.getX() < event1.getX()){
                Intent i = new Intent(UserProfile.this, Activity2.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
               // Toast.makeText(getApplicationContext(),"Slided left",Toast.LENGTH_LONG).show();

            }
            return  true;
        }
    }






    //FUNCTION TO CHECK IF MY NUMBER IS MTN OR TIGO



    public String validateMyPhone(String data)
    {
        String senderBank="";
        String newd=data.substring(2,3);
        if(Integer.parseInt(newd)==8)
        {
            senderBank="1";
        }
        else if(Integer.parseInt(newd)==2)
        {
            senderBank="2";
        }
        return senderBank;
    }
    public String textTobeConverted(String data)
    {
        String newData="D";
        int length=data.length();
        if(length>0)
        {
            newData=data.substring(0,1);

        }
        return newData;
    }

    //function ot remove all white space between phone numbers
    public String removeSpaceInPhone(String data)
    {
        String lineWithoutSpaces = data.replaceAll("\\s+","");
        return lineWithoutSpaces;
    }
    //function to chck submitted data
    public int returnlenght(String data)
    {
        return data.length();
    }

    //function get data from Data
    public void getData()
    {
        TransactionDB transactionDB=new TransactionDB(this);
        Cursor cursor=transactionDB.retrieveTransactions(receiverPhone);
        while(cursor.moveToNext())
        {
            Money.add(cursor.getString(cursor.getColumnIndex(TransactionContract.TRANSACTION_AMOUNT)));
            TransactionTime.add(cursor.getString(cursor.getColumnIndex(TransactionContract.TRANSACTION_TIME)));
            Status.add(cursor.getString(cursor.getColumnIndex(TransactionContract.STATUS)));
        }
        cursor.close();
        //CREATE GUI CALL
        recyclerView=(RecyclerView)findViewById(R.id.recyclerTransaction);
        adapter=new TransactionAdapter(getApplicationContext(),Money,TransactionTime,Status);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}