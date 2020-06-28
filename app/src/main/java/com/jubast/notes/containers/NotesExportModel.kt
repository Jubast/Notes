package com.jubast.notes.containers

class NotesExportModel {
    var noteTypes: MutableList<NoteTypeExportModel> = mutableListOf()
}

class NoteTypeExportModel {
    var noteTypeId: String? = null
    var name: String? = null
    var notes: MutableList<NoteExportModel> = mutableListOf()
}

class NoteExportModel {
    var noteId: String? = null
    var text: String? = null
    var checked: Boolean? = null
}