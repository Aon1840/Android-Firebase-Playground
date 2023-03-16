package com.example.android_firebase_playground

import android.app.Application
import com.facebook.FacebookSdk

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token));
        FacebookSdk.sdkInitialize(applicationContext)
    }
}