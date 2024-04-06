package com.mad.myapplication.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mad.myapplication.R
import com.mad.myapplication.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var mMap: GoogleMap
    private lateinit var db: FirebaseFirestore
    private val treelist = mutableListOf<Map<String, Any>>()
    var TAG = "Tester"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db = Firebase.firestore

        db.collection("trees").get().addOnSuccessListener { result ->
            for (document in result) {
                treelist.add(document.data)
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add additional map customizations here
        // For example, add a marker

        for (test in treelist) {
            mMap.addMarker(MarkerOptions().position(LatLng(test.get("lat").toString().toDouble(), test.get("lng").toString().toDouble())).title(""))
        }

        val bemidji = LatLng(47.47,-94.88)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bemidji, 10F))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Asynchronously initialize the map
        mapFragment.getMapAsync(this)

        binding.filterButton.setOnClickListener{
            mMap.clear()

            val treeCheck = mutableListOf<String>()

            if(binding.checkBoxAspen.isChecked){
                treeCheck.add("aspen")
            }
            if(binding.checkBoxAsh.isChecked){
                treeCheck.add("ash")
            }
            if(binding.checkBoxBirch.isChecked){
                treeCheck.add("birch")
            }
            if(binding.checkBoxCedar.isChecked){
                treeCheck.add("cedar")
            }
            if(binding.checkBoxCherry.isChecked){
                treeCheck.add("cherry")
            }
            if(binding.checkBoxElm.isChecked){
                treeCheck.add("elm")
            }
            if(binding.checkBoxMaple.isChecked){
                treeCheck.add("maple")
            }
            if(binding.checkBoxOak.isChecked){
                treeCheck.add("oak")
            }
            if(binding.checkBoxPine.isChecked){
                treeCheck.add("pine")
            }
            if(binding.checkBoxSpruce.isChecked){
                treeCheck.add("spruce")
            }


            for (test in treelist){
                if (treeCheck.contains(test.get("type").toString().lowercase())) {
                    mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                test.get("lat").toString().toDouble(),
                                test.get("lng").toString().toDouble()
                            )
                        ).title("")
                    )
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}