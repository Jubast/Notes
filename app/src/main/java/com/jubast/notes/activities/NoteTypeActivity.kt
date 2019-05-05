package com.jubast.notes.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.jubast.notes.*
import com.jubast.notes.virtualactors.*
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException
import kotlinx.android.synthetic.main.activity_note_type.*
import kotlinx.android.synthetic.main.app_note_type_layout.view.*

class NoteTypeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
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
        sWidgetId.onItemSelectedListener = this

        var selectedWidgetIndex :Int? = null
        val widgets = mutableListOf("None")
        for ((index, widgetId) in NoteWidgetManager(this).state.widgets.withIndex()){
            if (NoteWidget(widgetId, this).state.noteTypeId.contentEquals(_noteTypeId)) {
                selectedWidgetIndex = index + 1 // + 1 because of "None" at index 0
            }
            widgets.add(widgetId)
        }

        ArrayAdapter(this, R.layout.activity_settings_spiner_item, widgets).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sWidgetId.adapter = it
        }

        if(selectedWidgetIndex != null){
            sWidgetId.setSelection(selectedWidgetIndex)
        }
        else{
            sWidgetId.setSelection(0)
        }

        twNotification.text = ""
        btnEdit.text = _appStrings.edit
        btnAdd.text = _appStrings.addNote

        val noteType = NoteType(_noteTypeId, this)
        twNoteType.text = _appStrings.typeName
        twNoteTypeName.text = noteType.state.name

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
            NotesWidgetProvider.updateWidgets(this)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val selectedWidgetId = parent.getItemAtPosition(position) as String

        for (widgetId in NoteWidgetManager(this).state.widgets) {
            val widget = NoteWidget(widgetId, this)
            if(widget.state.noteTypeId == _noteTypeId){
                widget.setNoteTypeId("")
            }
        }

        if(selectedWidgetId == "None"){
            NotesWidgetProvider.updateWidgets(this)
            return
        }

        NoteWidget(selectedWidgetId, this).setNoteTypeId(_noteTypeId)
        NotesWidgetProvider.updateWidgets(this)
    }
}
