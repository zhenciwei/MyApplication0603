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

@Suppress("DEPRECATION")
class ParenttalktoteacherActivity : ComponentActivity() {
    private var recorder: MediaRecorder? = null
    private var isRecording by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParentTeacherInteractionScreen(
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
            StudentMessageSection()
            Spacer(modifier = Modifier.height(16.dp))
            TeacherMessageSection()
            Spacer(modifier = Modifier.height(16.dp))
            IconSection(
                startRecording = startRecording,
                stopRecording = stopRecording,
                isRecording = isRecording
            )
        }
    }

    @Composable
    fun StudentMessageSection() {
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
                    text = "老師的留言",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "今天小明有吃藥喔",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.volume), // 替換為您的圖標資源ID
                        contentDescription = "Play Audio",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun TeacherMessageSection() {
        val currentUser = Person("Teacher", "")

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
                    text = "給老師的話",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                SendMessage2(currentUser)
            }
        }
    }

    @Composable
    fun IconSection(
        startRecording: () -> Unit,
        stopRecording: () -> Unit,
        isRecording: Boolean
    ) {
        val context = LocalContext.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icons = listOf(
                R.drawable.mm
            )

            // Recording button
            IconButton(onClick = { if (isRecording) stopRecording() else startRecording() }) {
                Icon(
                    painter = painterResource(id = icons[0]),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = if (isRecording) Color.Red else Color.Black
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
                        val intent = Intent(context, ParentActivity::class.java)
                        context.startActivity(intent)
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ParentTeacherInteractionScreen(
            startRecording = {},
            stopRecording = {},
            isRecording = false,
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessage2(currentUser: Person) {
    var userMsg by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val db = Firebase.firestore

    Column {
        TextField(
            value = userMsg,
            onValueChange = { newText ->
                userMsg = newText
            },
            label = { Text("新增訊息") },
            placeholder = { Text("請輸入訊息") }
        )

        Button(onClick = {
            val message = Person(currentUser.userName, userMsg)
            db.collection("users")
                .document(currentUser.userName)
                .collection("Messages")
                .add(message)
                .addOnSuccessListener { _ ->
                    msg = "傳送成功"
                }
                .addOnFailureListener { e ->
                    msg = "傳送失敗：$e"
                }
        }) {
            Text("傳送")
        }
        Text(text = msg)
    }
}