package info.androidhive.uplus.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.uplus.*;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AddGroup extends AppCompatActivity {
    private static final int GALLERY_REQUEST=1;
    BottomSheetDialog dialog;
    private Uri mImageUri;
    ProgressDialog progress;
    private Bitmap bitmap;
    private String uploadURL="http://104.236.26.9/api/group.php";
    Spinner spnAmount,spnPerPerson,spnCollection;
    Boolean state;
    SaveGroupLocal saveGroupLocal;
    EditText txtAMounts,txtPerPersons,txtCollections,txtGroup;
    String image="samuel.jpg";
    String file;
    Button btnAdd,btnWedding,btnParty,btnFundRaising,btnOther;
    Button btn_cancel;
    CircleImageView btnImgGroup;
    android.support.v7.widget.Toolbar toolbar;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
              if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                        return;
                    }
                }

        btnImgGroup=(CircleImageView)findViewById(R.id.btnImgGroup);
        btnImgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
                //init_modal_bottomsheet();
                enable_button();
            }
        });
        try
        {
            toolbar=(Toolbar)findViewById(R.id.newGroupToolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        file="test.jpg";
        init_modal_bottomsheet();
        //CODE TO ADD SPINNER DATA
        final Spinner sp3 = (Spinner) findViewById(R.id.spnPerPerson);
        final EditText txtPerPerson=(EditText)findViewById(R.id.txtPerPerson);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,R.array.PerPerson, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(adp3);
        sp3.setOnItemSelectedListener(new OnItemSelectedListener()
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

        final Spinner sp4 = (Spinner) findViewById(R.id.spnAmount);
        final EditText txtAmount=(EditText)findViewById(R.id.txtAmount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.targetAmount, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp4.setAdapter(adapter);
        sp4.setOnItemSelectedListener(new OnItemSelectedListener()
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

        //variables
        txtAMounts=(EditText)findViewById(R.id.txtAmount);
        txtPerPersons=(EditText)findViewById(R.id.txtPerPerson);
        txtCollections=(EditText)findViewById(R.id.txtAmount);
        txtGroup=(EditText)findViewById(R.id.grpName);
        btnAdd=(Button)findViewById(R.id.btnNext);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGroup();
            }
        });
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK){
            Uri imageUri=data.getData();
           // btnImgGroup.setImageURI(imageUri);
            progress = new ProgressDialog(AddGroup.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type = getMimeType(f.getPath());
                    String file_path = f.getAbsolutePath();
                    showimg(file_path.substring(file_path.lastIndexOf("/") + 1));
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type", content_type)
                            .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                            .build();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(uploadURL)
                            .post(request_body)
                            .build();

                    try {
                        okhttp3.Response response = client.newCall(request).execute();
        // socket timeout

                        if (!response.isSuccessful()) {
                            throw new IOException("Error : " + response);
                        }

                        progress.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
            t.start();
        }
    }
private void showimg(String f)
{
   file=f.toString();
}
public void showf()
{
    Toast.makeText(getApplicationContext(),file,Toast.LENGTH_LONG).show();
}
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

        //END OF IMPLEMEMNTED CODES
    private void enable_button() {

        new MaterialFilePicker()
                .withActivity(AddGroup.this)
                .withRequestCode(10)
                .start();


    }



    //END OF IMPLEMENTED CODES

    public void uploadImage()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, uploadURL,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
                        String resp=response;
                        Toast.makeText(AddGroup.this,resp,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("name",txtGroup.getText().toString().trim());
                params.put("image",imageToString(bitmap));


                return params;
            }
        };
        MySingleton.getInstance(AddGroup.this).addToRequestQueue(stringRequest);
    }
    public String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //function to send data to the background task
    public void AddGroup()
    {
        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
        Boolean boolData;
        String grpName=txtGroup.getText().toString();
        spnAmount=(Spinner)findViewById(R.id.spnAmount);
        String targetType=spnAmount.getSelectedItem().toString();
        String targetAmount=txtAMounts.getText().toString();
        spnPerPerson=(Spinner)findViewById(R.id.spnPerPerson);
        String perPersonType=spnPerPerson.getSelectedItem().toString();
        //spnCollection=(Spinner)findViewById(R.id.spnCollectionAmount);
        String collectionAmount="0784848236";
        String perPerson=txtPerPersons.getText().toString();
        String collection=txtCollections.getText().toString();
        //now pass data to the background Worker
        String sentString;
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
        String errors="";
        String method ="register";
        String adminId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        String bankId ="2";
        String adminPhone ="078755556";
        String getThem="";
        //validate data from the user
        if(grpName.length()<3)
        {
            errors="Please enter valid Group Name";
            boolData=false;
        }
        else if(targetAmount.isEmpty())
        {
            errors="Please Enter Valid Target Amount";
            boolData=false;
        }
        else if(perPerson=="")
        {
            errors="please Enter Valid Per Person Amount";
            boolData=false;
        }
        else
        {
            boolData=true;
        }

        if(boolData==true)
        {
            if(isNetworkAvailable()==false)
            {
                //add data tto local database
                saveGroupLocal=new SaveGroupLocal(this);
                int current=saveGroupLocal.returnCurrentId();
                current=current+1;
                String groupId=String.valueOf(current);
                state=saveGroupLocal.addGroupLocal(groupId,grpName,targetType,targetAmount,perPersonType,perPerson,adminId,adminPhone,bankId,"0");
                if(state)
                {
                    Toast.makeText(getApplicationContext(),"Group Saved to Local Database",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Group Not Added Please check you connecction",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                //showf();
                //check if boolean data is true
                progress = new ProgressDialog(AddGroup.this);
                progress.setTitle("Creating Group");
                progress.setMessage("Please wait...");
                progress.show();
                BackgroundWorker bgWorker=new BackgroundWorker(this,progress);
                bgWorker.execute(method,grpName,sentString,targetAmount,perPersonType,perPerson,adminId,file);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),errors,Toast.LENGTH_SHORT).show();
        }

    }
    //end of function to send data to background task
    public void getBack()
    {
        Intent t=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(t);
    }
    //function to check availability of network

    public void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btnWedding=(Button) modalbottomsheet.findViewById(R.id.btnWedding);
        btnFundRaising=(Button) modalbottomsheet.findViewById(R.id.btnFundRaising);
        btnParty=(Button) modalbottomsheet.findViewById(R.id.btnBDParty);
        btnOther=(Button) modalbottomsheet.findViewById(R.id.btnOther);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnWedding.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });
        btnFundRaising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnFundRaising.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });
        btnParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnParty.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        //txtGroup
    }


}
