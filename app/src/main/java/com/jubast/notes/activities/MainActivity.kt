package com.jubast.notes.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jubast.notes.*
import com.jubast.notes.virtualactors.AppLanguage
import com.jubast.notes.virtualactors.NoteTypeManager
import com.jubast.notes.virtualactors.NoteType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_note_type_layout.view.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This view shows all note types

        // Gets application languge, and sets name in that language
        setText(AppLanguage(this).getAppStrings())
        setClickListeners()
        loadNoteTypes()
    }

    private fun setText(appStrings: AppStrings)
    {
        twTypes.text = appStrings.types
        btnAdd.text = appStrings.add
    }

    private fun loadNoteTypes()
    {
        linearLayoutTypes.removeAllViews()
        val manager = NoteTypeManager(this)
        val noteTypeIds: MutableList<String> = manager.getActorState().noteTypes
        for (noteTypeId: String in noteTypeIds)
        {
            val noteType = NoteType(noteTypeId, this).getActorState()

            val view: View = layoutInflater.inflate(R.layout.app_note_type_layout, linearLayoutTypes, false)
            view.twText.text = noteType.name
            view.tag = noteTypeId

            view.twText.setOnClickListener {
                val intent = Intent(this, NoteTypeActivity::class.java)
                intent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
                startActivityForResult(intent, NoteSettings.UPDATE_NOTE_TYPE)
            }

            view.btnRemove.setOnClickListener {
                NoteType(noteTypeId, this).delete()
                loadNoteTypes()
            }

            linearLayoutTypes.addView(view)
        }
    }

    private fun setClickListeners()
    {
        btnAdd.setOnClickListener {
            val intent = Intent(this, NoteTypeNameActivity::class.java)
            startActivityForResult(intent, NoteSettings.UPDATE_NOTE_TYPE)
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, NoteSettings.UPDATE_SETTINGS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == NoteSettings.UPDATE_NOTE_TYPE || requestCode == NoteSettings.UPDATE_SETTINGS)
        {
            loadNoteTypes()
            NotesWidgetProvider.updateWidgets(this)
        }
    }
}
