package com.jubast.notes

enum class AppStrings {
    English {
        override val unknownError = "UNKNOWN ERROR!"
        override val example = "EXAMPLE"
        override val add = "ADD"
        override val addNote = "ADD NOTE"
        override val edit = "EDIT"
        override val save = "SAVE"
        override val remove = "REMOVE"
        override val types = "NOTES TYPES"
        override val typeName = "Note type name:"
        override val typeExists = "Type already exists!"
        override val typeAdded = "Type successfully added!"
        override val noteAdded = "Note successfully added!"
        override val typeUpdated = "Type successfully updated!"
        override val noteUpdated = "Note successfully updated!"
        override val yourNotes = "Your notes:"
        override val language = "Set the language:"
        override val export = "Copy Notes as Json to Clipboard:"
        override val exportMsg = "COPY"
        override val import = "Import Notes:"
        override val importMsg = "IMPORT"
        override val restartApp = "Application restart?"
        override val restartAppMsg = "An application restart is needed!"
        override val yes = "Yes"
        override val no = "No"
        override val noTypeInput = "ENTER A TYPE!"
        override val noNoteInput = "ENTER A NOTE!"
    },
    Slovenian {
        override val unknownError = "NEZNANA NAPAKA!"
        override val example = "PRIMER"
        override val add = "DODAJ"
        override val addNote = "DODAJ ZAPISEK"
        override val edit = "UREDI"
        override val save = "SHRANI"
        override val remove = "ODSTRANI"
        override val types = "TIPI ZAPISKOV"
        override val typeName = "Tip zapiska:"
        override val typeExists = "Ta tip že obstaja!"
        override val typeAdded = "Tip usprešno dodan!"
        override val noteAdded = "Zapisek usprešno dodan!"
        override val typeUpdated = "Tip usprešno posodobljen!"
        override val noteUpdated = "Zapisek usprešno posodobljen!"
        override val yourNotes = "Tvoji zapiski:"
        override val language = "Nastavi jezik:"
        override val export = "Kopiraj zapiske kot json v odložišče:"
        override val exportMsg = "KOPIRAJ"
        override val import = "Uvozi zapiske iz json:"
        override val importMsg = "UVOZI"
        override val restartApp = "Ponovni zagon aplikacije?"
        override val restartAppMsg = "Ponovni zagon aplikacije je potreben!"
        override val yes = "Da"
        override val no = "Ne"
        override val noTypeInput = "VNESITE IME TIPA!"
        override val noNoteInput = "VNESITE ZAPISEK!"
    };

    abstract val unknownError: String
    abstract val example: String
    abstract val add: String
    abstract val addNote: String
    abstract val edit: String
    abstract val save: String
    abstract val remove: String
    abstract val types: String
    abstract val typeName: String
    abstract val typeExists: String
    abstract val typeAdded: String
    abstract val noteAdded: String
    abstract val typeUpdated: String
    abstract val noteUpdated: String
    abstract val yourNotes: String
    abstract val language: String
    abstract val export: String
    abstract val exportMsg: String
    abstract val import: String
    abstract val importMsg: String
    abstract val restartApp: String
    abstract val restartAppMsg: String
    abstract val yes: String
    abstract val no: String
    abstract val noTypeInput: String
    abstract val noNoteInput: String
}