package com.gramaurja.ui.screens.reports

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.ui.components.AccentBannerCard
import com.gramaurja.ui.components.MetricCard
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.ui.components.SectionTitle
import com.gramaurja.ui.components.SelectorField
import com.gramaurja.ui.theme.SoftGold
import com.gramaurja.utils.formatTimestamp
import com.gramaurja.utils.t
import com.gramaurja.viewmodel.ReportsUiState

@Composable
fun ReportIssueScreen(
    uiState: ReportsUiState,
    onIssueTypeChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageSelected: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val issueTypes = listOf("Transformer fault", "Voltage fluctuation", "Line damage")
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        onImageSelected(uri?.toString().orEmpty())
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
                title = t("report_issue"),
                subtitle = t("reports_subtitle"),
                trailing = { Text(uiState.reports.size.toString(), fontWeight = FontWeight.Bold) }
            )
        }
        item {
            MetricCard(
                label = t("saved_offline"),
                value = uiState.reports.size.toString(),
                supporting = t("reports_available_device"),
                modifier = Modifier.fillMaxWidth(),
                containerColor = SoftGold
            )
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SelectorField(
                        label = t("issue_type"),
                        value = uiState.issueType,
                        options = issueTypes,
                        onSelect = onIssueTypeChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = onDescriptionChange,
                        label = { Text(t("description")) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )
                    OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Rounded.Image, contentDescription = null)
                            Text(if (uiState.imageUri.isBlank()) t("add_fault_photo") else t("image_added"))
                            Button(
                                onClick = { launcher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (uiState.imageUri.isBlank()) t("upload_button") else t("image_added"))
                            }
                        }
                    }
                    Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                        Text(t("submit"))
                    }
                }
            }
        }
        item { SectionTitle(t("saved_reports")) }
        items(uiState.reports.take(8)) { report ->
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(report.issueType, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(report.description)
                    if (report.imageUri.isNotBlank()) Text(t("upload_images"))
                    Text(formatTimestamp(report.timestamp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
