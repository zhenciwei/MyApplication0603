package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class StudentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentScreen()

        }
    }
}

@Composable
fun StudentScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(85.dp)
    ) {
        Text(
            text = "學生專區",
            fontSize = 48.sp, // Adjusted size for better readability
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(100.dp) // Adjusted spacing for better separation
        ) {
            InteractionRow(
                imageResId = R.drawable.homework,
                buttonText = "任務",
                onClick = {context.startActivity(Intent(context, TaskActivity::class.java))}
            )

            InteractionRow(
                imageResId = R.drawable.speak,
                buttonText = "聊天",
                onClick = { context.startActivity(Intent(context, ChatActivity::class.java)) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentScreenPreview() {
    StudentScreen()

}