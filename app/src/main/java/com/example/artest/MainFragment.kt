package com.example.artest

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var arFragment: ArFragment
    private lateinit var robotModel: Renderable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arFragment = childFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
        loadModel()
    }

    private fun loadModel() {
        lifecycleScope.launch {
            robotModel = ModelRenderable
                .builder()
                .setSource(
                    context,
                    Uri.parse("scene.sfb")
                )
                .build()
                .await()
            Toast.makeText(
                requireContext(),
                "Model available",
                Toast.LENGTH_SHORT
            ).show()
            initTapListener()
        }
    }

    private fun initTapListener() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchorNode = AnchorNode(
                hitResult.createAnchor()
            )
            anchorNode.setParent(arFragment.arSceneView.scene)
            val robotNode = Node()
            robotNode.renderable = robotModel
            robotNode.setParent(anchorNode)
        }
    }

}
