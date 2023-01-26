package com.arturlasok.maintodo.interactors

import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity
import com.arturlasok.maintodo.cache.model.ItemToDoEntity
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.util.RoomDataState
import kotlinx.coroutines.flow.*


class RoomInter(
    private val categoryDao: CategoryDao,
    private val itemDao: ItemDao
) {
    //item from domain to entity
    fun itemFromDomainToEntity(itemToDo: ItemToDo) : ItemToDoEntity {
        return ItemToDoEntity(
         item_id_room = itemToDo.dItemId,
         item_title_room = itemToDo.dItemTitle,
         item_author_room = itemToDo.dItemAuthor,
         item_desc_room = itemToDo.dItemDescription,
         item_importance_room = itemToDo.dItemImportance,
         item_completed_room = itemToDo.dItemCompleted,
         item_added_room = itemToDo.dItemAdded,
         item_edited_room = itemToDo.dItemEdited,
         item_imported_room = itemToDo.dItemImported,
         item_exported_room= itemToDo.dItemExported,
         item_token_room = itemToDo.dItemToken,
         item_group_room = itemToDo.dItemGroup,
         item_info_room = itemToDo.dItemInfo,
         item_why_failed_room = itemToDo.dItemWhyFailed,
         item_delivery_time_room = itemToDo.dItemDeliveryTime,
         item_remind_time_room = itemToDo.dItemRemindTime,
         item_limit_time_room = itemToDo.dItemLimitTime
        )
    }
    //item from entity to domain
    fun itemFromEntityToDomain(itemEntity: ItemToDoEntity) : ItemToDo {
        return ItemToDo(
        dItemId = itemEntity.item_id_room,
        dItemTitle = itemEntity.item_title_room,
        dItemAuthor = itemEntity.item_author_room,
        dItemDescription = itemEntity.item_desc_room,
        dItemImportance = itemEntity.item_importance_room,
        dItemCompleted = itemEntity.item_completed_room,
        dItemAdded = itemEntity.item_added_room,
        dItemEdited = itemEntity.item_edited_room,
        dItemImported = itemEntity.item_imported_room,
        dItemExported = itemEntity.item_exported_room,
        dItemToken = itemEntity.item_token_room,
        dItemGroup = itemEntity.item_group_room,
        dItemInfo = itemEntity.item_info_room,
        dItemWhyFailed = itemEntity.item_why_failed_room,
        dItemDeliveryTime = itemEntity.item_delivery_time_room,
        dItemRemindTime = itemEntity.item_remind_time_room,
        dItemLimitTime =itemEntity.item_limit_time_room
        )
    }
    //item from entity list to domain list
    fun itemFromEntityListToDomainList(itemEntityList: List<ItemToDoEntity>) : List<ItemToDo> {
        return itemEntityList.map {
            itemFromEntityToDomain(it)
        }
    }
    //item from domain list to entity list
    fun itemFromDomainListToEntityList(itemDomainList: List<ItemToDo>) : List<ItemToDoEntity> {
        return itemDomainList.map {
            itemFromDomainToEntity(it)
        }
    }

    //category domain to entity
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
    //category entity to domain
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

    //category entity list to domain list
    fun categoryFromListEntityToListDomain(categoryListEntity: List<CategoryToDoEntity>) : List<CategoryToDo> {
        return categoryListEntity.map {
            categoryFromEntityToDomain(it)
        }
    }
    //category domain list to entity list
    fun categoryFromListDomainToListEntity(categoryListDomain: List<CategoryToDo>) : List<CategoryToDoEntity> {
        return categoryListDomain.map {
            categoryFromDomainToEntity(it)
        }
    }
    //update task item completion
    fun updateTaskItemCompletion(taskItem: ItemToDo) : Flow<RoomDataState<Boolean>> = flow {
        try {

            itemDao.updateItemCompleteInRoom(!taskItem.dItemCompleted,taskItem.dItemToken)
             emit(RoomDataState.data_stored(true))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }
    }

    //insert task item to Database with category token
    fun insertTaskItemToRoom(taskItem: ItemToDo) : Flow<RoomDataState<Boolean>> = flow {

        try {
            val categoryData = categoryDao.selectOneCategory(taskItem.dItemInfo.toLong())

            if(!categoryData.category_token_room.isNullOrEmpty()) {
                itemDao.insertItemToRoom(
                    itemFromDomainToEntity(taskItem)
                        .copy(item_info_room = categoryData.category_token_room)
                )
                emit(RoomDataState.data_stored(true))
            } else
            {
                emit(RoomDataState.data_error("room_error"))
            }

        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }
    //update category to Database
    fun insertCategoryToRoomWithOldToken(categoryToDo: CategoryToDo) : Flow<RoomDataState<Boolean>> = flow {

        try {
            val categoryData = categoryDao.selectOneCategory(categoryToDo.dCatId ?: 0)

            if(!categoryData.category_token_room.isNullOrEmpty()) {


                categoryDao.insertCategoryToRoom(
                    categoryFromDomainToEntity(categoryToDo)
                        .copy(category_token_room = categoryData.category_token_room)
                )
                emit(RoomDataState.data_stored(true))
            } else {
                emit(RoomDataState.data_error("room_error"))
            }


        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }
    //insert category to Database
    fun insertCategoryToRoom(categoryToDo: CategoryToDo) : Flow<RoomDataState<Boolean>> = flow {

        try {

            categoryDao.insertCategoryToRoom(categoryFromDomainToEntity(categoryToDo))
            emit(RoomDataState.data_stored(true))

        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }
    //delete item
    fun deleteItem(itemId: Long) : Flow<RoomDataState<Boolean>> = flow {
        try {
            itemDao.deleteFromItemRoomById(itemId)
            emit(RoomDataState.data_stored(true))

        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }
    //delete category
    fun deleteCategory(categoryId: Long) : Flow<RoomDataState<Boolean>> = flow {
        try {
            //token of deleted category
            val categoryToken = categoryFromEntityToDomain(categoryDao.selectOneCategory(categoryId))
            //delete category
            categoryDao.deleteFromCategoryRoomById(categoryId)
            //delete all task items with category token
            itemDao.deleteFromItemRoomByCategoryId(categoryToken.dCatToken.toString())

            emit(RoomDataState.data_stored(true))


        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }
    //get task
    fun getTasksFromRoom(categoryId: Long) : Flow<RoomDataState<Boolean>> = flow {


        try {

            val tasksList = if(categoryId<0) {
                itemDao.selectAllFromItemRoom()
            } else {

                val categoryData = categoryDao.selectOneCategory(categoryId)

                itemDao.selectAllFromItemRoomWithCategoryToken(categoryData.category_token_room ?: "")
            }

            if(tasksList.isNotEmpty()) {
                emit(RoomDataState.data_recived(itemFromEntityListToDomainList(tasksList.toMutableList())))
            } else emit(RoomDataState.data_recived(mutableListOf<ItemToDo>()))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }
    //count open tasks for badges
    fun getCount(categoryToken: String) : Flow<Int> = itemDao.countAllFromItemRoomWithCategoryToken(categoryToken)
    //
    fun categoryIdToCategoryString(categoryId: Long) : Flow<String> = flow {


        try {
            val categoryData = categoryFromEntityToDomain(categoryDao.selectOneCategory(categoryId))
            emit(categoryData.dCatToken ?:"")

        }
        catch(e:Exception) {

            emit("")
        }


    }
    //get all category from database
    fun getCategoryFromRoom() : Flow<RoomDataState<Boolean>> = flow {

        try {

            val categoryList = categoryDao.selectAllFromCategoryRoom()

            if(categoryList.isNotEmpty()) {
                emit(RoomDataState.data_recived(categoryFromListEntityToListDomain(categoryList)))
            } else emit(RoomDataState.data_recived(emptyList<CategoryToDo>()))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }
    //get one itemfrom database
    fun getOneItemFromRoom(itemId: Long) : Flow<RoomDataState<Boolean>> = flow {

        try {

            val oneItem = itemDao.selectOneItem(itemId)

            if(oneItem.item_id_room!=null) {
                emit(RoomDataState.data_recived(itemFromEntityToDomain(oneItem)))
            } else emit(RoomDataState.data_error("room_error"))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }
    //get one category from database
    fun getOneCategoryFromRoom(categoryId: Long) : Flow<RoomDataState<Boolean>> = flow {

        try {

            val oneCategory = categoryDao.selectOneCategory(categoryId)

            if(oneCategory.category_id_room!=null) {
                emit(RoomDataState.data_recived(categoryFromEntityToDomain(oneCategory)))
            } else emit(RoomDataState.data_error("room_error"))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }
    //get one category from database with token
    fun getOneCategoryFromRoomWithToken(categoryToken: String) : Flow<RoomDataState<Boolean>> = flow {

        try {

            val oneCategory = categoryDao.selectOneCategoryWithToken(categoryToken)

            if(oneCategory.category_id_room!=null) {
                emit(RoomDataState.data_recived(categoryFromEntityToDomain(oneCategory)))
            } else emit(RoomDataState.data_error("room_error"))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }
    //get last added category
    fun getLastCategoryId() : Flow<RoomDataState<Boolean>> = flow {

        try {
            val lastId = categoryDao.selectLastAddedCategoryId()
            if(lastId!=0L) {
                emit(RoomDataState.data_recived(lastId))
            } else {
                emit(RoomDataState.data_error("room_error"))
            }

        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }
    }


}