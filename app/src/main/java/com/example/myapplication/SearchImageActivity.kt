package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.Saver
import com.google.ar.core.AugmentedImage
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment

class SearchImageActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    private var videoPlaced = false
    private var configApplied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            val session = arFragment.arSceneView.session ?: return@addOnUpdateListener

            if (!configApplied) {
                val config = Config(session).apply {
                    focusMode = Config.FocusMode.AUTO
                    updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE

                    val input = assets.open("images.imgdb")
                    augmentedImageDatabase= AugmentedImageDatabase.deserialize(session, input)
                }
                session.configure(config)
                configApplied = true
            }

            val frame = arFragment.arSceneView.arFrame ?: return@addOnUpdateListener
            val updatedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)

            for (image in updatedImages) {
                if(image.trackingState == TrackingState.TRACKING && !videoPlaced) {
                    placeVideo(image)
                    videoPlaced = true
                    android.util.Log.d("t1","da")
                }
            }

        }
    }

    private fun placeVideo(augmentedImage: AugmentedImage) {
        ViewRenderable.builder()
            .setView(this, R.layout.video_layout)
            .build()
            .thenAccept { viewRenderable ->
                val anchor = augmentedImage.createAnchor(augmentedImage.centerPose)
                val anchorNode = AnchorNode(anchor).apply {
                    setParent(arFragment.arSceneView.scene)
                }
                val videoView = viewRenderable.view.findViewById<VideoView>(R.id.videoView)
                videoView.setVideoPath("android.resource://$packageName/${R.raw.lion}")
                videoView.setOnPreparedListener { mp: MediaPlayer ->
                    mp.isLooping = true
                    videoView.start()
                }
                val node = Node().apply { renderable = viewRenderable }
                anchorNode.addChild(node)
            }
    }
}