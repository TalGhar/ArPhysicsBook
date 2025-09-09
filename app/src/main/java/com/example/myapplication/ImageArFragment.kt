package com.example.myapplication

import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class ImageArFragment: ArFragment() {

    private fun getSessionConfiguration(session: Session): Config {
        val config = Config(session).apply {
            updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
            focusMode = Config.FocusMode.AUTO

            val input = requireContext().assets.open("images.imgdb")
            augmentedImageDatabase = AugmentedImageDatabase.deserialize(session,input)
        }
        arSceneView.session = session

        return config
    }

}