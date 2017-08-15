package info.androidhive.uplus;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
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
import java.util.ArrayList;


public class BackgroundTask extends AsyncTask<String,Void,String> {
    ProgressDialog progress;
    Context ctx;
    String name;
    String[] grp;
    int counter = 0;
    static ArrayList<String> data = new ArrayList<String>();
    static ArrayList<String> grpName = new ArrayList<String>();
    static ArrayList<String> DataId = new ArrayList<String>();
    public BackgroundTask(Context ctx,ProgressDialog progress) {

        this.ctx = ctx;
        this.progress=progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //get method passed
        String url = "http://104.236.26.9/api/index.php";
        String method = params[0];
        //check methos passed
        if (method.equals("add")) {
            try {
                URL conn_url = new URL(url);
                String action = "listGroups";
                String memberId =params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&" + URLEncoder.encode("memberId", "UTF-8") + "=" + URLEncoder.encode(memberId, "UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream = httpURLConnection.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String result = "";
                String line = "";
                while ((line = buffReader.readLine()) != null) {
                    result += line;
                }
                JSONArray array = new JSONArray(result);
                grp = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);

                    name = String.valueOf(jsonObject.get("groupName"));
                    String groupId = String.valueOf(jsonObject.get("groupId"));
                    String groupTargetType = String.valueOf(jsonObject.get("groupTargetType"));
                    String syncstatus = String.valueOf(jsonObject.get("syncstatus"));
                    String targetAmount = String.valueOf(jsonObject.get("targetAmount"));
                    String perPersonType = String.valueOf(jsonObject.get("perPersonType"));
                    String perPerson = String.valueOf(jsonObject.get("perPerson"));
                    String adminId = String.valueOf(jsonObject.get("adminId"));
                    String adminName = String.valueOf(jsonObject.get("adminName"));
                    String id = String.valueOf(jsonObject.get("groupId"));
                    String finalData = name + "\n" + targetAmount;
                    String amount = String.valueOf(jsonObject.get("targetAmount"));
                    //create instance of class databaseClass
                    //end of class databse codes
                    DataId.add(groupId);
                    data.add(name);
                    grpName.add(targetAmount);
                    grp[i] = name;
                    counter = counter + 1;
                }
                buffReader.close();
                inStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            DbHelper dbHelper=new DbHelper(ctx);
            boolean test;
            for (int j = 0; j < data.size(); j++) {
                Log.i("group", data.get(j));

                    if(dbHelper.checkGroupId(DataId.get(j))==false)
                    {
                        //now data can be added
                        test=dbHelper.saveToLocalDatabase(DataId.get(j),data.get(j),grpName.get(j),"no");
                        if(test)
                        {
                            Toast.makeText(ctx,"Data  Added "+data.get(j),Toast.LENGTH_LONG).show();
                            //notify gui
                            showNotification();
                        }
                        else
                        {
                            Toast.makeText(ctx,"Data not Added",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                      progress.dismiss();
                        //Toast.makeText(ctx,"Group Exit",Toast.LENGTH_LONG).show();
                    }




            }
            dbHelper.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showNotification()
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle("Uplus Notification");
        builder.setContentText("You Have received new Group ");

        //get an instance of the NotificationManager service
        NotificationManager notificationManager=(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001,builder.build());

    }
}
