/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.ui.text

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.PlatformTextInputService
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TextInputService
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalTextApi::class)
@RunWith(JUnit4::class)
class TextInputServiceTest {

    @Test
    fun startInputGeneratesDifferentToken() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )
        val secondToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        assertThat(firstToken).isNotEqualTo(secondToken)
    }

    @Test
    fun stopInput_with_valid_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        textInputService.stopInput(firstToken)
        verify(platformService, times(1)).stopInput()
    }

    @Test
    fun stopInput_with_expired_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        // Start another session. The firstToken is now expired.
        textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        textInputService.stopInput(firstToken)
        verify(platformService, never()).stopInput()
    }

    @Test
    fun showSoftwareKeyboard_with_valid_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        textInputService.showSoftwareKeyboard(firstToken)
        verify(platformService, times(1)).showSoftwareKeyboard()
    }

    @Test
    fun showSoftwareKeyboard_with_expired_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        // Start another session. The firstToken is now expired.
        textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        textInputService.showSoftwareKeyboard(firstToken)
        verify(platformService, never()).showSoftwareKeyboard()
    }

    @Test
    fun onStateUpdated_with_valid_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        val editorModel = TextFieldValue()
        textInputService.onStateUpdated(firstToken, editorModel)
        verify(platformService, times(1)).onStateUpdated(eq(editorModel))
    }

    @Test
    fun onStateUpdated_with_expired_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        // Start another session. The firstToken is now expired.
        textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        val editorModel = TextFieldValue()
        textInputService.onStateUpdated(firstToken, editorModel)
        verify(platformService, never()).onStateUpdated(any())
    }

    @Test
    fun notifyFocusedRect_with_valid_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        val rect = Rect(Offset.Zero, Size(100f, 100f))
        textInputService.notifyFocusedRect(firstToken, rect)
        verify(platformService, times(1)).notifyFocusedRect(eq(rect))
    }

    @Test
    fun notifyFocusedRect_with_expired_token() {
        val platformService = mock<PlatformTextInputService>()

        val textInputService = TextInputService(platformService)

        val firstToken = textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        // Start another session. The firstToken is now expired.
        textInputService.startInput(
            TextFieldValue(),
            KeyboardOptions.Default,
            {}, // onEditCommand
            {} // onImeActionPerformed
        )

        val rect = Rect(Offset.Zero, Size(100f, 100f))
        textInputService.notifyFocusedRect(firstToken, rect)
        verify(platformService, never()).notifyFocusedRect(any())
    }
}
