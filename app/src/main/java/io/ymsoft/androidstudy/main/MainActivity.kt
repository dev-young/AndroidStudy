package io.ymsoft.androidstudy.main

import android.content.Context
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import io.ymsoft.androidstudy.base.BaseActivity
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "test",
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, context.packageName))
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test1 = flow<Int> {
            delay(5000)
            repeat(100) {
                emit(it)
            }
        }
        lifecycleScope.launch {
            val f =  test1.first()
            println("[MainActivity][onCreate]: $f")
        }

        val first = runBlocking {
            dataStore.data.map { it.get(stringPreferencesKey("key")).let { data ->
                if (!data.isNullOrEmpty())
                    data.length
                else
                    null
            } }.firstOrNull()
        }

        println("[MainActivity][onCreate]: $first")

    }
}