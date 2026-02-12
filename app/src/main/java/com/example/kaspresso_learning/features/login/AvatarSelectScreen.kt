package com.example.kaspresso_learning.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.example.kaspresso_learning.Tags

@Composable
fun AvatarSelectScreen(
    onAvatarSelected: (Int) -> Unit
) {
    var selectedAvatarIndex by remember { mutableStateOf(-1) }
    var showError by remember { mutableStateOf(false) }

    val avatars = listOf(
        Icons.Default.Face,
        Icons.Default.AccountBox,
        Icons.Default.Person,
        Icons.Default.Home
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { testTag = Tags.AVATAR_CONTAINER },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Выберите аватар",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            avatars.forEachIndexed { index, icon ->
                AvatarItem(
                    icon = icon,
                    isSelected = selectedAvatarIndex == index,
                    testTag = "${Tags.AVATAR_ICON}_$index",
                    onClick = {
                        selectedAvatarIndex = index
                        showError = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (showError) {
            Text(
                text = "Сначала выберите аватар!",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics { testTag = Tags.AVATAR_ERROR }
            )
        }

        Button(
            onClick = {
                if (selectedAvatarIndex != -1) {
                    onAvatarSelected(selectedAvatarIndex)
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = Tags.AVATAR_NEXT_BUTTON }
        ) {
            Text("Далее")
        }
    }
}

@Composable
fun AvatarItem(
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable { onClick() }
            .semantics { this.testTag = testTag },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Avatar",
            modifier = Modifier.size(48.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
