package com.gramaurja.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gramaurja.BuildConfig
import com.gramaurja.ui.components.AccentBannerCard
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.ui.components.SelectorField
import com.gramaurja.utils.t
import com.gramaurja.utils.zoneCatalog
import com.gramaurja.viewmodel.AppShellUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: AppShellUiState,
    onLanguageChange: (String) -> Unit,
    onChangeZone: (String) -> Unit,
    onLogout: () -> Unit
) {
    val languages = listOf("en" to "English", "kn" to "Kannada", "hi" to "Hindi")
    val zoneOptions = zoneCatalog.map { "${it.village} - ${it.zone}" }
    val currentZoneValue = zoneCatalog.firstOrNull { it.zone == uiState.currentZoneLabel }?.let {
        "${it.village} - ${it.zone}"
    } ?: uiState.currentZoneLabel

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AccentBannerCard(
                title = uiState.name.ifBlank { t("profile") },
                subtitle = "${uiState.phone.ifBlank { "--" }} • ${uiState.currentZoneLabel}",
                trailing = {
                    Text(uiState.currentDistrict.ifBlank { "Local" }, fontWeight = FontWeight.Bold)
                }
            )
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(t("profile_language"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        languages.forEachIndexed { index, (code, label) ->
                            SegmentedButton(
                                shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index = index, count = languages.size),
                                onClick = { onLanguageChange(code) },
                                selected = uiState.languageCode == code
                            ) {
                                Text(label)
                            }
                        }
                    }
                }
            }
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(t("change_transformer_zone"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        t("change_zone_copy"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SelectorField(
                        label = t("working_zone"),
                        value = currentZoneValue,
                        options = zoneOptions,
                        onSelect = { onChangeZone(it.substringAfter("- ").trim()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(t("active_feeder_zones"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        t("active_feeder_copy"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        if (uiState.activeTransformerZones.isEmpty()) {
            item {
                OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        t("no_active_transformers"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            items(uiState.activeTransformerZones.distinct()) { zone ->
                OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                    Text(zone, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        item {
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text(t("logout"))
            }
        }
        item {
            Text(
                text = "${t("version")} ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
