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

    @Query("DELETE FROM category_room WHERE category_token_room= :category_token")
    suspend fun deleteFromCategoryRoomById(category_token:String)

    @Query("SELECT * FROM category_room")
    suspend fun selectAllFromCategoryRoom() : List<CategoryToDoEntity>

    @Query("SELECT * FROM category_room WHERE category_token_room=:category_token")
    suspend fun selectOneCategory(category_token: String) : CategoryToDoEntity

    @Query("SELECT * FROM category_room WHERE category_token_room=:category_token")
    suspend fun selectOneCategoryWithToken(category_token: String) : CategoryToDoEntity

    @Query("SELECT category_token_room FROM category_room ORDER BY category_id_room DESC LIMIT 1")
    suspend fun selectLastAddedCategoryToken() : String

    @Query("SELECT category_icon_room FROM category_room WHERE category_token_room=:category_token")
    suspend fun fromCategoryTokenToCategoryIcon(category_token: String) : Int

}