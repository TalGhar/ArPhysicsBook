package com.example.myapplication

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ExternalTexture
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var externalTexture: ExternalTexture
    private var videoRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        // Текстура для видео
        externalTexture = ExternalTexture()

        // MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.test)
        mediaPlayer.setSurface(externalTexture.surface)
        mediaPlayer.isLooping = true

        // Загружаем материал
        Material.builder()
            .setSource(this, R.raw.video_material)
            .build()
            .thenAccept { material ->
                material.setExternalTexture("videoTexture", externalTexture)

                videoRenderable = ShapeFactory.makeCube(
                    Vector3(0.5f, 0.01f, 0.3f),
                    Vector3.zero(),
                    material
                )
            }

        // Тап по плоскости
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, _ ->
            if (videoRenderable == null) return@setOnTapArPlaneListener

            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            val node = TransformableNode(arFragment.transformationSystem)
            node.renderable = videoRenderable
            node.setParent(anchorNode)
            node.select()

            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}