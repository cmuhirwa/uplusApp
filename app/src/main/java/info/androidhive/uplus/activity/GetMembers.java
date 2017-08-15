package info.androidhive.uplus.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;
import java.util.Map;

import info.androidhive.uplus.SaveMembers;
import info.androidhive.uplus.groupdetails;

/**
 * Created by RwandaFab on 7/17/2017.
 */

public class GetMembers extends AsyncTask<String,Void,String> {
    List<Map<String, String>> listData=new ArrayList<Map<String, String>>();
    Context context;
    ArrayList<String> data=new ArrayList<String>();
    ArrayList<String>memberAmount=new ArrayList<String>();
    ArrayList<String>membersContribution=new ArrayList<String>();
    ArrayList<String>memberTypes=new ArrayList<String>();
    ArrayList<String>groupsId=new ArrayList<String>();
    ArrayList<String>memberIds=new ArrayList<String>();
    RecyclerView recyclerView;
    Intent intent;
    RecyclerView.Adapter adapter;
    ProgressDialog progress;
    public GetMembers(Context context)
    {
        this.context=context;
    }


    @Override
    protected String doInBackground(String... params) {
        String url="http://104.236.26.9/api/index.php";
        String method=params[0];

        //check methos passed
        if(method.equals("members"))
        {
            try {
                URL conn_url=new URL(url);
                String action="listMembers";
                String groupId=params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"+URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpURLConnection.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));
                StringBuffer buffer=new StringBuffer();
                String result="";
                String line="";
                while((line= buffReader.readLine())!=null){
                    result+=line;
                }
                JSONArray array=new JSONArray(result);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObject=array.getJSONObject(i);
                    String memberName=jsonObject.getString("memberName");
                    String memberPhone=jsonObject.getString("memberPhone");
                    String groupIds=jsonObject.getString("groupId");
                    String memberContribution=jsonObject.getString("memberContribution");
                    String memberType=jsonObject.getString("memberType");
                    String memberId=jsonObject.getString("memberId");
                    data.add(memberName);
                    memberAmount.add(memberPhone);
                    groupsId.add(groupIds);
                    membersContribution.add(memberContribution);
                    memberTypes.add(memberType);
                    memberIds.add(memberId);
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
    protected void onPostExecute(String result) {
        //create listview array Adapter
        try
        {
            for(int i=0;i<data.size();i++)
            {
               String DatatoBeSave="name:"+data.get(i)+"\n groupid: "+groupsId.get(i)+"\n Member Contribution:"+membersContribution.get(i)+"\n memberType:"+memberTypes.get(i);
                //Toast.makeText(context,DatatoBeSave,Toast.LENGTH_LONG).show();
                SaveMembers saveMembers=new SaveMembers(context);
                if(saveMembers.checkGroupId(memberIds.get(i))==false)
                {
                    boolean state=saveMembers.saveMembers(groupsId.get(i),"Test","samuel",data.get(i),membersContribution.get(i),memberTypes.get(i),memberIds.get(i));
                    if(state)
                    {
                        //Toast.makeText(context,membersContribution.get(i),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //data added
                       // Toast.makeText(context,"Member not Added",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                }

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    //function to return
    public ArrayList<String> returnMember()
    {

        return data;
    }
    public ArrayList<String> returnAmount()
    {
        return memberAmount;
    }
    //METHOD TO refresh Data
    public void refreshGUI()
    {
        groupdetails groupdetails=new groupdetails();
        groupdetails.recreate();
    }
}
