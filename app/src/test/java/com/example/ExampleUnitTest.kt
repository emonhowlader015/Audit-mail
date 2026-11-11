package com.example

import com.example.data.HashUtils
import com.example.data.SecurityRepository
import com.example.model.BreachSeverity
import com.example.model.ConnectionStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SecurityUnitTest {

    @Test
    fun testSha1Hashing_knownVectors() {
        // "password" -> 5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8
        val hash = HashUtils.sha1("password")
        assertEquals("5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8", hash)
    }

    @Test
    fun testKAnonymitySplit_prefixAndSuffixLengths() {
        val hash = HashUtils.sha1("password")
        val (prefix, suffix) = HashUtils.splitHash(hash)
        assertEquals(5, prefix.length)
        assertEquals(35, suffix.length)
        assertEquals("5BAA6", prefix)
        assertEquals("1E4C9B93F3F0682250B6CF8331B7EE68FD8", suffix)
    }

    @Test
    fun testSecurityRepository_metricsComputation() {
        val repo = SecurityRepository()
        val metrics = repo.currentMetrics.value
        assertTrue(metrics.totalScans > 0)
        assertTrue(metrics.overallSecurityScore in 0..100)
        assertNotNull(metrics.securityGrade)
    }

    @Test
    fun testSecurityRepository_revokeOAuthConnection() {
        val repo = SecurityRepository()
        val initialConn = repo.connections.value.first()
        repo.revokeOAuthConnection(initialConn.id)

        val updatedConn = repo.connections.value.first { it.id == initialConn.id }
        assertEquals(ConnectionStatus.REVOKED, updatedConn.status)
    }

    @Test
    fun testSecurityRepository_toggleBreachResolved() {
        val repo = SecurityRepository()
        val initialBreach = repo.breaches.value.first()
        val initialResolved = initialBreach.isResolved

        repo.toggleBreachResolved(initialBreach.id)
        val updatedBreach = repo.breaches.value.first { it.id == initialBreach.id }
        assertEquals(!initialResolved, updatedBreach.isResolved)
    }
}
