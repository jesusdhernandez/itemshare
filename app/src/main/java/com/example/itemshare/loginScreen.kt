package com.example.itemshare

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@Composable
fun loginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize())
{
    var user: String
    var email: String
    var pass: String
    var loginSuccess: Boolean

    Column(modifier)
    {
        Spacer(
            Modifier.height(16.dp)
        )
        Text(
            text = "ItemShare",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )
        Spacer(
            Modifier.height(20.dp)
        )
        user = UserBox()
        email = emailBox()
        pass = passBox()

        Spacer(
            Modifier.height(20.dp)
        )

        Button(onClick = onLoginSuccess) { Text("Login") }
    }
}

@Composable
fun UserBox(): String
{
    var username by remember { mutableStateOf("") }
    OutlinedTextField(
        value = username,
        onValueChange = {newUsername-> username = newUsername},
        label = {Text("Enter name")},
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()

    )
    //send username info to backend/database
    return username
}
@Composable
fun emailBox(): String
{
    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        value = email,
        onValueChange = { newEmail -> email = newEmail },
        label = { Text("Enter Email") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor =
                when{
                    email.endsWith("@sdsu.edu") -> Color.Green
                    email == "" -> Color.Gray
                    else -> Color.Red
                },
            focusedIndicatorColor =
                when{
                    email.endsWith("@sdsu.edu") -> Color.Green
                    email == ""-> Color.Gray
                    else -> Color.Red
                },
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )

        //send email info to backend/database

    )
    return email
}
@Composable
fun passBox(): String
{
    var pass by remember {mutableStateOf("")}
    OutlinedTextField(
        value = pass,
        onValueChange = {newEmail->pass = newEmail},
        label = {Text("Enter Password")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
    //send pass info to backend/database
    return pass
}
