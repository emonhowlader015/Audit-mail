package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SecurityRepository
import com.example.model.AuditLogEntry
import com.example.model.BreachRecord
import com.example.model.BreachSeverity
import com.example.model.OAuthConnection
import com.example.model.PwnedCheckResult
import com.example.model.SecurityMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String, val badgeText: String? = null) {
    DASHBOARD("Overview"),
    BREACHES("Breach Analysis"),
    OAUTH_PANEL("Authorized OAuth"),
    K_ANONYMITY("k-Anonymity Lab"),
    AUDIT_LOGS("Audit History")
}

class SecurityViewModel(
    private val repository: SecurityRepository = SecurityRepository()
) : ViewModel() {

    val metrics: StateFlow<SecurityMetrics> = repository.currentMetrics
    val connections: StateFlow<List<OAuthConnection>> = repository.connections
    val auditLogs: StateFlow<List<AuditLogEntry>> = repository.auditLogs
    val isScanning: StateFlow<Boolean> = repository.isScanning
    val activeTargetEmail: StateFlow<String> = repository.activeTargetEmail

    private val _currentTab = MutableStateFlow(NavigationTab.DASHBOARD)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _severityFilter = MutableStateFlow<BreachSeverity?>(null)
    val severityFilter: StateFlow<BreachSeverity?> = _severityFilter.asStateFlow()

    private val _emailInput = MutableStateFlow("admin.sec@enterprise.cloud")
    val emailInput: StateFlow<String> = _emailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("P@ssw0rd2024!")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    private val _kAnonymityResult = MutableStateFlow<PwnedCheckResult?>(null)
    val kAnonymityResult: StateFlow<PwnedCheckResult?> = _kAnonymityResult.asStateFlow()

    private val _selectedBreach = MutableStateFlow<BreachRecord?>(null)
    val selectedBreach: StateFlow<BreachRecord?> = _selectedBreach.asStateFlow()

    private val _selectedConnection = MutableStateFlow<OAuthConnection?>(null)
    val selectedConnection: StateFlow<OAuthConnection?> = _selectedConnection.asStateFlow()

    private val _revokeTargetConnection = MutableStateFlow<OAuthConnection?>(null)
    val revokeTargetConnection: StateFlow<OAuthConnection?> = _revokeTargetConnection.asStateFlow()

    private val _showAddOAuthDialog = MutableStateFlow(false)
    val showAddOAuthDialog: StateFlow<Boolean> = _showAddOAuthDialog.asStateFlow()

    private val _showAttributionDialog = MutableStateFlow(false)
    val showAttributionDialog: StateFlow<Boolean> = _showAttributionDialog.asStateFlow()

    // Filtered breaches derived state
    val filteredBreaches: StateFlow<List<BreachRecord>> = combine(
        repository.breaches,
        _searchQuery,
        _severityFilter
    ) { allBreaches, query, severity ->
        allBreaches.filter { breach ->
            val matchesQuery = query.isBlank() ||
                breach.sourceName.contains(query, ignoreCase = true) ||
                breach.domain.contains(query, ignoreCase = true) ||
                breach.compromisedDataTypes.any { it.contains(query, ignoreCase = true) }

            val matchesSeverity = severity == null || breach.severity == severity
            matchesQuery && matchesSeverity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSeverityFilter(severity: BreachSeverity?) {
        _severityFilter.value = if (_severityFilter.value == severity) null else severity
    }

    fun updateEmailInput(email: String) {
        _emailInput.value = email
    }

    fun updatePasswordInput(pwd: String) {
        _passwordInput.value = pwd
    }

    fun runEmailAudit(email: String = _emailInput.value) {
        if (email.isBlank()) return
        viewModelScope.launch {
            repository.auditEmail(email.trim())
        }
    }

    fun runPasswordAudit(password: String = _passwordInput.value) {
        if (password.isBlank()) return
        viewModelScope.launch {
            val res = repository.checkPasswordKAnonymity(password)
            _kAnonymityResult.value = res
        }
    }

    fun toggleBreachResolved(id: String) {
        repository.toggleBreachResolved(id)
        if (_selectedBreach.value?.id == id) {
            _selectedBreach.value = _selectedBreach.value?.let { it.copy(isResolved = !it.isResolved) }
        }
    }

    fun openBreachDetails(breach: BreachRecord?) {
        _selectedBreach.value = breach
    }

    fun openConnectionDetails(connection: OAuthConnection?) {
        _selectedConnection.value = connection
    }

    fun requestRevoke(connection: OAuthConnection) {
        _revokeTargetConnection.value = connection
    }

    fun confirmRevoke() {
        _revokeTargetConnection.value?.let {
            repository.revokeOAuthConnection(it.id)
        }
        _revokeTargetConnection.value = null
        _selectedConnection.value = null
    }

    fun dismissRevokeDialog() {
        _revokeTargetConnection.value = null
    }

    fun auditConnection(id: String) {
        repository.auditOAuthConnection(id)
    }

    fun openAddOAuthDialog() {
        _showAddOAuthDialog.value = true
    }

    fun dismissAddOAuthDialog() {
        _showAddOAuthDialog.value = false
    }

    fun connectGoogleIdentity(email: String) {
        repository.addGoogleIdentityConnection(email)
        _showAddOAuthDialog.value = false
    }

    fun toggleAttributionDialog(show: Boolean) {
        _showAttributionDialog.value = show
    }
}
