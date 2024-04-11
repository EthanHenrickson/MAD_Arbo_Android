package com.mad.myapplication.ui.notifications

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mad.myapplication.R
import com.mad.myapplication.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment(), OnMapReadyCallback, OnMarkerDragListener {

    private var _binding: FragmentNotificationsBinding? = null
    private var TAG = "Hi"

    private val binding get() = _binding!!
    private val notificationsViewModel: NotificationsViewModel by viewModels()

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var db: FirebaseFirestore
    private val treelist = mutableListOf<Map<String, Any>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Asynchronously initialize the map
        mapFragment.getMapAsync(this)

    }

    override fun onMarkerDrag(p0: Marker) {

    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    override fun onMarkerDragEnd(p0: Marker) {

        val  value = p0.position
        val lat = value.latitude.toString()
        val lng = value.longitude.toString()


        binding.lat.setText("${lat}")
        binding.lng.setText("${lng}")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //call database and get all user placed trees
        //init google map
        mMap = googleMap

        mMap.setOnMarkerDragListener(this)

        //Sets camera to be in bemidji area
        val bemidji = LatLng(47.48555773270, -94.8789536207)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bemidji, 10F))

        marker = mMap.addMarker(
            MarkerOptions().position(
                bemidji
            ).draggable(true)
        )!!

        binding.lat.setText("47.48555773270")
        binding.lng.setText("-94.8789536207")


        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // this function is called before text is edited
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // this function is called when text is edited

            }

            override fun afterTextChanged(s: Editable) {
                // this function is called after text is edited
                if(binding.lat.text.toString().length > 2 && binding.lng.text.toString().length > 2){
                    Log.d(TAG, "CAlled")
                        if(binding.lat.text.toString().toDouble() > -90 && binding.lat.text.toString().toDouble() < 90 && binding.lng.text.toString().toDouble() > -180 && binding.lng.text.toString().toDouble() < 180){
                            marker.position = LatLng(binding.lat.text.toString().toDouble(), binding.lng.text.toString().toDouble())
                        }
                    }
            }
        }


        binding.lng.addTextChangedListener(textWatcher)
        binding.lat.addTextChangedListener(textWatcher)


        db = Firebase.firestore
        binding.submitButton.setOnClickListener{

            val lat = binding.lat.text.toString().toDouble()
            val lng = binding.lng.text.toString().toDouble()
            val notes = binding.notes.text.toString()
            val type = binding.type.text.toString()

            val data = hashMapOf(
                "lat" to lat,
                "lng" to lng,
                "type" to type,
                "notes" to notes
            )

            db.collection("trees").add(data)

            binding.lat.setText("47.48555773270")
            binding.lng.setText("-94.8789536207")
            binding.notes.setText("")
            binding.type.setText("")

            marker.position = LatLng(binding.lat.text.toString().toDouble(), binding.lng.text.toString().toDouble())

            Toast.makeText(requireContext(), "Added Tree!", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}