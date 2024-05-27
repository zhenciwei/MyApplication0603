package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskScreen()
        }
    }
}

@Composable
fun TaskScreen() {
    val task1 = remember { mutableStateOf("") }
    val task2 = remember { mutableStateOf("") }
    val task3 = remember { mutableStateOf("") }
    val task1Checked = remember { mutableStateOf(false) }
    val task2Checked = remember { mutableStateOf(false) }
    val task3Checked = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "我的任務",
            fontSize = 48.sp,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskItem(
                taskText = task1.value,
                onTextChange = { task1.value = it },
                isChecked = task1Checked.value,
                onCheckedChange = { task1Checked.value = it }
            )

            TaskItem(
                taskText = task2.value,
                onTextChange = { task2.value = it },
                isChecked = task2Checked.value,
                onCheckedChange = { task2Checked.value = it }
            )

            TaskItem(
                taskText = task3.value,
                onTextChange = { task3.value = it },
                isChecked = task3Checked.value,
                onCheckedChange = { task3Checked.value = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Transparent)
                    .clickable {
                        // 跳轉到 StudentActivity
                        val intent = Intent(context, StudentActivity::class.java)
                        context.startActivity(intent)
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "返回",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun TaskItem(
    taskText: String,
    onTextChange: (String) -> Unit,
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
        BasicTextField(
            value = taskText,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .height(62.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(id = R.drawable.volume),
            contentDescription = "Voice Input",
            modifier = Modifier.size(65.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    TaskScreen()
}