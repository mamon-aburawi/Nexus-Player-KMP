package com.nexusplayer

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import io.github.kdroidfilter.composemediaplayer.VideoMetadata
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState

import player.VideoPlayer
import player.component.PlayerMenu
import player.module.VideoModule
import player.module.VideoQuality


@Composable
fun Home(){
    HomeContent()
}


@Composable
fun HomeContent(){
    val videoList = getVideos()
    var currentVideoIndex by remember { mutableIntStateOf(0) }
    var currentVideoQualityIndex by remember { mutableIntStateOf(0) }
    val videoData = videoList[currentVideoIndex]

    var repeatEnabled by remember { mutableStateOf(false) }

    var currentVideoSpeed by remember { mutableStateOf(1.0f) }
    var videoMetaData by remember { mutableStateOf<VideoMetadata?>(null) }
    val playerState = rememberVideoPlayerState()


    VideoPlayer(
        data = videoData,
        currentVideoQualityIndex = currentVideoQualityIndex,
        playerState = playerState,
        repeatEnabled = repeatEnabled,
        currentSpeed = currentVideoSpeed,
        onProgress = {
            CircularProgressIndicator(
                color = Color.Yellow
            )
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
            println("VideoState: Video Complete ${videoList[currentVideoIndex].title}")
        },
        onDataLoaded = { videoMeta ->
            videoMetaData = videoMeta
        }
    )

}

private fun getVideos(): List<VideoModule> {

    return List(3){
        VideoModule(
            id = it.toString(),
            title = "Video $it",
            quality = getVideoQualities()
        )
    }
}

private fun getVideoQualities(): List<VideoQuality> {
    return listOf(
        VideoQuality(
            label = "144p",
            tagValue = "SD",
            url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772399969/Storm_144p_i1rxuw.mp4",
            byteValue = 1200
        ),
        VideoQuality(
            label = "240p",
            tagValue = "SD",
            byteValue = 450,
            url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772400041/Storm_240p_zcqdqk.mp4"
        ),
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
        ),
        VideoQuality(
            label = "1440p",
            tagValue = "HDR",
            byteValue = 200,
            url = "https://res.cloudinary.com/dxgzgmju8/video/upload/v1772401737/Storm_1440p_i9hzq7.mp4"
        )
    )
}


//@Preview
//@Composable
//private fun HomePreview(){
//    HomeContent()
//}