package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Core Slate Gray Cybersecurity Palette
val SlateBase = Color(0xFF090D16)         // Slate gray base background
val SlateCard = Color(0xFF161B26)         // Card background
val SlateCardSecondary = Color(0xFF1C2230)// Elevated card / hover state
val SlateCardHighlight = Color(0xFF222B3D)// Selected item background
val SlateBorder = Color(0xFF262C3A)       // 1px micro-border color
val SlateBorderSubtle = Color(0x33262C3A) // Translucent border

// Accent Muted Cyan for active status & cyber accents
val CyberCyan = Color(0xFF0EA5E9)         // Active status & primary actions
val CyberCyanLight = Color(0xFF38BDF8)    // Monospace highlight / text accent
val CyberCyanMuted = Color(0xFF0284C7)    // Darker cyan
val CyberCyanContainer = Color(0xFF082F49)// Container background for cyan chips
val CyberCyanGlow = Color(0x400EA5E9)     // Subtle glow halo

// Risk Level Indicators (Emerald, Amber, Orange, Crimson)
val RiskEmerald = Color(0xFF10B981)       // Low Risk / Safe / Verified
val RiskEmeraldBg = Color(0xFF064E3B)
val RiskAmber = Color(0xFFF59E0B)         // Moderate Risk / Notice
val RiskAmberBg = Color(0xFF78350F)
val RiskOrange = Color(0xFFF97316)        // High Risk / Action Required
val RiskOrangeBg = Color(0xFF7C2D12)
val RiskCritical = Color(0xFFEF4444)      // Critical Risk / Leaked Password
val RiskCriticalBg = Color(0xFF7F1D1D)

// Typography & Content Colors
val TextPrimary = Color(0xFFF8FAFC)       // Crisp white text
val TextSecondary = Color(0xFF94A3B8)     // Muted gray labels
val TextMuted = Color(0xFF64748B)         // Deep muted / timestamps
val CodeMonospace = Color(0xFF38BDF8)     // Monospace hashes / codes
