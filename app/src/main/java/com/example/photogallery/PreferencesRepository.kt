package com.example.photogallery

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.lang.IllegalArgumentException

class PreferencesRepository(private val datastore: DataStore<Preferences>) {

    val storedQuery: Flow<String> = datastore.data.map {
        it[SEARCH_QUERY_KEY] ?: ""
    }.distinctUntilChanged()

    suspend fun setStoredQuery(query: String) {
        datastore.edit {
            it[SEARCH_QUERY_KEY] = query
        }
    }

    companion object {
        private val SEARCH_QUERY_KEY = stringPreferencesKey("search_query")
        private var INSTANCE: PreferencesRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val datastore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("settings")
                }
                INSTANCE = PreferencesRepository(datastore)
            }
        }

        fun get() : PreferencesRepository {
            return INSTANCE ?: throw IllegalArgumentException(
                "PreferenceRepository must be initialized"
            )
        }
    }
}