package com.jubast.notes.virtualactors

import android.content.Context
import com.google.gson.Gson
import com.jubast.notes.containers.NoteExportModel
import com.jubast.notes.containers.NoteManagerState
import com.jubast.notes.containers.NoteTypeExportModel
import com.jubast.notes.containers.NotesExportModel
import com.jubast.notes.virtualactors.abstract.StateActor
import java.lang.Exception
import java.util.*

class NoteTypeManager(context: Context) : StateActor<NoteManagerState>(".", context, ::NoteManagerState) {
    fun addNoteType(name: String): String{
        var noteTypeId: String
        while (true){
            noteTypeId = UUID.randomUUID().toString()
            val noteType = NoteType(noteTypeId, context)
            if(!noteType.generate()) {
                // retry if a noteType with such actorId already exists
                continue
            }

            noteType.setNoteTypeName(name)
            break
        }

        state.noteTypes.add(noteTypeId)
        saveState()
        return noteTypeId
    }

    fun removeNoteType(actorId: String){
        if(state.noteTypes.contains(actorId)){
            state.noteTypes.remove(actorId)
        }

        saveState()
    }

    fun exportJson(): String {
        val export = NotesExportModel()

        for(noteTypeId in state.noteTypes)
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

                state.noteTypes.add(noteTypeExportModel.noteTypeId!!)
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

        saveState()
    }
}