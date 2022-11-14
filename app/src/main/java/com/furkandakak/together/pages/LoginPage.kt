package com.furkandakak.together.pages

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furkandakak.together.R
import com.furkandakak.together.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginPage : AppCompatActivity() {
    //binding başlangıç
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var show_pass : CheckBox

    //veri tabanı işlemleri
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      // show_pass=findViewById(R.id.checkBox_pass)
        //binding.password.transformationMethod= PasswordTransformationMethod.getInstance()

     /* show_pass.setOnCheckedChangeListener { _, isChecked ->

            if (binding.checkBoxPass.text=="Parolayı Göster")
            {
                binding.password.transformationMethod= PasswordTransformationMethod.getInstance()
            }
            else
            {binding.password.transformationMethod= HideReturnsTransformationMethod.getInstance()}
        }*/



        //binding
        binding= ActivityLoginPageBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        //veri tabanı
        auth= Firebase.auth

        // kullanıcının giriş yapıp yapmadığının kontrolü // eğer kullanıcı giriş yapmışsa direkt ana sayfaya gidecek
        val Current_user=auth.currentUser
        if (Current_user!= null)
        {
            val intent= Intent(this@LoginPage,MainPage::class.java)
            startActivity(intent)
            finish()
        }


    }
    fun sign_in_clicked(view: View)

    {   //kullanıcıdan verileri binding ile almak
        val email = binding.email.text.toString().trimStart().trimEnd()
        val password = binding.password.text.toString()
        val emailPattern = """[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"""

        if(!email.matches(emailPattern.toRegex())||password.equals(""))
        {
            Toast.makeText(this,"Mail Veya Parolayı Kontrol Edin !",Toast.LENGTH_LONG).show()
        }
        else
        {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent= Intent(this@LoginPage,MainPage::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {Toast.makeText(this,"Parola Veya Mail Yanlış",Toast.LENGTH_LONG).show() }
        }

    }

    fun sign_up_clicked(view: View)
    {
        //kullanıcıdan verileri binding ile almak
        val email = binding.email.text.toString().trimStart().trimEnd()
        val password = binding.password.text.toString()

        var re = Regex("^\\s*$")
        val emailPattern = """[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"""


        // kullanıcının parolayı girip girmediğini kontrol etmek
        if(!email.matches(emailPattern.toRegex())||password.equals(""))
        {
            Toast.makeText(this,"Parola Ve Maili Kontrol Edin !",Toast.LENGTH_LONG).show()
        }

        else if (password.length<6)
        {
            Toast.makeText(this,"Parola En Az 6 Haneli Olmalı !",Toast.LENGTH_LONG).show()
        }

        else
        {  //başarılı ise burası çalışacak ve ardından yeni activity'i açacak
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent= Intent(this@LoginPage,MainPage::class.java)
                startActivity(intent)
                finish()
            }
                .addOnFailureListener { Toast.makeText(applicationContext, "Bu Kullanıcı Kayıtlı !", Toast.LENGTH_SHORT).show() }
            // eğer başarısız olursa kullanıcıya hata mesajı gösterecek
        }


    }

    fun reset_pass(view: View)
    {
        var re = Regex("^\\s*$")
        val email = binding.email.text.toString()
        val emailPattern = """[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"""

        if (email.matches(regex = re)||!email.matches(emailPattern.toRegex()))
        {
            Toast.makeText(applicationContext, "E-mail Adresi Boş Veya Hatalı", Toast.LENGTH_SHORT).show()
        }

        else{
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Sıfırlama linki Mail Adresinize Gönderildi")
                    Toast.makeText(applicationContext, "Sıfırlama linki Mail Adresinize Gönderildi", Toast.LENGTH_SHORT).show()
                }
            }}
    }


}


