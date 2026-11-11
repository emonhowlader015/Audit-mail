package com.example.model

enum class BreachSeverity(val label: String, val level: Int) {
    LOW("Low", 1),
    MODERATE("Moderate", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4)
}

enum class ConnectionStatus(val label: String) {
    ACTIVE("Active"),
    AUDIT_REQUIRED("Audit Needed"),
    SUSPICIOUS("High Risk Scopes"),
    REVOKED("Revoked")
}

data class BreachRecord(
    val id: String,
    val sourceName: String,
    val domain: String,
    val breachDate: String,
    val pwnCount: Long,
    val compromisedDataTypes: List<String>,
    val severity: BreachSeverity,
    val description: String,
    val isVerified: Boolean = true,
    val isFabricated: Boolean = false,
    val isSpamList: Boolean = false,
    val isResolved: Boolean = false,
    val remediationSteps: List<String> = emptyList(),
    val sampleHash: String = ""
)

data class OAuthScopeInfo(
    val scope: String,
    val label: String,
    val isDangerous: Boolean,
    val description: String
)

data class OAuthConnection(
    val id: String,
    val provider: String,
    val providerIconKey: String,
    val accountEmail: String,
    val scopes: List<OAuthScopeInfo>,
    val status: ConnectionStatus,
    val lastAuditedTimestamp: Long,
    val grantDate: String,
    val riskRating: String,
    val tokenExpiryDays: Int,
    val clientId: String = "client_7x9a_prod"
)

data class PwnedCheckResult(
    val query: String,
    val isPasswordQuery: Boolean,
    val sha1Prefix: String,
    val sha1Suffix: String,
    val fullHash: String,
    val pwnedCount: Long,
    val isPwned: Boolean,
    val latencyMs: Long,
    val timestamp: Long,
    val error: String? = null
)

data class SecurityMetrics(
    val totalScans: Int,
    val knownLeaksDetected: Int,
    val activeOAuthConnections: Int,
    val overallSecurityScore: Int,
    val securityGrade: String,
    val criticalBreachesCount: Int,
    val highBreachesCount: Int,
    val moderateBreachesCount: Int,
    val engineStatus: String = "ENGINE ONLINE",
    val latencyMs: Long = 28,
    val lastScanTimeFormatted: String = "Just now"
)

data class AuditLogEntry(
    val id: String,
    val target: String,
    val type: String, // "EMAIL_SCAN", "PASSWORD_HASH", "OAUTH_AUDIT", "SCOPE_REVOKE"
    val resultSummary: String,
    val isThreatDetected: Boolean,
    val timestamp: Long
)
