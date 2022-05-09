package br.com.douglasmotta.jetpackdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationAttributes
import android.widget.Button
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import br.com.douglasmotta.jetpackdatastore.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> = createDataStore("settings")

    private lateinit var binding: ActivityMainBinding


    val isSaveBoolean = preferencesKey<Boolean>("IS_SAVE_BOOLEAN")
    val isSaveString = preferencesKey<String>("IS_SAVE_STRING")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.run {
            buttonSave.setOnClickListener {
                if (swType.isChecked){
                    lifecycleScope.launch {
                        val value = switch2.isChecked
                        setIsSaveBoolean(value)
                    }
                }else{
                    lifecycleScope.launch {
                        val value = inputValue.text?.toString() ?: ""
                        saveData(value)
                        binding.inputValue.setText("")
                    }
                }
            }

            buttonRead.setOnClickListener {
                if (swType.isChecked){
                    lifecycleScope.launch {
                        textDataStoredValue.text = getIsReadBoolean().toString()
                    }
                }else{
                    lifecycleScope.launch {
                        textDataStoredValue.text = readData() ?: "Nenhum valor encontrato para a chave"
                    }
                }
            }
        }
    }

    private suspend fun saveData(value: String) {
        dataStore.edit { settings ->
            settings[isSaveString] = value
        }
    }

    private suspend fun readData(): String? {
        val prefs = dataStore.data.first()
        return prefs[isSaveString]
    }

    private suspend fun setIsSaveBoolean(isDarkTheme: Boolean) {
        dataStore.edit { pref ->
            pref[isSaveBoolean] = isDarkTheme
        }
    }

    private suspend fun getIsReadBoolean() : Boolean {
        val preferences = dataStore.data.first()
        return preferences[isSaveBoolean] ?: false
    }
}