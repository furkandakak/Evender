package com.furkandakak.together.pages

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkandakak.together.databinding.ActivityAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Account : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


         auth = Firebase.auth

        //show user mail at textview
       val loged_user= auth.currentUser?.email.toString()
        binding.textViewUsermail.setText(loged_user)


        //show user nick at textview
        val string = auth.currentUser!!.email.toString()
        val domain: String? = string.substringBeforeLast("@")
        binding.textViewUserNick.setText(domain)

    }

    fun sign_out_clicked(view: View)
    {
        //sign out and go to login page
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
        auth.signOut()

        val intent= Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish()
        onBackPressed()
        finishAffinity()


    }

    //go to main page
    fun account_to_main_button(view: View)
    {
        val intent= Intent(this, MainPage::class.java)
        startActivity(intent)
        finish()
        onBackPressed()

    }

    //go to user agreement
    fun button_doc(view: View)
    {
        val intent= Intent(this, UserAgreement::class.java)
        startActivity(intent)
        finish()
        onBackPressed()

    }

    override fun onBackPressed() {

        finish()
        super.finish()
        super.finishAndRemoveTask()

    }

    fun reset_pass(view: View)
    {
        val email = binding.textViewUsermail.text.toString()

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Sıfırlama linki Mail Adresinize Gönderildi")
                    Toast.makeText(applicationContext, "Sıfırlama linki Mail Adresinize Gönderildi", Toast.LENGTH_SHORT).show()
                }
            }
    }

}