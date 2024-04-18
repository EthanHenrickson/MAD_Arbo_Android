package com.mad.myapplication.ui.dashboard

import android.graphics.BitmapRegionDecoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mad.myapplication.MainViewModel
import com.mad.myapplication.R
import com.mad.myapplication.databinding.FragmentDashboardBinding
import com.mad.myapplication.miniDatabase2

class DashboardFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var mMap: GoogleMap
    private lateinit var db: FirebaseFirestore
    private val treelist = mutableListOf<Map<String, Any>>()
    var TAG = "Tester"
    private val viewModel: MainViewModel.SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        //call database and get all user placed trees
        db = Firebase.firestore

        db.collection("trees").get().addOnSuccessListener { result ->
            for (document in result) {
                val newDoc = document.data
                newDoc.put("id", "${document.id}")

                treelist.add(newDoc)
            }
            filterRefresh()
        }

        //init google map
        mMap = googleMap


        //Sets camera to be in bemidji area
        val bemidji = LatLng(47.47, -94.88)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bemidji, 10F))

    }

    override fun onResume() {
        super.onResume()

        binding.checkBoxAspen.isChecked = miniDatabase2.field2
        binding.checkBoxAsh.isChecked = miniDatabase2.field1
        binding.checkBoxBirch.isChecked = miniDatabase2.field3
        binding.checkBoxCedar.isChecked = miniDatabase2.field4
        binding.checkBoxCherry.isChecked = miniDatabase2.field5
        binding.checkBoxElm.isChecked = miniDatabase2.field6
        binding.checkBoxMaple.isChecked = miniDatabase2.field7
        binding.checkBoxOak.isChecked = miniDatabase2.field8
        binding.checkBoxPine.isChecked = miniDatabase2.field9
        binding.checkBoxSpruce.isChecked = miniDatabase2.field10
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Lots of Code to keep data same between Home and Dashboard


            binding.checkBoxAspen.isChecked = miniDatabase2.field2
            binding.checkBoxAsh.isChecked = miniDatabase2.field1
            binding.checkBoxBirch.isChecked = miniDatabase2.field3
            binding.checkBoxCedar.isChecked = miniDatabase2.field4
            binding.checkBoxCherry.isChecked = miniDatabase2.field5
            binding.checkBoxElm.isChecked = miniDatabase2.field6
            binding.checkBoxMaple.isChecked = miniDatabase2.field7
            binding.checkBoxOak.isChecked = miniDatabase2.field8
            binding.checkBoxPine.isChecked = miniDatabase2.field9
            binding.checkBoxSpruce.isChecked = miniDatabase2.field10
            // Update your views here



        binding.checkBoxAsh.setOnClickListener {
            miniDatabase2.field1 = binding.checkBoxAsh.isChecked
        }

        binding.checkBoxAspen.setOnClickListener {
            miniDatabase2.field2 = binding.checkBoxAspen.isChecked
        }

        binding.checkBoxBirch.setOnClickListener {
            miniDatabase2.field3 = binding.checkBoxBirch.isChecked
        }

        binding.checkBoxCedar.setOnClickListener {
            miniDatabase2.field4 = binding.checkBoxCedar.isChecked
        }

        binding.checkBoxCherry.setOnClickListener {
            miniDatabase2.field5 = binding.checkBoxCherry.isChecked
        }
        binding.checkBoxElm.setOnClickListener {
            miniDatabase2.field6 = binding.checkBoxElm.isChecked
        }
        binding.checkBoxMaple.setOnClickListener {
            miniDatabase2.field7 = binding.checkBoxMaple.isChecked
        }
        binding.checkBoxOak.setOnClickListener {
            miniDatabase2.field8 = binding.checkBoxOak.isChecked
        }
        binding.checkBoxPine.setOnClickListener {
            miniDatabase2.field9 = binding.checkBoxPine.isChecked
        }
        binding.checkBoxSpruce.setOnClickListener {
            miniDatabase2.field10 = binding.checkBoxSpruce.isChecked
        }





        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Asynchronously initialize the map
        mapFragment.getMapAsync(this)

        binding.filterButton.setOnClickListener {
            filterRefresh()
        }
        binding.selectNoneButton.setOnClickListener {
            noneSelect()
        }
        binding.selectAllButton.setOnClickListener {
            allSelect()
        }

    }


    fun allSelect() {

        binding.checkBoxAspen.isChecked = true
        binding.checkBoxAsh.isChecked = true
        binding.checkBoxBirch.isChecked = true
        binding.checkBoxCedar.isChecked = true
        binding.checkBoxCherry.isChecked = true
        binding.checkBoxElm.isChecked = true
        binding.checkBoxMaple.isChecked = true
        binding.checkBoxOak.isChecked = true
        binding.checkBoxPine.isChecked = true
        binding.checkBoxSpruce.isChecked = true
        binding.selectAllButton.visibility = View.INVISIBLE
        binding.selectNoneButton.visibility = View.VISIBLE

        miniDatabase2.field2 = true
        miniDatabase2.field1 = true
        miniDatabase2.field3 = true
        miniDatabase2.field4 = true
        miniDatabase2.field5 = true
        miniDatabase2.field6 = true
        miniDatabase2.field7 = true
        miniDatabase2.field8 = true
        miniDatabase2.field9 = true
        miniDatabase2.field10 = true

    }

    fun noneSelect() {
        val updatedData = viewModel.data.value ?: MainViewModel.FormData()

        binding.checkBoxAspen.isChecked = false
        binding.checkBoxAsh.isChecked = false
        binding.checkBoxBirch.isChecked = false
        binding.checkBoxCedar.isChecked = false
        binding.checkBoxCherry.isChecked = false
        binding.checkBoxElm.isChecked = false
        binding.checkBoxMaple.isChecked = false
        binding.checkBoxOak.isChecked = false
        binding.checkBoxPine.isChecked = false
        binding.checkBoxSpruce.isChecked = false
        binding.selectNoneButton.visibility = View.INVISIBLE
        binding.selectAllButton.visibility = View.VISIBLE


        miniDatabase2.field2 = false
        miniDatabase2.field1 = false
        miniDatabase2.field3 = false
        miniDatabase2.field4 = false
        miniDatabase2.field5 = false
        miniDatabase2.field6 = false
        miniDatabase2.field7 = false
        miniDatabase2.field8 = false
        miniDatabase2.field9 = false
        miniDatabase2.field10 = false
    }

    fun filterRefresh() {
        mMap.clear()

        val treeCheck = mutableListOf<String>()

        if (binding.checkBoxAspen.isChecked) {
            treeCheck.add("aspen")
        }
        if (binding.checkBoxAsh.isChecked) {
            treeCheck.add("ash")
        }
        if (binding.checkBoxBirch.isChecked) {
            treeCheck.add("birch")
        }
        if (binding.checkBoxCedar.isChecked) {
            treeCheck.add("cedar")
        }
        if (binding.checkBoxCherry.isChecked) {
            treeCheck.add("cherry")
        }
        if (binding.checkBoxElm.isChecked) {
            treeCheck.add("elm")
        }
        if (binding.checkBoxMaple.isChecked) {
            treeCheck.add("maple")
        }
        if (binding.checkBoxOak.isChecked) {
            treeCheck.add("oak")
        }
        if (binding.checkBoxPine.isChecked) {
            treeCheck.add("pine")
        }
        if (binding.checkBoxSpruce.isChecked) {
            treeCheck.add("spruce")
        }


        for (test in treelist) {
            if (treeCheck.contains(test.get("type").toString().lowercase())) {
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            test.get("lat").toString().toDouble(),
                            test.get("lng").toString().toDouble()
                        )
                    ).title("${test.get("type").toString().capitalize()}   ${test.get("id")}")
                        .snippet(
                            "Lat: ${test.get("lat").toString()}  Long: ${
                                test.get("lng").toString()
                            }"
                        ).icon(
                        BitmapDescriptorFactory.defaultMarker(
                            colorPicker(test.get("type").toString())
                        )
                    )
                )
            }
        }
    }

    fun colorPicker(type: String): Float{
        if(type == "aspen"){
            return 32.1F
        }
        if(type == "ash"){
            return 62.1F
        }
        if(type == "birch"){
            return 92.1F
        }
        if(type == "cedar"){
            return 359.1F
        }
        if(type == "cherry"){
            return 152.1F
        }
        if(type == "elm"){
            return 182.1F
        }
        if(type == "maple"){
            return 212.1F
        }
        if(type == "oak"){
            return 242.1F
        }
        if(type == "pine"){
            return 272.1f
        }
        return 302.1f

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}