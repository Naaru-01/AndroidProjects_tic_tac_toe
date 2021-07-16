package com.example.tictactoeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_firstpage.*
import kotlin.system.exitProcess

var singleUser = false
class Firstpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_firstpage)

        button11.setOnClickListener {
            startActivity(Intent(this , MainActivity::class.java))
            singleUser = true;
        }
        button12.setOnClickListener {
            startActivity(Intent(this , SecondPage::class.java))
            singleUser = false;
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
        exitProcess(1)
    }
}
