package com.jubast.notes.virtualactors.abstract

import android.content.Context
import com.google.gson.Gson
import com.jubast.notes.containers.NoteState
import com.jubast.notes.virtualactors.storage.SharedPreferencesActorStateStorage

open class StateActor<TState: Any>(actorId: String, val context: Context, val factory: () -> TState) : Actor(actorId) {
    private val stateStorage = SharedPreferencesActorStateStorage("${this.javaClass.name}-$actorId", context, factory)
    protected val state = stateStorage.getState()

    fun getActorState(): TState {
        return Gson().fromJson(Gson().toJson(state), factory()::class.java) // deep copy
    }

    protected fun saveState(){
        stateStorage.saveState(state)
    }

    protected fun deleteState(){
        stateStorage.deleteState()
    }
}