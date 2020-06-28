package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.NoteTypeState
import com.jubast.notes.virtualactors.abstract.StateActor
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException
import java.util.*

class NoteType(actorId: String, context: Context) : StateActor<NoteTypeState>(actorId, context, ::NoteTypeState) {
    fun generate(): Boolean{
        if(state.generated){
            return false
        }

        state.generated = true
        return true
    }

    fun setNoteTypeName(name: String){
        if(!state.generated) throw InvalidOperationException("NoteType '$actorId' not generated")

        state.name = name
        saveState()
    }

    fun createNote(noteText: String) {
        if(!state.generated) throw InvalidOperationException("NoteType '$actorId' not generated")

        var noteId: String
        while (true){
            noteId = UUID.randomUUID().toString()
            val note = Note(noteId, context)
            if(!note.generate(actorId)) {
                // retry if a note with such actorId already exists
                continue
            }

            note.setNoteText(noteText)
            note.setChecked(false)
            break
        }

        state.notes.add(noteId)
        saveState()
    }

    fun addNote(noteId: String) {
        if(!state.generated) throw InvalidOperationException("NoteType '$actorId' not generated")

        if(!state.notes.contains(noteId))
            state.notes.add(noteId)

        saveState()
    }

    fun removeNote(noteId: String){
        if(!state.generated) throw InvalidOperationException("NoteType '$actorId' not generated")

        if (state.notes.contains(noteId)) {
            state.notes.remove(noteId)
        }

        saveState()
    }

    fun delete(){
        if(!state.generated) throw InvalidOperationException("NoteType '$actorId' not generated")

        for (actorId in state.notes){
            Note(actorId, context).delete()
        }

        NoteTypeManager(context).removeNoteType(actorId)
        deleteState()
    }
}