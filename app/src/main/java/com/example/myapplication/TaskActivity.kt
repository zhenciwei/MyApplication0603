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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

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
    var taskMessage1 by remember { mutableStateOf("查無資料") }
    var taskMessage2 by remember { mutableStateOf("查無資料") }
    var taskMessage3 by remember { mutableStateOf("查無資料") }

    val checkedStates = remember { mutableStateListOf(false, false, false) }
    val context = LocalContext.current

    val db = Firebase.firestore

    db.collection("users")
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var msg1 = ""
                var msg2 = ""
                var msg3 = ""

                for (document in task.result!!) {
                    val userId = document.id
                    db.collection("users").document(userId).collection("tasks").document("dailyTasks")
                        .get()
                        .addOnSuccessListener { dailyTasks ->
                            if (dailyTasks.exists()) {
                                val morningTask = dailyTasks.get("morningTask.task") as String? ?: ""
                                val afternoonTask = dailyTasks.get("afternoonTask.task") as String? ?: ""
                                val nightTask = dailyTasks.get("nightTask.task") as String? ?: ""

                                if (morningTask.isNotEmpty()) msg1 += "早上的作業:" + morningTask
                                if (afternoonTask.isNotEmpty()) msg2 += "中午的作業:" + afternoonTask
                                if (nightTask.isNotEmpty()) msg3 += "晚上的作業:" + nightTask
                            }
                            // Handle the msg, e.g., display it or log it
                            taskMessage1 = msg1
                            taskMessage2 = msg2
                            taskMessage3 = msg3
                        }
                }
            } else {
                // Handle the error
                taskMessage1 = "no message"
                taskMessage2 = "no message"
                taskMessage3 = "no message"
            }
        }


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
                taskText = taskMessage1,
                isChecked = checkedStates[0],
                onCheckedChange = { checkedStates[0] = it }
            )

            TaskItem(
                taskText = taskMessage2,
                isChecked = checkedStates[1],
                onCheckedChange = { checkedStates[1] = it }
            )

            TaskItem(
                taskText = taskMessage3,
                isChecked = checkedStates[2],
                onCheckedChange = { checkedStates[2] = it }
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
            text = taskText,
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