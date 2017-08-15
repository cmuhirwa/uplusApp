package info.androidhive.uplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import info.androidhive.uplus.activity.RecyclerAdapter;

/**
 * Created by RwandaFab on 7/29/2017.
 */

public class SaveMembers extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public static final String CREATE_TABLE="create table if not exists members(id text,groupName text,groupdesc text,memberName text, memberAmount text,memberType text,memberId varchar(50))";
    public static final String DROP_TABLE="drop table if exists "+MemberContract.TABLE_NAME;

    //gui components
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> memberAmount=new ArrayList<String>();
    ArrayList<String>memberName=new ArrayList<String>();
    ArrayList<String>memberType=new ArrayList<String>();
    public SaveMembers(Context context)
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
    public void recreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(CREATE_TABLE);
    }
    //method save members offline
    public boolean saveMembers(String id,String groupName,String groupdesc,String memberName,String memberAmount,String memberType,String memberId)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MemberContract.ID,id);
        contentValues.put(MemberContract.GROUP_NAME,groupName);
        contentValues.put(MemberContract.GROUP_DESC,groupdesc);
        contentValues.put(MemberContract.MEMBER_NAME,memberName);
        contentValues.put(MemberContract.MEMBER_AMOUNT,memberAmount);
        contentValues.put(MemberContract.MEMBER_TYPE,memberType);
        contentValues.put(MemberContract.MEMBER_ID,memberId);
        long result=database.insert(MemberContract.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    //method to get Data from Database offline
    public Cursor getAllData(String groupId)
    {
        String query="select * from members where id='"+groupId+"'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    //function to clear data from databse
    public void clearMembers()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from members");
    }
    //function to fill member recyclerview
    public void fillMemberRecyclerView(String id,RecyclerView recyclerView,Context context)
    {
        String query="select * from members where id='"+id+"'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext())
        {
            while(cursor.moveToNext())
            {
                memberName.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_NAME)));
                memberAmount.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_AMOUNT)));
                memberType.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_TYPE)));
            }
            cursor.close();
        }

        //Create recyclerview adapter
        adapter=new RecyclerAdapter(memberName,memberAmount,memberType);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    //FUNCTION TO CHECK IF DATA IS AVAILABLE
    public boolean checkGroupId(String memberId)
    {
        String query="select * from members where memberId='"+memberId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
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
    //SUM UP GROUP MEMBER AMOUNT
    public String sumGroupAmount(String groupId)
    {
        String query="select * from members where id='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        Double total=0.0;
        while(cursor.moveToNext())
        {
          total+=Double.parseDouble(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_AMOUNT)));
        }

        return String.valueOf(total);
    }

    //METHOD TO DELETE DATA FROM LOCALSTORAGE
    public boolean deleteFromLocalGroup(String groupId,String memberId)
    {
        String query="delete from members where id='"+groupId+"' and memberId='"+memberId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
           counter=counter+1;
        }
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void dropMember()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_TABLE);
    }
    //fucntion to delete group from localdb

}
