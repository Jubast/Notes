package com.jubast.notes.virtualactors.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jubast.notes.virtualactors.interfaces.IActorStateStorage
import java.lang.reflect.Type
import kotlin.reflect.KClass

class SharedPreferencesActorStateStorage<TState: Any>(val actorId: String, val context: Context, val factory: () -> TState): IActorStateStorage<TState> {
    private val spName : String = "SharedPreferencesDefaultActorStateStorage"

    override fun getState(): TState {
        val json = getSharedPreferences().getString(actorId, "")
        return Gson().fromJson<TState>(json, factory()::class.java) ?: factory()
    }

    override fun saveState(state: TState) {
        val json = Gson().toJson(state)
        getSharedPreferences().edit().putString(actorId, json).apply()
    }

    override fun deleteState() {
        getSharedPreferences().edit().remove(actorId).apply()
    }

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }
}