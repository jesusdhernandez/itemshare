package com.example.itemshare

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@Composable
fun screenSwitch()
{
    var loggedIn by rememberSaveable { mutableStateOf(false)};

    if (loggedIn)
        ItemShareApp()
    else
        loginScreen(onLoginSuccess = { loggedIn = true})
}