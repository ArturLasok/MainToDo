package com.arturlasok.maintodo.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arturlasok.maintodo.cache.model.ItemToDoEntity

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemToRoom(item: ItemToDoEntity)

    @Query("DELETE FROM item_room WHERE item_id_room= :item_id")
    suspend fun deleteFromItemRoomById(item_id: Long)

    @Query("SELECT * FROM item_room")
    suspend fun selectAllFromItemRoom() : List<ItemToDoEntity>

    @Query("SELECT * FROM item_room WHERE item_id_room=:item_id")
    suspend fun selectOneItem(item_id: Long) : ItemToDoEntity

    @Query("SELECT * FROM item_room WHERE item_info=:category_token")
    suspend fun selectAllFromItemRoomWithCategoryToken(category_token:String) : List<ItemToDoEntity>

    @Query("UPDATE item_room SET item_completed=:new_completed_value WHERE item_token=:item_token")
    suspend fun updateItemCompleteInRoom(new_completed_value: Boolean,item_token: String)



}