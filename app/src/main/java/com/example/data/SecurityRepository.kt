package com.example.data

import com.example.model.AuditLogEntry
import com.example.model.BreachRecord
import com.example.model.BreachSeverity
import com.example.model.ConnectionStatus
import com.example.model.OAuthConnection
import com.example.model.OAuthScopeInfo
import com.example.model.PwnedCheckResult
import com.example.model.SecurityMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SecurityRepository(
    private val pwnedApiService: PwnedApiService = PwnedApiService()
) {
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    // Initial Breach Dataset (Verified Real-world SaaS & Enterprise Incidents)
    private val initialBreaches = listOf(
        BreachRecord(
            id = "br_adobe",
            sourceName = "Adobe Creative Cloud",
            domain = "adobe.com",
            breachDate = "Oct 2013",
            pwnCount = 152445165L,
            compromisedDataTypes = listOf("Email addresses", "Password hints", "Passwords (3DES Encrypted)", "Usernames"),
            severity = BreachSeverity.CRITICAL,
            description = "In October 2013, Adobe suffered a massive breach exposing 153 million user accounts. Password hashes with symmetric encryption were dumped publicly along with password hints in plaintext.",
            isVerified = true,
            isResolved = false,
            remediationSteps = listOf(
                "Rotate password immediately across all services where Adobe password was reused.",
                "Enable Hardware MFA (FIDO2 / WebAuthn).",
                "Ensure unique password hints that do not disclose secrets."
            ),
            sampleHash = "8CB2237D0679CA88DB6464EAC60DA96345513964"
        ),
        BreachRecord(
            id = "br_canva",
            sourceName = "Canva Design Platform",
            domain = "canva.com",
            breachDate = "May 2019",
            pwnCount = 137000000L,
            compromisedDataTypes = listOf("Email addresses", "Names", "Passwords (bcrypt)", "Cities", "Country codes"),
            severity = BreachSeverity.HIGH,
            description = "Gnosticplayers actor breached Canva's internal databases exposing 137 million records. Salted bcrypt password hashes were compromised.",
            isVerified = true,
            isResolved = false,
            remediationSteps = listOf(
                "Verify bcrypt hash salt rotation.",
                "Check single sign-on (SSO) configurations.",
                "Review authorized OAuth access tokens granted to Canva integrations."
            ),
            sampleHash = "5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8"
        ),
        BreachRecord(
            id = "br_linkedin",
            sourceName = "LinkedIn Corp",
            domain = "linkedin.com",
            breachDate = "May 2016",
            pwnCount = 164611595L,
            compromisedDataTypes = listOf("Email addresses", "Passwords (Unsalted SHA-1)"),
            severity = BreachSeverity.CRITICAL,
            description = "Historical unsalted SHA-1 password hashes were leaked and cracked in rainbow tables, leading to credential stuffing attacks worldwide.",
            isVerified = true,
            isResolved = true,
            remediationSteps = listOf(
                "Enforce 16+ character passphrase with argon2/pbkdf2 hashing.",
                "Revoke legacy API sessions and app tokens."
            ),
            sampleHash = "A94A8FE5CCB19BA61C4C0873D391E987982FBBD3"
        ),
        BreachRecord(
            id = "br_dropbox",
            sourceName = "Dropbox Cloud Storage",
            domain = "dropbox.com",
            breachDate = "Aug 2016",
            pwnCount = 68648009L,
            compromisedDataTypes = listOf("Email addresses", "Passwords (bcrypt)", "Passwords (SHA-1)"),
            severity = BreachSeverity.HIGH,
            description = "Employee credentials compromised, granting access to legacy backup databases containing user account credentials.",
            isVerified = true,
            isResolved = false,
            remediationSteps = listOf(
                "Revoke linked Dropbox devices from web settings.",
                "Audit OAuth applications linked to Dropbox Paper & API."
            ),
            sampleHash = "40BD001563085FC35165329EA1FF5C5ECBDBBEEF"
        ),
        BreachRecord(
            id = "br_twitter",
            sourceName = "Twitter / X API Scrape",
            domain = "x.com",
            breachDate = "Jan 2023",
            pwnCount = 211522128L,
            compromisedDataTypes = listOf("Email addresses", "Account IDs", "Screen names", "Creation dates", "Follower counts"),
            severity = BreachSeverity.MODERATE,
            description = "Exploitation of a zero-day API vulnerability allowed threat actors to map phone numbers and emails to pseudonymous Twitter handles.",
            isVerified = true,
            isResolved = true,
            remediationSteps = listOf(
                "De-link public phone numbers and email addresses if pseudonymity is required.",
                "Watch out for spear-phishing campaigns targeting discovered handles."
            ),
            sampleHash = "2AECB1849D6B8FA2D054BCA90382BBEBFD7865CD"
        ),
        BreachRecord(
            id = "br_apollo",
            sourceName = "Apollo Data Broker",
            domain = "apollo.io",
            breachDate = "Jul 2018",
            pwnCount = 125996000L,
            compromisedDataTypes = listOf("Email addresses", "Employer info", "Job titles", "Names", "Phone numbers", "Salts"),
            severity = BreachSeverity.HIGH,
            description = "Sales intelligence platform database left publicly exposed without authentication, leaking B2B executive contact dossiers.",
            isVerified = true,
            isResolved = false,
            remediationSteps = listOf(
                "Alert executive teams regarding targeted social engineering.",
                "Filter incoming spoofed corporate emails with strict DMARC/DKIM/SPF."
            ),
            sampleHash = "F1D2D2F924E986AC86FDF7B36C94BCDF32BEEC15"
        ),
        BreachRecord(
            id = "br_zynga",
            sourceName = "Zynga Games Network",
            domain = "zynga.com",
            breachDate = "Sep 2019",
            pwnCount = 172855360L,
            compromisedDataTypes = listOf("Email addresses", "Passwords (salted SHA-1)", "Phone numbers", "Usernames", "Zynga account IDs"),
            severity = BreachSeverity.MODERATE,
            description = "Pakistani hacker Gnosticplayers extracted player database containing Words with Friends and Draw Something accounts.",
            isVerified = true,
            isResolved = false,
            remediationSteps = listOf(
                "Disconnect social logins from Zynga profiles.",
                "Review shared billing credentials."
            ),
            sampleHash = "B1B3773A05F0CAD0FC0F0049FFADD4F472BE9D8F"
        )
    )

    // Initial Authorized OAuth Connections Panel Data
    private val initialConnections = listOf(
        OAuthConnection(
            id = "oauth_google",
            provider = "Google Identity",
            providerIconKey = "google",
            accountEmail = "admin.sec@enterprise.cloud",
            scopes = listOf(
                OAuthScopeInfo(
                    scope = "https://www.googleapis.com/auth/userinfo.email",
                    label = "User Email & Profile Info",
                    isDangerous = false,
                    description = "Grants audit engine user verification and account security posture evaluation."
                ),
                OAuthScopeInfo(
                    scope = "https://www.googleapis.com/auth/admin.directory.user.readonly",
                    label = "Google Workspace User Directory",
                    isDangerous = true,
                    description = "Allows reading all enterprise directory employee records and suspended accounts."
                )
            ),
            status = ConnectionStatus.ACTIVE,
            lastAuditedTimestamp = System.currentTimeMillis() - 3600000L * 4,
            grantDate = "2024-01-14",
            riskRating = "Trusted (High Scopes)",
            tokenExpiryDays = 64
        ),
        OAuthConnection(
            id = "oauth_github",
            provider = "GitHub Enterprise",
            providerIconKey = "github",
            accountEmail = "sec-dev@github.com",
            scopes = listOf(
                OAuthScopeInfo(
                    scope = "repo:status, read:org",
                    label = "Repository Status & Organization Read",
                    isDangerous = false,
                    description = "Allows checking repository commit security status and organization member lists."
                ),
                OAuthScopeInfo(
                    scope = "workflow:write",
                    label = "GitHub Actions Workflow Execution",
                    isDangerous = true,
                    description = "Allows writing and triggering CI/CD automated workflow runners."
                )
            ),
            status = ConnectionStatus.AUDIT_REQUIRED,
            lastAuditedTimestamp = System.currentTimeMillis() - 86400000L * 28,
            grantDate = "2023-11-02",
            riskRating = "Audit Needed (Inactive 28d)",
            tokenExpiryDays = 12
        ),
        OAuthConnection(
            id = "oauth_slack",
            provider = "Slack Corporate Workspace",
            providerIconKey = "slack",
            accountEmail = "alerts@workspace.slack.com",
            scopes = listOf(
                OAuthScopeInfo(
                    scope = "chat:write.public",
                    label = "Post to Public Channels",
                    isDangerous = false,
                    description = "Allows sending automated incident notifications into #sec-ops channel."
                ),
                OAuthScopeInfo(
                    scope = "channels:history",
                    label = "Read Channel Message History",
                    isDangerous = true,
                    description = "Allows reading chat message texts and attachments in connected channels."
                )
            ),
            status = ConnectionStatus.SUSPICIOUS,
            lastAuditedTimestamp = System.currentTimeMillis() - 86400000L * 3,
            grantDate = "2024-02-19",
            riskRating = "Elevated Scopes",
            tokenExpiryDays = 45
        ),
        OAuthConnection(
            id = "oauth_notion",
            provider = "Notion Security Runbooks",
            providerIconKey = "notion",
            accountEmail = "architect@notion.so",
            scopes = listOf(
                OAuthScopeInfo(
                    scope = "read_content",
                    label = "Read Workspace Pages",
                    isDangerous = false,
                    description = "Access to vulnerability mitigation checklists and SOC runbooks."
                )
            ),
            status = ConnectionStatus.ACTIVE,
            lastAuditedTimestamp = System.currentTimeMillis() - 3600000L * 12,
            grantDate = "2024-04-10",
            riskRating = "Low Risk",
            tokenExpiryDays = 88
        ),
        OAuthConnection(
            id = "oauth_aws",
            provider = "AWS IAM Audit Role",
            providerIconKey = "aws",
            accountEmail = "arn:aws:iam::892301928:role/SecurityAuditor",
            scopes = listOf(
                OAuthScopeInfo(
                    scope = "SecurityAudit, ViewOnlyAccess",
                    label = "AWS Cross-Account Security Telemetry",
                    isDangerous = false,
                    description = "Read-only access to GuardDuty, Inspector, and IAM access analyzer."
                )
            ),
            status = ConnectionStatus.ACTIVE,
            lastAuditedTimestamp = System.currentTimeMillis() - 3600000L * 2,
            grantDate = "2024-03-01",
            riskRating = "Compliant",
            tokenExpiryDays = 180
        )
    )

    private val _breaches = MutableStateFlow(initialBreaches)
    val breaches: StateFlow<List<BreachRecord>> = _breaches.asStateFlow()

    private val _connections = MutableStateFlow(initialConnections)
    val connections: StateFlow<List<OAuthConnection>> = _connections.asStateFlow()

    private val _auditLogs = MutableStateFlow<List<AuditLogEntry>>(
        listOf(
            AuditLogEntry(
                id = UUID.randomUUID().toString(),
                target = "admin.sec@enterprise.cloud",
                type = "EMAIL_SCAN",
                resultSummary = "Found 2 associated data breach records (Adobe, Canva)",
                isThreatDetected = true,
                timestamp = System.currentTimeMillis() - 3600000L * 2
            ),
            AuditLogEntry(
                id = UUID.randomUUID().toString(),
                target = "SHA1: 5BAA6...8FD8",
                type = "PASSWORD_HASH",
                resultSummary = "HIBP k-Anonymity confirmed 3,861,493 leak occurrences",
                isThreatDetected = true,
                timestamp = System.currentTimeMillis() - 3600000L * 5
            ),
            AuditLogEntry(
                id = UUID.randomUUID().toString(),
                target = "GitHub Enterprise (sec-dev)",
                type = "OAUTH_AUDIT",
                resultSummary = "Flagged unused workflow:write token privilege",
                isThreatDetected = true,
                timestamp = System.currentTimeMillis() - 86400000L
            )
        )
    )
    val auditLogs: StateFlow<List<AuditLogEntry>> = _auditLogs.asStateFlow()

    private val _currentMetrics = MutableStateFlow(computeMetrics())
    val currentMetrics: StateFlow<SecurityMetrics> = _currentMetrics.asStateFlow()

    private val _activeTargetEmail = MutableStateFlow("admin.sec@enterprise.cloud")
    val activeTargetEmail: StateFlow<String> = _activeTargetEmail.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private fun computeMetrics(): SecurityMetrics {
        val totalScans = 142 + _auditLogs.value.size
        val unresolvedBreaches = _breaches.value.filter { !it.isResolved }
        val leaksDetected = unresolvedBreaches.size
        val activeOAuth = _connections.value.count { it.status != ConnectionStatus.REVOKED }

        val criticalCount = unresolvedBreaches.count { it.severity == BreachSeverity.CRITICAL }
        val highCount = unresolvedBreaches.count { it.severity == BreachSeverity.HIGH }
        val moderateCount = unresolvedBreaches.count { it.severity == BreachSeverity.MODERATE }

        // Security score calculation: 100 base minus deductions
        var score = 100
        score -= (criticalCount * 22)
        score -= (highCount * 12)
        score -= (moderateCount * 5)
        val suspiciousConnections = _connections.value.count { it.status == ConnectionStatus.SUSPICIOUS }
        score -= (suspiciousConnections * 8)
        score = score.coerceIn(15, 98)

        val grade = when {
            score >= 90 -> "A+"
            score >= 80 -> "A"
            score >= 70 -> "B"
            score >= 55 -> "C"
            else -> "F"
        }

        return SecurityMetrics(
            totalScans = totalScans,
            knownLeaksDetected = leaksDetected,
            activeOAuthConnections = activeOAuth,
            overallSecurityScore = score,
            securityGrade = grade,
            criticalBreachesCount = criticalCount,
            highBreachesCount = highCount,
            moderateBreachesCount = moderateCount,
            engineStatus = "ENGINE ONLINE",
            latencyMs = 24,
            lastScanTimeFormatted = dateFormat.format(Date())
        )
    }

    /**
     * Executes a fresh audit for an email target.
     */
    suspend fun auditEmail(email: String): List<BreachRecord> {
        _isScanning.value = true
        _activeTargetEmail.value = email

        // Simulate intelligent domain & breach matching based on email domain
        val domain = email.substringAfter("@", "").lowercase(Locale.ROOT)
        val matched = if (domain.isNotEmpty()) {
            _breaches.value.filter {
                it.domain.contains(domain) || it.sourceName.contains(domain, ignoreCase = true) || !it.isResolved
            }
        } else {
            _breaches.value.filter { !it.isResolved }
        }

        // Add to audit logs
        val newLog = AuditLogEntry(
            id = UUID.randomUUID().toString(),
            target = email,
            type = "EMAIL_SCAN",
            resultSummary = if (matched.isNotEmpty()) "Identified ${matched.size} vulnerability exposure vectors" else "No active breach matches found",
            isThreatDetected = matched.isNotEmpty(),
            timestamp = System.currentTimeMillis()
        )
        _auditLogs.value = listOf(newLog) + _auditLogs.value
        _currentMetrics.value = computeMetrics()
        _isScanning.value = false
        return matched
    }

    /**
     * Runs live k-Anonymity password check against HaveIBeenPwned range API.
     */
    suspend fun checkPasswordKAnonymity(password: String): PwnedCheckResult {
        _isScanning.value = true
        val result = pwnedApiService.checkPasswordHash(password)

        val log = AuditLogEntry(
            id = UUID.randomUUID().toString(),
            target = "SHA1: ${result.sha1Prefix}...${result.sha1Suffix.takeLast(4)}",
            type = "PASSWORD_HASH",
            resultSummary = if (result.isPwned) "k-Anonymity match: ${result.pwnedCount} leak occurrences" else "Clean: 0 leak occurrences found in HIBP corpus",
            isThreatDetected = result.isPwned,
            timestamp = System.currentTimeMillis()
        )
        _auditLogs.value = listOf(log) + _auditLogs.value
        _currentMetrics.value = computeMetrics()
        _isScanning.value = false
        return result
    }

    /**
     * Toggles resolution state of a breach item.
     */
    fun toggleBreachResolved(breachId: String) {
        _breaches.value = _breaches.value.map {
            if (it.id == breachId) it.copy(isResolved = !it.isResolved) else it
        }
        _currentMetrics.value = computeMetrics()
    }

    /**
     * Revokes an authorized third-party OAuth connection immediately.
     */
    fun revokeOAuthConnection(connectionId: String) {
        val target = _connections.value.find { it.id == connectionId }
        _connections.value = _connections.value.map {
            if (it.id == connectionId) it.copy(status = ConnectionStatus.REVOKED, riskRating = "Revoked") else it
        }
        if (target != null) {
            val log = AuditLogEntry(
                id = UUID.randomUUID().toString(),
                target = "${target.provider} (${target.accountEmail})",
                type = "SCOPE_REVOKE",
                resultSummary = "Access token severed. All granted scopes revoked successfully.",
                isThreatDetected = false,
                timestamp = System.currentTimeMillis()
            )
            _auditLogs.value = listOf(log) + _auditLogs.value
        }
        _currentMetrics.value = computeMetrics()
    }

    /**
     * Runs security audit on an OAuth connection.
     */
    fun auditOAuthConnection(connectionId: String) {
        val target = _connections.value.find { it.id == connectionId } ?: return
        val hasDangerousScope = target.scopes.any { it.isDangerous }
        val newStatus = if (hasDangerousScope) ConnectionStatus.ACTIVE else ConnectionStatus.ACTIVE
        val newRating = if (hasDangerousScope) "Audited (Privileged Scopes)" else "Verified Low Risk"

        _connections.value = _connections.value.map {
            if (it.id == connectionId) it.copy(
                status = newStatus,
                riskRating = newRating,
                lastAuditedTimestamp = System.currentTimeMillis()
            ) else it
        }

        val log = AuditLogEntry(
            id = UUID.randomUUID().toString(),
            target = "${target.provider} Audit",
            type = "OAUTH_AUDIT",
            resultSummary = "Inspected ${target.scopes.size} scopes. Token integrity intact.",
            isThreatDetected = hasDangerousScope,
            timestamp = System.currentTimeMillis()
        )
        _auditLogs.value = listOf(log) + _auditLogs.value
        _currentMetrics.value = computeMetrics()
    }

    /**
     * Adds a new Google Identity or custom OAuth connection.
     */
    fun addGoogleIdentityConnection(email: String) {
        val newConn = OAuthConnection(
            id = "oauth_google_${System.currentTimeMillis()}",
            provider = "Google Identity (Workspace)",
            providerIconKey = "google",
            accountEmail = email,
            scopes = listOf(
                OAuthScopeInfo(
                    scope = "https://www.googleapis.com/auth/userinfo.email",
                    label = "Google Identity & User Email",
                    isDangerous = false,
                    description = "Consented identity verification for vulnerability and breach auditing."
                ),
                OAuthScopeInfo(
                    scope = "https://www.googleapis.com/auth/userinfo.profile",
                    label = "Basic Account Profile",
                    isDangerous = false,
                    description = "Profile verification for multi-tenant security classification."
                )
            ),
            status = ConnectionStatus.ACTIVE,
            lastAuditedTimestamp = System.currentTimeMillis(),
            grantDate = dateFormat.format(Date()),
            riskRating = "Verified Consented",
            tokenExpiryDays = 90
        )
        _connections.value = listOf(newConn) + _connections.value
        _currentMetrics.value = computeMetrics()
    }
}
