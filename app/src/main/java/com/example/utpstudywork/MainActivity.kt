package com.example.utpstudywork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.utpstudywork.data.di.ServiceLocator
import com.example.utpstudywork.ui.home.HomeScreen
import com.example.utpstudywork.ui.home.HomeViewModel
import com.example.utpstudywork.ui.home.HomeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val useCases = ServiceLocator.provideUseCases(this)

        setContent {
            val vm: HomeViewModel = viewModel(factory = HomeViewModelFactory(useCases))
            HomeScreen(vm)
        }
    }
}
