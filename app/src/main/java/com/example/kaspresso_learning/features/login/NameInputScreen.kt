package com.example.kaspresso_learning.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.example.kaspresso_learning.Tags

@Composable
fun NameInputScreen(
    onLoginClick: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { testTag = Tags.NAME_CONTAINER },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Как вас зовут?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .testTag(Tags.NAME_INPUT_TITLE)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                if (it.isNotEmpty()) isError = false
            },
            label = { Text("Имя") },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = Tags.NAME_INPUT_TEXT }
        )

        if (isError) {
            Text(
                text = "Имя не может быть пустым",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // КНОПКА БЕЗ ТЕГА НАМЕРЕННО — ДЛЯ ОБУЧЕНИЯ
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    onLoginClick(name)
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Tags.NAME_INPUT_LOGIN_BUTTON)
        ) {
            Text("Войти")
        }
    }
}
