package com.arturlasok.maintodo.interactors

import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.interactors.util.RoomDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomInter(
    private val categoryDao: CategoryDao
) {

    fun categoryFromDomainToEntity(categoryToDo: CategoryToDo) : CategoryToDoEntity {
        return CategoryToDoEntity(
            category_id_room = categoryToDo.dCatId,
            category_name_room = categoryToDo.dCatName,
            category_desc_room = categoryToDo.dCatDescription,
            category_token_room = categoryToDo.dCatToken,
            category_icon_room = categoryToDo.dCatIcon,
            category_sort_room = categoryToDo.dCatSort,
            category_fav_room = categoryToDo.dCatFav
        )
    }
    fun categoryFromEntityToDomain(categoryToDoEntity: CategoryToDoEntity) : CategoryToDo {
        return CategoryToDo(
             dCatId = categoryToDoEntity.category_id_room,
             dCatName = categoryToDoEntity.category_name_room,
             dCatDescription = categoryToDoEntity.category_desc_room,
             dCatToken = categoryToDoEntity.category_token_room,
             dCatIcon = categoryToDoEntity.category_icon_room,
             dCatSort = categoryToDoEntity.category_sort_room,
             dCatFav = categoryToDoEntity.category_fav_room
        )
    }

    fun insertCategoryToRoom(categoryToDo: CategoryToDo) : Flow<RoomDataState<Boolean>> = flow {

        try {

            categoryDao.insertCategoryToRoom(categoryFromDomainToEntity(categoryToDo))

            emit(RoomDataState.data_stored(true))

        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }


}