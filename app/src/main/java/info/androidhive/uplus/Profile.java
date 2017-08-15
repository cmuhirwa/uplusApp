package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.activity.UpdateProfile;

public class Profile extends AppCompatActivity {
    private static final int GALLERY_REQUEST=1;
    private Uri mImageUri;
    private Bitmap bitmap;
    private String uploadURL="http://104.236.26.9/api/upload.php";
    EditText editText;
    Button btnUpdate;
    String pin,userId,userName;
    ImageButton fabbtnUpload;
    ImageView imageView;
    ProgressDialog progress;
    SharedPrefManager sharedPrefManager;
    String NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get data from intent extras
        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        String userName=intent.getStringExtra("userName");
        String finalData="userId:"+userId+"\n"+"userName:"+userName;
        //Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_LONG).show();
        editText=(EditText)findViewById(R.id.txtProfile);
        editText.setText(userName);

        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });
        imageView=(ImageView)findViewById(R.id.imgProfile);
        fabbtnUpload=(ImageButton)findViewById(R.id.fabbtnUpload);
        fabbtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK)
        {
            Uri imageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void uploadImage()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, uploadURL,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
                            String resp=response;
                        Toast.makeText(Profile.this,resp,Toast.LENGTH_LONG).show();
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

                params.put("name",editText.getText().toString().trim());
                params.put("image",imageToString(bitmap));


                return params;
            }
        };
        MySingleton.getInstance(Profile.this).addToRequestQueue(stringRequest);
    }
    public String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    public void UpdateProfile()
    {
        String txtName=editText.getText().toString();
        if(!TextUtils.isEmpty(txtName) && mImageUri!=null)
        {
            try
            {
                //Toast.makeText(getApplicationContext(),txtName+"\n"+userId,Toast.LENGTH_LONG).show();
                UpdateProfile updateProfile=new UpdateProfile(getApplicationContext(),progress);
                updateProfile.execute("verify",userId,txtName);

                //save user Info
                Intent intent=new Intent(this,HomeActivity.class);
                intent.putExtra("Load","1");
                startActivity(intent);
                SharedPrefManager.getInstance(getApplicationContext()).login(userId,userName);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Choose Profile Image",Toast.LENGTH_LONG).show();
        }

    }
}
