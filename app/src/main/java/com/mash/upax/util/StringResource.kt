package com.mash.upax.util

import android.content.res.Resources
import androidx.annotation.StringRes

interface StringResource {
    fun getString(@StringRes resourceId: Int): String
    fun getString(@StringRes resourceId: Int, parameter: String): String
}

class StringResourceImp(private val resource: Resources): StringResource {
    override fun getString(resourceId: Int): String =
        resource.getString(resourceId)

    override fun getString(resourceId: Int, parameter: String): String =
        resource.getString(resourceId, parameter)
}