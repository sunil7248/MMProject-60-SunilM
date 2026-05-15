package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.local.entity.UserEntity
import com.gramaurja.data.repository.AppRepositories
import com.gramaurja.data.repository.UserPreferencesRepository
import com.gramaurja.navigation.Route
import com.gramaurja.utils.findZoneOption
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isCreateMode: Boolean = false,
    val isResetMode: Boolean = false,
    val existingAccount: Boolean = false,
    val name: String = "",
    val phone: String = "",
    val zone: String = "",
    val password: String = "",
    val confirmPassword: String = ""
) {
    val nameError: String?
        get() = when {
            !isCreateMode -> null
            name.isBlank() -> "Full name is required."
            name.trim().length < 3 -> "Enter at least 3 letters for the full name."
            !name.trim().all { it.isLetter() || it.isWhitespace() } -> "Use letters and spaces only for the full name."
            else -> null
        }

    val phoneError: String?
        get() = when {
            phone.isBlank() -> "Phone number is required."
            phone.any { !it.isDigit() } -> "Phone number must contain digits only."
            phone.length < 10 -> "Phone number must be exactly 10 digits."
            phone.length > 10 -> "Phone number cannot be more than 10 digits."
            phone.firstOrNull() !in '6'..'9' -> "Phone number must start with 6, 7, 8, or 9."
            else -> null
        }

    val zoneError: String?
        get() = when {
            !isCreateMode && !isResetMode -> null
            zone.isBlank() -> "Please select the correct transformer zone."
            findZoneOption(zone) == null -> "Selected transformer zone is not valid."
            else -> null
        }

    val passwordError: String?
        get() = when {
            password.isBlank() -> "Password is required."
            password.length < 8 -> "Password must be at least 8 characters."
            !password.any(Char::isUpperCase) -> "Password must include at least 1 uppercase letter."
            !password.any(Char::isLowerCase) -> "Password must include at least 1 lowercase letter."
            !password.any(Char::isDigit) -> "Password must include at least 1 number."
            else -> null
        }

    val confirmPasswordError: String?
        get() = when {
            !isCreateMode && !isResetMode -> null
            confirmPassword.isBlank() -> "Confirm password is required."
            password != confirmPassword -> "Password and confirm password must match."
            else -> null
        }

    val canSignIn: Boolean
        get() = !isResetMode && phoneError == null && password.isNotBlank()

    val canResetPassword: Boolean
        get() = isResetMode &&
            phoneError == null &&
            zoneError == null &&
            passwordError == null &&
            confirmPasswordError == null

    val canCreate: Boolean
        get() = isCreateMode &&
            nameError == null &&
            phoneError == null &&
            zoneError == null &&
            passwordError == null &&
            confirmPasswordError == null
}

sealed interface OnboardingEvent {
    data class Message(val text: String) : OnboardingEvent
    data class Navigate(val route: String) : OnboardingEvent
}

