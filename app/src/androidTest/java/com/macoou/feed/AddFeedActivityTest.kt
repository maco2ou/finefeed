package com.macoou.feed

import androidx.compose.ui.test.*

import com.macoou.feed.activities.MainActivity
import org.junit.Rule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Test

class AddFeedActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun AddFeedButtonExistsTest() {
        composeTestRule.onNodeWithContentDescription("Add Feed Activity").performClick()
        composeTestRule.onNodeWithText("Enter Feed URL").assertExists()
    }
}