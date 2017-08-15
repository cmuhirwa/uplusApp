package info.androidhive.uplus;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by RwandaFab on 7/29/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public static final String CREATE_TABLE="create table if not exists groups(groupId varchar(50),groupName text,targetAmount text,fetched varchar(10))";
    public static final String DROP_TABLE="drop table if exists "+DbContract.TABLE_NAME;
    //

    ArrayList<String>groupNames=new ArrayList<String>();
    ArrayList<String>groupIds=new ArrayList<String>();
    ArrayList<String>targetAmount=new ArrayList<String>();
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public DbHelper(Context context)
    {
        super(context,DbContract.DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    //create a method to save data to the databse
    public boolean saveToLocalDatabase(String groupId,String groupName,String targetAmount,String fetched)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.GROUP_ID,groupId);
        contentValues.put(DbContract.GROUP_NAME,groupName);
        contentValues.put(DbContract.TARGET_AMOUNT,targetAmount);
        contentValues.put(DbContract.FETCHED,fetched);
        //now add data to databse
        long result=database.insert(DbContract.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor getAllData()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from groups",null);
        return cursor;
    }
    //public to check if there is data
    public void clearTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL("delete from groups");
    }
    //function to return id data selected
    public ArrayList<String>groupsId()
    {
        ArrayList<String>datas=new ArrayList<String>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from groups",null);
        while(cursor.moveToNext())
        {
            datas.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_ID)));
        }
        cursor.close();
        return datas;

    }
    public void showNotifications(Context context)
    {
        String query="select * from groups where fetched='no'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int count=0;
        while(cursor.moveToNext())
        {
            count++;
        }
        cursor.close();
        if(count>=0)
        {
            try
            {
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
                    builder.setSmallIcon(R.drawable.ic_action_plus);
                    builder.setContentTitle("Uplus Notification");
                    builder.setContentText("You have new Group Added");

                    //get an instance of the NotificationManager service
                    NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(001,builder.build());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

    }
    //function to drop table
    public void dropTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(DROP_TABLE);
    }
    //function to recreate the table
    public void recreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(CREATE_TABLE);
    }
    //fill the adapter
    public void fillAdapter(RecyclerView recyclerView,Context context)
    {
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from groups",null);
        while(cursor.moveToNext())
        {
            groupIds.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_ID)));
            groupNames.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)));
            targetAmount.add(cursor.getString(cursor.getColumnIndex(DbContract.TARGET_AMOUNT)));


        }
        //update their status
        cursor.close();
        //add data to the adapter
        for(int i=0;i<groupNames.size();i++)
        {
            Log.i("GroupNames are",groupNames.get(i));
        }
        adapter=new MyAdapter(groupNames,targetAmount,context,recyclerView,groupIds);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);


    }

    public boolean checkGroupId(String groupId)
    {
        String query="select * from groups where groupId='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();;
        //create cursor to return data
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        //check counters returned
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void notifyData()
    {
        adapter.notifyDataSetChanged();
    }
    //FUNCTION TO CHECK IF SQLITE HAS DATA
//    public boolean checkDatabase()
//    {
//
//    }
    public void deleteGroup(String groupId)
    {
        String query="delete from groups where groupId='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(query);
    }
}
