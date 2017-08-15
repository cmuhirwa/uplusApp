package info.androidhive.uplus.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.uplus.ModifyWorker;
import info.androidhive.uplus.R;

public class ModifyGroup extends AppCompatActivity {

    EditText ModGrpName;
     EditText txtPerPerson;
    CircleImageView btnImgGroup;
    Button btnNext;
    String selectedIndex;
    String gName,perAmount,amount;
     Spinner sp4;
     Spinner sp3;
    String gId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group);

        //get group id from Group Details
        ModGrpName = (EditText)findViewById(R.id.modGrpName);
        Intent id = getIntent();
          gId = id.getStringExtra("ModGroupId");
        final String gpName = id.getStringExtra("ModGroupName");
        ModGrpName.setText(gpName);
        if(gId.isEmpty()){
            finish();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        //getting group info for modification


        btnNext = (Button)findViewById(R.id.btnNext);
        btnImgGroup=(CircleImageView)findViewById(R.id.btnImgGroup);
        btnImgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               enable_button();
            }
        });
        //CODE TO ADD SPINNER DATA
                 sp3 = (Spinner) findViewById(R.id.spnPerPerson);
        txtPerPerson=(EditText)findViewById(R.id.perUserAmount);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,R.array.PerPerson, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(adp3);
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String ss = sp3.getSelectedItem().toString();
                String data=parent.getItemAtPosition(position).toString();
                //check the selected field
                int index=sp3.getSelectedItemPosition();
                if(index ==2)
                {

                    //txtPerPerson.setVisibility(View.GONE);
                    txtPerPerson.setEnabled(false);
                    txtPerPerson.setText("");
                    txtPerPerson.setHint("Any");
                    selectedIndex="Any";
                }
                else
                {
                    //txtPerPerson.setVisibility(View.VISIBLE);
                    txtPerPerson.setEnabled(true);
                    txtPerPerson.setFocusable(true);
                    txtPerPerson.setHint("RWF");
                }
                //Toast.makeText(getBaseContext(), "Second :"+ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //END OF CODE TO ADD SPINNER DATA

        //code to add data to the next spinner

         sp4 = (Spinner) findViewById(R.id.spnAmount);
        final EditText txtAmount=(EditText)findViewById(R.id.gAmount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.targetAmount, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp4.setAdapter(adapter);
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                int index=sp4.getSelectedItemPosition();
                String ss = sp4.getSelectedItem().toString();
                String data=parent.getItemAtPosition(position).toString();
                //check the selected field
                if(index ==1)
                {
                    //txtAmount.setVisibility(View.GONE);
                    txtAmount.setEnabled(false);
                    txtAmount.setText("");
                    txtAmount.setHint("Any");

                }
                else if(index==0)
                {
                    //txtAmount.setVisibility(View.VISIBLE);
                    txtAmount.setEnabled(true);
                    txtAmount.setFocusable(true);
                    txtAmount.setHint("RWF");
                }
                //Toast.makeText(getBaseContext(), "Second :"+ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //end of code to add data to the next spinnner

        //When user clicked  on Button validate group info and then Update Group
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 gName = ModGrpName.getText().toString();
                 amount = txtAmount.getText().toString();
                 perAmount = txtPerPerson.getText().toString();

                //check if group field contained valid info
                if(gName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Invalid Group Name", Toast.LENGTH_LONG).show();
                }else if(amount.isEmpty() || Integer.parseInt(amount) == 0){
                    Toast.makeText(getApplicationContext(),"You can Not Set Amount to 0",Toast.LENGTH_LONG).show();
                }else if(perAmount.isEmpty() || Integer.parseInt(perAmount) == 0){
                    Toast.makeText(getApplicationContext(),"You can not set Person Amount to 0",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Group id: "+gId+"Group Name:"+gName+" Group Amount:"+amount+" Amount Per Person:"+perAmount,Toast.LENGTH_LONG).show();
                    modifyGroup();
                }
            }
        });
    }
    //accessing phone storage
    private void enable_button(){
        new MaterialFilePicker()
                .withActivity(ModifyGroup.this)
                .withRequestCode(10)
                .start();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }
    //function to modify group
    public void modifyGroup()
    {
        String targetType=sp4.getSelectedItem().toString();
        String perPersonType=sp4.getSelectedItem().toString();
        String perPerson;
        String sentString;
        String targetAmount;
        switch (targetType)
        {
            case "Exactly":
                sentString="fixed";
                break;
            case "Any":
                targetAmount="0";
            default:
                sentString=targetType;
        }
        switch (perPersonType)
        {
            case "Exactly":
                perPersonType="fixed";
                break;
            case "Any":
                perPersonType="any";
                perPerson="0";
                break;
            case "Atleast":
                perPersonType="min";
        }
        //create instance of class backgroundworker
        ModifyWorker modifyWorker=new ModifyWorker(this);
        modifyWorker.execute("modify",gName,sentString,amount,perPersonType,perAmount,"1",gId);
    }
}
