package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ConnectionStatus
import com.example.model.OAuthConnection
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanContainer
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.MonoTypography
import com.example.ui.theme.RiskAmber
import com.example.ui.theme.RiskAmberBg
import com.example.ui.theme.RiskCritical
import com.example.ui.theme.RiskCriticalBg
import com.example.ui.theme.RiskEmerald
import com.example.ui.theme.RiskEmeraldBg
import com.example.ui.theme.RiskOrange
import com.example.ui.theme.RiskOrangeBg
import com.example.ui.theme.SlateBase
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AuthorizedConnectionsPanel(
    connections: List<OAuthConnection>,
    onAuditConnection: (String) -> Unit,
    onRequestRevoke: (OAuthConnection) -> Unit,
    onAddNewConnection: () -> Unit,
    onConnectionDetails: (OAuthConnection) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Panel Header & Add Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Authorized Connections",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Third-party OAuth 2.0 grants & consented identity scopes",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Connect New Provider CTA
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberCyanContainer)
                    .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .clickable {
                        VibrationHelper.tactileTick(context)
                        onAddNewConnection()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("add_oauth_connection_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = CyberCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Authorize Integration",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = CyberCyan
                    )
                }
            }
        }

        // Info Banner: Google Identity RFC 6749 Compliance
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SlateCardSecondary)
                .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "OAuth Identity",
                    tint = CyberCyan,
                    modifier = Modifier.size(20.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "User-Consented Google Identity Audit Scope",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Scope https://www.googleapis.com/auth/userinfo.email allows verified continuous auditing of federated identity breaches without password storage.",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Structured List of OAuth Providers
        connections.forEach { conn ->
            OAuthConnectionCard(
                connection = conn,
                onAudit = {
                    VibrationHelper.tactileTick(context)
                    onAuditConnection(conn.id)
                },
                onRevoke = {
                    VibrationHelper.tactileWarning(context)
                    onRequestRevoke(conn)
                },
                onClick = {
                    VibrationHelper.tactileTick(context)
                    onConnectionDetails(conn)
                }
            )
        }
    }
}

@Composable
private fun OAuthConnectionCard(
    connection: OAuthConnection,
    onAudit: () -> Unit,
    onRevoke: () -> Unit,
    onClick: () -> Unit
) {
    val isRevoked = connection.status == ConnectionStatus.REVOKED
    val providerIcon: ImageVector = when (connection.providerIconKey) {
        "google" -> Icons.Default.Domain
        "github" -> Icons.Default.Code
        "slack" -> Icons.Default.Hub
        "notion" -> Icons.Default.Policy
        "aws" -> Icons.Default.CloudQueue
        else -> Icons.Default.Link
    }

    val statusColor = when (connection.status) {
        ConnectionStatus.ACTIVE -> RiskEmerald
        ConnectionStatus.AUDIT_REQUIRED -> RiskAmber
        ConnectionStatus.SUSPICIOUS -> RiskOrange
        ConnectionStatus.REVOKED -> RiskCritical
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SlateCard)
            .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("oauth_card_${connection.id}")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Provider Icon + Account Name + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateCardSecondary)
                            .border(1.dp, SlateBorder, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = providerIcon,
                            contentDescription = connection.provider,
                            tint = if (isRevoked) TextMuted else CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = connection.provider,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = if (isRevoked) TextMuted else TextPrimary
                        )
                        Text(
                            text = connection.accountEmail,
                            style = MonoTypography.emailDisplay,
                            color = if (isRevoked) TextMuted else CyberCyan
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = connection.status.label.uppercase(),
                        style = MonoTypography.codeBadge,
                        color = statusColor
                    )
                }
            }

            // Scopes list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(SlateCardSecondary.copy(alpha = 0.6f))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Granted Scopes (${connection.scopes.size}):",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                connection.scopes.forEach { scope ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = scope.scope,
                            style = MonoTypography.hashSmall,
                            color = if (scope.isDangerous) RiskOrange else TextSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        if (scope.isDangerous) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(RiskOrange.copy(alpha = 0.15f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "PRIVILEGED",
                                    style = MonoTypography.codeBadge,
                                    color = RiskOrange
                                )
                            }
                        }
                    }
                }
            }

            // Footer Row: Metadata + Explicit Audit & Revoke Action Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Granted: ${connection.grantDate} • Token expires in ${connection.tokenExpiryDays}d",
                    fontFamily = InterFontFamily,
                    fontSize = 10.sp,
                    color = TextMuted
                )

                // Action Controls: Audit & Revoke
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isRevoked) {
                        // Explicit "Audit" Action
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SlateCardSecondary)
                                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .clickable { onAudit() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("audit_oauth_${connection.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Audit",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Audit",
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = CyberCyan
                                )
                            }
                        }

                        // Explicit "Revoke" Action
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(RiskCritical.copy(alpha = 0.1f))
                                .border(1.dp, RiskCritical.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .clickable { onRevoke() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("revoke_oauth_${connection.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Block,
                                    contentDescription = "Revoke",
                                    tint = RiskCritical,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Revoke",
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = RiskCritical
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Access Terminated",
                            style = MonoTypography.codeBadge,
                            color = RiskCritical
                        )
                    }
                }
            }
        }
    }
}
