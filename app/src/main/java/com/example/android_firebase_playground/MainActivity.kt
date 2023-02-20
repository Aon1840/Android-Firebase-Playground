package com.example.android_firebase_playground

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            this.onSignInResult(res)
        }
    private val firebase = FirebaseAuth.getInstance()
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )

    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (RESULT_OK == result.resultCode) {
            val user = firebase.currentUser
            findViewById<TextView>(R.id.tv_user).text = user?.email
            Log.d("RESPONSE", ": $response")
            val logout = findViewById<Button>(R.id.btn_logout)
            logout.visibility = View.VISIBLE
            logout.setOnClickListener {
                AuthUI.getInstance().signOut(this)
                firebase.addIdTokenListener(FirebaseAuth.IdTokenListener {
                    if (it.currentUser == null) {
                        signInLauncher.launch(signInIntent)
                    }
                })
            }
        }
    }
}