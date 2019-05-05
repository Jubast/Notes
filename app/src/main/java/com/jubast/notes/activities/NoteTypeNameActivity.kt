package com.jubast.notes.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jubast.notes.AppStrings
import com.jubast.notes.NoteSettings
import com.jubast.notes.NotesWidgetProvider
import com.jubast.notes.R
import com.jubast.notes.virtualactors.AppLanguage
import com.jubast.notes.virtualactors.NoteType
import com.jubast.notes.virtualactors.NoteTypeManager
import kotlinx.android.synthetic.main.activity_note_type_name.*
import kotlinx.android.synthetic.main.activity_note_type_name.btnAdd
import kotlinx.android.synthetic.main.activity_note_type_name.btnBack
import kotlinx.android.synthetic.main.activity_note_type_name.twNoteTypeName

class NoteTypeNameActivity : AppCompatActivity() {
    private var _appStrings : AppStrings = AppStrings.English
    private var _noteTypeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_type_name)

        _appStrings = AppLanguage(this).getAppStrings()
        twNotification.text = ""
        twInstruction.text = _appStrings.typeName

        _noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
        if(_noteTypeId == null){
            btnAdd.text = _appStrings.add
        }
        else{
            twNoteTypeName.setText(NoteType(_noteTypeId as String, this).state.name)
            btnAdd.text = _appStrings.save
        }

        setClickListeners()
    }

    private fun setClickListeners() {
        btnAdd.setOnClickListener {
            if(twNoteTypeName.text.isEmpty())
            {
                twNotification.text = _appStrings.noNoteInput
                return@setOnClickListener
            }

            if(_noteTypeId == null){
                _noteTypeId = NoteTypeManager(this).addNoteType(twNoteTypeName.text.toString())
            }else{
                NoteType(_noteTypeId as String, this).setNoteTypeName(twNoteTypeName.text.toString())
            }

            setResult(Activity.RESULT_OK)
            NotesWidgetProvider.updateWidgets(this)
            finish()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
