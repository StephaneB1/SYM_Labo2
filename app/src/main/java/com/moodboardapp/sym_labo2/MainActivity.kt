package com.moodboardapp.sym_labo2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var activity1Launcher: Button
    private lateinit var activity2Launcher: Button
    private lateinit var activity3Launcher: Button
    private lateinit var activity4Launcher: Button
    private lateinit var activity5Launcher: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity1Launcher = findViewById(R.id.activity1)
        activity2Launcher = findViewById(R.id.activity2)
        activity3Launcher = findViewById(R.id.activity3)
        activity4Launcher = findViewById(R.id.activity4)
        activity5Launcher = findViewById(R.id.activity5)

        activity1Launcher.setOnClickListener {
            val intent = Intent(this, Activity1::class.java)
            startActivity(intent)
        }
        activity2Launcher.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }
        activity3Launcher.setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            startActivity(intent)
        }
        activity4Launcher.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
        }
        activity5Launcher.setOnClickListener {
            val intent = Intent(this, Activity5::class.java)
            startActivity(intent)
        }
    }


    companion object {
        private const val TAG: String = "MainActivity"
        const val LAB_SERVER: String = "http://sym.iict.ch/"
    }
}