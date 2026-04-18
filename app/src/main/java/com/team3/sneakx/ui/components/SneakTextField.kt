package com.team3.sneakx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.team3.sneakx.ui.theme.SneakFieldShape
import com.team3.sneakx.ui.theme.SneakSpacing

/**
 * Spec: label above field (not floating), 52dp min height, 16dp corners.
 */
@Composable
fun SneakLabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.8.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(SneakSpacing.sm))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            supportingText = supportingText,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            suffix = suffix,
            placeholder = placeholder,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            label = null,
            shape = SneakFieldShape,
            colors = sneakOutlinedTextFieldColors(),
        )
    }
}
