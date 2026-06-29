package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CalculationRecord
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Tab
import com.example.ui.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

// Custom Color Palette for Clean Utility / Minimal Theme
val BackgroundColor = Color(0xFFF8F9FF)
val SurfaceContainer = Color(0xFFFFFFFF)
val SurfaceContainerHigh = Color(0xFFF1F3F9)
val SurfaceContainerLowest = Color(0xFFE1E2E9)
val AccentOrange = Color(0xFF005FB0)
val FrostBlue = Color(0xFF005FB0)
val OnSurfaceColor = Color(0xFF191C1E)
val OnSurfaceVariantColor = Color(0xFF5E6266)
val BorderColor = Color(0xFFE1E2E9)

@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val history by viewModel.historyState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                currentTab = viewModel.currentTab,
                onTabSelected = { viewModel.onTabSelected(it) }
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewModel.currentTab) {
                Tab.Dashboard -> DashboardTab(
                    viewModel = viewModel,
                    history = history
                )
                Tab.Calculator -> CalculatorTab(
                    viewModel = viewModel
                )
                Tab.Results -> ResultsTab(
                    viewModel = viewModel
                )
                Tab.Settings -> SettingsTab(
                    viewModel = viewModel,
                    history = history
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(currentTab: Tab, onTabSelected: (Tab) -> Unit) {
    NavigationBar(
        containerColor = SurfaceContainer,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp)
    ) {
        val tabs = listOf(
            TabInfo(Tab.Dashboard, "Dashboard", Icons.Default.Home, "tab_dashboard"),
            TabInfo(Tab.Calculator, "Calculadora", Icons.Default.PlayArrow, "tab_calculator"),
            TabInfo(Tab.Results, "Resultados", Icons.Default.CheckCircle, "tab_results"),
            TabInfo(Tab.Settings, "Config", Icons.Default.Settings, "tab_settings")
        )

        tabs.forEach { tab ->
            val selected = currentTab == tab.tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab.tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (selected) AccentOrange else OnSurfaceVariantColor,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        color = if (selected) AccentOrange else OnSurfaceVariantColor,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag(tab.testTag)
            )
        }
    }
}

data class TabInfo(
    val tab: Tab,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardTab(viewModel: MainViewModel, history: List<CalculationRecord>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Industrial Logo",
                        tint = AccentOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AFRICANO WORKSHOP",
                        color = OnSurfaceColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentOrange.copy(alpha = 0.2f))
                        .border(1.dp, AccentOrange, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AF",
                        color = AccentOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Welcome Section
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "Bienvenido al Taller AFRICANO",
                    color = OnSurfaceColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(AccentOrange)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Estación activa: Taller Central",
                        color = OnSurfaceVariantColor,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Main Visual Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(SurfaceContainerHigh, SurfaceContainerHigh.copy(alpha = 0.5f))
                        )
                    )
                    .border(1.dp, BorderColor, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DIAGNÓSTICO EN TIEMPO REAL",
                        color = AccentOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Column {
                        Text(
                            text = "Óptimo para Operaciones",
                            color = OnSurfaceColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Protocolo de cálculo termodinámico activo",
                            color = OnSurfaceVariantColor,
                            fontSize = 12.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Diagnostics",
                    tint = AccentOrange.copy(alpha = 0.08f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(100.dp)
                        .offset(x = 10.dp, y = 10.dp)
                )
            }
        }

        // Quick Access Bento Grid / List
        item {
            Text(
                text = "ACCESO RÁPIDO",
                color = OnSurfaceVariantColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Calculator Bento
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                        .clickable { viewModel.onTabSelected(Tab.Calculator) }
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AccentOrange.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Calc",
                            tint = AccentOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nueva Carga",
                        color = OnSurfaceColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Asistente guiado",
                        color = OnSurfaceVariantColor,
                        fontSize = 11.sp
                    )
                }

                // specs Bento
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                        .clickable { viewModel.onTabSelected(Tab.Settings) }
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AccentOrange.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Specs",
                            tint = AccentOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Especificaciones",
                        color = OnSurfaceColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Base de datos gases",
                        color = OnSurfaceVariantColor,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Live measurements
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "PRESIÓN DE ALTA",
                        color = OnSurfaceVariantColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "320 PSI",
                        color = AccentOrange,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.75f },
                        color = AccentOrange,
                        trackColor = BorderColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "VACÍO",
                        color = OnSurfaceVariantColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "500 MCR",
                        color = FrostBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.45f },
                        color = FrostBlue,
                        trackColor = BorderColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }
            }
        }

        // Recent Activity Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVIDAD RECIENTE",
                    color = OnSurfaceVariantColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                if (history.isNotEmpty()) {
                    Text(
                        text = "Historial completo (${history.size})",
                        color = AccentOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { viewModel.onTabSelected(Tab.Settings) }
                    )
                }
            }
        }

        if (history.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "No history",
                            tint = OnSurfaceVariantColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No hay registros de cálculos guardados",
                            color = OnSurfaceVariantColor,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.onTabSelected(Tab.Calculator) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Iniciar Cálculo", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(history.take(4)) { record ->
                HistoryRecordItem(
                    record = record,
                    onItemClick = { viewModel.selectRecord(record) },
                    onDelete = { viewModel.deleteRecord(record) }
                )
            }
        }
    }
}

