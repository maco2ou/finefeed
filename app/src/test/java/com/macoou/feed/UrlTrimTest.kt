package com.macoou.feed

import com.macoou.feed.operations.trimWhitespace
import org.junit.Test

import org.junit.Assert.*

/**
 * Test to see if URL trimming works properly.
 *
 */
class UrlTrimTest {
    @Test
    fun windowsLineEndingTrimTest() {
        val actual = trimWhitespace("hello world!\r\n")
        assertEquals("hello world!", actual)
    }
}