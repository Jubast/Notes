package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.AppStrings
import com.jubast.notes.containers.AppLanguageState

class AppLanguage(context: Context) : StateActor<AppLanguageState>(".", context, ::AppLanguageState) {
    fun setLanguage(lang: String){
        state.language = lang
        saveState()
    }

    fun getAppStrings(): AppStrings{
        if(state.language.isNotEmpty()){
            try {
                return AppStrings.valueOf(state.language)
            } catch (e: IllegalArgumentException) { /* Nothing to do */ }
        }

        return AppStrings.English
    }
}