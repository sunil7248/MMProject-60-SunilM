package com.gramaurja.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gramaurja.BuildConfig
import com.gramaurja.ui.components.AppLogo
import com.gramaurja.utils.t
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    nextRoute: String,
    onFinished: (String) -> Unit
) {
    LaunchedEffect(nextRoute) {
        delay(2200)
        onFinished(nextRoute)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = t("app_name"),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = t("tagline"),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(18.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${t("version")} ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.82f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
