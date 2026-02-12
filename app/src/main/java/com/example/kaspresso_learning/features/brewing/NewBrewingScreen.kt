package com.example.kaspresso_learning.features.brewing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kaspresso_learning.Tags
import com.example.kaspresso_learning.data.Tea
import com.example.kaspresso_learning.data.Vessel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBrewingScreen(
    selectedTea: Tea?,
    onSelectTeaClick: () -> Unit,
    onBack: () -> Unit,
    onSave: (tea: Tea, weightGrams: Int, volumeMl: Int?, vessel: Vessel?, infusions: Int?) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var volumeText by remember { mutableStateOf("") }
    var selectedVessel by remember { mutableStateOf<Vessel?>(null) }
    var selectedInfusions by remember { mutableStateOf<Int?>(null) }
    var infusionsExpanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новая запись") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .semantics { testTag = Tags.BREWING_CONTAINER }
        ) {
            // Select tea (обязательно)
            Text(
                text = "Чай *",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedButton(
                onClick = onSelectTeaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTag = Tags.BREWING_SELECT_TEA }
            ) {
                Text(text = selectedTea?.name ?: "Выбрать чай")
            }

            if (selectedTea != null) {
                Text(
                    text = "${selectedTea.name} · ${selectedTea.type.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .semantics { testTag = Tags.BREWING_SELECTED_TEA_NAME }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Weight (обязательно)
            Text(
                text = "Вес (г) *",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = weightText,
                onValueChange = { weightText = it.filter { ch -> ch.isDigit() } },
                label = { Text("Граммы") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTag = Tags.BREWING_WEIGHT }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Vessel (необязательно)
            Text(
                text = "Посуда",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected = selectedVessel == Vessel.GAIWAN,
                    onClick = {
                        selectedVessel = if (selectedVessel == Vessel.GAIWAN) null else Vessel.GAIWAN
                    },
                    label = { Text("Гайвань") },
                    modifier = Modifier.semantics { testTag = Tags.BREWING_VESSEL_GAIWAN }
                )
                FilterChip(
                    selected = selectedVessel == Vessel.TEAPOT,
                    onClick = {
                        selectedVessel = if (selectedVessel == Vessel.TEAPOT) null else Vessel.TEAPOT
                    },
                    label = { Text("Чайник") },
                    modifier = Modifier.semantics { testTag = Tags.BREWING_VESSEL_TEAPOT }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Volume (необязательно)
            Text(
                text = "Объём (мл)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = volumeText,
                onValueChange = { volumeText = it.filter { ch -> ch.isDigit() } },
                label = { Text("Миллилитры") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTag = Tags.BREWING_VOLUME }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Infusions dropdown (необязательно)
            Text(
                text = "Количество проливов",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = infusionsExpanded,
                onExpandedChange = { infusionsExpanded = it },
                modifier = Modifier.semantics { testTag = Tags.BREWING_INFUSIONS }
            ) {
                OutlinedTextField(
                    value = selectedInfusions?.toString() ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Проливы") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = infusionsExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = infusionsExpanded,
                    onDismissRequest = { infusionsExpanded = false }
                ) {
                    (0..10).forEach { n ->
                        DropdownMenuItem(
                            text = { Text("$n") },
                            onClick = {
                                selectedInfusions = n
                                infusionsExpanded = false
                            },
                            modifier = Modifier.semantics {
                                testTag = "${Tags.BREWING_INFUSIONS_ITEM}_$n"
                            }
                        )
                    }
                }
            }

            if (showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Выберите чай и укажите вес",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val weight = weightText.toIntOrNull()
                    val volume = volumeText.toIntOrNull()
                    if (selectedTea != null && weight != null && weight > 0) {
                        onSave(
                            selectedTea,
                            weight,
                            if (volume != null && volume > 0) volume else null,
                            selectedVessel,
                            selectedInfusions
                        )
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .semantics { testTag = Tags.BREWING_SAVE }
            ) {
                Text("Сохранить")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
