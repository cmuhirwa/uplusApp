package info.androidhive.uplus.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import info.androidhive.uplus.CreateGroup;

/**
 * Created by RwandaFab on 6/15/2017.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    ProgressDialog progress;
    AlertDialog alert;
    String result="";
    TextView returnId;
    String type;
    public BackgroundWorker(Context ctx,ProgressDialog progress){
        context=ctx;
       this.progress=progress;
    }
    @Override
    protected String doInBackground(String... params) {
         type=params[0];
        String login_url="http://104.236.26.9/api/index.php";
        String register_url="http://192.168.177.205/android/register.php";
        String data_url="http://192.168.177.205/android/data.php";
        if(type.equals("register")){
            try{
                String action="createGroup";
                String groupName=params[1];
                String groupTargetType=params[2];
                String targetAmount=params[3];
                String perPersonType=params[4];
                String perPerson=params[5];
                String adminId=params[6];
                String groupImage=params[7];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupName","UTF-8")+"="+URLEncoder.encode(groupName,"UTF-8")+"&"
                        +URLEncoder.encode("groupTargetType","UTF-8")+"="+URLEncoder.encode(groupTargetType,"UTF-8")+"&"
                        +URLEncoder.encode("targetAmount","UTF-8")+"="+URLEncoder.encode(targetAmount,"UTF-8")+"&"
                        +URLEncoder.encode("perPersonType","UTF-8")+"="+URLEncoder.encode(perPersonType,"UTF-8")+"&"
                        +URLEncoder.encode("perPerson","UTF-8")+"="+URLEncoder.encode(perPerson,"UTF-8")+"&"
                        +URLEncoder.encode("adminId","UTF-8")+"="+URLEncoder.encode(adminId,"UTF-8")+"&"
                        +URLEncoder.encode("imageoldname","UTF-8")+"="+URLEncoder.encode(groupImage,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpCon.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));

                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("invite"))
        {
            try{
                String action="inviteMember";
                String groupId=params[1];
                String invitorId=params[2];
                String invitedPhone=params[3];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("invitorId","UTF-8")+"="+URLEncoder.encode(invitorId,"UTF-8")+"&"
                        +URLEncoder.encode("invitedPhone","UTF-8")+"="+URLEncoder.encode(invitedPhone,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpCon.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));

                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("modify"))
        {
            try{
                String action="modifyGroup";
                String groupName=params[1];
                String groupTargetType=params[2];
                String targetAmount=params[3];
                String perPersonType=params[4];
                String perPerson=params[5];
                String adminId=params[6];
                String adminPhone=params[7];
                String groupId=params[8];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupName","UTF-8")+"="+URLEncoder.encode(groupName,"UTF-8")+"&"
                        +URLEncoder.encode("groupTargetType","UTF-8")+"="+URLEncoder.encode(groupTargetType,"UTF-8")+"&"
                        +URLEncoder.encode("targetAmount","UTF-8")+"="+URLEncoder.encode(targetAmount,"UTF-8")+"&"
                        +URLEncoder.encode("perPersonType","UTF-8")+"="+URLEncoder.encode(perPersonType,"UTF-8")+"&"
                        +URLEncoder.encode("perPerson","UTF-8")+"="+URLEncoder.encode(perPerson,"UTF-8")+"&"
                        +URLEncoder.encode("adminId","UTF-8")+"="+URLEncoder.encode(adminId,"UTF-8")+"&"
                        +URLEncoder.encode("adminPhone","UTF-8")+"="+URLEncoder.encode(adminPhone,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpCon.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));

                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("collection"))
        {
            try{
                String action="createcollection";
                String groupId=params[1];
                String accountNumber=params[2];
                String bankId=params[3];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("accountNumber","UTF-8")+"="+URLEncoder.encode(accountNumber,"UTF-8")+"&"
                        +URLEncoder.encode("bankId","UTF-8")+"="+URLEncoder.encode(bankId,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpCon.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));

                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alert=new AlertDialog.Builder(context).create();
        alert.setTitle("Return status.......");

    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        progress.dismiss();
                SharedPreferences sharedPreferences=context.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("groupId",result).apply();
                if(result!="")
                {
                    Intent intent=new Intent(context, CreateGroup.class);
                    intent.putExtra("groupId",result);
                    context.startActivity(intent);
                    Log.i("groupId",result);
                }
                else
                {
                    Toast.makeText(context,"System Error Please try again later",Toast.LENGTH_LONG).show();
                }
    }
    //function to return the id
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    //function to return result returned
    public String resultReturned()
    {
        return result;
    }
}
