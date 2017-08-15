package info.androidhive.uplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Contribution extends AppCompatActivity {
    TextView txtcontributeGrpName;
    EditText editAmount,editAccount;
    Button btnContribute;
    Spinner spnAccount;
    int bankId;
    String groupId;
    String result;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution);
        try {

            toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.newGroupToolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        spnAccount=(Spinner)findViewById(R.id.spnAccount);
        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccount.setAdapter(spnAdapter);
        spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=spnAccount.getSelectedItemPosition();
                if(index==0)
                {
                    bankId=1;
                }
                else if(index==1)
                {
                    bankId=2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bankId=0;
            }
        });
        btnContribute=(Button)findViewById(R.id.btnContribute);
        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Button pressed",Toast.LENGTH_LONG).show();
                ContributeNow();
            }
        });
        editAmount=(EditText)findViewById(R.id.editAmount);
        editAccount=(EditText)findViewById(R.id.editAccount);
        txtcontributeGrpName=(TextView)findViewById(R.id.txtcontributeGrpName);
        Intent intent=getIntent();

        String grpName=intent.getStringExtra("groupName");
         groupId=intent.getStringExtra("groupId");
        txtcontributeGrpName.setText(grpName);
    }
    //function to contribute
    public void ContributeNow()
    {
        String amount=editAmount.getText().toString();
        String account=editAccount.getText().toString();
        //check data to be submitted
        if(amount.length()>=3)
        {
            //change account values
            String accountvalues=returnNewValues(account);
            if(accountvalues.length()==9 && accountvalues!="")
            {

                String memberId="1";
                String method="contribute";
                //display data in a toast
                String finaldata="memberId:"+memberId+"\n GroupId:"+groupId+"\n amount:"+amount+"\n fromPhone:"+account+"\n bankId:"+bankId;
                //
                //create instance of class to contribute
           ContributeWorker contributeWorker=new ContributeWorker(this);
           contributeWorker.execute(method,memberId,groupId,amount,accountvalues,String.valueOf(bankId));
                      Toast.makeText(getApplicationContext(),finaldata,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Enter valid Account",Toast.LENGTH_LONG).show();
                editAccount.setText("");
                editAccount.setFocusable(true);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Enter Valid Amount",Toast.LENGTH_LONG).show();
            editAmount.setFocusable(true);
            editAmount.setText("");
        }
        Toast.makeText(getApplicationContext(),returnNewValues(account),Toast.LENGTH_LONG).show();
    }
    //function to remove number infront of seven
    public String returnNewValues(String oldValues)
    {
        String s=  oldValues;
        String news=s.substring(s.indexOf("7"));
        return news;
    }
    public void sendRequest()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        toast.show();
                    }
                });
            }
        },0,10000);
    }
}
