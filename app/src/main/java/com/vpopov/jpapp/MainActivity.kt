package com.vpopov.jpapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.vpopov.jpapp.databinding.MainActivityBinding
import com.vpopov.jpapp.extension.gone
import com.vpopov.jpapp.extension.visible
import com.vpopov.jpapp.ui.toolbar.HasToolbar
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration.ImageToolbarConfiguration
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration.TitleToolbarConfiguration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HasToolbar {
    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            .let { it as NavHostFragment }
            .let { navController = it.navController }
        NavigationUI.setupWithNavController(binding.toolbar, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun applyConfiguration(toolbarConfiguration: ToolbarConfiguration) {
        binding.toolbarLayout.isTitleEnabled = false
        when (toolbarConfiguration) {
            is TitleToolbarConfiguration -> {
                binding.toolbar.title = toolbarConfiguration.title
                binding.toolbarImage.gone()
            }
            is ImageToolbarConfiguration -> {
                binding.toolbar.title = toolbarConfiguration.title
                binding.toolbarImage.setImageDrawable(toolbarConfiguration.image)
                binding.toolbarImage.visible()
            }
        }
        binding.appbar.setExpanded(true, true)
    }
}