package com.jubast.notes.containers

class NotesExportModel {
    var noteTypes: MutableList<NoteTypeExportModel> = mutableListOf()
    var widgets: MutableList<WidgetExportModel> = mutableListOf()
    var services: ServiceExportModel = ServiceExportModel()
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

class WidgetExportModel {
    var widgetId: String? = null
    var noteTypeId: String? = null
}

class ServiceExportModel {
    var service1: String? = null
    var service2: String? = null
    var service3: String? = null
    var service4: String? = null
}