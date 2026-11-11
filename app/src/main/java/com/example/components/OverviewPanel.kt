package com.example.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SecurityMetrics
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
fun OverviewPanel(
    metrics: SecurityMetrics,
    onRunFullAudit: () -> Unit,
    onNavigateToBreaches: () -> Unit,
    onNavigateToOAuth: () -> Unit,
    onNavigateToKAnonymity: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Overall Security Score Hero Card
        SecurityScoreHeroCard(
            metrics = metrics,
            onRunFullAudit = {
                VibrationHelper.tactileTick(context)
                onRunFullAudit()
            }
        )

        // 4-Card SaaS Key Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Metric 1: Total Scans
            MetricCard(
                title = "TOTAL SCANS",
                value = "${metrics.totalScans}",
                subtext = "+18% this month",
                icon = Icons.Default.Bolt,
                accentColor = CyberCyan,
                badgeText = "LIVE",
                badgeColor = RiskEmerald,
                modifier = Modifier.weight(1f),
                onClick = onRunFullAudit
            )

            // Metric 2: Known Leaks Detected
            MetricCard(
                title = "KNOWN LEAKS",
                value = "${metrics.knownLeaksDetected}",
                subtext = "${metrics.criticalBreachesCount} Critical • ${metrics.highBreachesCount} High",
                icon = Icons.Default.Warning,
                accentColor = if (metrics.knownLeaksDetected > 0) RiskCritical else RiskEmerald,
                badgeText = if (metrics.knownLeaksDetected > 0) "ACTION REQ" else "CLEAN",
                badgeColor = if (metrics.knownLeaksDetected > 0) RiskCritical else RiskEmerald,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToBreaches
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Metric 3: Active OAuth Connections
            MetricCard(
                title = "OAUTH APPS",
                value = "${metrics.activeOAuthConnections}",
                subtext = "Google, GitHub, Slack",
                icon = Icons.Default.Hub,
                accentColor = CyberCyan,
                badgeText = "RFC 6749",
                badgeColor = CyberCyan,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToOAuth
            )

            // Metric 4: Overall Security Grade
            MetricCard(
                title = "SECURITY GRADE",
                value = metrics.securityGrade,
                subtext = "Score: ${metrics.overallSecurityScore}/100",
                icon = Icons.Default.Policy,
                accentColor = when {
                    metrics.overallSecurityScore >= 80 -> RiskEmerald
                    metrics.overallSecurityScore >= 60 -> RiskAmber
                    else -> RiskCritical
                },
                badgeText = "${metrics.overallSecurityScore}%",
                badgeColor = when {
                    metrics.overallSecurityScore >= 80 -> RiskEmerald
                    metrics.overallSecurityScore >= 60 -> RiskAmber
                    else -> RiskCritical
                },
                modifier = Modifier.weight(1f),
                onClick = onNavigateToKAnonymity
            )
        }

        // Quick Telemetry & Zero-Knowledge Protocol Strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SlateCard)
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
                .clickable {
                    VibrationHelper.tactileTick(context)
                    onNavigateToKAnonymity()
                }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CyberCyanContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Zero-Knowledge k-Anonymity Guard",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Only 5-char SHA-1 prefixes queried. Raw credentials never leave this device.",
                            fontFamily = InterFontFamily,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Inspect",
                    tint = CyberCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SecurityScoreHeroCard(
    metrics: SecurityMetrics,
    onRunFullAudit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val score = metrics.overallSecurityScore
    val scoreColor = when {
        score >= 80 -> RiskEmerald
        score >= 60 -> RiskAmber
        else -> RiskCritical
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SlateCard)
            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Score information & action button
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "OVERALL SECURITY SCORE",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(scoreColor.copy(alpha = 0.15f))
                            .border(1.dp, scoreColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "GRADE ${metrics.securityGrade}",
                            style = MonoTypography.codeBadge,
                            color = scoreColor
                        )
                    }
                }

                Text(
                    text = if (score >= 80) "Posture Compliant & Hardened"
                    else "${metrics.knownLeaksDetected} Account Vulnerabilities Detected",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )

                Text(
                    text = "Last audit completed: ${metrics.lastScanTimeFormatted}",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tactile Run Audit Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberCyan)
                        .clickable { onRunFullAudit() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("hero_full_audit_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Audit Now",
                            tint = SlateBase,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Execute System Audit",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = SlateBase
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Column: Canvas Circular Gauge Ring
            Box(
                modifier = Modifier.size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(90.dp)) {
                    val strokeWidth = 8.dp.toPx()
                    // Track circle
                    drawCircle(
                        color = Color(0xFF262C3A),
                        radius = (size.minDimension - strokeWidth) / 2,
                        style = Stroke(width = strokeWidth)
                    )
                    // Progress arc
                    val sweepAngle = (score / 100f) * 360f
                    drawArc(
                        color = scoreColor,
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$score%",
                        style = MonoTypography.numberStat,
                        color = TextPrimary
                    )
                    Text(
                        text = "HEALTH",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    badgeText: String,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SlateCard)
            .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
            .clickable {
                VibrationHelper.tactileTick(context)
                onClick()
            }
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = title,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .border(1.dp, badgeColor.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MonoTypography.codeBadge,
                        color = badgeColor
                    )
                }
            }

            Text(
                text = value,
                style = MonoTypography.numberStat,
                color = TextPrimary
            )

            Text(
                text = subtext,
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}
