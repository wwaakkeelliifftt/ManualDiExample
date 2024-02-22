package com.example.manualdi.appsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    sheetState: SheetState,
    action: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: AppSearchViewModel
) {
    var todoTitle by remember { mutableStateOf(viewModel.newTodo.title) }
    var todoContent by remember { mutableStateOf(viewModel.newTodo.text) }
    var todoScore by remember { mutableIntStateOf(viewModel.newTodo.score) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            viewModel.holdNewTodoData(
                title = todoTitle,
                content = todoContent,
                score = todoScore
            )
            onDismiss()
        },
    ) {
        TextField(
            value = todoTitle,
            onValueChange = { todoTitle = it },
            singleLine = true,
            placeholder = { Text(text = "title") },
            trailingIcon = {
                if (todoTitle.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable { todoTitle = "" }
                    )
                } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 5.dp)
        )

        TextField(
            value = todoContent,
            onValueChange = { todoContent = it },
            maxLines = 4,
            placeholder = { Text(text = "content") },
            trailingIcon = {
                if (todoContent.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable { todoContent = "" }
                    )
                } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            (1..10).forEach { score -> 
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .background(if (todoScore == score) Color.Green else Color.LightGray)
                        .clickable {
                            todoScore = score
                        }
                ) {
                    Text(text = "$score", modifier = Modifier
                        .padding(1.dp)
                        .align(Alignment.Center))
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = {
                viewModel.holdNewTodoData(
                    title = todoTitle,
                    content = todoContent,
                    score = todoScore
                )
                action()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "ADD")
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}
