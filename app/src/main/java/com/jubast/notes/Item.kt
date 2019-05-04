package com.jubast.notes

data class Item(var text: String, var checked: Boolean){
    var id: Int = 0

    override fun toString(): String {
        return text
    }

    fun toString1(): String{
        return "id: $id, name: $text, checked: $checked"
    }

    fun toIdentifyString(): String{
        return id.toString() + text
    }
}