package com.jubast.notes.activities.redirect

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jubast.notes.NoteSettings
import com.jubast.notes.NotesWidgetProvider
import com.jubast.notes.activities.NoteTypeActivity
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException

class RedirectActivity1 : BaseRedirectActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class RedirectActivity2 : BaseRedirectActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class RedirectActivity3 : BaseRedirectActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class RedirectActivity4 : BaseRedirectActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

abstract class BaseRedirectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        val redirectIntent = Intent(this, NoteTypeActivity::class.java)
        redirectIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        startActivityForResult(redirectIntent, NoteSettings.REDIRECT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == NoteSettings.REDIRECT_ACTIVITY)
        {
            finish()
        }
    }
}