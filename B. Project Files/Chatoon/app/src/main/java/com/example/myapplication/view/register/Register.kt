package com.example.myapplication.view.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.view.Appbar
import com.example.myapplication.view.Buttons
import com.example.myapplication.view.TextFormField

@Composable
fun RegisterView(
    home: () -> Unit,
    back: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val email: String by registerViewModel.email.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val loading: Boolean by registerViewModel.loading.observeAsState(initial = false)
    val showPass = remember {
        mutableStateOf(false)
    }
    val confirm = remember {
        mutableStateOf(TextFieldValue())
    }
    val showDialog = remember { mutableStateOf(false) }

    val dialogText = remember { mutableStateOf("") }

    fun validateTextFields(): Boolean {

        val emailText = email
        val passwordText = password
        val confirmText = confirm.value.text

        if (emailText.isBlank()) {
            showDialog.value = true
            dialogText.value = "Please enter your email."
            return false
        }


        if (!emailText.matches(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
                    .toRegex()
            )
        ) {
            showDialog.value = true
            dialogText.value =
                "Email is invalid."
            return false
        }

        if (passwordText.isBlank()) {
            showDialog.value = true
            dialogText.value = "Please enter a password."
            return false
        }

        if (passwordText.length < 8 || !passwordText.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+\$".toRegex())) {
            showDialog.value = true
            dialogText.value =
                "Password should be at least 8 characters long and include a mix of letters, numbers, and symbols."
            return false
        }

        if (confirmText.isBlank()) {
            showDialog.value = true
            dialogText.value = "Please confirm your password."
            return false
        }

        if (passwordText != confirmText) {
            showDialog.value = true
            dialogText.value = "Password and confirm password do not match."
            return false
        }

        return true

    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (loading) {
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Appbar(
                title = "Register",
                action = back
            )
            Image(painterResource(id = R.drawable.steps), contentDescription = "")
            TextFormField(
                value = email,
                onValueChange = { registerViewModel.updateEmail(it) },
                label = "Email",
                keyboardType = KeyboardType.Email,
                visualTransformation = VisualTransformation.None
            )
            TextFormField(
                value = password,
                onValueChange = { registerViewModel.updatePassword(it) },
                label = "Password",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (!showPass.value) PasswordVisualTransformation() else VisualTransformation.None
            )
            TextFormField(
                value = confirm.value.text,
                onValueChange = { confirm.value = TextFieldValue(it) },
                label = "Confirm",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (!showPass.value) PasswordVisualTransformation() else VisualTransformation.None
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                Checkbox(
                    checked = showPass.value, onCheckedChange = {
                        showPass.value =
                            !showPass
                                .value
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color
                            (
                            26, 115,
                            232
                        )
                    )
                )

                Text(
                    text = "Show password", color = Color.Black, fontFamily =
                    FontFamily.SansSerif, fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Buttons(
                title = "Register",
                onClick = {
                    if (validateTextFields()) registerViewModel.registerUser(
                        home =
                        home
                    )
                },
                backgroundColor = Color.Black,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Alert", color = Color(32, 33, 36)) },
                    text = { Text(dialogText.value, color = Color(95, 99, 104)) },
                    confirmButton = {
                        Buttons(
                            title = "Ok",
                            onClick = { showDialog.value = false },
                            backgroundColor = Color.Black
                        )
                    },
                    shape = RoundedCornerShape(5.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}