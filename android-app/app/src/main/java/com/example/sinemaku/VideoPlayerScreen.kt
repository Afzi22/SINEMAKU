package com.example.sinemaku

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerScreen(
    title: String,
    videoResId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val previousOrientation = remember(activity) { activity.requestedOrientation }
    var manualFullscreen by remember { mutableStateOf(false) }

    val exoPlayer = remember(context, videoResId) {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = Player.REPEAT_MODE_OFF
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        onDispose { activity.requestedOrientation = previousOrientation }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    DisposableEffect(activity, isLandscape) {
        val controller = WindowCompat.getInsetsController(
            activity.window, activity.window.decorView
        )
        if (isLandscape) {
            controller.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
        onDispose { controller.show(WindowInsetsCompat.Type.systemBars()) }
    }

    // Setelah keluar dari fullscreen manual ke portrait, aktifkan sensor lagi.
    LaunchedEffect(isLandscape, manualFullscreen) {
        if (!isLandscape && !manualFullscreen &&
            activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
    }

    BackHandler {
        if (isLandscape) {
            if (manualFullscreen) {
                manualFullscreen = false
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                onBack()
            }
        } else {
            onBack()
        }
    }

    if (isLandscape) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = true
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        player = exoPlayer
                    }
                },
                update = { view -> view.player = exoPlayer },
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = {
                    manualFullscreen = false
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali ke portrait",
                    tint = Color.White
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFF101116))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
                Text("Tonton Trailer", style = MaterialTheme.typography.titleMedium)
            }

            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = true
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        player = exoPlayer
                    }
                },
                update = { view -> view.player = exoPlayer },
                modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
            )

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                Text("Trailer lokal • Dapat ditonton offline", style = MaterialTheme.typography.bodyMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { exoPlayer.seekTo(0); exoPlayer.play() }) {
                        Icon(Icons.Default.Replay, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Putar Ulang")
                    }
                    Button(onClick = {
                        manualFullscreen = true
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }) {
                        Icon(Icons.Default.Fullscreen, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Fullscreen")
                    }
                }

                HorizontalDivider()
                Text("Tentang Trailer", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Nikmati cuplikan $title langsung di SINEMAKU. " +
                            "Video tersimpan secara lokal dan dapat diputar tanpa koneksi internet.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Tips: Miringkan HP ke samping untuk menonton dalam mode fullscreen.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
