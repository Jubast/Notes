package com.jubast.notes.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.jubast.notes.AppStrings
import com.jubast.notes.R
import com.jubast.notes.virtualactors.AppLanguage
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val item = parent.getItemAtPosition(position) as String
        val appLang = AppLanguage(this)
        if (appLang.state.language.contentEquals(item)) {
            return
        }
        appLang.setLanguage(item)

        val appStrings = appLang.getAppStrings()

        AlertDialog.Builder(this).also{
            it.setTitle(appStrings.restartApp)
            it.setMessage(appStrings.restartAppMsg)
            it.setPositiveButton(appStrings.yes) { _, _ ->
                restartApp()
            }
            it.setNegativeButton(appStrings.no) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            it.setIcon(R.drawable.refresh)
            it.show()
        }
    }

    private fun restartApp()
    {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(AlarmManager.RTC,
                System.currentTimeMillis() + 1000, pendingIntent)
        System.exit(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setText(AppLanguage(this).getAppStrings())
        setSpinnerArrayAdapter()
        // sets the index of spinner
        setPositionOfSavedLanguage()
        setListeners()
    }

    private fun setText(appStrings: AppStrings) {
        twLanguage.text = appStrings.language
    }

    private fun setSpinnerArrayAdapter()
    {
        val appLangs = mutableListOf<String>()
        AppStrings.values().forEach {
            appLangs.add(it.name)
        }

        ArrayAdapter(this, R.layout.settings_spiner_item, appLangs).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sLanguage.adapter = it
        }
    }

    // sets the selected item for the spinner
    // so if English is selected, it puts English as the selected item
    private fun setPositionOfSavedLanguage()
    {
        val lang = AppLanguage(this).state.language
        var i: Int = 0
        while (i < sLanguage.count)
        {
            val item = sLanguage.getItemAtPosition(i) as String
            if(lang.contentEquals(item))
            {
                sLanguage.setSelection(i)
                return
            }
            ++i
        }
    }

    private fun setListeners()
    {
        sLanguage.onItemSelectedListener = this
    }
}
