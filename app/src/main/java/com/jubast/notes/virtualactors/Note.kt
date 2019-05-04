package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.NoteState
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException

class Note(actorId: String, context: Context) : StateActor<NoteState>(actorId, context, ::NoteState) {
    fun generate(noteTypeId: String) : Boolean{
        if(state.generated){
            return false
        }

        state.generated = true
        state.noteTypeId = noteTypeId
        return true
    }

    fun setNoteText(text: String) {
        if(!state.generated) throw InvalidOperationException("Note '$actorId' not generated")

        state.text = text
        saveState()
    }

    fun setChecked(checked: Boolean){
        if(!state.generated) throw InvalidOperationException("Note '$actorId' not generated")

        state.checked = checked
        saveState()
    }

    fun delete(){
        if(!state.generated) throw InvalidOperationException("Note '$actorId' not generated")

        NoteType(state.noteTypeId, context).removeNote(actorId)
        deleteState()
    }
}