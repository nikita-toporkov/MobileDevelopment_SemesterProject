package com.example.courseproject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(navController: NavController, viewModel: TodoViewModel, listId: Int?) {
    val list = viewModel.lists.find { it.id == listId }
    var newTodoText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(list?.title ?: "My New List") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        if (list != null) {
                            viewModel.deleteDoneItems(list.id) 
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete done items")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (list != null) {
                // --- A. New ToDo Input Section ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newTodoText,
                        onValueChange = { newTodoText = it },
                        label = { Text("What we do?") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    FloatingActionButton(
                        onClick = { 
                            if (newTodoText.isNotBlank()) {
                                viewModel.addTodoItem(list.id, newTodoText)
                                newTodoText = ""
                            }
                         },
                        modifier = Modifier.height(56.dp) // Match the TextField height visually
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Task")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- B. ToDo List Display ---
                if (list.items.isEmpty()) {
                    Text(
                        text = "Nothing to display",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(list.items) { item ->
                            TodoItemRow(
                                item = item,
                                onToggleDone = { viewModel.toggleTodoStatus(list.id, item) },
                                onRemove = { viewModel.removeTodoItem(list.id, item) }
                            )
                        }
                    }
                }
            } else {
                Text(text = "List not found")
            }
        }
    }
}

// --- 2. Composable for a single ToDo item row ---
@Composable
fun TodoItemRow(
    item: TodoItem,
    onToggleDone: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleDone) // Toggle completion by clicking the card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkbox and Text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = item.isDone,
                    onCheckedChange = { onToggleDone() }
                )
                Text(
                    text = item.text,
                    style = if (item.isDone) {
                        MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            // A simple strike-through visualization (Compose doesn't have a direct strike-through modifier)
                            // This is a common way to visually indicate completion.
                            // textDecoration = TextDecoration.LineThrough // Uncomment this if you import TextDecoration
                        )
                    } else {
                        MaterialTheme.typography.bodyLarge
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Delete Button
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Remove task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