class OnboardingViewModel(
    private val repositories: AppRepositories,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val authState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = authState.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingEvent>()
    val events = _events.asSharedFlow()

    val startDestination: StateFlow<String> = combine(
        repositories.userRepository.observeUser(),
        repositories.zoneRepository.observeCurrentZone(),
        preferencesRepository.isSignedIn
    ) { user, zone, isSignedIn ->
        authState.update { state ->
            state.copy(
                existingAccount = user != null,
                phone = if (state.phone.isBlank()) user?.phone.orEmpty() else state.phone
            )
        }
        when {
            user == null -> Route.Login.value
            !isSignedIn -> Route.Login.value
            zone == null && user.zone.isBlank() -> Route.Login.value
            else -> Route.Home.value
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Route.Language.value)

    fun saveLanguage(code: String) {
        viewModelScope.launch {
            preferencesRepository.saveLanguage(code)
        }
    }

    fun setAuthMode(isCreateMode: Boolean) {
        authState.update {
            it.copy(
                isCreateMode = isCreateMode,
                isResetMode = false,
                password = "",
                confirmPassword = "",
                zone = if (isCreateMode) it.zone else ""
            )
        }
    }

    fun startPasswordReset() {
        authState.update {
            it.copy(
                isCreateMode = false,
                isResetMode = true,
                password = "",
                confirmPassword = ""
            )
        }
    }

    fun updateName(value: String) = authState.update { it.copy(name = value) }
    fun updatePhone(value: String) = authState.update { it.copy(phone = value) }
    fun updateZone(value: String) = authState.update { it.copy(zone = value) }
    fun updatePassword(value: String) = authState.update { it.copy(password = value) }
    fun updateConfirmPassword(value: String) = authState.update { it.copy(confirmPassword = value) }

    fun submitAuth() {
        viewModelScope.launch {
            val state = authState.value
            if (state.isResetMode) {
                resetPassword(state)
            } else if (state.isCreateMode) {
                createAccount(state)
            } else {
                signIn(state)
            }
        }
    }

    private suspend fun createAccount(state: AuthUiState) {
        if (state.existingAccount) {
            _events.emit(OnboardingEvent.Message("A local account already exists. Please sign in instead."))
            return
        }
        state.nameError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.phoneError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.zoneError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.passwordError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.confirmPasswordError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        if (!state.canCreate) {
            _events.emit(OnboardingEvent.Message("Please complete all account details before continuing."))
            return
        }
        val selectedZone = findZoneOption(state.zone)
        if (selectedZone == null) {
            _events.emit(OnboardingEvent.Message("Please select a valid transformer zone."))
            return
        }

        repositories.userRepository.saveUser(
            UserEntity(
                name = state.name,
                phone = state.phone,
                village = selectedZone.village,
                zone = selectedZone.zone,
                password = state.password
            )
        )
        repositories.zoneRepository.selectZone(
            district = selectedZone.district,
            village = selectedZone.village,
            zone = selectedZone.zone,
            timestamp = System.currentTimeMillis()
        )
        authState.value = AuthUiState(
            isCreateMode = false,
            existingAccount = true,
            phone = state.phone
        )
        preferencesRepository.setSignedIn(false)
        _events.emit(OnboardingEvent.Message("Account created. Please sign in."))
    }

    private suspend fun signIn(state: AuthUiState) {
        state.phoneError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        val storedUser = repositories.userRepository.getUserByPhone(state.phone)
        if (storedUser == null || storedUser.password != state.password) {
            _events.emit(OnboardingEvent.Message("Invalid phone number or password."))
            return
        }

        if (repositories.zoneRepository.observeCurrentZone().first() == null && storedUser.zone.isNotBlank()) {
            val selectedZone = findZoneOption(storedUser.zone)
            if (selectedZone != null) {
                repositories.zoneRepository.selectZone(
                    district = selectedZone.district,
                    village = selectedZone.village,
                    zone = selectedZone.zone,
                    timestamp = System.currentTimeMillis()
                )
            }
        }
        preferencesRepository.setSignedIn(true)
        _events.emit(OnboardingEvent.Navigate(Route.Home.value))
    }

    private suspend fun resetPassword(state: AuthUiState) {
        state.phoneError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.zoneError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.passwordError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }
        state.confirmPasswordError?.let {
            _events.emit(OnboardingEvent.Message(it))
            return
        }

        val storedUser = repositories.userRepository.getUserByPhone(state.phone)
        if (storedUser == null) {
            _events.emit(OnboardingEvent.Message("No account was found for this phone number."))
            return
        }
        if (storedUser.zone != state.zone) {
            _events.emit(OnboardingEvent.Message("Entered transformer zone does not match this account."))
            return
        }

        repositories.userRepository.saveUser(storedUser.copy(password = state.password))
        authState.value = AuthUiState(
            isCreateMode = false,
            isResetMode = false,
            existingAccount = true,
            phone = state.phone
        )
        preferencesRepository.setSignedIn(false)
        _events.emit(OnboardingEvent.Message("Password reset successful. Please sign in with the new password."))
    }
}
