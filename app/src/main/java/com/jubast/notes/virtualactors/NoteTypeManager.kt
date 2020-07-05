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
    fun createNoteType(name: String): String{
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

    fun addNoteType(noteTypeId: String) {
        state.noteTypes.add(noteTypeId)
        saveState()
    }

    fun removeNoteType(actorId: String){
        if(state.noteTypes.contains(actorId)){
            state.noteTypes.remove(actorId)
        }

        saveState()
    }


}