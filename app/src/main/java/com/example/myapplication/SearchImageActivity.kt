package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.AugmentedImage
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment

class SearchImageActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    private var configApplied = false

    // Храним активные маркеры
    private val placedMarkers = mutableMapOf<AugmentedImage, AnchorNode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arFragment.instructionsController.isEnabled = false
        arFragment.instructionsController.isVisible = false
        arFragment.arSceneView.planeRenderer.isVisible = false
        arFragment.arSceneView.planeRenderer.isEnabled = false

        arFragment.arSceneView.scene.addOnUpdateListener {

            val session = arFragment.arSceneView.session ?: return@addOnUpdateListener

            if (!configApplied) {
                val config = Config(session).apply {
                    focusMode = Config.FocusMode.AUTO
                    updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE

                    val input = assets.open("images.imgdb")
                    augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, input)
                }
                session.configure(config)
                configApplied = true
            }

            val frame = arFragment.arSceneView.arFrame ?: return@addOnUpdateListener
            val updatedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)

            for (image in updatedImages) {
                if (image.trackingState == TrackingState.TRACKING && !placedMarkers.containsKey(
                        image
                    )
                ) {
                    placeMarker(image)
                }
            }
        }
    }

    private fun placeMarker(augmentedImage: AugmentedImage) {
        val markerView = ImageView(this).apply {
            setImageResource(android.R.drawable.presence_online)
            layoutParams = ViewGroup.LayoutParams(100, 100)
        }

        ViewRenderable.builder()
            .setView(this, markerView)
            .build()
            .thenAccept { viewRenderable ->
                val anchor = augmentedImage.createAnchor(augmentedImage.centerPose)
                val anchorNode = AnchorNode(anchor).apply {
                    setParent(arFragment.arSceneView.scene)
                }

                MaterialFactory.makeOpaqueWithColor(
                    this,
                    com.google.ar.sceneform.rendering.Color(Color.WHITE)
                ).thenAccept { material ->
                    val sphere = ShapeFactory.makeSphere(
                        0.02f,
                        Vector3.zero(),
                        material
                    )

                    val node = Node().apply {
                        renderable = sphere
                        localPosition = Vector3(0f, 0.05f, 0f)
                    }

                    node.setOnTapListener { _, _ ->
                        val intent = Intent(this, DetailedActivity::class.java).apply {
                            putExtra("IMAGE_NAME", augmentedImage.name)
                        }
                        startActivity(intent)
                    }

                    anchorNode.addChild(node)
                    placedMarkers[augmentedImage] = anchorNode
                }
            }
    }

}
