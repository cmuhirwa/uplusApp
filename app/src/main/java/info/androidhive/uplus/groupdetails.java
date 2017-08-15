package info.androidhive.uplus;


import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.GetMembers;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.ModifyGroup;
import info.androidhive.uplus.activity.RecyclerAdapter;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.fragments.OneFragment;

import static android.R.id.progress;
import static info.androidhive.uplus.R.id.action_Modify;
import static info.androidhive.uplus.R.id.imageView;
import static info.androidhive.uplus.R.id.txtCurrent;


public class groupdetails extends AppCompatActivity {
    SaveMembers saveMembers;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    Intent shareIntent;
     String Name;
    ImageButton btnShare;
    Button btnContribute;
    String holdGroupId;
    String Ids;
    ProgressBar progressBar;
    ProgressDialog progress;
    String Bankid;
    Dialog dialog;
    Spinner spnWithDrawAccount;
    Button btnWithDraw;
    EditText txtWithDrawAmount,txtWithDrawPhone;
    TextView txtError,textView3,txtCurrent;
    ImageView profile_id;
    String url;
    String Amount;
    EditText editAmount,editAccount;
    ArrayList<String> memberAmount=new ArrayList<String>();
    ArrayList<String>memberName=new ArrayList<String>();
    ArrayList<String>memberType=new ArrayList<String>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    public static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupinfo);
        //get extra data passed
        Intent intent=getIntent();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerSam);
        textView3=(TextView)findViewById(R.id.textView3);
        txtCurrent=(TextView)findViewById(R.id.txtCurrent);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        fa=this;
        Ids=intent.getStringExtra("Id");
        Name=intent.getStringExtra("Name");
        Amount=intent.getStringExtra("Amount");

        if(Ids!=null)
        {
            holdGroupId=Ids;
            //showLoader();
            if(isNetworkAvailable()==false)
            {
                Toast.makeText(getApplicationContext(),"No Network available",Toast.LENGTH_LONG).show();
            }
            else
            {
                loopData();
            }
            askForData();
            url="http://104.236.26.9/groupimg/"+Ids+".jpg";
            saveMembers=new SaveMembers(getApplicationContext());
            saveMembers.recreateTable();
            Random rand = new Random();
            int amounts = 0;
            try
            {
                amounts = rand.nextInt(Integer.parseInt(Amount))+1;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


            String Allmoney  = String.valueOf(Amount);
            int exact = Integer.parseInt(Allmoney);
            int amount = Integer.parseInt(Amount);
            exact = rand.nextInt(amount)+1;
            int percentage = (exact * 100) / amount;
            txtCurrent.setText(currencyConverter(Allmoney));
            textView3.setText(currencyConverter(Amount));
            progressBar.setProgress(percentage);
            //saveMembers.fillMemberRecyclerView(Ids,recyclerView,this);
            retrieveMember();
            profile_id=(ImageView)findViewById(R.id.profile_id);

            Toast.makeText(this,Ids,Toast.LENGTH_LONG).show();
            Picasso.with(getApplicationContext())
                    .load(url)
                    .into(profile_id, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
           /// viewData1();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"there was an error",Toast.LENGTH_LONG).show();
        }

        try
        {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(Name);

            dynamicToolbarColor();

            toolbarTextAppernce();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        btnShare=(ImageButton)findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener(){
            String shareBody = "Checkout our contribution group on uplus called ("+Name+"): {{https://mobile.uplus.rw/Linkhere}}";
            @Override
            public void onClick(View view){
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My App");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });
        btnContribute=(Button)findViewById(R.id.btnContribute);
        contributeActivity(btnContribute);
    }

    public String currencyConverter(String data)
    {


        Double value=Double.parseDouble(data);
        DecimalFormat decimalFormat=new DecimalFormat("#,###.00 RWF");
        return(decimalFormat.format(value));
    }
        public void loadImage()
        {
            URL urls = null;
            try {
                urls = new URL(url);
                Bitmap bmp = BitmapFactory.decodeStream(urls.openConnection().getInputStream());
                profile_id.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    public void contributeActivity(Button btn)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
//                Intent intent=new Intent(getApplicationContext(),Contribution.class);
//                intent.putExtra("groupName",Name);
//                intent.putExtra("groupId",Ids);
                contributeNow();
            }
        });
    }
    public void showNotification()
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_action_plus);
        builder.setContentTitle("Uplus Notification");
        builder.setContentText("You are About to Contribute to group :"+Name);

        //get an instance of the NotificationManager service
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001,builder.build());

    }
    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wed);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

            }
        });
    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int clicked=item.getItemId();
        if(clicked==R.id.action_settings){
            try
            {
                Intent intent=new Intent(getApplicationContext(),InviteMember.class);
                intent.putExtra("addedId",holdGroupId);
                startActivity(intent);
            }
            catch (Exception ex)
            {

            }
        }else if(clicked==R.id.action_refresh)
        {
            Intent intent=getIntent();
            finish();
            startActivity(intent);
        }
        else if(clicked==R.id.action_WithDraw)
        {
            WithDraw();
        }else if(clicked == R.id.action_Modify){
            try{
                Intent intent  = new Intent(getApplicationContext(), ModifyGroup.class);
                intent.putExtra("ModGroupName",Name);
                intent.putExtra("ModGroupId",holdGroupId);
                startActivity(intent);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }else if(clicked == R.id.action_exit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Group");
            builder.setMessage("Are you sure you want to exit "+Name+" Group?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                    progress = new ProgressDialog(groupdetails.this);
                    progress.setTitle("Waiting Status");
                    progress.setMessage("Please wait while we are Removing You...");
                    progress.show();
                    //delete from local database
                    ExitGroup exitGroup=new ExitGroup(getApplicationContext(),progress);
                    exitGroup.execute("exit",Ids,userId);
//                    Toast.makeText(getApplicationContext(),"You are Not "+Name+" Member Anymore!",Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Thanks to Be With Us",Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
            AlertDialog exit = builder.create();
            exit.show();
        }
        return super.onOptionsItemSelected(item);
    }
//function to create custom dialog
    public void WithDraw()
    {
        dialog=new Dialog(this);
        dialog.setTitle("WithDraw Money");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.withdraw_layout);
        dialog.show();
        txtError=(TextView)dialog.findViewById(R.id.txtError);

        spnWithDrawAccount= (Spinner) dialog.findViewById(R.id.spnWithdraw);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
        spnWithDrawAccount.setAdapter(adapter);
        spnWithDrawAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=spnWithDrawAccount.getSelectedItemPosition();
                if(index==0)
                {
                    Bankid="1";
                }
                else if(index==1)
                {
                    Bankid="2";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtWithDrawAmount=(EditText)dialog.findViewById(R.id.txtWithDrawAmount);
        txtWithDrawPhone=(EditText)dialog.findViewById(R.id.txtWithDrawPhone);
        btnWithDraw=(Button)dialog.findViewById(R.id.withdrawBtn);
        btnWithDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate data
                String error="";
                String amount=txtWithDrawAmount.getText().toString();
                String phone=txtWithDrawPhone.getText().toString();
                if((Bankid!="" && Bankid!=null))
                {
                    if(amount.length()>=3)
                    {
                        if(phone.length()==10)
                        {
                           String finalData="BankId:"+Bankid+"\n Amount:"+ amount+"\n Phone:"+phone;
                            Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            error="Please enter valid Phone";
                        }
                    }
                    else
                    {
                        error="Please enter valid Amount";
                    }
                }
                else
                {
                    error="Please choose With Account";
                }
                txtError.setText(error);
            }
        });
    }
    public void contributeNow()
    {
      try
      {
          dialog=new Dialog(this);
          dialog.setTitle("WithDraw Money");
          dialog.setCancelable(true);
          dialog.setContentView(R.layout.layout_contribute);
          dialog.show();

          editAmount=(EditText)dialog.findViewById(R.id.editAmount);
          final Spinner spnAccount=(Spinner)dialog.findViewById(R.id.spnAccount);
          ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
          spnAccount.setAdapter(adapter);
          spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  int index=spnAccount.getSelectedItemPosition();

                  if(index==0)
                  {
                      Bankid="1";
                  }
                  else
                  {
                      Bankid="2";
                  }
              }
              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });
          btnContribute=(Button)dialog.findViewById(R.id.btnContribute);
          editAccount=(EditText)dialog.findViewById(R.id.editAccount);

          btnContribute.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String amount=editAmount.getText().toString();
                  String account=editAccount.getText().toString();
                  //validate data
                  if(amount.length()>0 && Integer.parseInt(amount)>100)
                  {
                    if(account.length()==10)
                    {
                       //call contribute Worker
                        String method="contribute";
                        String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                        String finaldata="memberId:"+userId+"\n GroupId:"+Ids+"\n amount:"+amount+"\n fromPhone:"+account+"\n bankId:"+Bankid;
                        Toast.makeText(getApplicationContext(),finaldata,Toast.LENGTH_LONG).show();
                        ContributeWorker contributeWorker=new ContributeWorker(getApplicationContext());

                        contributeWorker.execute(method,userId,Ids,amount,account,Bankid);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please ENter valida Phone",Toast.LENGTH_LONG).show();
                    }
                  }
                  else
                  {
                      Toast.makeText(getApplicationContext(),"Please ENter valida amount",Toast.LENGTH_LONG).show();
                  }

              }
          });
      }
      catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }
    //method to return the firstcolor of every string
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

    //ASK FOR DATA
    public void askForData()
    {
        if(isNetworkAvailable())
        {
            final Intent intent=getIntent();
            progress = new ProgressDialog(groupdetails.this);
            progress.setTitle("Loading Data");
            progress.setMessage("Please wait...");
            progress.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progress.dismiss();
                            retrieveMember();
                        }
                    }, 10000);
        }
    }
    public void loopData()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +" sam",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(isNetworkAvailable())
                        {
                            GetMembers getMembers=new GetMembers(getApplicationContext());
                            getMembers.execute("members",Ids);
                        }
                    }
                });
            }
        },0,5000);
    }
    public void loopLocal()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +" sam",Toast.LENGTH_SHORT );
                        saveMembers=new SaveMembers(getApplicationContext());
                        saveMembers.recreateTable();
