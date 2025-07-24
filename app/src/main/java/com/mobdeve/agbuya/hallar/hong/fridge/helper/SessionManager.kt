package com.mobdeve.agbuya.hallar.hong.fridge.helper

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "logged_in_email"
        private const val KEY_NAME = "logged_in_name"
    }

    fun saveUserSession(email: String, name: String) {
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            apply()
        }
    }

    fun getUserName(): String? = prefs.getString(KEY_NAME, null)
    fun getUserEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun clearSession() {
        prefs.edit { clear() }
    }
}
