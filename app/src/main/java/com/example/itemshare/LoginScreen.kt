package com.example.itemshare

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginSuccess: (email: String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
) {
    var username by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var pass     by remember { mutableStateOf("") }

    Column(modifier) {
        Spacer(
            Modifier.height(40.dp)
        )
        Text(
            text = "ItemShare",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )
        Spacer(
            Modifier.height(20.dp)
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter name") },
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email") },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor =
                when {
                    email.endsWith("@sdsu.edu") -> Color.Green
                    email.isEmpty() -> Color.Gray
                    else -> Color.Red
                },
                focusedIndicatorColor =
                when {
                    email.endsWith("@sdsu.edu") -> Color.Green
                    email.isEmpty() -> Color.Gray
                    else -> Color.Red
                },
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Enter Password") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(
            Modifier.height(20.dp)
        )

        Button(onClick = { onLoginSuccess(email) }) { Text("Login") }
    }
}
