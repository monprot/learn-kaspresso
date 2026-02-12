package com.example.kaspresso_learning.features.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kaspresso_learning.Tags
import com.example.kaspresso_learning.data.Tea
import com.example.kaspresso_learning.data.TeaType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaSelectScreen(
    teas: List<Tea>,
    onTeaSelected: (Tea) -> Unit,
    onBack: () -> Unit
) {
    var selectedType by remember { mutableStateOf<TeaType?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedType != null) selectedType!!.displayName
                        else "Выбрать чай"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedType != null) {
                            selectedType = null
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (selectedType == null) {
            TeaTypeList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .semantics { testTag = Tags.TEA_SELECT_CONTAINER },
                onTypeSelected = { selectedType = it }
            )
        } else {
            TeaVarietyList(
                teas = teas.filter { it.type == selectedType },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onTeaSelected = onTeaSelected
            )
        }
    }
}

@Composable
private fun TeaTypeList(
    modifier: Modifier = Modifier,
    onTypeSelected: (TeaType) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(TeaType.entries.toList()) { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeSelected(type) }
                    .padding(horizontal = 16.dp, vertical = 18.dp)
                    .semantics { testTag = "${Tags.TEA_TYPE}_${type.ordinal}" },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = type.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun TeaVarietyList(
    teas: List<Tea>,
    modifier: Modifier = Modifier,
    onTeaSelected: (Tea) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(teas) { tea ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTeaSelected(tea) }
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .semantics { testTag = "${Tags.TEA_ITEM}_${tea.id}" },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tea.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
