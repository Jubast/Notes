package com.jubast.notes.virtualactors.interfaces

interface IActorStateStorage<TState> {
    fun saveState(state: TState)
    fun getState() : TState
    fun deleteState()
}