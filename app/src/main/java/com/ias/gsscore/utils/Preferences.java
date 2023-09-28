package com.ias.gsscore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    @SuppressLint("StaticFieldLeak")
    private static Preferences instance;

    private static final String login = "login";
    private static final String token = "AUTH_TOKEN";
    private static final String USER_ID = "USER_ID";
    private static final String PACKAGE_ID = "PACKAGE_ID";
    private static final String USER_TYPE = "USER_TYPE";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_MOBILE = "USER_MOBILE";
    private static final String FR_TITLE = "FR_TITLE";
    private static final String FCM_TOKEN = "fcm_token";
    private Preferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("Meest", 0);
        editor = pref.edit();
    }


    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }


    public void setToken(String cname) {
        editor.putString(token, cname);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(token, "");
    }

    public void setFcmToken(String fcm) {
        editor.putString(FCM_TOKEN, fcm);
        editor.commit();
    }

    public String getFcmToken() {
        return pref.getString(FCM_TOKEN, "");
    }


    public void setUserId(String cname) {
        editor.putString(USER_ID, cname);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setFRTitle(String title) {
        editor.putString(FR_TITLE, title);
        editor.commit();
    }

    public String getFRTitle() {
        return pref.getString(FR_TITLE, "");
    }



    public void setPackageId(String cname) {
        editor.putString(PACKAGE_ID, cname);
        editor.commit();
    }

    public String getPackageId() {
        return pref.getString(PACKAGE_ID, "");
    }



    public void setUserType(String cname) {
        editor.putString(USER_TYPE, cname);
        editor.commit();
    }

    public String getUserType() {
        return pref.getString(USER_TYPE, "");
    }



    public void setLogin(Boolean cname) {
        editor.putBoolean(login, cname);
        editor.commit();
    }

    public boolean isLogin() {
        return pref.getBoolean(login, false);
    }

    public void setUserName(String cname) {
        editor.putString(USER_NAME, cname);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserEmail(String cname) {
        editor.putString(USER_EMAIL, cname);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserMobile(String cname) {
        editor.putString(USER_MOBILE, cname);
        editor.commit();
    }

    public String getUserMobile() {
        return pref.getString(USER_MOBILE, "");
    }


}