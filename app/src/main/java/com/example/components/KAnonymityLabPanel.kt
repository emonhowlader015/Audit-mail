package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PwnedCheckResult
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanContainer
import com.example.ui.theme.CyberCyanLight
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.MonoTypography
import com.example.ui.theme.RiskAmber
import com.example.ui.theme.RiskCritical
import com.example.ui.theme.RiskCriticalBg
import com.example.ui.theme.RiskEmerald
import com.example.ui.theme.RiskEmeraldBg
import com.example.ui.theme.SlateBase
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun KAnonymityLabPanel(
    passwordInput: String,
    onPasswordChange: (String) -> Unit,
    onExecuteCheck: (String) -> Unit,
    result: PwnedCheckResult?,
    isScanning: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val samplePasswords = listOf("password", "P@ssw0rd2024!", "123456", "admin", "Correct-Horse-Battery-Staple")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Lab Header
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = "k-Anonymity Cryptographic Lab",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )
            Text(
                text = "Live HaveIBeenPwned v3 Range API & Zero-Knowledge Verification",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Mathematical Architecture Explainer Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SlateCard)
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "k-Anonymity Architecture",
                        tint = CyberCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "How k-Anonymity Zero-Knowledge Proof Works:",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "1. Your input string is converted into a 40-character SHA-1 hash locally.\n" +
                        "2. Only the first 5 characters (Prefix) are transmitted to the HIBP Range API.\n" +
                        "3. The remaining 35 characters (Suffix) NEVER leave your device.\n" +
                        "4. The server returns all matching candidate hashes. Your device inspects the response locally to find an exact match.",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
            }
        }

        // Live Test Credential Input Field
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "AUDIT CREDENTIAL STRING",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = onPasswordChange,
                    placeholder = {
                        Text(
                            text = "Enter password to test hash exposure...",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Password Input",
                            tint = CyberCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    singleLine = true,
                    textStyle = MonoTypography.hashMedium.copy(color = TextPrimary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            if (passwordInput.isNotBlank()) {
                                VibrationHelper.tactileTick(context)
                                onExecuteCheck(passwordInput)
                            }
                        }
                    ),
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
                        .height(46.dp)
                        .testTag("k_anonymity_input")
                )

                // Execute Audit Button
                Box(
                    modifier = Modifier
                        .height(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (passwordInput.isNotBlank()) CyberCyan else SlateCardSecondary)
                        .border(
                            1.dp,
                            if (passwordInput.isNotBlank()) CyberCyan else SlateBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(enabled = !isScanning && passwordInput.isNotBlank()) {
                            VibrationHelper.tactileTick(context)
                            onExecuteCheck(passwordInput)
                        }
                        .padding(horizontal = 14.dp)
                        .testTag("execute_k_anonymity_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = SlateBase,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Run",
                                tint = if (passwordInput.isNotBlank()) SlateBase else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Test",
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = if (passwordInput.isNotBlank()) SlateBase else TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Sample Credential Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preset tests:",
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = TextMuted
            )

            samplePasswords.forEach { pwd ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SlateCardSecondary)
                        .border(1.dp, SlateBorder, RoundedCornerShape(6.dp))
                        .clickable {
                            VibrationHelper.tactileTick(context)
                            onPasswordChange(pwd)
                            onExecuteCheck(pwd)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pwd,
                        style = MonoTypography.codeBadge,
                        color = CyberCyan
                    )
                }
            }
        }

        // Live Cryptographic Telemetry & Result Card
        if (result != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SlateCard)
                    .border(
                        1.dp,
                        if (result.isPwned) RiskCritical.copy(alpha = 0.5f) else RiskEmerald.copy(alpha = 0.5f),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(14.dp)
                    .testTag("k_anonymity_result_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Result Header Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (result.isPwned) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = if (result.isPwned) "Exposed" else "Clean",
                                tint = if (result.isPwned) RiskCritical else RiskEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = if (result.isPwned) "VULNERABILITY DETECTED" else "CRYPTOGRAPHICALLY SECURE",
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (result.isPwned) RiskCritical else RiskEmerald
                            )
                        }

                        Text(
                            text = "${result.latencyMs}ms API Latency",
                            style = MonoTypography.codeBadge,
                            color = TextMuted
                        )
                    }

                    // Pwn Count Stat
                    if (result.isPwned) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(RiskCritical.copy(alpha = 0.12f))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Found in ${String.format("%,d", result.pwnedCount)} publicly leaked data breaches worldwide. Threat actors can compromise accounts using this credential in credential-stuffing attacks.",
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = RiskCritical
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(RiskEmerald.copy(alpha = 0.12f))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "0 occurrences detected across over 850 million breached passwords in the HIBP repository. This credential has never appeared in a known corporate data dump.",
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = RiskEmerald
                            )
                        }
                    }

                    // Hash Breakdown Visualizer
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SlateCardSecondary)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "FULL SHA-1 HASH:",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = result.fullHash,
                            style = MonoTypography.hashSmall,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Visual breakdown: Prefix (Wire) vs Suffix (Local Device)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Prefix Sent
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "SENT OVER WIRE (5 Chars)",
                                    fontFamily = InterFontFamily,
                                    fontSize = 9.sp,
                                    color = CyberCyan
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyberCyanContainer)
                                        .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = result.sha1Prefix,
                                        style = MonoTypography.codeBadge,
                                        color = CyberCyanLight
                                    )
                                }
                            }

                            // Suffix Retained
                            Column(modifier = Modifier.weight(2f)) {
                                Text(
                                    text = "RETAINED LOCALLY (35 Chars)",
                                    fontFamily = InterFontFamily,
                                    fontSize = 9.sp,
                                    color = RiskEmerald
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(RiskEmerald.copy(alpha = 0.1f))
                                        .border(1.dp, RiskEmerald.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = result.sha1Suffix,
                                        style = MonoTypography.codeBadge,
                                        color = RiskEmerald,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
