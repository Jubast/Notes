package com.jubast.notes.containers

// name -> NoteTypeName. notes List of String -> actorId of note
class NoteTypeState{
    var name: String = ""
    var generated: Boolean = false
    var notes: MutableList<String> = mutableListOf()
}