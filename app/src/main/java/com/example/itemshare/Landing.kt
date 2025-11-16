package com.example.itemshare

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun screenSwitch()
{
    var loggedIn by rememberSaveable { mutableStateOf(false) }
    var userEmail by rememberSaveable { mutableStateOf("") }

    if (loggedIn) {
        ItemShareApp(userEmail)
    } else {
        loginScreen(
            onLoginSuccess = {
                loggedIn = true
                userEmail = it
            }
        )
    }
}