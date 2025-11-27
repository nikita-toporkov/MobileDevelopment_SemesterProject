package com.example.courseproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.courseproject.ui.theme.CourseProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            CourseProjectTheme(darkTheme = themeViewModel.isDarkTheme) {
                AppNavigation(themeViewModel = themeViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val todoViewModel: TodoViewModel = viewModel()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController, todoViewModel)
        }
        composable("settings") {
            SettingsScreen(navController, themeViewModel, todoViewModel)
        }
        composable(
            "todo/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId")
            ToDoScreen(navController, todoViewModel, listId)
        }
    }
}
