package com.example.courseproject

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class TodoItem(
    val id: Int = 0,
    val text: String,
    val isDone: Boolean = false
)

data class TodoList(val id: Int, val title: String, val items: List<TodoItem> = emptyList())

class TodoViewModel : ViewModel() {
    private val _lists = mutableStateOf<List<TodoList>>(emptyList())
    val lists: List<TodoList> get() = _lists.value

    private var listIdCounter = 1
    private var listTitleCounter = 1

    fun createNewList() {
        val newList = TodoList(id = listIdCounter++, title = "My New List ${listTitleCounter++}")
        _lists.value = _lists.value + newList
    }

    fun getList(id: Int): TodoList? {
        return _lists.value.find { it.id == id }
    }

    fun renameList(listId: Int, newTitle: String) {
        _lists.value = _lists.value.map {
            if (it.id == listId) {
                it.copy(title = newTitle)
            } else {
                it
            }
        }
    }

    fun deleteList(listId: Int) {
        _lists.value = _lists.value.filter { it.id != listId }
    }

    fun addTodoItem(listId: Int, itemText: String) {
        val list = getList(listId) ?: return
        val newItem = TodoItem(id = (list.items.maxOfOrNull { it.id } ?: 0) + 1, text = itemText)
        val updatedList = list.copy(items = list.items + newItem)
        _lists.value = _lists.value.map {
            if (it.id == listId) updatedList else it
        }
    }

    fun toggleTodoStatus(listId: Int, item: TodoItem) {
        val list = getList(listId) ?: return
        val updatedItems = list.items.map {
            if (it.id == item.id) it.copy(isDone = !it.isDone) else it
        }
        val updatedList = list.copy(items = updatedItems)
        _lists.value = _lists.value.map {
            if (it.id == listId) updatedList else it
        }
    }

    fun removeTodoItem(listId: Int, item: TodoItem) {
        val list = getList(listId) ?: return
        val updatedItems = list.items.filter { it.id != item.id }
        val updatedList = list.copy(items = updatedItems)
        _lists.value = _lists.value.map {
            if (it.id == listId) updatedList else it
        }
    }

    fun deleteDoneItems(listId: Int) {
        val list = getList(listId) ?: return
        val updatedItems = list.items.filter { !it.isDone }
        val updatedList = list.copy(items = updatedItems)
        _lists.value = _lists.value.map {
            if (it.id == listId) updatedList else it
        }
    }

    fun deleteAllLists() {
        _lists.value = emptyList()
        listTitleCounter = 1
    }
}
