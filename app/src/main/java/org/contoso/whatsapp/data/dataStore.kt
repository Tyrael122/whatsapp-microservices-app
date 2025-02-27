package org.contoso.whatsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreHelper(private val context: Context) {

    companion object {
        val QUEUE_NAME_KEY = stringPreferencesKey("queue_name")
    }

    suspend fun saveQueueName(queueName: String) {
        context.dataStore.edit { preferences ->
            preferences[QUEUE_NAME_KEY] = queueName
        }
    }

    fun getQueueName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[QUEUE_NAME_KEY]
        }
    }
}