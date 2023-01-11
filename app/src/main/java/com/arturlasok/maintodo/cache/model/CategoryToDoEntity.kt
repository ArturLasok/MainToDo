package com.arturlasok.maintodo.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_room")
data class CategoryToDoEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id_room")
    val category_id_room : Long?,

    @ColumnInfo(name ="category_name_room")
    val category_name_room: String?,

    @ColumnInfo(name="category_desc_room")
    val category_desc_room: String?,

    @ColumnInfo(name="category_token_room")
    val category_token_room: String?,

    @ColumnInfo(name="category_icon_room")
    val category_icon_room: Int?,

    @ColumnInfo(name = "category_sort_room")
    val category_sort_room: Int?,

    @ColumnInfo(name = "category_fav_room")
    val category_fav_room: Boolean?

)
