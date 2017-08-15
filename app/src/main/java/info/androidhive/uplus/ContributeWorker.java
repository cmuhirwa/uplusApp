package info.androidhive.uplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by RwandaFab on 7/30/2017.
 */

public class ContributeWorker extends AsyncTask<String,Void,String> {
Context context;
    String result="";
    String transaction="";
    String status="";
    private static int a;
    private static final String STATUS_APPROVED="APPROVED";
    private static final String STATUS_COMPLETE="COMPLETE";
    private static final String STATUS_PENDING="PENDING";

    public ContributeWorker(Context context)
    {
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {
        String api_url="http://104.236.26.9/api/index.php";
        String type=params[0];
        if(type.equals("contribute")){
            try{
                String action="contribute";
                String memberId=params[1];
                String groupId=params[2];
                String amount=params[3];
                String fromPhone=params[4];
                String bankId=params[5];
                URL url=new URL(api_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("memberId","UTF-8")+"="+URLEncoder.encode(memberId,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amount,"UTF-8")+"&"
                        +URLEncoder.encode("fromPhone","UTF-8")+"="+URLEncoder.encode(fromPhone,"UTF-8")+"&"
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
//                JSONArray array = new JSONArray(result);
//                for(int i=0;i<array.length();i++)
//                {
//                    JSONObject jsonObject = array.getJSONObject(i);
//                    transaction=jsonObject.getString("transactionId");
//                    status=jsonObject.getString("status");
//                }
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
        else if(type.equals("resend"))
        {
            try{
                String action="checkstatus";
                String transactionId=params[1];
                URL url=new URL(api_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("transactionId","UTF-8")+"="+URLEncoder.encode(transactionId,"UTF-8");
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
//                JSONArray array = new JSONArray(result);
//                for(int i=0;i<array.length();i++)
//                {
//                    JSONObject jsonObject = array.getJSONObject(i);
//                    transaction=jsonObject.getString("transactionId");
//                    status=jsonObject.getString("status");
//                }
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


    }

    @Override
    protected void onPostExecute(String result) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("Result returned");
        dialog.setMessage(result);
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
////add data to shared preferences
//        SharedPreferences sharedPreferences=context.getSharedPreferences("info.androidhive.materialtabs",Context.MODE_PRIVATE);
//        //check status being returned
//        if(status==STATUS_PENDING)
//        {
//
//            sharedPreferences.edit().putString("contributeStatus",STATUS_PENDING);
//            Receipt receipt=new Receipt();
//            receipt.sendRequest();
//        }
//        else
//        {
//            sharedPreferences.edit().putString("contributeStatus",STATUS_APPROVED);
//        }
    }
//function to return status
    public String returnStatus()
    {
        return status;
    }
    public String returnTransactionId()
    {
        return transaction;
    }

}
