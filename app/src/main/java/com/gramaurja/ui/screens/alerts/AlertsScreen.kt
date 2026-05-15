package com.gramaurja.ui.screens.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.ui.components.AccentBannerCard
import com.gramaurja.ui.components.MetricCard
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.ui.components.SectionTitle
import com.gramaurja.ui.theme.SoftGold
import com.gramaurja.ui.theme.SoftLeaf
import com.gramaurja.utils.formatTimestamp
import com.gramaurja.utils.t
import com.gramaurja.viewmodel.AlertsUiState

@Composable
fun AlertsScreen(uiState: AlertsUiState) {
    val warningCount = uiState.alerts.count { it.message.contains("OFF") || it.message.contains("fault", true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AccentBannerCard(
                title = t("community_alert_center"),
                subtitle = t("alerts_subtitle"),
                trailing = { Icon(Icons.Rounded.Notifications, contentDescription = null) }
            )
        }
        item { SectionTitle(t("alerts")) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    label = t("alerts_summary"),
                    value = uiState.alerts.size.toString(),
                    supporting = t("stored_warning_messages"),
                    modifier = Modifier.weight(1f),
                    containerColor = SoftGold
                )
                MetricCard(
                    label = t("warnings"),
                    value = warningCount.toString(),
                    supporting = t("needs_field_attention"),
                    modifier = Modifier.weight(1f),
                    containerColor = SoftLeaf,
                    valueColor = MaterialTheme.colorScheme.error
                )
            }
        }
        item { SectionTitle(t("activity")) }
        items(uiState.powerUpdates.take(6)) { update ->
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Rounded.Notifications, contentDescription = null)
                        Text(
                            "${if (update.status == "ON") t("power_on") else t("power_off")} • ${update.zone}",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(formatTimestamp(update.timestamp), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        if (uiState.alerts.isNotEmpty()) {
            item { SectionTitle(t("warnings")) }
            items(uiState.alerts.take(6)) { alert ->
                OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Rounded.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        androidx.compose.foundation.layout.Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(alert.message, fontWeight = FontWeight.SemiBold)
                            Text(formatTimestamp(alert.timestamp), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