//            saveMembers.clearMembers();
                        saveMembers.fillMemberRecyclerView(Ids,recyclerView,getApplicationContext());
                    }
                });
            }
        },0,10000);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showMessage(String title,String Message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public void viewData1()
    {
        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
        try
        {
            SaveMembers saveMembers=new SaveMembers(getApplicationContext());
            Cursor cursor=saveMembers.getAllData(Ids);
            if(cursor.getCount()==0)
            {
                Toast.makeText(getApplicationContext(),"no data",Toast.LENGTH_LONG).show();
                cursor.close();
            }
            StringBuffer buffer=new StringBuffer();

            while(cursor.moveToNext())
            {
                buffer.append("memberName:"+cursor.getString(2)+"\n");
                buffer.append("memberAmount:"+cursor.getString(3)+"\n\n");
                //formatted numers
            }
            cursor.close();
        Toast.makeText(getApplicationContext(),buffer.toString(),Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void showLoader()
    {
        Intent intent=getIntent();

        String Load=intent.getStringExtra("Load");
                final ProgressDialog progress;
                progress = new ProgressDialog(groupdetails.this);
                progress.setTitle("Loading Contents now");
                progress.setMessage("Please wait while we are Downloading Data...");
                progress.setCancelable(false);
                progress.show();
//                BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
//                backgroundTask.execute("add");


    }
    //FUNCTION TO LOAD DATA FROM DATABASE

    public void retrieveMember()
    {
        Cursor cursor=saveMembers.getAllData(Ids);
//        if(memberName.size()>0)
//        {
//            memberName.clear();
//            memberAmount.clear();
//            memberType.clear();
//        }
        while(cursor.moveToNext())
        {
            memberName.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_NAME)));
            memberAmount.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_AMOUNT)));
            memberType.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_TYPE)));
        }
        cursor.close();
        //Create recyclerview adapter
        recyclerView=(RecyclerView)findViewById(R.id.recyclerSam);
        adapter=new RecyclerAdapter(memberName,memberAmount,memberType);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


}

