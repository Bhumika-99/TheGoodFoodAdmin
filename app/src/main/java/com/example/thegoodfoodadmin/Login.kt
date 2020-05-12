package com.example.thegoodfoodadmin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class Login : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences
    var active = ""
    var storedVerificationId: String = ""
    lateinit var credentialOtp: PhoneAuthCredential
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(
            "+919967412396",
            "123456"
        )

        databaseRef = FirebaseDatabase.getInstance().reference
        sharedPreferences = getSharedPreferences("com.example.thegoodfood", 0)
        checkExistingUser()
        login.setOnClickListener() {
            if (phonenumber.text.toString().isNotEmpty()) {
                loginphone()
                active = "otp"
//                phone.visibility == View.GONE
//                otp.visibility = View.VISIBLE
                phonenumber.visibility = View.GONE
                otpcode.visibility = View.VISIBLE
                login.text = "Enter OTP"
                subreg.visibility = View.VISIBLE
            }
//            if (active == "otp") {
//                verifyVerificationCode(otpcode.text.toString())
//
//            }
        }
        subreg.setOnClickListener()
        {
            active = "no"
//            phone.visibility == View.VISIBLE
            phonenumber.visibility = View.VISIBLE
//            otp.visibility = View.GONE
            otpcode.visibility = View.GONE
            login.text = "Login"
            subreg.visibility = View.GONE
        }
    }

    fun checkExistingUser() {
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }

    fun loginphone() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91" + phonenumber.text.toString(), // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks
        )
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {


            //Getting the code sent by SMS
            val code = credential.smsCode
            Log.d("data", code!!)
            credentialOtp = credential
            if (code != null) {
                otpcode.setText(code)
                verifyVerificationCode(code)
            }
        }


        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("data", "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show();
            } else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show();
            }

            fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("data", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
            }
        }
    }

    fun verifyVerificationCode(otp: String) {
        //creating the credential
//        val credential = PhoneAuthProvider.getCredential("",otp)
        signInWithPhoneAuthCredential(credentialOtp)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    //verification successful we will start the profile activity
                    val intent =
                        Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                    //verification unsuccessful.. display an error message
                    var message =
                        "Somthing is wrong, we will fix it soon..."
                    if (task.getException() is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
