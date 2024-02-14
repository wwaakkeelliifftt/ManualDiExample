package com.example.manualdi.appsearch

import android.content.Context
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.RemoveByDocumentIdRequest
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.localstorage.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoSearchManager(
    private val appContext: Context
) {

    companion object {
        const val DATABASE_NAME = "todo"
        const val NAMESPACE_FILTER_FOR_SOME_USER = "user_John_Doe_#12345"

        fun getRankingFilter(use: Int): Int {
            return when (use) {
                0 -> SearchSpec.RANKING_STRATEGY_RELEVANCE_SCORE
                1 -> SearchSpec.RANKING_STRATEGY_CREATION_TIMESTAMP             // always rewrite without save as data and == USAGE_COUNT
                2 -> SearchSpec.RANKING_STRATEGY_USAGE_COUNT
                3 -> SearchSpec.RANKING_STRATEGY_DOCUMENT_SCORE
                else -> SearchSpec.RANKING_STRATEGY_CREATION_TIMESTAMP
            }
        }

    }

    private var appSearchSession: AppSearchSession? = null

    suspend fun init() {
        withContext(Dispatchers.IO) {
            val sessionFuture = LocalStorage.createSearchSessionAsync(
                LocalStorage.SearchContext.Builder(
                    appContext,
                    DATABASE_NAME
                ).build()
            )
            val setSchemaRequest = SetSchemaRequest.Builder()
                .addDocumentClasses(Todo::class.java)
                .build()

            appSearchSession = sessionFuture.get()
            appSearchSession?.setSchemaAsync(setSchemaRequest)
        }
    }

    suspend fun putTodos(todos: List<Todo>): Boolean {
        return withContext(Dispatchers.IO) {
            // "put" == "insert" in appSearch
            appSearchSession?.putAsync(
                PutDocumentsRequest.Builder()
                    .addDocuments(todos)
                    .build()
            )
                ?.get()
                ?.isSuccess == true
        }
    }

    suspend fun searchTodos(
        query: String,
        count: Int = 50,
        rankingStrategy: Int = SearchSpec.RANKING_STRATEGY_CREATION_TIMESTAMP
    ): List<Todo> {
        return withContext(Dispatchers.IO) {
            val searchSpec = SearchSpec.Builder()
                .setSnippetCount(count)
                .addFilterNamespaces(NAMESPACE_FILTER_FOR_SOME_USER)
                .setRankingStrategy(rankingStrategy)
                .build()
            val result = appSearchSession?.search(query, searchSpec) ?: return@withContext emptyList()
            val page = result.nextPageAsync.get()
            page.mapNotNull { searchResult ->
                if (searchResult.genericDocument.schemaType == Todo::class.java.simpleName) {
                    searchResult.getDocument(Todo::class.java)
                } else null
            }
        }
    }

    suspend fun clearDb() {
        withContext(Dispatchers.IO) {
            val searchSpec = SearchSpec.Builder().build()
            appSearchSession?.removeAsync("", searchSpec)
        }
    }

    fun closeSession() {
        appSearchSession?.close()
        appSearchSession = null
    }

}