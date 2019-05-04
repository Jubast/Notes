package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.virtualactors.storage.SharedPreferencesActorStateStorage

open class StateActor<TState: Any>(actorId: String, val context: Context, factory: () -> TState) : Actor(actorId) {
    private val stateStorage = SharedPreferencesActorStateStorage("${this.javaClass.name}-$actorId", context, factory)
    val state = stateStorage.getState()

    protected fun saveState(){
        stateStorage.saveState(state)
    }

    protected fun deleteState(){
        stateStorage.deleteState()
    }
}