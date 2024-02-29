package com.shurish.adminblinkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.shurish.adminblinkit.databinding.ActivityAdminMainBinding
import com.shurish.adminblinkit.databinding.ActivityMainBinding

class AdminMainActivity : AppCompatActivity() {
    private  lateinit var binding :ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomMenu, Navigation.findNavController(this, R.id.fragmentContainerView2))
    }


}