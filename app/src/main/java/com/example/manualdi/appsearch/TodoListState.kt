package com.example.manualdi.appsearch

data class TodoListState(
    val todos: List<Todo> = emptyList(),
    val searchQuery: String = "",
    val filter: Int = 0
)
