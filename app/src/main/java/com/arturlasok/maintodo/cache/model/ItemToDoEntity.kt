package com.arturlasok.maintodo.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_room")
data class ItemToDoEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id_room")
    val item_id_room: Long?,

    @ColumnInfo(name = "item_title")
    val item_title_room : String,

    @ColumnInfo(name = "item_author")
    val item_author_room: String,

    @ColumnInfo(name = "item_desc")
    val item_desc_room: String,

    @ColumnInfo(name = "item_importance")
    val item_importance_room: Int,

    @ColumnInfo(name = "item_completed")
    val item_completed_room: Boolean,

    @ColumnInfo(name = "item_added")
    val item_added_room: Long,

    @ColumnInfo(name ="item_edited")
    val item_edited_room: Long,

    @ColumnInfo(name= "item_imported")
    val item_imported_room: String,

    @ColumnInfo(name ="item_exported")
    val item_exported_room: String,

    @ColumnInfo(name="item_token")
    val item_token_room: String,

    @ColumnInfo(name="item_group")
    val item_group_room:String,

    @ColumnInfo(name="item_info")
    val item_info_room:String,

    @ColumnInfo(name = "item_why_failed")
    val item_why_failed_room:String,

    @ColumnInfo(name="item_delivery_time")
    val item_delivery_time_room: Long,

    @ColumnInfo(name = "item_remind_time")
    val item_remind_time_room: Long,

    @ColumnInfo(name = "item_limit_time")
    val item_limit_time_room:Long

)
