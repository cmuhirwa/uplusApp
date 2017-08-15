package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

import info.androidhive.uplus.activity.BackgroundWorker;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;

public class InviteMember extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    String returnedId;
    EditText txtInvited;
    String user;
    ProgressDialog progress;
    Button btnDOne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        //check if in activity there are some data that come together
        Intent i=getIntent();
        String addedId=i.getStringExtra("addedId");
        if(addedId!="")
        {
            user=addedId;
        }
        else
        {
            Toast.makeText(getApplicationContext(),addedId,Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
            user=sharedPreferences.getString("groupId","");
            Log.i("groupId",user);
        }

        txtInvited=(EditText)findViewById(R.id.txtInvited);
        try {

            toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.newGroupToolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBack();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        btnDOne=(Button)findViewById(R.id.btnDOne);
        btnDOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        }
    //method to invite members
    public void inviteMember(View View)
    {
        String invitePhone=txtInvited.getText().toString();
        String adminId= SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        String method="invite";
        //create instance og backgroundWorker
        //check the dat submit
        String status="";
        if(invitePhone!="")
        {
            if(invitePhone.length()==10)
            {
                progress = new ProgressDialog(InviteMember.this);
                progress.setTitle("Inviting now");
                progress.setMessage("Please wait while we are Inviting user...");
                progress.setCancelable(false);
                progress.show();
                BackgroundWorker bgWorker=new BackgroundWorker(this,progress);
                bgWorker.execute(method,user,adminId,invitePhone);
                status="Your Invitation Sent Successfully";
            }
            else
            {
                status="Please Enter valid Phone Number";
                txtInvited.setFocusable(true);
                txtInvited.setText("");
            }
        }
        else
        {
            status="Please Enter Phone Number of you Invitee";
            txtInvited.setFocusable(true);
            txtInvited.setText("");
        }
        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
    }
    public void goBack()
    {
      try
      {
          Intent newt=new Intent(getApplicationContext(), HomeActivity.class);
          startActivity(newt);
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
    }
    public void getBack()
    {
       finish();
    }
}
