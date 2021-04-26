package com.vpopov.jpapp.util

import android.content.Context
import androidx.annotation.StringRes

/**
 * Used for providing charsequence without knowing context
 */
class CharSequenceContainer private constructor(
    @StringRes private val contentId: Int,
    private val content: CharSequence?
) {
    constructor(content: String) : this(-1, content)
    constructor(@StringRes contentId: Int) : this(contentId, null)

    fun get(context: Context): CharSequence {
        return content ?: context.getString(contentId)
    }
}