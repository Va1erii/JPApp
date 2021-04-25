package com.vpopov.jpapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
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