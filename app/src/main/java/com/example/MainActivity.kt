package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.AddOAuthDialog
import com.example.components.AttributionModal
import com.example.components.AuditHistoryPanel
import com.example.components.AuthorizedConnectionsPanel
import com.example.components.BreachDetailModal
import com.example.components.BreachTablePanel
import com.example.components.CyberNavigationTabs
import com.example.components.FooterAttribution
import com.example.components.KAnonymityLabPanel
import com.example.components.OAuthRevokeDialog
import com.example.components.OverviewPanel
import com.example.components.TopCyberBar
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateBase
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.SecurityViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                VulnAuditApp()
            }
        }
    }
}

@Composable
fun VulnAuditApp(
    viewModel: SecurityViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val metrics by viewModel.metrics.collectAsStateWithLifecycle()
    val filteredBreaches by viewModel.filteredBreaches.collectAsStateWithLifecycle()
    val connections by viewModel.connections.collectAsStateWithLifecycle()
    val auditLogs by viewModel.auditLogs.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val emailInput by viewModel.emailInput.collectAsStateWithLifecycle()
    val passwordInput by viewModel.passwordInput.collectAsStateWithLifecycle()
    val kAnonymityResult by viewModel.kAnonymityResult.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val severityFilter by viewModel.severityFilter.collectAsStateWithLifecycle()

    val selectedBreach by viewModel.selectedBreach.collectAsStateWithLifecycle()
    val revokeTargetConnection by viewModel.revokeTargetConnection.collectAsStateWithLifecycle()
    val showAddOAuthDialog by viewModel.showAddOAuthDialog.collectAsStateWithLifecycle()
    val showAttributionDialog by viewModel.showAttributionDialog.collectAsStateWithLifecycle()

    // Android back navigation: return to Dashboard if on sub-screens
    BackHandler(enabled = currentTab != NavigationTab.DASHBOARD) {
        viewModel.selectTab(NavigationTab.DASHBOARD)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBase)
            .testTag("vuln_audit_scaffold"),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            CyberNavigationTabs(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateBase)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // High-End Cyber Top Bar with Search & Attribution Badge
            TopCyberBar(
                searchEmail = emailInput,
                onEmailChange = { viewModel.updateEmailInput(it) },
                onTriggerAudit = {
                    viewModel.runEmailAudit(it)
                    viewModel.selectTab(NavigationTab.BREACHES)
                },
                isScanning = isScanning,
                engineStatus = metrics.engineStatus,
                latencyMs = metrics.latencyMs,
                onAttributionClick = { viewModel.toggleAttributionDialog(true) }
            )

            // Dynamic Screen Body
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (currentTab) {
                        NavigationTab.DASHBOARD -> {
                            OverviewPanel(
                                metrics = metrics,
                                onRunFullAudit = {
                                    viewModel.runEmailAudit()
                                },
                                onNavigateToBreaches = {
                                    viewModel.selectTab(NavigationTab.BREACHES)
                                },
                                onNavigateToOAuth = {
                                    viewModel.selectTab(NavigationTab.OAUTH_PANEL)
                                },
                                onNavigateToKAnonymity = {
                                    viewModel.selectTab(NavigationTab.K_ANONYMITY)
                                }
                            )

                            // Quick High-Density Breach Preview in Dashboard
                            BreachTablePanel(
                                breaches = filteredBreaches.take(4),
                                searchQuery = searchQuery,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                selectedSeverity = severityFilter,
                                onSeveritySelect = { viewModel.updateSeverityFilter(it) },
                                onBreachClick = { viewModel.openBreachDetails(it) },
                                onToggleResolved = { viewModel.toggleBreachResolved(it) }
                            )
                        }

                        NavigationTab.BREACHES -> {
                            BreachTablePanel(
                                breaches = filteredBreaches,
                                searchQuery = searchQuery,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                selectedSeverity = severityFilter,
                                onSeveritySelect = { viewModel.updateSeverityFilter(it) },
                                onBreachClick = { viewModel.openBreachDetails(it) },
                                onToggleResolved = { viewModel.toggleBreachResolved(it) }
                            )
                        }

                        NavigationTab.OAUTH_PANEL -> {
                            AuthorizedConnectionsPanel(
                                connections = connections,
                                onAuditConnection = { viewModel.auditConnection(it) },
                                onRequestRevoke = { viewModel.requestRevoke(it) },
                                onAddNewConnection = { viewModel.openAddOAuthDialog() },
                                onConnectionDetails = { viewModel.openConnectionDetails(it) }
                            )
                        }

                        NavigationTab.K_ANONYMITY -> {
                            KAnonymityLabPanel(
                                passwordInput = passwordInput,
                                onPasswordChange = { viewModel.updatePasswordInput(it) },
                                onExecuteCheck = { viewModel.runPasswordAudit(it) },
                                result = kAnonymityResult,
                                isScanning = isScanning
                            )
                        }

                        NavigationTab.AUDIT_LOGS -> {
                            AuditHistoryPanel(
                                logs = auditLogs
                            )
                        }
                    }

                    // Footer Attribution Badge (Mandatory requirement in footer)
                    FooterAttribution(
                        onAttributionClick = { viewModel.toggleAttributionDialog(true) }
                    )
                }
            }
        }

        // Modals & Dialogs
        BreachDetailModal(
            breach = selectedBreach,
            onDismiss = { viewModel.openBreachDetails(null) },
            onToggleResolved = { viewModel.toggleBreachResolved(it) }
        )

        OAuthRevokeDialog(
            connection = revokeTargetConnection,
            onConfirm = { viewModel.confirmRevoke() },
            onDismiss = { viewModel.dismissRevokeDialog() }
        )

        AddOAuthDialog(
            isOpen = showAddOAuthDialog,
            onDismiss = { viewModel.dismissAddOAuthDialog() },
            onConnectGoogle = { viewModel.connectGoogleIdentity(it) }
        )

        AttributionModal(
            isOpen = showAttributionDialog,
            onDismiss = { viewModel.toggleAttributionDialog(false) }
        )
    }
}
