package com.example.kaspresso_learning.tests

import com.example.kaspresso_learning.BaseTestCase
import com.example.kaspresso_learning.steps.AvatarSelectSteps
import org.junit.Test

class AvatarSelectScreenTest : BaseTestCase() {

    @Test
    fun checkAvatarScreen() = run {
        step("Проверить, что отображаются 4 иконки аватаров") {
            for (i in 0..3) {
                AvatarSelectSteps.assertAvatarDisplayed(i)
            }
        }

        step("Проверить, что кнопка «Далее» отображается") {
            AvatarSelectSteps.assertNextButtonDisplayed()
        }
    }

    @Test
    fun checkAvatarError() = run {
        step("Нажать на кнопку 'Далее'") {
            AvatarSelectSteps.clickNextBtn()
        }

        step("Проверить отображение ошибки") {
            AvatarSelectSteps.assertErrorDisplayed("Сначала выберите аватар!")
        }
    }
}
