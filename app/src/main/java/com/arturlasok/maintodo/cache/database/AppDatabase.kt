package com.arturlasok.maintodo.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity
import com.arturlasok.maintodo.cache.model.ItemToDoEntity

@Database(entities = arrayOf(
CategoryToDoEntity::class, ItemToDoEntity::class
),
    version = 2,
    exportSchema = true,
    //autoMigrations = [ AutoMigration(from = 18, to = 19, spec = AppDatabase.MyAutoMig::class)]


)

abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao() : CategoryDao
    abstract fun itemDao() : ItemDao

    companion object{
        val DATABASE_NAME: String = "maintodo_db"
    }

}