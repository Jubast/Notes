package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.NoteManagerState
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
}