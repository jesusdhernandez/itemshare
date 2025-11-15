package com.example.itemshare

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults

@Composable
fun HomeScreen() {
    var text by remember { mutableStateOf("You are my sunshine!") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        )
        {
            Text(
                text
            )
            Spacer(
                Modifier.width(8.dp)
            )
            Text(
                "bazinga"
            )
        }

        Spacer(
            Modifier.height(8.dp)
        )
        Text("hello world")
        Spacer(
            Modifier.height(8.dp)
        )
        UserBox()
        emailBox()
        passBox()
    }


}

@Composable
fun UserBox(){
    var username by remember { mutableStateOf("") }
    OutlinedTextField(
        value = username,
        onValueChange = {newUsername-> username = newUsername},
        label = {Text("Enter name")},
        modifier = Modifier
            .padding(16.dp)

    )

}
@Composable
fun emailBox() {
    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        value = email,
        onValueChange = { newEmail -> email = newEmail },
        label = { Text("Enter Email") },
        modifier = Modifier
            .padding(16.dp),
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


    )
}
@Composable
fun passBox(){
    var pass by remember {mutableStateOf("")}
    OutlinedTextField(
        value = pass,
        onValueChange = {newEmail->pass = newEmail},
        label = {Text("Enter Password")},
        modifier = Modifier
            .padding(16.dp),
    )

}

