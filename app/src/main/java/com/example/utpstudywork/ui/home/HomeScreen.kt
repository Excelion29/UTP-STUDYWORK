package com.example.utpstudywork.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.utpstudywork.domain.model.SessionType

private fun Int.formatAsTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun HomeScreen(vm: HomeViewModel) {
    val ui = vm.state.collectAsState().value
    HomeContent(
        state = ui,
        onStart = vm::startTimer,
        onPause = vm::pauseTimer,
        onStop = vm::stopTimer,
        onSwitchTab = vm::switchTab
    )
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onSwitchTab: (SessionType) -> Unit
) {
    val tabIndex = if (state.timer.type == SessionType.WORK) 0 else 1
    val primary = MaterialTheme.colorScheme.primary

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true,  onClick = {}, icon = { Icon(Icons.Filled.Home, null) }, label = { Text("Inicio") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.EventNote, null) }, label = { Text("Notas") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.BarChart, null) }, label = { Text("Stats") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.Settings, null) }, label = { Text("Config") })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("¡Hola!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))

            TabRow(selectedTabIndex = tabIndex, containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                Tab(selected = tabIndex == 0, onClick = { onSwitchTab(SessionType.WORK) }, text = { Text("Trabajo") })
                Tab(selected = tabIndex == 1, onClick = { onSwitchTab(SessionType.STUDY) }, text = { Text("Estudio") })
            }

            Spacer(Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = primary.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (state.timer.type == SessionType.WORK) "Sesión de Trabajo" else "Sesión de Estudio",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = state.timer.remainingSec.formatAsTime(),
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FilledIconButton(onClick = onStart) { Icon(Icons.Filled.PlayArrow, "Iniciar") }
                        FilledTonalIconButton(onClick = onPause) { Icon(Icons.Filled.Pause, "Pausar") }
                        OutlinedIconButton(onClick = onStop) { Icon(Icons.Filled.Stop, "Detener") }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Progreso de Hoy", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ProgressItem(label = "Trabajo", minutes = state.workMinToday)
            Spacer(Modifier.height(8.dp))
            ProgressItem(label = "Estudio", minutes = state.studyMinToday)

            Spacer(Modifier.height(16.dp))

            Text("Notas Recientes", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            if (state.upcomingNotes.isEmpty()) {
                NoteItem(title = "Reunión cliente proyecto X", subtitle = "Revisar propuesta y presupuesto • 18:30")
                NoteItem(title = "Matemáticas - Derivadas", subtitle = "Estudiar regla de la cadena • 20:00")
            } else {
                state.upcomingNotes.forEach { note ->
                    NoteItem(title = note.title, subtitle = note.description)
                }
            }
        }
    }
}

@Composable
private fun ProgressItem(label: String, minutes: Int) {
    val goal = 60 * 4
    val ratio = (minutes.toFloat() / goal).coerceIn(0f, 1f)
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("$label: ${minutes.asHm()}")
            Text("${(ratio * 100).toInt()}%")
        }
        LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(6.dp))
        )
    }
}

@Composable
private fun NoteItem(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun Int.asHm(): String = "${this / 60}h ${this % 60}m"

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeContent(
        state = HomeUiState(
            timer = TimerUi(),
            workMinToday = 150,
            studyMinToday = 105
        ),
        onStart = {},
        onPause = {},
        onStop = {},
        onSwitchTab = {}
    )
}