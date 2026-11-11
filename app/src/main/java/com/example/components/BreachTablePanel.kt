package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreachTablePanel(
    breaches: List<BreachRecord>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedSeverity: BreachSeverity?,
    onSeveritySelect: (BreachSeverity?) -> Unit,
    onBreachClick: (BreachRecord) -> Unit,
    onToggleResolved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Table Title & Stats Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Breach Analysis Table",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Text(
                    text = "High-density incident telemetry & compromised credential audit",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SlateCardSecondary)
                    .border(1.dp, SlateBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${breaches.size} ENTRIES",
                    style = MonoTypography.codeBadge,
                    color = CyberCyan
                )
            }
        }

        // Search bar + Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = "Filter source, domain, or data type...",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                },
                singleLine = true,
                textStyle = MonoTypography.hashSmall.copy(color = TextPrimary),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SlateCard,
                    unfocusedContainerColor = SlateCard,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SlateBorder,
                    cursorColor = CyberCyan
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("breach_table_search")
            )
        }

        // Severity Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SeverityFilterChip(
                label = "ALL",
                isSelected = selectedSeverity == null,
                color = CyberCyan,
                onClick = { onSeveritySelect(null) }
            )
            BreachSeverity.values().forEach { sev ->
                val sevColor = when (sev) {
                    BreachSeverity.CRITICAL -> RiskCritical
                    BreachSeverity.HIGH -> RiskOrange
                    BreachSeverity.MODERATE -> RiskAmber
                    BreachSeverity.LOW -> RiskEmerald
                }
                SeverityFilterChip(
                    label = sev.label.uppercase(),
                    isSelected = selectedSeverity == sev,
                    color = sevColor,
                    onClick = { onSeveritySelect(sev) }
                )
            }
        }

        // High-Density Data Table Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SlateCard)
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlateCardSecondary)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SOURCE",
                        style = MonoTypography.codeBadge,
                        color = TextSecondary,
                        modifier = Modifier.weight(1.3f)
                    )
                    Text(
                        text = "BREACH DATE",
                        style = MonoTypography.codeBadge,
                        color = TextSecondary,
                        modifier = Modifier.weight(0.9f)
                    )
                    Text(
                        text = "SEVERITY",
                        style = MonoTypography.codeBadge,
                        color = TextSecondary,
                        modifier = Modifier.weight(0.9f)
                    )
                    Text(
                        text = "STATUS",
                        style = MonoTypography.codeBadge,
                        color = TextSecondary,
                        modifier = Modifier.weight(0.8f)
                    )
                }

                // Table Rows
                if (breaches.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "No breaches",
                                tint = RiskEmerald,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "No matching vulnerabilities found",
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "All audited accounts within filter criteria are clean",
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                } else {
                    breaches.forEachIndexed { index, breach ->
                        BreachTableRowItem(
                            breach = breach,
                            isEven = index % 2 == 0,
                            onClick = {
                                VibrationHelper.tactileTick(context)
                                onBreachClick(breach)
                            },
                            onToggleResolved = {
                                VibrationHelper.tactileTick(context)
                                onToggleResolved(breach.id)
                            }
                        )
                        if (index < breaches.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(SlateBorder)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeverityFilterChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) color.copy(alpha = 0.15f) else SlateCard)
            .border(
                1.dp,
                if (isSelected) color else SlateBorder,
                RoundedCornerShape(6.dp)
            )
            .clickable {
                VibrationHelper.tactileTick(context)
                onClick()
            }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            style = MonoTypography.codeBadge,
            color = if (isSelected) color else TextSecondary
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BreachTableRowItem(
    breach: BreachRecord,
    isEven: Boolean,
    onClick: () -> Unit,
    onToggleResolved: () -> Unit
) {
    val severityColor = when (breach.severity) {
        BreachSeverity.CRITICAL -> RiskCritical
        BreachSeverity.HIGH -> RiskOrange
        BreachSeverity.MODERATE -> RiskAmber
        BreachSeverity.LOW -> RiskEmerald
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isEven) SlateCard else SlateCardSecondary.copy(alpha = 0.4f))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("breach_row_${breach.id}")
    ) {
        // Upper Table Line: Source | Date | Severity | Status
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column 1: Source & Domain
            Column(modifier = Modifier.weight(1.3f)) {
                Text(
                    text = breach.sourceName,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = breach.domain,
                    style = MonoTypography.hashSmall,
                    color = CyberCyan
                )
            }

            // Column 2: Date
            Text(
                text = breach.breachDate,
                style = MonoTypography.hashSmall,
                color = TextSecondary,
                modifier = Modifier.weight(0.9f)
            )

            // Column 3: Severity Badge
            Box(
                modifier = Modifier.weight(0.9f)
            ) {
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

            // Column 4: Status / Resolved toggle
            Row(
                modifier = Modifier.weight(0.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (breach.isResolved) RiskEmerald.copy(alpha = 0.15f) else RiskAmber.copy(alpha = 0.15f))
                        .border(
                            1.dp,
                            if (breach.isResolved) RiskEmerald.copy(alpha = 0.4f) else RiskAmber.copy(alpha = 0.4f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (breach.isResolved) "RESOLVED" else "OPEN",
                        style = MonoTypography.codeBadge,
                        color = if (breach.isResolved) RiskEmerald else RiskAmber
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Details",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Lower Line: Compromised Data Types chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Exposed:",
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = TextMuted
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.weight(1f)
            ) {
                breach.compromisedDataTypes.take(4).forEach { item ->
                    val isPass = item.contains("Password", ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isPass) RiskCritical.copy(alpha = 0.12f) else SlateBorder.copy(alpha = 0.5f))
                            .border(
                                1.dp,
                                if (isPass) RiskCritical.copy(alpha = 0.35f) else SlateBorder,
                                RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = item,
                            style = MonoTypography.codeBadge,
                            color = if (isPass) RiskCritical else TextSecondary
                        )
                    }
                }
                if (breach.compromisedDataTypes.size > 4) {
                    Text(
                        text = "+${breach.compromisedDataTypes.size - 4} more",
                        style = MonoTypography.codeBadge,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
