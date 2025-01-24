package com.example.notes.noteList

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.notes.MainActivity
import com.example.notes.di.AppModule
import com.example.notes.util.AppConstant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NodeListScreenTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.waitForIdle()

    }


    @Test
    fun clickFloatingActionButton_navigateToNoteDetailScreen(){
            composeRule.onNodeWithContentDescription("Add Note").performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithTag(AppConstant.TITLE).performTextInput("Hi")
            composeRule.onNodeWithTag(AppConstant.DESCRIPTION).performTextInput("Hello")
            composeRule.onNodeWithContentDescription(AppConstant.SAVE_NOTE).performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithText("Hi").assertIsDisplayed()
            composeRule.onNodeWithText("Hello").assertIsDisplayed()
            composeRule.waitForIdle()

            composeRule.onNodeWithText("Hi").performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithTag(AppConstant.TITLE).assertTextEquals("Hi")
            composeRule.onNodeWithTag(AppConstant.DESCRIPTION).assertTextEquals("Hello")

            composeRule.onNodeWithTag(AppConstant.TITLE).performTextInput("Title")
            composeRule.waitForIdle()
            composeRule.onNodeWithTag(AppConstant.DESCRIPTION).performTextInput("Description")
            composeRule.waitForIdle()
            composeRule.onNodeWithContentDescription(AppConstant.SAVE_NOTE).performClick()
            composeRule.waitForIdle()

    }

}