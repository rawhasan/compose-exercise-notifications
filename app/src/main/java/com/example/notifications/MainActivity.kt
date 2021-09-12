package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current
    val channelId = "MyTestChannel"
    val notificationId = 0
    val myBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.header)
    val bigText = "This is my test notification in one line. Made it longer " +
            "by setting the setStyle property. " +
            "It should not fit in one line anymore, " +
            "rather show as a longer notification content."

    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            "Notifications in Jetpack Compose",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 100.dp)
        )

        // simple notification button
        Button(onClick = {
            showSimpleNotification(
                context,
                channelId,
                notificationId,
                "Simple notification",
                "This is a simple notification with default priority."
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Simple Notification")
        }

        // simple notification button with tap action
        Button(onClick = {
            showSimpleNotificationWithTapAction(
                context,
                channelId,
                notificationId,
                "Simple notification + Tap action",
                "This simple notification will open an activity on tap."
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Simple Notification + Tap Action")
        }

        // large text notification button
        Button(onClick = {
            showLargeTextNotification(
                context,
                channelId,
                notificationId + 1,
                "My Large Text Notification",
                bigText
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Large Text Notification")
        }

        // large text with big icon notification
        Button(onClick = {
            showLargeTextWithBigIconNotification(
                context,
                channelId,
                notificationId + 2,
                "Large Text with Big Icon Notification",
                "This is a large text notification with a big icon on the right.",
                myBitmap
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Large Text + Big Icon Notification")
        }

        // big picture with auto hiding thumbnail notification
        Button(onClick = {
            showBigPictureWithThumbnailNotification(
                context,
                channelId,
                notificationId + 3,
                "Big Picture + Avatar Notification",
                "This is a notification showing a big picture and an auto-hiding avatar.",
                myBitmap
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Big Picture + Big Icon Notification")
        }
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

// shows notification with a title and one-line content text
fun showSimpleNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_edit_location)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

// shows a simple notification with a tap action to show an activity
fun showSimpleNotificationWithTapAction(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_edit_location)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

// shows notification with large text
fun showLargeTextNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_edit_location)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(textContent)
        )
        .setPriority(priority)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

// shows notification with large text and a thumbnail image on the right
fun showLargeTextWithBigIconNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    largeIcon: Bitmap,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_edit_location)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setLargeIcon(largeIcon)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(textContent)
        )
        .setPriority(priority)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

// shows notification with a big picture and an auto-hiding thumbnail
fun showBigPictureWithThumbnailNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    bigImage: Bitmap,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_edit_location)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setLargeIcon(bigImage)
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(bigImage)
                .bigLargeIcon(null)
        )
        .setPriority(priority)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotificationsTheme {
        NotificationApp()
    }
}