@Composable
fun HistoryRecordItem(
    record: CalculationRecord,
    onItemClick: () -> Unit,
    onDelete: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val dateStr = formatter.format(Date(record.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainer)
            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
            .clickable { onItemClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Record",
                    tint = FrostBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${record.equipmentType} - ${record.refrigerantType}",
                    color = OnSurfaceColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "T. Amb: ${record.ambientTemp}°C | T. Evap: ${record.evaporatorTemp}°C",
                    color = OnSurfaceVariantColor,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = dateStr,
                    color = OnSurfaceVariantColor.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "Carga",
                    color = OnSurfaceVariantColor,
                    fontSize = 10.sp
                )
                Text(
                    text = "${record.estimatedChargeGrams}g",
                    color = AccentOrange,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun CalculatorTab(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()

    var showEquipmentDropdown by remember { mutableStateOf(false) }
    var showGasDropdown by remember { mutableStateOf(false) }

    val equipmentOptions = listOf("Split", "Heladera", "Aire Central", "Refrigeración Comercial")
    val gasOptions = listOf("R134a", "R410a", "R600", "R22", "R404a")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = "Cálculo de Carga de Gas",
                color = OnSurfaceColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ingrese los parámetros técnicos para determinar la carga óptima de refrigerante según equipo y condiciones.",
                color = OnSurfaceVariantColor,
                fontSize = 14.sp
            )
        }

        // Technical Setup Section
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Orange accent header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(AccentOrange)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONFIGURACIÓN DEL EQUIPO",
                        color = AccentOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Equipment Type Select
                Column {
                    Text(
                        text = "Tipo de Equipo",
                        color = OnSurfaceVariantColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                            .clickable { showEquipmentDropdown = true }
                            .padding(horizontal = 16.dp)
                            .testTag("equipment_type_select"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (viewModel.equipmentType.isEmpty()) "Seleccione equipo" else viewModel.equipmentType,
                                color = if (viewModel.equipmentType.isEmpty()) OnSurfaceVariantColor.copy(alpha = 0.5f) else OnSurfaceColor,
                                fontSize = 15.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = OnSurfaceVariantColor
                            )
                        }

                        DropdownMenu(
                            expanded = showEquipmentDropdown,
                            onDismissRequest = { showEquipmentDropdown = false },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .background(SurfaceContainerHigh)
                                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                        ) {
                            equipmentOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = OnSurfaceColor) },
                                    onClick = {
                                        viewModel.equipmentType = option
                                        showEquipmentDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Refrigerant Type Select
                Column {
                    Text(
                        text = "Tipo de Gas Refrigerante",
                        color = OnSurfaceVariantColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                            .clickable { showGasDropdown = true }
                            .padding(horizontal = 16.dp)
                            .testTag("refrigerant_type_select"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (viewModel.refrigerantType.isEmpty()) "Seleccione refrigerante" else viewModel.refrigerantType,
                                color = if (viewModel.refrigerantType.isEmpty()) OnSurfaceVariantColor.copy(alpha = 0.5f) else OnSurfaceColor,
                                fontSize = 15.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = OnSurfaceVariantColor
                            )
                        }

                        DropdownMenu(
                            expanded = showGasDropdown,
                            onDismissRequest = { showGasDropdown = false },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .background(SurfaceContainerHigh)
                                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                        ) {
                            gasOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = OnSurfaceColor) },
                                    onClick = {
                                        viewModel.refrigerantType = option
                                        showGasDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Thermal Parameters Section
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(AccentOrange)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PARÁMETROS TÉRMICOS",
                        color = AccentOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Ambient Temp Field
                Column {
                    Text(
                        text = "Temperatura Ambiente (°C)",
                        color = OnSurfaceVariantColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.ambientTemp,
                        onValueChange = { viewModel.ambientTemp = it },
                        placeholder = { Text("Ej: 35.0", color = OnSurfaceVariantColor.copy(alpha = 0.4f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ambient_temp_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentOrange,
                            unfocusedBorderColor = BorderColor,
                            focusedContainerColor = SurfaceContainerHigh,
                            unfocusedContainerColor = SurfaceContainerHigh,
                            focusedTextColor = OnSurfaceColor,
                            unfocusedTextColor = OnSurfaceColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }

                // Evaporator Temp Field
                Column {
                    Text(
                        text = "Temperatura de Evaporador (°C)",
                        color = OnSurfaceVariantColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.evaporatorTemp,
                        onValueChange = { viewModel.evaporatorTemp = it },
                        placeholder = { Text("Ej: 5.0", color = OnSurfaceVariantColor.copy(alpha = 0.4f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("evaporator_temp_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentOrange,
                            unfocusedBorderColor = BorderColor,
                            focusedContainerColor = SurfaceContainerHigh,
                            unfocusedContainerColor = SurfaceContainerHigh,
                            focusedTextColor = OnSurfaceColor,
                            unfocusedTextColor = OnSurfaceColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }

                // Condensator Temp Field
                Column {
                    Text(
                        text = "Temperatura de Condensador (°C)",
                        color = OnSurfaceVariantColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.condensatorTemp,
                        onValueChange = { viewModel.condensatorTemp = it },
                        placeholder = { Text("Ej: 45.0", color = OnSurfaceVariantColor.copy(alpha = 0.4f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("condensator_temp_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentOrange,
                            unfocusedBorderColor = BorderColor,
                            focusedContainerColor = SurfaceContainerHigh,
                            unfocusedContainerColor = SurfaceContainerHigh,
                            focusedTextColor = OnSurfaceColor,
                            unfocusedTextColor = OnSurfaceColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }
        }

        // Action Button
        Button(
            onClick = { viewModel.performCalculation() },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .testTag("calculate_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CALCULAR CARGA",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Calcular",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ResultsTab(viewModel: MainViewModel) {
    val context = LocalContext.current

    when (val state = viewModel.uiState) {
        UiState.Idle -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Calculadora",
                    tint = OnSurfaceVariantColor.copy(alpha = 0.4f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Sin Cálculos Activos",
                    color = OnSurfaceColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Vaya a la pestaña 'Calculadora' para ingresar los parámetros de su equipo de refrigeración.",
                    color = OnSurfaceVariantColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.onTabSelected(Tab.Calculator) },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ir a Calculadora", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        UiState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = AccentOrange, strokeWidth = 4.dp)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "CALCULANDO CARGA OPTIMA...",
                    color = OnSurfaceColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "La IA de Gemini está analizando la termodinámica del gas en tiempo real.",
                    color = OnSurfaceVariantColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            val record = state.record
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Block
                Column {
                    Text(
                        text = "Resultados de Cálculo",
                        color = OnSurfaceColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Informe técnico generado y guardado en la base de datos local.",
                        color = OnSurfaceVariantColor,
                        fontSize = 14.sp
                    )
                }

                // Carga Estimada Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "CARGA ESTIMADA DE GAS",
                            color = AccentOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${record.estimatedChargeGrams}",
                                color = AccentOrange,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 48.sp
                            )
                            Text(
                                text = "gramos",
                                color = OnSurfaceVariantColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = BorderColor)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Refrigerant",
                                tint = FrostBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Refrigerante: ${record.refrigerantType} | Equipo: ${record.equipmentType}",
                                color = FrostBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Pressure Optimization status bar
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Estado de Presión Optimizada",
                            color = OnSurfaceColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.75f },
                            color = AccentOrange,
                            trackColor = BorderColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Baja: 35 PSI",
                                color = OnSurfaceVariantColor,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Alta: 155 PSI",
                                color = OnSurfaceVariantColor,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // AI Recommendations Block
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "AI Advice",
                                tint = FrostBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RECOMENDACIONES DE GEMINI",
                                color = FrostBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Render text content
                        Text(
                            text = record.geminiResponse,
                            color = OnSurfaceColor,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }

                // Pressure Relations Table
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceContainerHigh)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Relaciones Presión / Temperatura (${record.refrigerantType})",
                                color = OnSurfaceColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        // Table header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceContainerHigh.copy(alpha = 0.5f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                "Temp (°C)",
                                modifier = Modifier.weight(1f),
                                color = OnSurfaceVariantColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Presión (PSI)",
                                modifier = Modifier.weight(1f),
                                color = OnSurfaceVariantColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                            Text(
                                "Estado",
                                modifier = Modifier.weight(1f),
                                color = OnSurfaceVariantColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                        }

                        // Table rows
                        val rows = listOf(
                            Triple("-10.0", "29.2", "Vapor"),
                            Triple("-5.0", "35.4", "Vapor"),
                            Triple("0.0", "42.5", "Saturado"),
                            Triple("5.0", "50.8", "Saturado")
                        )

                        rows.forEachIndexed { idx, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (idx % 2 == 0) Color.Transparent else SurfaceContainerHigh.copy(alpha = 0.2f))
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    row.first,
                                    modifier = Modifier.weight(1f),
                                    color = OnSurfaceColor,
                                    fontSize = 13.sp
                                )
                                Text(
                                    row.second,
                                    modifier = Modifier.weight(1f),
                                    color = OnSurfaceColor,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.End
                                )
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (row.third == "Vapor") FrostBlue.copy(alpha = 0.15f) else AccentOrange.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            row.third,
                                            color = if (row.third == "Vapor") FrostBlue else AccentOrange,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Action area buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Informe de Carga Guardado Correctamente", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("save_report_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Done, contentDescription = "Save", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GUARDAR INFORME", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Informe de Refrigeración - Taller AFRICANO")
                                putExtra(
                                    Intent.EXTRA_TEXT, """
                                    Informe Técnico de Carga de Refrigerante:
                                    Equipo: ${record.equipmentType}
                                    Gas: ${record.refrigerantType}
                                    T. Ambiente: ${record.ambientTemp}°C
                                    T. Evaporador: ${record.evaporatorTemp}°C
                                    T. Condensador: ${record.condensatorTemp}°C
                                    Carga Estimada: ${record.estimatedChargeGrams} gramos
                                    
                                    Diagnóstico & Recomendación de Gemini:
                                    ${record.geminiResponse}
                                """.trimIndent()
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Compartir Informe"))
                        },
                        border = BorderStroke(1.dp, BorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("share_pdf_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = OnSurfaceColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("COMPARTIR INFORME", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        is UiState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ocurrió un error",
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.message,
                    color = OnSurfaceVariantColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { viewModel.clearForm() },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Limpiar", color = OnSurfaceColor)
                    }
                    Button(
                        onClick = { viewModel.onTabSelected(Tab.Calculator) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reintentar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTab(viewModel: MainViewModel, history: List<CalculationRecord>) {
    var searchGasQuery by remember { mutableStateOf("") }
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab Title
        Column {
            Text(
                text = "Especificaciones & Database",
                color = OnSurfaceColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Base de datos técnica de refrigerantes, preferencias de unidad y sincronización local.",
                color = OnSurfaceVariantColor,
                fontSize = 14.sp
            )
        }

        // Preferences Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PREFERENCIAS DE UNIDAD",
                    color = AccentOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Unidad de Temperatura", color = OnSurfaceColor, fontSize = 14.sp)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AccentOrange)
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Celsius", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Fahrenheit", color = OnSurfaceVariantColor, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = BorderColor)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Unidad de Presión", color = OnSurfaceColor, fontSize = 14.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Bar / PSI", color = OnSurfaceColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Database Sync Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "BASE DE DATOS LOCAL (ROOM)",
                    color = FrostBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "La aplicación utiliza Room para sincronización y almacenamiento local de cálculos.",
                    color = OnSurfaceVariantColor,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.clearAllHistory() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("clear_history_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Limpiar Todo", color = Color.White, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = { viewModel.clearForm() },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Nueva Sesión", color = OnSurfaceColor, fontSize = 12.sp)
                    }
                }
            }
        }

        // Technical Specs list
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REFRIGERANT SPECS",
                        color = AccentOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    OutlinedTextField(
                        value = searchGasQuery,
                        onValueChange = { searchGasQuery = it },
                        placeholder = { Text("Buscar...", fontSize = 11.sp, color = OnSurfaceVariantColor.copy(alpha = 0.5f)) },
                        modifier = Modifier
                            .width(130.dp)
                            .height(42.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentOrange,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = OnSurfaceColor,
                            unfocusedTextColor = OnSurfaceColor
                        ),
                        shape = RoundedCornerShape(6.dp),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                val specs = listOf(
                    RefrigerantSpec("R-410A", "-48.5°C", "15.57 bar", "2088", "A1"),
                    RefrigerantSpec("R-134a", "-26.3°C", "6.65 bar", "1430", "A1"),
                    RefrigerantSpec("R-32", "-51.7°C", "16.89 bar", "675", "A2L"),
                    RefrigerantSpec("R-290", "-42.1°C", "9.51 bar", "3", "A3"),
                    RefrigerantSpec("R-744", "-78.5°C", "64.3 bar", "1", "A1")
                ).filter { it.id.contains(searchGasQuery, ignoreCase = true) }

                specs.forEachIndexed { index, spec ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(spec.id, color = AccentOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("B. Point: ${spec.boilingPoint}", color = OnSurfaceVariantColor, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("P@25C: ${spec.pressure}", color = OnSurfaceColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("GWP: ${spec.gwp} | ", color = OnSurfaceVariantColor, fontSize = 10.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (spec.safety == "A1") FrostBlue.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.1f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(spec.safety, color = if (spec.safety == "A1") FrostBlue else Color.Red, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    if (index < specs.size - 1) {
                        Divider(color = BorderColor.copy(alpha = 0.4f))
                    }
                }
            }
        }

        // Full Database History Section
        if (history.isNotEmpty()) {
            Text(
                text = "HISTORIAL COMPLETO DE CÁLCULOS",
                color = OnSurfaceVariantColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            history.forEach { record ->
                HistoryRecordItem(
                    record = record,
                    onItemClick = { viewModel.selectRecord(record) },
                    onDelete = { viewModel.deleteRecord(record) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

data class RefrigerantSpec(
    val id: String,
    val boilingPoint: String,
    val pressure: String,
    val gwp: String,
    val safety: String
)
