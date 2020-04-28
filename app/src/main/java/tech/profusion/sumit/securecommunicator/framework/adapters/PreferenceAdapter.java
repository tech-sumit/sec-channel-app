package tech.profusion.sumit.securecommunicator.framework.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import tech.profusion.sumit.securecommunicator.framework.entities.Constants;

/**
 * Created by Sumit Agrawal on 12-01-2018.
 */

public class PreferenceAdapter {
    private SharedPreferences preferences;
    private Context context;
    private String stringPreference;

    public PreferenceAdapter(Context context,String stringPreference){
        this.stringPreference=stringPreference;
        preferences=context.getSharedPreferences(stringPreference,Context.MODE_PRIVATE);
    }

    public boolean checkLogin(){
        if(preferences.getString(Constants.LOGIN_STATUS,"").equals("")){
            return false;
        }
        return true;
    }
    public void setLogin(boolean status){
        SharedPreferences.Editor editor=preferences.edit();
        if(status){
            editor.putString(Constants.LOGIN_STATUS,Constants.STATUS_CODE);
        }else{
            editor.clear();
        }
        editor.apply();
    }

    public void putStringPreference(String key,String value){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringPreference(String key){
        return preferences.getString(key, "");
    }

    public boolean isSet(String key,String value){
        if(preferences.getString(key, "").equals(value)){
            return true;
        }
        return false;
    }

    public void putSelfContact(String contact_no,String name,String salt){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(Constants.COLUMN_CONTACT_NO,contact_no);
        editor.putString(Constants.COLUMN_NAME,name);
        editor.putString(Constants.COLUMN_PASSWORD,salt);
        editor.apply();
    }

    public Bundle getSelfContact(){
        Bundle bundle=new Bundle();
        bundle.putString(Constants.COLUMN_CONTACT_NO,preferences.getString(Constants.COLUMN_CONTACT_NO,""));
        bundle.putString(Constants.COLUMN_NAME,preferences.getString(Constants.COLUMN_NAME,""));
        bundle.putString(Constants.COLUMN_PASSWORD,preferences.getString(Constants.COLUMN_PASSWORD,""));
        return bundle;
    }
}
