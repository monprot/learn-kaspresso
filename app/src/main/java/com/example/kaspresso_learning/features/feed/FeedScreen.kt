package com.example.kaspresso_learning.features.feed

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kaspresso_learning.Tags
import com.example.kaspresso_learning.data.BrewingEntry
import com.example.kaspresso_learning.data.Vessel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val avatarIcons = listOf(
    Icons.Default.Face,
    Icons.Default.AccountBox,
    Icons.Default.Person,
    Icons.Default.Home
)

@Composable
fun FeedScreen(
    userName: String,
    entries: List<BrewingEntry>,
    onNewBrewingClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewBrewingClick,
                modifier = Modifier.semantics { testTag = Tags.FEED_FAB }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Новая запись")
            }
        },
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .semantics { testTag = Tags.FEED_CONTAINER }
        ) {
            Text(
                text = "Привет, $userName!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .semantics { testTag = Tags.FEED_TITLE }
            )

            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Лента пуста. Добавьте первую запись!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .semantics { testTag = Tags.FEED_LIST },
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(entries) { index, entry ->
                        BrewingCard(
                            entry = entry,
                            modifier = Modifier.semantics {
                                testTag = "${Tags.FEED_POST}_$index"
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun BrewingCard(entry: BrewingEntry, modifier: Modifier = Modifier) {
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale("ru"))

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon: ImageVector = avatarIcons.getOrElse(entry.authorAvatarIndex) { Icons.Default.Face }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .semantics { testTag = Tags.FEED_POST_AVATAR },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = entry.authorName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.semantics { testTag = Tags.FEED_POST_AUTHOR_NAME }
                    )
                    Text(
                        text = dateFormat.format(Date(entry.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.semantics { testTag = Tags.FEED_POST_DATE }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = entry.tea.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { testTag = Tags.FEED_POST_TEA_NAME }
            )
            Text(
                text = entry.tea.type.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { testTag = Tags.FEED_POST_TEA_TYPE }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "${entry.weightGrams} г",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { testTag = Tags.FEED_POST_WEIGHT }
                )
                if (entry.volumeMl != null) {
                    Text(
                        text = "${entry.volumeMl} мл",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.semantics { testTag = Tags.FEED_POST_VOLUME }
                    )
                }
                if (entry.vessel != null) {
                    Text(
                        text = when (entry.vessel) {
                            Vessel.GAIWAN -> "Гайвань"
                            Vessel.TEAPOT -> "Чайник"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.semantics { testTag = Tags.FEED_POST_VESSEL }
                    )
                }
                if (entry.infusions != null) {
                    Text(
                        text = "${entry.infusions} прол.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.semantics { testTag = Tags.FEED_POST_INFUSIONS }
                    )
                }
            }
        }
    }
}
