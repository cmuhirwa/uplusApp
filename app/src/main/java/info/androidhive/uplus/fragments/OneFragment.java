package info.androidhive.uplus.fragments;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import info.androidhive.uplus.BackgroundTask;
import info.androidhive.uplus.DbContract;
import info.androidhive.uplus.DbHelper;

import info.androidhive.uplus.MyAdapter;
import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.R;
import info.androidhive.uplus.activity.HomeActivity;


public class OneFragment extends Fragment{
    RecyclerView recyclerView;
    FloatingActionButton fabbtn;
    ArrayList<String>groupNames=new ArrayList<String>();
    ArrayList<String>groupIds=new ArrayList<String>();
    ArrayList<String>targetAmount=new ArrayList<String>();
    DbHelper helper;
    ImageView imageView;
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static int counter=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        fabbtn=(FloatingActionButton)rootView.findViewById(R.id.fabbtn);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.rv_recycler_view);
        layoutManager=new LinearLayoutManager(getActivity());
        helper=new DbHelper(getActivity());
        helper.recreateTable();
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity instanceof HomeActivity){
            try
            {
                if(activity.loadFirst()==true)
                {
                    //Toast.makeText(getActivity(),"first Activity from profile",Toast.LENGTH_SHORT).show();
                    activity.showLoader();
                }
                else
                {
                    callLopper();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        displayGroups();
        showAlert(fabbtn);
        return rootView;
    }
    public void showAlert(FloatingActionButton btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(getActivity(), AddGroup.class);
                startActivity(t);
            }
        });

    }

    //function to check if network is avalaible or not
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showMessage(String title,String Message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

public void displayGroups()
{
    DbHelper dbHelper=new DbHelper(getActivity());
    Cursor cursor=dbHelper.getAllData();
    while (cursor.moveToNext())
    {
        groupIds.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_ID)));
        groupNames.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)));
        targetAmount.add(cursor.getString(cursor.getColumnIndex(DbContract.TARGET_AMOUNT)));
    }
    cursor.close();
    adapter=new MyAdapter(groupNames,targetAmount,getActivity(),recyclerView,groupIds);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    layoutManager=new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
}
    public void fetchLocal()
    {
        DbHelper dbHelper=new DbHelper(getActivity());
        dbHelper.fillAdapter(recyclerView,getActivity());
    }

public void callLopper()
{
    HomeActivity activity = (HomeActivity) getActivity();
    if(activity instanceof HomeActivity){
        try
        {
            if(isNetworkAvailable())
            {
                activity.loopData();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

    public void callFetcher()
    {
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity instanceof HomeActivity){
            try
            {
                    activity.loopLocal();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}


