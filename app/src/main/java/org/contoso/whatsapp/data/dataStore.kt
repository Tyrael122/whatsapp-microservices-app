package org.contoso.whatsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreHelper(private val context: Context) {

    companion object {
        val USER_QUEUE_MAPPING_KEY = stringPreferencesKey("user_queue_mapping")
    }

    suspend fun saveUserQueueMapping(userId: String, queueName: String) {
        context.dataStore.edit { preferences ->
            val currentMapping = preferences[USER_QUEUE_MAPPING_KEY]?.let {
                Gson().fromJson(it, Map::class.java) as? Map<String, String>
            } ?: emptyMap()

            val updatedMapping = currentMapping.toMutableMap().apply {
                put(userId, queueName)
            }

            preferences[USER_QUEUE_MAPPING_KEY] = Gson().toJson(updatedMapping)
        }
    }

    fun getUserQueueMapping(): Flow<Map<String, String>> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_QUEUE_MAPPING_KEY]?.let {
                Gson().fromJson(it, Map::class.java) as? Map<String, String>
            } ?: emptyMap()
        }
    }

    suspend fun getQueueNameForUser(userId: String): String? {
        return context.dataStore.data.map { preferences ->
            preferences[USER_QUEUE_MAPPING_KEY]?.let {
                Gson().fromJson(it, Map::class.java) as? Map<String, String>
            }?.get(userId)
        }.first()
    }
}