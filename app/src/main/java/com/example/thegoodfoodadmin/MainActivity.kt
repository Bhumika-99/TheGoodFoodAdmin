package com.example.thegoodfoodadmin

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(hometoolbar)
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("com.example.thegoodfood", 0)
        addm.setOnClickListener {
            startActivity(Intent(this, AddMenuItem::class.java))
        }
        showitems.setOnClickListener {
            startActivity(Intent(this, Displayitem::class.java))
        }
        orders.setOnClickListener {
            startActivity(Intent(this, Orders::class.java))
        }
        admin_feedback.setOnClickListener {
            startActivity(Intent(this, feedback::class.java))
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.clear()
        val item3 = menu.add(0, 1234, Menu.NONE, "Logout")
        item3.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        var s = SpannableString(item3.title.toString())
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
        item3.title = s
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1234 -> {
                sharedPreferences.edit().putBoolean("isadmin", false).apply()
                firebaseAuth.signOut()
                startActivity(Intent(this, Login::class.java))
            }
        }
        return true
    }
}
