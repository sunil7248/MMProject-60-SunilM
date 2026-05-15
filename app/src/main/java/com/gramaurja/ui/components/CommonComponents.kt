package com.gramaurja.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.R
import com.gramaurja.ui.theme.DeepTeal
import com.gramaurja.ui.theme.GreenOn
import com.gramaurja.ui.theme.RedOff
import com.gramaurja.ui.theme.SoftGold
import com.gramaurja.ui.theme.SoftLeaf
import com.gramaurja.utils.t

@Composable
fun SectionTitle(title: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        if (actionLabel != null && onAction != null) {
            AssistChip(
                onClick = onAction,
                label = { Text(actionLabel) },
                colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Bolt, contentDescription = null)
                }
            )
        }
    }
}

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = "Grama-Urja logo",
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        painter = painterResource(id = R.drawable.grama_urja_logo),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}

@Composable
fun BrandedSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        action = {
            snackbarData.visuals.actionLabel?.let { label ->
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = snackbarData::performAction)
                )
            }
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppLogo(modifier = Modifier.size(48.dp), contentDescription = null)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = snackbarData.visuals.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = t("app_name"),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(isOn: Boolean) {
    val color = if (isOn) GreenOn else RedOff
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(color, color.copy(alpha = 0.25f))
                )
            )
    )
}

@Composable
fun OutdoorReadableCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun AccentBannerCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    supportColor: Color = MaterialTheme.colorScheme.secondary,
    trailing: @Composable (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = accentColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(title, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(subtitle, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f), style = MaterialTheme.typography.bodyMedium)
            }
            if (trailing != null) {
                Box(
                    modifier = Modifier
                        .background(supportColor, RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    trailing()
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    supporting: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    valueColor: Color = MaterialTheme.colorScheme.primary
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = DeepTeal)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = valueColor)
            Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SelectorField(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProgressSummary(label: String, progress: Float, tint: Color) {
    val animated by animateFloatAsState(targetValue = progress, label = label)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        LinearProgressIndicator(
            progress = animated.coerceIn(0f, 1f),
            modifier = Modifier.fillMaxWidth(),
            color = tint
        )
    }
}

@Composable
fun EmptyStateCard(message: String) {
    OutdoorReadableCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = message)
        }
    }
}

@Composable
fun FilterChipRow(
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { option ->
                    FilterChip(
                        selected = option == selectedOption,
                        onClick = { onSelect(option) },
                        label = { Text(option) }
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineList(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { item ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                StatusIndicator(isOn = !item.contains("OFF"))
                Text(text = item)
            }
        }
    }
}

@Composable
fun SimpleBarChart(
    values: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    val max = (values.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)
    OutdoorReadableCard(modifier = modifier) {
        Text(t("weekly_outage_trends"), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            values.forEach { (label, value) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .height((32 + (120 * (value / max.toFloat()))).dp)
                            .fillMaxWidth(0.2f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(label)
                    Text(value.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
