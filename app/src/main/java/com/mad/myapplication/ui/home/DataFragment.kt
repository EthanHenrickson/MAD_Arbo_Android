package com.mad.myapplication.ui.home

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
import com.mad.myapplication.MainViewModel
import com.mad.myapplication.R
import com.mad.myapplication.databinding.FragmentDataBinding
import com.mad.myapplication.databinding.FragmentNotificationsBinding
import com.mad.myapplication.miniDatabase


class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private var TAG = "Hi"

    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private val treelist = mutableListOf<Map<String, Any>>()
    private val viewModel: MainViewModel.SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore


        binding.treeId.text = miniDatabase.id
        binding.type.text = miniDatabase.type
        binding.lat.text = miniDatabase.lat
        binding.lng.text = miniDatabase.lng
        binding.desc.text = miniDatabase.notes

        binding.delete.setOnClickListener {
            db.collection("trees").document(miniDatabase.id).delete()
            findNavController().navigate(R.id.navigation_home)


        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}