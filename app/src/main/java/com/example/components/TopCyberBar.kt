package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeMonospace
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanContainer
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.MonoTypography
import com.example.ui.theme.RiskEmerald
import com.example.ui.theme.SlateBase
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TopCyberBar(
    searchEmail: String,
    onEmailChange: (String) -> Unit,
    onTriggerAudit: (String) -> Unit,
    isScanning: Boolean,
    engineStatus: String,
    latencyMs: Long,
    onAttributionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Pulsing green dot animation for real-time engine telemetry
    val transition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SlateBase)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Upper row: Brand Logo + Engine Telemetry + Attribution Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand & Engine Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberCyanContainer)
                        .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Security Shield",
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "VulnAudit",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SlateCardSecondary)
                                .border(1.dp, SlateBorder, RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "v3.8",
                                style = MonoTypography.codeBadge,
                                color = CyberCyan
                            )
                        }
                    }

                    // Engine Status Telemetry Indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(RiskEmerald)
                                .alpha(pulseAlpha)
                        )
                        Text(
                            text = engineStatus,
                            style = MonoTypography.codeBadge,
                            color = RiskEmerald
                        )
                        Text(
                            text = "•",
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "${latencyMs}ms",
                            style = MonoTypography.codeBadge,
                            color = TextMuted
                        )
                    }
                }
            }

            // Top Bar Attribution Badge (Mandatory: Designed & Engineered by EMON HOWLADER)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SlateCard)
                    .border(1.dp, SlateBorder, RoundedCornerShape(20.dp))
                    .clickable {
                        VibrationHelper.tactileTick(context)
                        onAttributionClick()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("top_bar_attribution_badge")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CyberCyan)
                    )
                    Text(
                        text = "Designed & Engineered by EMON HOWLADER",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search / Audit Input Field with Email Validation & Tactile Action
        val isEmailValid = searchEmail.contains("@") && searchEmail.contains(".")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchEmail,
                onValueChange = onEmailChange,
                placeholder = {
                    Text(
                        text = "Enter email or domain to audit vulnerability...",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (searchEmail.isNotEmpty()) CyberCyan else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = CyberCyan
                        )
                    } else if (searchEmail.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isEmailValid) RiskEmerald.copy(alpha = 0.2f) else SlateBorder)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isEmailValid) "VALID" else "INPUT",
                                style = MonoTypography.codeBadge,
                                color = if (isEmailValid) RiskEmerald else TextMuted
                            )
                        }
                    }
                },
                singleLine = true,
                textStyle = MonoTypography.emailDisplay.copy(color = TextPrimary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchEmail.isNotBlank()) {
                            VibrationHelper.tactileTick(context)
                            onTriggerAudit(searchEmail)
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SlateCard,
                    unfocusedContainerColor = SlateCard,
                    disabledContainerColor = SlateCard,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SlateBorder,
                    cursorColor = CyberCyan
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("audit_email_input")
            )

            // Tactile Run Audit Button
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (searchEmail.isNotBlank()) CyberCyan else SlateCardSecondary)
                    .border(
                        1.dp,
                        if (searchEmail.isNotBlank()) CyberCyan else SlateBorder,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(enabled = !isScanning && searchEmail.isNotBlank()) {
                        VibrationHelper.tactileTick(context)
                        onTriggerAudit(searchEmail)
                    }
                    .padding(horizontal = 14.dp)
                    .testTag("run_audit_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Audit",
                        tint = if (searchEmail.isNotBlank()) SlateBase else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Audit",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (searchEmail.isNotBlank()) SlateBase else TextMuted
                    )
                }
            }
        }
    }
}
