package com.jubast.notes.activities.redirect

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jubast.notes.NoteSettings
import com.jubast.notes.activities.NoteTypeActivity
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException

class RedirectActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        val redirectIntent = Intent(this, NoteTypeActivity::class.java)
        redirectIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        startActivity(redirectIntent)
    }
}

class RedirectActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        val redirectIntent = Intent(this, NoteTypeActivity::class.java)
        redirectIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        startActivity(redirectIntent)
    }
}

class RedirectActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        val redirectIntent = Intent(this, NoteTypeActivity::class.java)
        redirectIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        startActivity(redirectIntent)
    }
}

class RedirectActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        val redirectIntent = Intent(this, NoteTypeActivity::class.java)
        redirectIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        startActivity(redirectIntent)
    }
}