package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class ParentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ParentScreen()

        }
    }
}

@Composable
fun ParentScreen() {
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
            text = "家長專區",
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
                imageResId = R.drawable.student,
                buttonText = "與孩子互動",
                onClick = {context.startActivity(Intent(context, ParenttalktostudentActivity::class.java)) }
            )

            InteractionRow(
                imageResId = R.drawable.teacher,
                buttonText = "與老師互動",
                onClick = {context.startActivity(Intent(context, ParenttalktoteacherActivity::class.java)) }
            )
        }
    }
}

@Composable
fun InteractionRow(
    imageResId: Int,
    buttonText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start, // Align images and buttons to the start
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = buttonText,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(100.dp) // Adjusted height for a more balanced look
                .weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = buttonText, fontSize = 24.sp) // Adjusted font size for better readability
        }
    }
}