package com.jubast.notes.virtualactors

import android.content.Context
import com.google.gson.Gson
import com.jubast.notes.containers.NoteExportModel
import com.jubast.notes.containers.NoteTypeExportModel
import com.jubast.notes.containers.NotesExportModel
import com.jubast.notes.containers.WidgetExportModel
import com.jubast.notes.virtualactors.abstract.Actor
import java.lang.Exception

class ExportManager(val context: Context): Actor(".") {
    fun exportJson(): String {
        val export = NotesExportModel()
        val noteTypeManagerState = NoteTypeManager(context).getActorState()

        for(noteTypeId in noteTypeManagerState.noteTypes)
        {
            val noteTypeExportModel = NoteTypeExportModel()
            val noteType = NoteType(noteTypeId, context)
            val noteTypeState = noteType.getActorState()
            noteTypeExportModel.noteTypeId = noteType.actorId
            noteTypeExportModel.name = noteTypeState.name

            for(noteId in noteTypeState.notes)
            {
                val note = Note(noteId, context)
                val noteState = note.getActorState()

                val noteExportModel = NoteExportModel()
                noteExportModel.noteId = note.actorId
                noteExportModel.text = noteState.text
                noteExportModel.checked = noteState.checked

                noteTypeExportModel.notes.add(noteExportModel)
            }

            export.noteTypes.add(noteTypeExportModel)
        }

        val noteWidgetManagerState = NoteWidgetManager(context).getActorState()
        for(noteWidgetId in noteWidgetManagerState.widgets){
            val noteWidget = NoteWidget(noteWidgetId, context)
            val noteWidgetState = noteWidget.getActorState()

            val noteWidgetExportModel = WidgetExportModel()
            noteWidgetExportModel.widgetId = noteWidgetId
            noteWidgetExportModel.noteTypeId = noteWidgetState.noteTypeId

            export.widgets.add(noteWidgetExportModel)
        }

        val serviceManagerState = ServiceManager(context).getActorState()
        export.services.service1 = serviceManagerState.service1Used
        export.services.service2 = serviceManagerState.service2Used
        export.services.service3 = serviceManagerState.service3Used
        export.services.service4 = serviceManagerState.service4Used

        return Gson().toJson(export)
    }

    fun importJson(json: String) {

        val exportModel: NotesExportModel
        try {
            exportModel = Gson().fromJson(json, NotesExportModel::class.java)
        }catch (e: Exception) {
            // do nothing.
            return;
        }

        val noteTypeManager = NoteTypeManager(context)
        for(noteTypeExportModel in exportModel.noteTypes)
        {
            if(noteTypeExportModel.noteTypeId == null)
                continue

            val noteType = NoteType(noteTypeExportModel.noteTypeId!!, context)
            if(!noteType.getActorState().generated)
            {
                if(noteTypeExportModel.name == null)
                    continue

                noteType.generate()
                noteType.setNoteTypeName(noteTypeExportModel.name!!)

                noteTypeManager.addNoteType(noteTypeExportModel.noteTypeId!!)
            }

            for(noteExportModel in noteTypeExportModel.notes)
            {
                if(noteExportModel.noteId == null)
                    continue

                val note = Note(noteExportModel.noteId!!, context)
                if(!note.getActorState().generated)
                {
                    if(noteExportModel.text == null)
                        continue

                    note.generate(noteTypeExportModel.noteTypeId!!)
                    note.setNoteText(noteExportModel.text!!)
                    note.setChecked(noteExportModel.checked ?: false)

                    noteType.addNote(noteExportModel.noteId!!)
                }
            }
        }
    }
}