package com.example.data

import java.security.MessageDigest
import java.util.Locale

object HashUtils {
    /**
     * Computes the SHA-1 hash of the given string and returns it in uppercase hex format.
     */
    fun sha1(input: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val bytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02X".format(it) }
    }

    /**
     * Splits a 40-character SHA-1 hash into:
     * - prefix: first 5 characters (sent to HIBP k-anonymity API)
     * - suffix: remaining 35 characters (retained strictly on client device)
     */
    fun splitHash(fullHash: String): Pair<String, String> {
        val upper = fullHash.uppercase(Locale.ROOT)
        return if (upper.length >= 5) {
            Pair(upper.substring(0, 5), upper.substring(5))
        } else {
            Pair(upper, "")
        }
    }
}
