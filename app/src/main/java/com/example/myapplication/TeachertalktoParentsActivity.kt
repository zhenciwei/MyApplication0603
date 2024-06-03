package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException

class TeachertalktoparentActivity : ComponentActivity() {
    private var recorder: MediaRecorder? = null
    private var isRecording by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 獲取傳遞的使用者資料
        val userName = intent.getStringExtra("userName") ?: ""
        val userAccount = intent.getStringExtra("userAccount") ?: ""
        val userPassword = intent.getStringExtra("userPassword") ?: ""
        val currentUser = Person(userName, userAccount, userPassword)

        setContent {
            ParentTeacherInteractionScreen(
                currentUser = currentUser,
                startRecording = { startRecording() },
                stopRecording = { stopRecording() },
                isRecording = isRecording,
            )
        }
    }
    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 0)
            return
        }

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile("${externalCacheDir?.absolutePath}/audiorecordtest.3gp")
            try {
                prepare()
                start()
                isRecording = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        isRecording = false
    }
    @Composable
    fun ParentTeacherInteractionScreen(
        currentUser: Person,
        startRecording: () -> Unit,
        stopRecording: () -> Unit,
        isRecording: Boolean
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0))
                .padding(16.dp)
        ) {
            ParentMessageSection()
            Spacer(modifier = Modifier.height(16.dp))

            TeacherMessageSection(currentUser = currentUser)
            Spacer(modifier = Modifier.height(16.dp))
            IconSection(
                startRecording = startRecording,
                stopRecording = stopRecording,
                isRecording = isRecording,
                mmIcon = R.drawable.mm,
                homeworkIcon = R.drawable.homework,
                peopleIcon = R.drawable.people
            )
        }
    }

    @Composable
    fun ParentMessageSection() {
        val db = Firebase.firestore
        val usersCollectionRef = db.collection("users")
        val query = usersCollectionRef
            .whereEqualTo("userID", "家長")

        // 創建用於存儲消息的狀態
        var studentMessages by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
        var loading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            query.get()
                .addOnSuccessListener { documents ->
                    val messages = documents.mapNotNull {
                        val message1 = it.getString("message")
                        val message2 = it.getString("message2")
                        if (message1 != null && message2 != null) {
                            Pair(message1, message2)
                        } else {
                            null
                        }
                    }
                    studentMessages = messages
                    loading = false
                }
                .addOnFailureListener { exception ->
                    errorMessage = "Error getting documents: ${exception.message}"
                    loading = false
                }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFFFF59D))
                    .padding(16.dp)
            ) {
                Text(
                    text = "家長的留言",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                when {
                    loading -> {
                        Text(text = "Loading...", fontSize = 16.sp, color = Color.Gray)
                    }
                    errorMessage != null -> {
                        Text(text = errorMessage ?: "Unknown error", fontSize = 16.sp, color = Color.Red)
                    }
                    studentMessages.isEmpty() -> {
                        Text(text = "No messages found", fontSize = 16.sp, color = Color.Gray)
                    }
                    else -> {
                        studentMessages.forEach { (message1, message2) ->
                            Text(
                                text = "Message: $message1",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Message2: $message2",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.volume),
                        contentDescription = "Play Audio",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun TeacherMessageSection(currentUser: Person) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFFFF59D))
                    .padding(16.dp)
            ) {
                Text(
                    text = "給家長的話",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 在這裡添加老師的留言內容或文本框
                SendMessage5(currentUser)
            }
        }
    }

    @Composable
    fun IconSection(
        startRecording: () -> Unit,
        stopRecording: () -> Unit,
        isRecording: Boolean,
        mmIcon: Int,
        homeworkIcon: Int,
        peopleIcon: Int
    ) {
        val context = LocalContext.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (isRecording) stopRecording() else startRecording() }) {
                Icon(
                    painter = painterResource(id = mmIcon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = if (isRecording) Color.Red else Color.Black
                )
            }

            IconButton(onClick = {context.startActivity(Intent(context, HomeworkActivity::class.java))}) {
                Icon(
                    painter = painterResource(id = homeworkIcon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.Black
                )
            }
            IconButton(onClick = {context.startActivity(Intent(context, TeachertostudentActivity::class.java))}) {
                Icon(
                    painter = painterResource(id = peopleIcon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.Black
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Transparent)
                    .clickable {
                        val intent = Intent(context, TeacherActivity::class.java)
                        context.startActivity(intent)
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        // 獲取傳遞的使用者資料
        val userName = intent.getStringExtra("userName") ?: ""
        val userAccount = intent.getStringExtra("userAccount") ?: ""
        val userPassword = intent.getStringExtra("userPassword") ?: ""
        val currentUser = Person(userName, userAccount, userPassword)

        ParentTeacherInteractionScreen(
            currentUser = Person(userName, userAccount, userPassword),
            startRecording = {},
            stopRecording = {},
            isRecording = false
        )
    }
}
// 傳訊息功能
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessage5(currentUser: Person) {
    var userMsg by remember { mutableStateOf("") }
    var userMsg2 by remember { mutableStateOf("") } // Add this line
    var msg by remember { mutableStateOf("") }
    val db = Firebase.firestore

    Column {
        TextField(
            value = userMsg,
            onValueChange = { newText -> userMsg = newText },
            label = { Text("新增訊息") },
            placeholder = { Text("請輸入訊息") }
        )
        TextField( // Add this TextField for message2
            value = userMsg2,
            onValueChange = { newText -> userMsg2 = newText },
            label = { Text("新增訊息 2") },
            placeholder = { Text("請輸入訊息 2") }
        )
        Button(onClick = {
            val message = mapOf(
                "message" to userMsg,
                "message2" to userMsg2
            )
            db.collection("users")
                .document(currentUser.userName) // 使用者名稱作為文件ID
                .collection("Messages") // 新增訊息子集合
                .add(message) // 添加新訊息
                .addOnSuccessListener { documentReference ->
                    msg = "傳送成功"
                }
                .addOnFailureListener { e ->
                    msg = "傳送失敗：" + e.toString()
                }
        }) {
            Text("傳送")
        }
        Text(text = msg)
    }
}