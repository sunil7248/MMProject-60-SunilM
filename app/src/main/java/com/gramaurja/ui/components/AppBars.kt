package com.gramaurja.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.InsertChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gramaurja.navigation.Route
import com.gramaurja.utils.t

@Composable
fun GramaUrjaTopBar(
    currentZone: String,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        tonalElevation = 0.dp,
        color = Color.Transparent,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(22.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppLogo(modifier = Modifier.height(48.dp), contentDescription = null)
                Column {
                    Text(t("app_name"), color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text(
                        currentZone,
                        color = Color.White.copy(alpha = 0.84f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onAlertsClick) {
                    Icon(Icons.Rounded.Notifications, contentDescription = "Alerts", tint = Color.White)
                }
                IconButton(onClick = onProfileClick) {
                    Icon(Icons.Rounded.Person, contentDescription = "Profile", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun GramaUrjaBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Triple(Route.Home.value, t("home"), Icons.Rounded.Home),
        Triple(Route.PumpTimer.value, t("pump"), Icons.Rounded.Timer),
        Triple(Route.Reports.value, t("reports"), Icons.Rounded.Report),
        Triple(Route.Analytics.value, t("analytics"), Icons.Rounded.InsertChart)
    )

    Surface(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        color = Color.Transparent
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(22.dp))
        ) {
            items.forEach { (route, label, icon) ->
                val selected = currentRoute == route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(route) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    icon = { Icon(imageVector = icon, contentDescription = label) },
                    label = { Text(label) }
                )
            }
        }
    }
}
