package com.example.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BreachRecord
import com.example.model.BreachSeverity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanContainer
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.MonoTypography
import com.example.ui.theme.RiskAmber
import com.example.ui.theme.RiskCritical
import com.example.ui.theme.RiskEmerald
import com.example.ui.theme.RiskOrange
import com.example.ui.theme.SlateBase
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BreachDetailModal(
    breach: BreachRecord?,
    onDismiss: () -> Unit,
    onToggleResolved: (String) -> Unit
) {
    if (breach == null) return
    val context = LocalContext.current

    val severityColor = when (breach.severity) {
        BreachSeverity.CRITICAL -> RiskCritical
        BreachSeverity.HIGH -> RiskOrange
        BreachSeverity.MODERATE -> RiskAmber
        BreachSeverity.LOW -> RiskEmerald
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SlateCard)
            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
            .testTag("breach_detail_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = breach.sourceName,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(severityColor.copy(alpha = 0.15f))
                                .border(1.dp, severityColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = breach.severity.label.uppercase(),
                                style = MonoTypography.codeBadge,
                                color = severityColor
                            )
                        }
                    }
                    Text(
                        text = "${breach.domain} • Breach occurred ${breach.breachDate}",
                        style = MonoTypography.hashSmall,
                        color = CyberCyan
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // High-Impact Summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlateCardSecondary)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TOTAL COMPROMISED ACCOUNTS",
                            fontFamily = InterFontFamily,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = String.format("%,d", breach.pwnCount),
                            style = MonoTypography.numberStat.copy(fontSize = 16.sp),
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = breach.description,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = TextSecondary
                    )
                }
            }

            // Compromised Data Attributes
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "COMPROMISED DATA TYPES",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    letterSpacing = 0.5.sp
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    breach.compromisedDataTypes.forEach { item ->
                        val isCrit = item.contains("Password", ignoreCase = true) || item.contains("Salt", ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isCrit) RiskCritical.copy(alpha = 0.15f) else SlateCardSecondary)
                                .border(
                                    1.dp,
                                    if (isCrit) RiskCritical.copy(alpha = 0.4f) else SlateBorder,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = item,
                                style = MonoTypography.codeBadge,
                                color = if (isCrit) RiskCritical else TextPrimary
                            )
                        }
                    }
                }
            }

            // Cryptographic Hash Vector Sample
            if (breach.sampleHash.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "INCIDENT SHA-1 VECTOR SAMPLE",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SlateCardSecondary)
                            .border(1.dp, SlateBorder, RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = breach.sampleHash,
                            style = MonoTypography.hashSmall,
                            color = CyberCyan,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cb.setPrimaryClip(ClipData.newPlainText("SHA1 Hash", breach.sampleHash))
                                Toast.makeText(context, "Hash copied", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Hash",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Remediation Steps
            if (breach.remediationSteps.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "SECURITY REMEDIATION RUNBOOK",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = RiskEmerald,
                        letterSpacing = 0.5.sp
                    )

                    breach.remediationSteps.forEachIndexed { i, step ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${i + 1}.",
                                style = MonoTypography.codeBadge,
                                color = CyberCyan
                            )
                            Text(
                                text = step,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Action: Mark Resolved vs Re-open
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (breach.isResolved) SlateCardSecondary else RiskEmerald)
                    .border(
                        1.dp,
                        if (breach.isResolved) SlateBorder else RiskEmerald,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        VibrationHelper.tactileTick(context)
                        onToggleResolved(breach.id)
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Status",
                        tint = if (breach.isResolved) TextSecondary else SlateBase,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (breach.isResolved) "Re-Open Vulnerability Incident" else "Mark Vulnerability Mitigated & Resolved",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (breach.isResolved) TextSecondary else SlateBase
                    )
                }
            }
        }
    }
}
