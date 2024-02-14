package com.example.manualdi.appsearch

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.manualdi.ManualDIApp
import com.example.manualdi.viewModelFactory

@Composable
fun TodoAppSearchScreen(navController: NavController) {
    val viewModel = viewModel<AppSearchViewModel>(
        factory = viewModelFactory {
            AppSearchViewModel(ManualDIApp.appModule.todoSearchManager)
        }
    )
    val context = LocalContext.current
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            placeholder = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .background(Color(0xff112233))
                .padding(12.dp)
                .fillMaxWidth()
        )

        FunctionRow(
            vm = viewModel,
            context = context,
            filterState = state.filter
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(state.todos) { todo ->
                TodoItem(
                    todo = todo,
                    onDoneChange = { isDone ->
                        viewModel.onDoneChange(todo, isDone = isDone)
                    }
                )
            }
        }

        BottomRow(navController, viewModel)
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onDoneChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = todo.title, fontSize = 16.sp)
            Text(text = todo.text, fontSize = 12.sp)
        }
        Checkbox(checked = todo.isDone, onCheckedChange = onDoneChange)
    }
}

@Composable
fun FunctionRow(
    vm: AppSearchViewModel,
    context: Context,
    filterState: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff112233))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0..3) {
            Button(
                onClick = {
                    vm.changeFilterState(use = i)
//                    Toast.makeText(context, "use == $i", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (filterState == i) Color.DarkGray else Color.Red
                ),
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
                    .background(
                        if (filterState == i) Color.Green else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )

            ) {
                Text(
                    text = when (i) {
                        0 -> "Relevance"
                        1 -> "Timestamp"
                        2 -> "Usage"
                        3 -> "Rank"
                        else -> ".?."
                    },
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
fun BottomRow(
    navController: NavController,
    viewModel: AppSearchViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff112233)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.clickable {
                    viewModel.clearDatabase()
                }
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.clickable {
                    viewModel.demoFill()
                }
            )
        }
        Row(
            modifier = Modifier
                .weight(4f)
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray)
                .clickable { navController.popBackStack() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Go back",
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                color = Color.Cyan,
                modifier = Modifier.padding(6.dp)
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.clickable {
                    // todo: open bottom sheet
                }
            )
        }
    }
}

@Composable
@Preview
fun Testo() {

}
