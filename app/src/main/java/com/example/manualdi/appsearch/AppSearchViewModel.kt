package com.example.manualdi.appsearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class AppSearchViewModel(
    private val todoSearchManager: TodoSearchManager
): ViewModel() {

    var state by mutableStateOf(TodoListState())
        private set

    private var searchJob: Job? = null

    init {
        demoFill()
    }

    fun demoFill() {
        viewModelScope.launch {
            todoSearchManager.init()
            val todos = (1..100).map {
                Todo(
                    namespace = TodoSearchManager.NAMESPACE_FILTER_FOR_SOME_USER,
                    id = UUID.randomUUID().toString(),
                    score = Random.nextInt(1, 10),
                    title = "Todo title #$it",
                    text = "text content $it",
                    isDone = Random.nextBoolean()
                )
            }
            todoSearchManager.putTodos(todos)
        }
        onSearchQueryChange(state.searchQuery)
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            val todos = todoSearchManager.searchTodos(query = query)
            state = state.copy(todos = todos)
        }
    }

    fun onDoneChange(todo: Todo, isDone: Boolean) {
        viewModelScope.launch {
            todoSearchManager.putTodos(
                listOf(todo.copy(isDone = isDone))
            )
            state = state.copy(
                todos = state.todos.map {
                    if (it.id == todo.id) {
                        it.copy(isDone = isDone)
                    } else it
                }
            )
        }
    }

    fun changeFilterState(use: Int) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val todos = todoSearchManager.searchTodos(
                query = state.searchQuery,
                rankingStrategy = TodoSearchManager.getRankingFilter(use = use)
            )
            state = state.copy(todos = todos, filter = use)
        }
    }

    fun clearDatabase() {
        searchJob?.cancel()
        viewModelScope.launch {
            todoSearchManager.clearDb()
            state = state.copy(todos = emptyList())
        }
    }

    override fun onCleared() {
        todoSearchManager.closeSession()
        super.onCleared()
    }

}