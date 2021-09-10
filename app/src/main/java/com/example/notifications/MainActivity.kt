package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notifications.ui.theme.NotificationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotificationsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NotificationApp()
                }
            }
        }
    }
}

@Composable
fun NotificationApp() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text("Notification in Compose.", style = MaterialTheme.typography.subtitle1)
    }

    val context = LocalContext.current
    val channelId = "MyTestChannel"
    val notificationId = 0
    val myBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.header)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_edit_location) // 48 x 48 px looks perfect
        .setContentTitle("My Test Notification")
        .setContentText("This is my test notification in one line...")
        .setLargeIcon(myBitmap)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(
                    "This is my test notification in one line. Made it longer " +
                            "by setting the setStyle property. " +
                            "It should not fit in one line anymore, " +
                            "rather show as a longer notification content."
                )
        )
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(myBitmap)
                .bigLargeIcon(null)
        )

        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    createNotificationChannel(channelId, context)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

fun createNotificationChannel(channelId: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MyTestChannel"
        val descriptionText = "My important test channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotificationsTheme {
        NotificationApp()
    }
}