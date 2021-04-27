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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharSequenceContainer

        if (contentId != other.contentId) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentId
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CharSequenceContainer(contentId=$contentId, content=$content)"
    }
}