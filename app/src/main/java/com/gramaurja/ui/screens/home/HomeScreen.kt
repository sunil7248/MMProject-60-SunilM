package com.gramaurja.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.ui.components.AccentBannerCard
import com.gramaurja.ui.components.MetricCard
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.ui.components.SectionTitle
import com.gramaurja.ui.components.StatusIndicator
import com.gramaurja.ui.theme.DeepTeal
import com.gramaurja.ui.theme.SoftGold
import com.gramaurja.ui.theme.SoftLeaf
import com.gramaurja.utils.t
import com.gramaurja.utils.timeAgo
import com.gramaurja.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSetPower: (String) -> Unit
) {
    val isOn = uiState.currentStatus == "ON"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AccentBannerCard(
                title = if (isOn) "${t("power_on")} • ${uiState.currentZone}" else "${t("power_off")} • ${uiState.currentZone}",
                subtitle = if (isOn) uiState.energyTip else "${t("latest_feeder_state")}: ${uiState.freshness}",
                trailing = {
                    Icon(
                        imageVector = Icons.Rounded.TrendingUp,
                        contentDescription = null,
                        tint = DeepTeal
                    )
                }
            )
        }
        item { SectionTitle(t("dashboard")) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    label = t("status_now"),
                    value = if (isOn) t("power_on") else t("power_off"),
                    supporting = uiState.lastUpdated,
                    modifier = Modifier.weight(1f),
                    containerColor = if (isOn) SoftLeaf else MaterialTheme.colorScheme.surface,
                    valueColor = if (isOn) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                )
                MetricCard(
                    label = t("outages_today"),
                    value = uiState.totalOutagesToday.toString(),
                    supporting = uiState.freshness,
                    modifier = Modifier.weight(1f),
                    containerColor = SoftGold,
                    valueColor = DeepTeal
                )
            }
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(t("quick_action"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        text = t("home_quick_action_copy"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onSetPower("ON") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(t("power_on"))
                        }
                        Button(
                            onClick = { onSetPower("OFF") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = Color.White
                            )
                        ) {
                            Text(t("power_off"))
                        }
                    }
                }
            }
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(t("useful_now"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(uiState.weatherMessage, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${t("energy_tip_prefix")} ${uiState.energyTip}", fontWeight = FontWeight.Medium)
                }
            }
        }
        item { SectionTitle(t("recent_updates")) }
        items(uiState.statusHistory.take(6)) { status ->
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (status.status == "ON") SoftLeaf else SoftGold,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Bolt, contentDescription = null, tint = DeepTeal)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                if (status.status == "ON") t("power_on") else t("power_off"),
                                fontWeight = FontWeight.Bold
                            )
                            Text(status.zone, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        StatusIndicator(isOn = status.status == "ON")
                        Text(timeAgo(status.timestamp), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
