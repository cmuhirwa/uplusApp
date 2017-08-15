package info.androidhive.uplus.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.BackgroundTask;
import info.androidhive.uplus.DbHelper;
import info.androidhive.uplus.Login;
import info.androidhive.uplus.MyAdapter;
import info.androidhive.uplus.Profile;
import info.androidhive.uplus.R;
import info.androidhive.uplus.SaveGroupLocal;
import info.androidhive.uplus.SaveMembers;
import info.androidhive.uplus.fragments.OneFragment;
import info.androidhive.uplus.fragments.ThreeFragment;
import info.androidhive.uplus.fragments.TwoFragment;

public class HomeActivity extends AppCompatActivity {
    ImageButton myImageButton;
    public int count;
    static int counter=0;
     OneFragment oneFragment;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ProgressDialog progress;
    private ListView list;
    private int[] tabIcons = {
            R.drawable.groupicon,
            R.drawable.ic_tab_contacts,
            R.drawable.events
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                return true;

            case R.id.menu_refresh:  //other menu items if you have any
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                return true;
            case R.id.menu_item3:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.help, null);
                myImageButton = (ImageButton) mView.findViewById(R.id.btnhelp);



                //Button mHelp = (Button) mView.findViewById(R.id.btnhelp);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                myImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"We are going to help you",Toast.LENGTH_LONG).show();
                    }
                });


                dialog.show();

                 return true;

            default: return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.menu_item).setEnabled(false);

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        recyclerView=(RecyclerView)findViewById(R.id.rv_recycler_view);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        //WRITE FUNCTION TO VALIDATE IF A USER IS LOGGED IN HERE
        if(!SharedPrefManager.getInstance(getApplication()).isLoggedIn()){
            Intent i =  new Intent(getApplicationContext(),Login.class);
            finish();
            startActivity(i);
        }
    }

    //FUNCTION TO ASK FOR PERMISSION
    public void askPermission()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= 23){
                //call the function to ask for permission
                SharedPreferences preferences=this.getSharedPreferences("info.androidhive.uplus",Context.MODE_PRIVATE);
                if(preferences.getString("Contacts","")!="")
                {
                    int test=Integer.parseInt(preferences.getString("Contact",""));
                    if(test==1)
                    {
                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        viewPager = (ViewPager) findViewById(R.id.viewpager);
                        setupViewPager(viewPager);
                        tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                        setupTabIcons();
                        //CHECK USER SESSION
                        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},100);
                                    return;
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Errors",Toast.LENGTH_LONG).show();
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                viewPager = (ViewPager) findViewById(R.id.viewpager);
                setupViewPager(viewPager);
                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
                checkUserSession();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //END OF PERMISSION REQUESTOR

    //FUNCTION TO CHECK USER SESSION

    public void checkUserSession()
    {
        SharedPreferences preferences=this.getSharedPreferences("info.androidhive.uplus",Context.MODE_PRIVATE);
        try
        {
                    //check value of shared preference
                    if(preferences.getString("Login","")!="")
                    {
                        if(Integer.parseInt(preferences.getString("Login",""))!=5)
                        {
                            Intent intent=new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"not logged in",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Please Login",Toast.LENGTH_LONG).show();
                    }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //END OF SESSION CHECKER

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.uplus",Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("Contacts","1");
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},100);
            }
        }
    }

    public void showMessage(String title,String Message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "Groups");
        adapter.addFrag(new TwoFragment(), "People");
        adapter.addFrag(new ThreeFragment(), " Event");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void loopData()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(isNetworkAvailable())
                        {
                            Intent intent=getIntent();

                            String Load=intent.getStringExtra("Load");
                                final ProgressDialog progress;
                                progress = new ProgressDialog(HomeActivity.this);
                                progress.setTitle("Loading Contents now");
                                progress.setMessage("Please wait while we are Downloading Data...");
                                progress.setCancelable(false);
                                String memberId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                                //Toast.makeText(getApplicationContext(), memberId+" fuck you", Toast.LENGTH_SHORT).show();
                                BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
                                backgroundTask.execute("add",memberId);
                                oneFragment=new OneFragment();

                        }
                    }
                });
            }
        },0,10000);
    }
    public void loopLocal()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        toast.show();
                        DbHelper dbHelper=new DbHelper(getApplicationContext());
                        dbHelper.fillAdapter(recyclerView,getApplicationContext());
                    }
                });
            }
        },0,5000);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showLoader()
    {
         Intent intent=getIntent();

        String Load=intent.getStringExtra("Load");

        if(Load!="" && Load!=null)
        {
            if(Integer.parseInt(Load)==1)
            {
                final ProgressDialog progress;
                progress = new ProgressDialog(HomeActivity.this);
                progress.setTitle("Loading Contents now");
                progress.setMessage("Please wait while we are Downloading Data...");
                progress.show();
                progress.setCancelable(false);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String memberId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                                BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
                                backgroundTask.execute("add",memberId);
                                if(counter==2)
                                {
                                    Intent intent1=getIntent();
                                    finish();
                                    intent1.putExtra("Load","2");
                                    startActivity(intent1);
                                }
                                else
                                {
                                    Intent intent1=getIntent();
                                    finish();
                                    startActivity(intent1);
                                }
                                counter=counter+1;
                                Toast.makeText(getApplicationContext(),String.valueOf(counter),Toast.LENGTH_SHORT).show();
                            }
                        }, 10000);
            }
        }

    }
    public boolean loadFirst()
    {
        Intent intent=getIntent();
        boolean state = false;
        String Load=intent.getStringExtra("Load");

        if(Load!="" && Load!=null)
        {
            if(Integer.parseInt(Load)==1)
            {
                state=true;
            }
            else
            {
                state=false;
            }
        }
        return state;
    }
}
