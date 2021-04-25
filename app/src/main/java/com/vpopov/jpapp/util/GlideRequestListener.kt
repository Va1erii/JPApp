package com.vpopov.jpapp.util

import android.graphics.drawable.Drawable
import android.os.Handler
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlideRequestListener(
    private val handler: Handler,
    private val onLoadFailed: () -> Unit,
    private val onLoadSuccess: (Drawable) -> Unit
) : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        handler.post(onLoadFailed)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        resource?.let { handler.post { onLoadSuccess(it) } } ?: handler.post(onLoadFailed)
        return false
    }
}