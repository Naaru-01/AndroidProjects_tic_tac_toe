package com.example.tictactoeapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_firstpage.*
import kotlinx.android.synthetic.main.activity_second_page.*
var Online = true;
class SecondPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_second_page)
        // for online activity we will go the mode of codeactivity
        buttonOnline.setOnClickListener {
            startActivity(Intent(this , CodeActivity::class.java))
            singleUser = true;
            Online = true;
        }

        // for offline mode we will go to the intent of the main activity
        buttonOffline.setOnClickListener {
            startActivity(Intent(this , MainActivity::class.java))
            singleUser = false;
            Online = false;
        }
    }
}