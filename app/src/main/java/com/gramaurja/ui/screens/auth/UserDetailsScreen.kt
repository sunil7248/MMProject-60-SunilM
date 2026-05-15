package com.gramaurja.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.gramaurja.ui.components.AppLogo
import com.gramaurja.ui.components.OutdoorReadableCard
import com.gramaurja.ui.components.SelectorField
import com.gramaurja.utils.t
import com.gramaurja.utils.zoneCatalog
import com.gramaurja.viewmodel.AuthUiState

@Composable
fun UserDetailsScreen(
    uiState: AuthUiState,
    onModeChange: (Boolean) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onZoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSubmit: () -> Unit
) {
    val zoneOptions = zoneCatalog.map { "${it.village} - ${it.zone}" }
    val currentZoneValue = zoneCatalog.firstOrNull { it.zone == uiState.zone }?.let {
        "${it.village} - ${it.zone}"
    }.orEmpty()
    val titleText = when {
        uiState.isResetMode -> t("reset_password")
        uiState.isCreateMode -> t("create_account")
        else -> t("sign_in")
    }
    val headerCopy = when {
        uiState.isResetMode -> t("reset_header_copy")
        uiState.isCreateMode -> t("create_header_copy")
        else -> t("signin_header_copy")
    }
    val cardCopy = when {
        uiState.isResetMode -> t("reset_card_copy")
        uiState.isCreateMode -> t("create_card_copy")
        else -> t("signin_card_copy")
    }
    val footerCopy = when {
        uiState.isResetMode -> t("reset_footer_copy")
        uiState.isCreateMode -> t("create_footer_copy")
        else -> t("signin_footer_copy")
    }
    var showPassword by rememberSaveable(uiState.isCreateMode, uiState.isResetMode) { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable(uiState.isCreateMode, uiState.isResetMode) { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
                        .padding(8.dp)
                ) {
                    AppLogo(modifier = Modifier.height(52.dp), contentDescription = null)
                }
                Text(
                    text = titleText,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = headerCopy,
                    color = Color.White.copy(alpha = 0.92f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 54.dp)
                )
            }
        }
        item {
            OutdoorReadableCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-34).dp)
                    .padding(horizontal = 8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = cardCopy,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!uiState.isResetMode) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = !uiState.isCreateMode,
                                onClick = { onModeChange(false) },
                                label = { Text(t("sign_in")) }
                            )
                            FilterChip(
                                selected = uiState.isCreateMode,
                                onClick = { onModeChange(true) },
                                label = { Text(t("create")) }
                            )
                        }
                    }

                    if (uiState.isCreateMode) {
                        OutlinedTextField(
                            value = uiState.name,
                            onValueChange = onNameChange,
                            label = { Text(t("full_name")) },
                            leadingIcon = { Icon(Icons.Rounded.PersonAdd, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = uiState.name.isNotBlank() && uiState.nameError != null,
                            supportingText = {
                                if (uiState.name.isNotBlank() && uiState.nameError != null) {
                                    Text(uiState.nameError!!)
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = uiState.phone,
                        onValueChange = { onPhoneChange(it.take(10)) },
                        label = { Text(t("phone_number")) },
                        leadingIcon = { Icon(Icons.Rounded.PhoneAndroid, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.phone.isNotBlank() && uiState.phoneError != null,
                        supportingText = {
                            if (uiState.phone.isNotBlank() && uiState.phoneError != null) {
                                Text(uiState.phoneError!!)
                            }
                        },
                        shape = RoundedCornerShape(16.dp)
                    )

                    if (uiState.isCreateMode || uiState.isResetMode) {
                        SelectorField(
                            label = t("zone"),
                            value = currentZoneValue,
                            options = zoneOptions,
                            onSelect = { onZoneChange(it.substringAfter("- ").trim()) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        label = { Text(t("password")) },
                        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = if (showPassword) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.password.isNotBlank() && uiState.passwordError != null,
                        supportingText = {
                            if (uiState.password.isNotBlank() && uiState.passwordError != null) {
                                Text(uiState.passwordError!!)
                            }
                        },
                        shape = RoundedCornerShape(16.dp)
                    )

                    if (uiState.isCreateMode || uiState.isResetMode) {
                        OutlinedTextField(
                            value = uiState.confirmPassword,
                            onValueChange = onConfirmPasswordChange,
                            label = { Text(t("confirm_password")) },
                            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(
                                        imageVector = if (showConfirmPassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password"
                                    )
                                }
                            },
                            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = uiState.confirmPassword.isNotBlank() && uiState.confirmPasswordError != null,
                            supportingText = {
                                if (uiState.confirmPassword.isNotBlank() && uiState.confirmPasswordError != null) {
                                    Text(uiState.confirmPasswordError!!)
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    if ((uiState.isCreateMode || uiState.isResetMode) && uiState.zoneError != null) {
                        Text(
                            text = uiState.zoneError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text(t("remember_me"), style = MaterialTheme.typography.bodySmall)
                        }
                        if (uiState.isCreateMode || uiState.isResetMode) {
                            Text(
                                text = t("zone_required"),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            TextButton(onClick = onForgotPasswordClick) {
                                Icon(Icons.Rounded.Refresh, contentDescription = null)
                                Text(t("forgot_password"))
                            }
                        }
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = when {
                            uiState.isResetMode -> uiState.canResetPassword
                            uiState.isCreateMode -> uiState.canCreate
                            else -> uiState.canSignIn
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            when {
                                uiState.isResetMode -> t("reset_password")
                                uiState.isCreateMode -> t("create_account")
                                else -> t("sign_in")
                            },
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    TextButton(
                        onClick = {
                            if (uiState.isResetMode) onModeChange(false) else onModeChange(!uiState.isCreateMode)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            when {
                                uiState.isResetMode -> t("back_to_signin")
                                uiState.isCreateMode -> t("back_to_signin")
                                else -> t("create_account_link")
                            }
                        )
                    }
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .offset(y = (-20).dp)
            ) {
                Text(
                    text = footerCopy,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
