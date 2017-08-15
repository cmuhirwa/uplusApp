package info.androidhive.uplus.activity;

import android.content.Context;

import android.content.SharedPreferences;


public class SharedPrefManager {
    private static SharedPrefManager sM;
    private static Context contexts;
    private static String shName = "login";
    private static String id = "id";
    private  static  String userNam ="userName";
    private  static  String phone = "phone";
    private SharedPrefManager(Context context){
        contexts = context;
    }
    public  static synchronized SharedPrefManager getInstance(Context context){
        if(sM == null){
            sM = new SharedPrefManager(context);
        }
        return sM;
    }
    //get updated user info
    public void login(String userIds,String userName){

        SharedPreferences sharedPreferences = contexts.getSharedPreferences(shName,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(id,userIds);

        editor.putString(userNam,userName);

        editor.apply();
    }
    //get Current User Phone number
    public void number(String phones){
        SharedPreferences sharedPreferences = contexts.getSharedPreferences(shName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(phone,phones);
        editor.apply();
    }

    // access User Phone numbers
    public  String getPhone(){
        SharedPreferences sharedPreferences = contexts.getSharedPreferences(shName,Context.MODE_PRIVATE);
        String number = sharedPreferences.getString(phone,null).toString();
        return number;
    }
    public  String getUserName(){
        SharedPreferences sharedPreferences = contexts.getSharedPreferences(shName,Context.MODE_PRIVATE);
        String number = sharedPreferences.getString(userNam,null).toString();
        return number;
    }
    //Check if user is logged in or not
    public  boolean isLoggedIn(){
        SharedPreferences sharedPreference = contexts.getSharedPreferences(shName,Context.MODE_PRIVATE);
        if(sharedPreference.getString("id",null) != null){
            return  true;
        }
        return  false;
    }
    //method to read value from shared preference for getting value of userId
    public String getUserId()
    {
        SharedPreferences sharedPreferences = contexts.getSharedPreferences(shName,Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(id,null).toString();
        return userId;
    }
}
