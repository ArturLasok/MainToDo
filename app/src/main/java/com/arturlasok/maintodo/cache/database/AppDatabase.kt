package com.arturlasok.maintodo.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity

@Database(entities = arrayOf(
CategoryToDoEntity::class
),
    version = 1,
    exportSchema = true,
    //autoMigrations = [ AutoMigration(from = 18, to = 19, spec = AppDatabase.MyAutoMig::class)]


)

abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao() : CategoryDao

    companion object{
        val DATABASE_NAME: String = "maintodo_db"
    }

}