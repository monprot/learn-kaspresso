package com.example.kaspresso_learning.steps

import com.example.kaspresso_learning.screens.AvatarSelectScreen

object AvatarSelectSteps {

    fun selectAvatar(index: Int) {
        AvatarSelectScreen {
            with(avatar(index)) {
                assertIsDisplayed()
                performClick()
            }
        }
    }

    fun clickNextBtn() {
        AvatarSelectScreen {
            nextButton {
                assertIsDisplayed()
                performClick()
            }
        }
    }

    fun assertAvatarDisplayed(index: Int) {
        AvatarSelectScreen {
            avatar(index).assertIsDisplayed()
        }
    }

    fun assertNextButtonDisplayed() {
        AvatarSelectScreen.nextButton.assertIsDisplayed()
    }

    fun assertErrorDisplayed(expectedText: String) {
        AvatarSelectScreen {
            error {
                assertIsDisplayed()
                assertTextEquals(expectedText)
            }
        }
    }
}
