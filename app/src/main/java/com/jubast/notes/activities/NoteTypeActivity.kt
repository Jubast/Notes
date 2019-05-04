package com.jubast.notes.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jubast.notes.*
import com.jubast.notes.virtualactors.AppLanguage
import com.jubast.notes.virtualactors.Note
import com.jubast.notes.virtualactors.NoteType
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException
import kotlinx.android.synthetic.main.activity_note_type.*
import kotlinx.android.synthetic.main.note_type_edit.view.*

class NoteTypeActivity : AppCompatActivity() {
    private var _appStrings: AppStrings = AppStrings.English
    private var _noteTypeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_type)

        _appStrings = AppLanguage(this).getAppStrings()
        _noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        setClickListeners()
        refreshView()
    }

    private fun refreshView() {
        twNotification.text = ""
        btnEdit.text = _appStrings.edit
        btnAdd.text = _appStrings.addNote

        val noteType = NoteType(_noteTypeId, this)
        twNoteType.text = _appStrings.typeName
        twNoteTypeName.setText(noteType.state.name)

        linearLayoutNotes.removeAllViews()
        for (noteId in noteType.state.notes) {
            val note = Note(noteId, this)
            val view: View = layoutInflater.inflate(R.layout.app_note_type_layout, linearLayoutNotes, false)
            view.twText.text = note.state.text
            view.twText.setOnClickListener {
                val intent = Intent(this, NoteActivity::class.java)
                intent.putExtra(NoteSettings.NOTE_TYPE_ID, _noteTypeId)
                intent.putExtra(NoteSettings.NOTE_ID, noteId)
                startActivityForResult(intent, NoteSettings.UPDATE_NOTE)
            }

            view.btnRemove.setOnClickListener {
                Note(noteId, this).delete()
                refreshView()
            }

            linearLayoutNotes.addView(view)
        }
    }

    private fun setClickListeners() {
        btnEdit.setOnClickListener {
            val intent = Intent(this, NoteTypeNameActivity::class.java)
            intent.putExtra(NoteSettings.NOTE_TYPE_ID, _noteTypeId)
            startActivityForResult(intent, NoteSettings.UPDATE_NOTE_TYPE)
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra(NoteSettings.NOTE_TYPE_ID, _noteTypeId)
            startActivityForResult(intent, NoteSettings.UPDATE_NOTE)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == NoteSettings.UPDATE_NOTE || requestCode == NoteSettings.UPDATE_NOTE_TYPE){
            refreshView()
        }
    }
}
