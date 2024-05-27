package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeworkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeworkScreen()

        }
    }
}

@Composable
fun HomeworkScreen() {
    var morningTask by remember { mutableStateOf(TextFieldValue("")) }
    var afternoonTask by remember { mutableStateOf(TextFieldValue("")) }
    var nightTask by remember { mutableStateOf(TextFieldValue("")) }
    var morningChecked by remember { mutableStateOf(false) }
    var afternoonChecked by remember { mutableStateOf(false) }
    var nightChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "課堂作業",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 32.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeworkItem(
                label = "早上:",
                homeworkText = morningTask,
                onTextChange = { morningTask = it },
                isChecked = morningChecked,
                onCheckedChange = { morningChecked = it }
            )

            HomeworkItem(
                label = "下午:",
                homeworkText = afternoonTask,
                onTextChange = { afternoonTask = it },
                isChecked = afternoonChecked,
                onCheckedChange = { afternoonChecked = it }
            )

            HomeworkItem(
                label = "晚上:",
                homeworkText = nightTask,
                onTextChange = { nightTask = it },
                isChecked = nightChecked,
                onCheckedChange = { nightChecked = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.member),
                contentDescription = "Member",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Transparent)
                    .clickable { /* Handle member button click */ }
            )

            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFF6200EA), shape = RoundedCornerShape(4.dp))
            ) {
                Text(text = "保存", color = Color.White, fontSize = 16.sp)
            }


        }

    }
}

@Composable
fun HomeworkItem(
    label: String,
    homeworkText: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            fontSize = 26.sp,
            modifier = Modifier.width(70.dp)
        )
        BasicTextField(
            value = homeworkText,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .height(62.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeworkScreenPreview() {
    HomeworkScreen()

}