package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.NoteWidgetState
import com.jubast.notes.virtualactors.abstract.StateActor
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException

class NoteWidget(actorId: String, context: Context): StateActor<NoteWidgetState>(actorId, context, ::NoteWidgetState) {
    fun generate(): Boolean{
        if(state.generated){
            return false
        }

        state.generated = true
        saveState()
        return true
    }

    fun setNoteTypeId(noteTypeId: String){
        if(!state.generated) throw InvalidOperationException("NoteWidget '$actorId' not generated")

        state.noteTypeId = noteTypeId
        saveState()
    }

    fun delete(){
        if(!state.generated) throw InvalidOperationException("NoteWidget '$actorId' not generated")

        deleteState()
    }
}

