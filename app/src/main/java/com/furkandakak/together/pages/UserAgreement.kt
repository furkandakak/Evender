package com.furkandakak.together.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.furkandakak.together.databinding.ActivityUserAgreementBinding

class UserAgreement : AppCompatActivity() {

    private lateinit var binding: ActivityUserAgreementBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAgreementBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun agg_to_account(view: View)
    {
        val intent= Intent(this, Account::class.java)
        startActivity(intent)
        finish()
        onBackPressed()
    }
    override fun onBackPressed() {

        finish()
        super.finish()
        super.finishAndRemoveTask()



    }
}
