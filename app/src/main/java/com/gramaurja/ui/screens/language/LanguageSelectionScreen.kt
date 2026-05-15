package com.gramaurja.ui.screens.language

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.ui.components.AccentBannerCard
import com.gramaurja.ui.components.AppLogo
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.utils.t

@Composable
fun LanguageSelectionScreen(
    selectedLanguage: String,
    onSelectLanguage: (String) -> Unit
) {
    val languages = listOf("en" to "English", "kn" to "Kannada", "hi" to "Hindi")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AccentBannerCard(
                title = t("choose_language"),
                subtitle = t("language_daily_use"),
                trailing = { AppLogo(modifier = Modifier.height(54.dp), contentDescription = null) }
            )
        }
        item {
            OutdoorReadableCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(t("why_this_matters"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        t("language_impact_copy"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        items(languages.size) { index ->
            val code = languages[index].first
            val label = languages[index].second
            OutdoorReadableCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectLanguage(code) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedLanguage == code) MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)
                            else MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (selectedLanguage == code) t("selected") else t("tap_continue"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
