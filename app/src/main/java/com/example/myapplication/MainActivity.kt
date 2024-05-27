package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var currentUser by remember { mutableStateOf<Person?>(null) }
    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "行動式聯絡簿",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (currentUser == null) {
                RegisterOrLogin { user ->
                    currentUser = user
                    val intent = Intent(context, identitychoiceActivity::class.java).apply {
                        putExtra("userName", user.userName)
                        putExtra("userAccount", user.userAccount)
                        putExtra("userPassword", user.userPassword)
                    }
                    context.startActivity(intent)
                }
            } else {
                // 顯示其他內容或功能
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterOrLogin(onRegisterOrLoginSuccess: (Person) -> Unit) {
    var userName by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val db = Firebase.firestore

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )
        TextField(
            value = userName,
            onValueChange = { newText -> userName = newText },
            label = { Text("姓名") },
            placeholder = { Text("請輸入您的姓名") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = userPassword,
            onValueChange = { newText -> userPassword = newText },
            label = { Text("密碼") },
            placeholder = { Text("請輸入您的密碼") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                val user = Person(userName, userName, userPassword) // 使用者名稱與帳號相同
                db.collection("users")
                    .document(userName)
                    .set(user)
                    .addOnSuccessListener {
                        msg = "註冊成功"
                        onRegisterOrLoginSuccess(user)
                    }
                    .addOnFailureListener { e ->
                        msg = "註冊失敗：$e"
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("註冊 / 登入")
        }
        Text(
            text = msg,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

data class Person(
    var userName: String = "",
    var userAccount: String = "",
    var userPassword: String = "",
    var message: String = ""
)