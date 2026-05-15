package com.gramaurja.ui.screens.pump

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.ui.components.AccentBannerCard
import com.gramaurja.ui.components.MetricCard
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.ui.components.SectionTitle
import com.gramaurja.ui.components.SelectorField
import com.gramaurja.ui.theme.DeepTeal
import com.gramaurja.ui.theme.SoftGold
import com.gramaurja.ui.theme.SoftLeaf
import com.gramaurja.utils.formatTimestamp
import com.gramaurja.utils.t
import com.gramaurja.viewmodel.PumpTimerUiState

@Composable
fun PumpTimerScreen(
    uiState: PumpTimerUiState,
    onCropChange: (String) -> Unit,
    onMotorHpChange: (String) -> Unit,
    onWaterRequirementChange: (String) -> Unit,
    onCalculate: () -> Unit
) {
    val crops = listOf("Paddy", "Sugarcane", "Vegetables", "Millets")
    val loadPercent = (uiState.progress * 100).toInt()
    val loadLabel = when {
        uiState.progress < 0.34f -> t("light_load")
        uiState.progress < 0.67f -> t("moderate_load")
        else -> t("heavy_load")
    }
    val loadTip = when {
        uiState.progress < 0.34f -> t("light_load_copy")
        uiState.progress < 0.67f -> t("moderate_load_copy")
        else -> t("heavy_load_copy")
    }
    val loadColor = when {
        uiState.progress < 0.34f -> MaterialTheme.colorScheme.tertiary
        uiState.progress < 0.67f -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AccentBannerCard(
                title = t("pump_timer"),
                subtitle = t("pump_subtitle"),
                trailing = { Text("${loadPercent}%", fontWeight = FontWeight.Bold) }
            )
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SelectorField(
                        label = t("crop_type"),
                        value = uiState.cropType,
                        options = crops,
                        onSelect = onCropChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.motorHp,
                        onValueChange = onMotorHpChange,
                        label = { Text(t("motor_hp")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.waterRequirement,
                        onValueChange = onWaterRequirementChange,
                        label = { Text(t("water_requirement")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = onCalculate, modifier = Modifier.fillMaxWidth()) {
                        Text(t("calculate"))
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutdoorReadableCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(220.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(t("runtime_guidance"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(
                                uiState.result.substringAfter(": ", uiState.result),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                        }
                        Text(
                            t("runtime_guidance_copy"),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                OutdoorReadableCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(220.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(t("load_meter"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { uiState.progress },
                                    modifier = Modifier.size(88.dp),
                                    color = loadColor,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    strokeWidth = 8.dp
                                )
                                Text("${loadPercent}%", fontWeight = FontWeight.Bold, color = DeepTeal)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(loadColor, CircleShape)
                                    )
                                    Text(loadLabel, fontWeight = FontWeight.SemiBold)
                                }
                                Text(loadTip, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        LinearProgressIndicator(
                            progress = { uiState.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
                            color = loadColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(t("light"), style = MaterialTheme.typography.labelSmall)
                            Text(t("moderate"), style = MaterialTheme.typography.labelSmall)
                            Text(t("heavy"), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    label = t("motor_input"),
                    value = uiState.motorHp.ifBlank { "--" },
                    supporting = t("horsepower_entered"),
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    containerColor = SoftLeaf
                )
                MetricCard(
                    label = t("water_need"),
                    value = uiState.waterRequirement.ifBlank { "--" },
                    supporting = t("requested_units"),
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }
        item { SectionTitle(t("recent_updates")) }
        items(uiState.history.take(6)) { item ->
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.cropType, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${t("runtime_prefix")} ${item.duration}")
                    Text(formatTimestamp(item.timestamp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
