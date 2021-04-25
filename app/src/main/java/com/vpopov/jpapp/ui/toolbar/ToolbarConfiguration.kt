package com.vpopov.jpapp.ui.toolbar

import android.graphics.drawable.Drawable

sealed class ToolbarConfiguration(val title: String) {
    class TitleToolbarConfiguration(
        title: String
    ) : ToolbarConfiguration(title)

    class ImageToolbarConfiguration(
        title: String,
        val image: Drawable
    ) : ToolbarConfiguration(title)
}