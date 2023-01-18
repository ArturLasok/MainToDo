package com.arturlasok.maintodo.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryToRoom(category: CategoryToDoEntity)

    @Query("DELETE FROM category_room WHERE category_id_room= :category_id")
    suspend fun deleteFromCategoryRoomById(category_id: Long)

    @Query("SELECT * FROM category_room")
    suspend fun selectAllFromCategoryRoom() : List<CategoryToDoEntity>

    @Query("SELECT * FROM category_room WHERE category_id_room=:category_id")
    suspend fun selectOneCategory(category_id: Long) : CategoryToDoEntity

    @Query("SELECT category_id_room FROM category_room ORDER BY category_id_room DESC LIMIT 1")
    suspend fun selectLastAddedCategoryId() : Long


}