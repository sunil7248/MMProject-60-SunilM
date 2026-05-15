package com.gramaurja.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.gramaurja.ui.components.SimpleBarChart
import com.gramaurja.ui.components.TimelineList
import com.gramaurja.ui.theme.SoftGold
import com.gramaurja.ui.theme.SoftLeaf
import com.gramaurja.utils.formatTimestamp
import com.gramaurja.utils.t
import com.gramaurja.viewmodel.AnalyticsUiState

@Composable
fun AnalyticsScreen(uiState: AnalyticsUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AccentBannerCard(
                title = t("history_analytics"),
                subtitle = t("alerts_subtitle"),
                trailing = {
                    Text(
                        text = uiState.totalUpdates.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    label = t("current_status"),
                    value = uiState.currentStatus,
                    supporting = t("latest_feeder_state"),
                    modifier = Modifier.weight(1f),
                    containerColor = SoftLeaf
                )
                MetricCard(
                    label = t("outages_today"),
                    value = uiState.outageCountToday.toString(),
                    supporting = t("power_off_events_today"),
                    modifier = Modifier.weight(1f),
                    containerColor = SoftGold
                )
            }
        }
        item {
            SimpleBarChart(values = uiState.weeklyOutageTrend, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(t("recent_timeline"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    TimelineList(items = uiState.timeline.take(5))
                }
            }
        }
        item { SectionTitle(t("status_table")) }
        items(uiState.allStatuses.take(10)) { item ->
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                val full = formatTimestamp(item.timestamp)
                val date = full.substringBefore(',')
                val time = full.substringAfter(", ").ifBlank { full }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(date)
                    Text(time)
                    Text(item.status, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
