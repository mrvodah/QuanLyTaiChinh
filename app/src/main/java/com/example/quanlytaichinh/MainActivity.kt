package com.example.quanlytaichinh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initClick()
    }

    private fun initClick() {
        btn_home.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
        }

        btn_spend.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.spendFragment)
        }

        btn_analysts.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.analystsFragment)
        }
    }
}
