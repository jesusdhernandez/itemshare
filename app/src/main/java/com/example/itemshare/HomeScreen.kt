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
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
            .fillMaxWidth()

    )
    //send username info to backend/database
}
@Composable
fun emailBox() {
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
}
@Composable
fun passBox(){
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

}
