# 🎬 Nexus Player KMP

A highly customizable, Jetpack Compose-based video player for Kotlin Multiplatform (KMP) and Android. Nexus Player KMP provides out-of-the-box support for multi-quality video switching, playback speed control, repeat toggling, and fully customizable UI components like loading indicators and settings menus.

<div align="center">





<img src="[https://github.com/user-attachments/assets/ef7b2b0b-0590-4bd6-95be-bfb574b7709d](https://github.com/user-attachments/assets/ef7b2b0b-0590-4bd6-95be-bfb574b7709d)" alt="Mobile(Android, IOS)" style="border-radius: 16px; box-shadow: 0 8px 16px rgba(0,0,0,0.2); max-width: 100%; width: 300px; border: 2px solid #eaeaea;">





</div>

---

## 🚀 Features

* **Jetpack Compose Ready**: Built entirely with Compose for seamless integration into modern UIs.
* **Multi-Quality Support**: Easily switch between SD, HD, FHD, and HDR streams (e.g., 144p to 1440p).
* **Playback Controls**: Built-in support for adjusting playback speed and looping.
* **Customizable UI**: Override default loading indicators, settings menus, and progress behaviors.
* **Metadata Extraction**: Easily hook into video metadata once the stream is loaded.

---

## 📦 Installation

Add the dependency to your module's `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("io.github.mamon-aburawi:nexus-player-kmp:{last_version}")
}

```


## 🛠️ Usage Guide

Using **Nexus Player KMP** requires setting up your video data and managing a few standard Compose states.

### Step 1: Define Your Video Data

First, create your video sources using the `VideoModule` and `VideoQuality` data classes. This allows the player to know what resolutions are available.

```kotlin
import player.module.VideoModule
import player.module.VideoQuality

private fun getVideos(): List<VideoModule> {
    return listOf(
        VideoModule(
            id = "1",
            title = "Sample Video",
            quality = listOf(
                VideoQuality(
                    label = "360p",
                    tagValue = "SD",
                    byteValue = 200,
                    url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772400078/Storm_360p_wtsr2j.mp4"
                ),
                VideoQuality(
                    label = "480p",
                    tagValue = "SD",
                    byteValue = 200,
                    url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772400233/Storm_480p_oexi4l.mp4"
                ),
                VideoQuality(
                    label = "720p",
                    tagValue = "HD",
                    byteValue = 200,
                    url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772400348/Storm_720p_sfxagn.mp4"
                ),
                VideoQuality(
                    label = "1080p",
                    tagValue = "FHD",
                    byteValue = 200,
                    url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772400621/Storm_1080p_xaq3cz.mp4"
                )
            )
        )
    )
}

```

### Step 2: Initialize State Variables

In your Composable, set up the states needed to control the active video, quality selection, speed, and metadata.

```kotlin
val videoList = getVideos()
var currentVideoIndex by remember { mutableIntStateOf(0) }
var currentVideoQualityIndex by remember { mutableIntStateOf(0) }
val videoData = videoList[currentVideoIndex]

var repeatEnabled by remember { mutableStateOf(false) }
var currentVideoSpeed by remember { mutableStateOf(1.0f) }
var videoMetaData by remember { mutableStateOf<NexusVideoMetaData?>(null) }

```

### Step 3: Implement the `VideoPlayer`

Pass your states into the `VideoPlayer` composable. You can provide a custom loading indicator via `onProgress`.

```kotlin
VideoPlayer(
    data = videoData,
    currentVideoQualityIndex = currentVideoQualityIndex,
    repeatEnabled = repeatEnabled,
    currentSpeed = currentVideoSpeed,
    onProgress = {
        CircularProgressIndicator(color = Color.Yellow)
    },
    // Settings and callbacks defined in Step 4...
)

```

### Step 4: Handle Settings with `PlayerMenu`

The `onSettings` lambda allows you to display a custom settings overlay. Use the built-in `PlayerMenu` to effortlessly handle quality selection, playback speed, and repeat toggles.

```kotlin
    onSettings = { onDismiss ->
        PlayerMenu(
            videoMetadata = videoMetaData,
            onDismiss = { onDismiss() },
            currentQualityIndex = currentVideoQualityIndex,
            qualities = videoData.quality,
            onSelectQuality = { currentQuality ->
                currentVideoQualityIndex = videoData.quality.indexOf(currentQuality)
            },
            currentSpeed = currentVideoSpeed,
            onPlaybackSpeed = { newSpeed -> currentVideoSpeed = newSpeed },
            repeatEnabled = repeatEnabled,
            onRepeatToggle = { repeatEnabled = it }
        )
    },
    onVideoComplete = {
        println("Video Complete: ${videoData.title}")
    },
    onDataLoaded = { videoMeta ->
        videoMetaData = videoMeta
    }

```

---

## 💻 Full Complete Example

Here is how everything comes together in a single UI component:

```kotlin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import player.VideoPlayer
import player.component.PlayerMenu
import player.module.NexusVideoMetaData
import player.module.VideoModule
import player.module.VideoQuality

@Composable
fun HomeContent() {
    val videoList = getVideos()
    var currentVideoIndex by remember { mutableIntStateOf(0) }
    var currentVideoQualityIndex by remember { mutableIntStateOf(0) }
    val videoData = videoList[currentVideoIndex]

    var repeatEnabled by remember { mutableStateOf(false) }
    var currentVideoSpeed by remember { mutableStateOf(1.0f) }
    var videoMetaData by remember { mutableStateOf<NexusVideoMetaData?>(null) }

    VideoPlayer(
        data = videoData,
        currentVideoQualityIndex = currentVideoQualityIndex,
        repeatEnabled = repeatEnabled,
        currentSpeed = currentVideoSpeed,
        onProgress = {
            CircularProgressIndicator(color = Color.Yellow)
        },
        onSettings = { onDismiss ->
            PlayerMenu(
                videoMetadata = videoMetaData,
                onDismiss = { onDismiss() },
                currentQualityIndex = currentVideoQualityIndex,
                qualities = videoData.quality,
                onSelectQuality = { currentQuality ->
                    currentVideoQualityIndex = videoData.quality.indexOf(currentQuality)
                },
                currentSpeed = currentVideoSpeed,
                onPlaybackSpeed = { newSpeed ->
                    currentVideoSpeed = newSpeed
                },
                repeatEnabled = repeatEnabled,
                onRepeatToggle = {
                    repeatEnabled = it
                }
            )
        },
        onVideoComplete = {
            println("VideoState: Video Complete ${videoData.title}")
        },
        onDataLoaded = { videoMeta ->
            videoMetaData = videoMeta
        }
    )
}

// Ensure you include your getVideos() and getVideoQualities() functions here...

```

---

## 🧩 Core Components API

| Component | Description |
| --- | --- |
| `VideoPlayer` | The primary Composable that renders the video surface and handles playback state. |
| `PlayerMenu` | A pre-built UI component for adjusting stream quality, speed, and looping settings. |
| `VideoModule` | Data class containing the unique ID, title, and a list of `VideoQuality` streams. |
| `VideoQuality` | Data class outlining a specific video stream (URL, label, tag value, byte size). |

---
