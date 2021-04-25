package com.vpopov.jpapp.extension

import androidx.fragment.app.Fragment
import com.vpopov.jpapp.ui.toolbar.HasToolbar
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration

fun Fragment.setToolbarConfiguration(toolbarConfiguration: ToolbarConfiguration) {
    activity
        ?.takeIf { it is HasToolbar }
        ?.let { it as HasToolbar }
        ?.applyConfiguration(toolbarConfiguration)
}