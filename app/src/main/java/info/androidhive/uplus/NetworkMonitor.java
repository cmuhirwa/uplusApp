package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import info.androidhive.uplus.activity.BackgroundWorker;

/**
 * Created by RwandaFab on 7/30/2017.
 */

public class NetworkMonitor extends BroadcastReceiver {
    ProgressDialog progress;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(isNetworkAvailable(context))
        {
            SaveGroupLocal saveGroupLocal=new SaveGroupLocal(context);
            saveGroupLocal.recreateTable();
            Cursor cursor=saveGroupLocal.getAllData();
            while(cursor.moveToNext())
            {
                int sync_status=cursor.getInt(cursor.getColumnIndex(SaveGroupContract.SYNC_STATUS));
                if(sync_status==SaveGroupContract.SYNC_FAILED)
                {
                   //create variables to hold data to be sent to remote database
                    String groupId=cursor.getString(cursor.getColumnIndex(SaveGroupContract.ID));
                    String groupName=cursor.getString(cursor.getColumnIndex(SaveGroupContract.GROUP_NAME));
                    String targetType=cursor.getString(cursor.getColumnIndex(SaveGroupContract.TARGET_TYPE));
                    String targetAmount=cursor.getString(cursor.getColumnIndex(SaveGroupContract.TARGET_AMOUNT));
                    String perPersonType=cursor.getString(cursor.getColumnIndex(SaveGroupContract.PER_PERSON_TYPE));
                    String perPerson=cursor.getString(cursor.getColumnIndex(SaveGroupContract.PER_PERSON));
                    String adminId=cursor.getString(cursor.getColumnIndex(SaveGroupContract.ADMIN_ID));
                    String collectionType=cursor.getString(cursor.getColumnIndex(SaveGroupContract.COLLECTION_TYPE));
                    String collectionAccount=cursor.getString(cursor.getColumnIndex(SaveGroupContract.COLLECTION_ACCOUNT));
                    String method="createGroup";
                    //CREATE INSTANCE OF CLASS TO ADD DATA TO THE WEBSERVER
                    BackgroundWorker backgroundWorker=new BackgroundWorker(context,progress);
                    backgroundWorker.execute(method,groupName,targetType,targetAmount,perPersonType,perPerson,adminId,collectionType,collectionAccount);
                    //check the result returned
                    String result=backgroundWorker.resultReturned();
                    if(result!="")
                    {
                        boolean state=saveGroupLocal.updateGroupTable(String.valueOf(SaveGroupContract.SYNC_OK),groupId);
                        if(state==true)
                        {
                          //send broadcast
                            Toast.makeText(context,"Data synchronized successfully",Toast.LENGTH_LONG).show();
                            context.sendBroadcast(new Intent(SaveGroupContract.UI_UPDATE_BROADCAST));
                        }
                        else
                        {
                            Toast.makeText(context,"Data synchronized worng error",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {

                    }
                }

                cursor.close();            }

        }
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
