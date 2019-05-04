package com.jubast.notes.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jubast.notes.AppStrings
import com.jubast.notes.NoteSettings
import com.jubast.notes.R
import com.jubast.notes.virtualactors.AppLanguage
import com.jubast.notes.virtualactors.Note
import com.jubast.notes.virtualactors.NoteType
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private var _appStrings: AppStrings = AppStrings.English
    private var _noteId: String? = null
    private var _noteTypeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        _noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("NoteTypeId is required in NoteActivity!")

        _appStrings = AppLanguage(this).getAppStrings()
        _noteId = intent.extras?.getString(NoteSettings.NOTE_ID, null)

        setClickListeners()
        refreshView()
    }

    private fun refreshView() {
        twNotification.text = ""

        if(_noteId == null){
            btnAdd.text = _appStrings.add
        }
        else{
            btnAdd.text = _appStrings.save
        }


        if (_noteId != null) {
            val noteType = Note(_noteId as String, this)
            twNoteTypeName.setText(noteType.state.text)
        }
    }

    private fun setClickListeners() {
        btnAdd.setOnClickListener {
            if (twNoteTypeName.text.isEmpty()) {
                twNotification.text = _appStrings.noNoteInput
                return@setOnClickListener
            }

            if (_noteId != null) {
                val noteType = Note(_noteId as String, this)
                noteType.setNoteText(twNoteTypeName.text.toString())
            }
            else{
                NoteType(_noteTypeId, this).createNote(twNoteTypeName.text.toString())
            }

            setResult(Activity.RESULT_OK)
            finish()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
