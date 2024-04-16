package com.mad.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mad.myapplication.MainViewModel
import com.mad.myapplication.R
import com.mad.myapplication.databinding.FragmentHomeBinding
import com.mad.myapplication.miniDatabase

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val treelist = mutableListOf<Map<String, Any>>()
    private lateinit var db: FirebaseFirestore
    private lateinit var cardItem: View
    private lateinit var cardContainer: LinearLayout
    private lateinit var dataFragment: DataFragment
    private val viewModel: MainViewModel.SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    fun addCardItem(type: String, ids: String, lat: String, lng: String, notes: String) {
        cardItem = layoutInflater.inflate(R.layout.carditem, cardContainer, false)
        val treeType: TextView = cardItem.findViewById(R.id.card_item_text)
        treeType.text = type
        val id: TextView = cardItem.findViewById(R.id.card_id)
        id.text = ids

        cardItem.setOnClickListener {
            miniDatabase.id = ids
            miniDatabase.type = type
            miniDatabase.lat = lat
            miniDatabase.lng = lng
            miniDatabase.notes = notes

            // Create an instance of the DataFragment and pass the item data
            findNavController().navigate(R.id.navigation_data)
        }
        cardContainer.addView(cardItem)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.data.observe(viewLifecycleOwner, Observer { formData ->

            binding.checkBoxAspen.isChecked = formData.field2
            binding.checkBoxAsh.isChecked = formData.field1
            binding.checkBoxBirch.isChecked = formData.field3
            binding.checkBoxCedar.isChecked = formData.field4
            binding.checkBoxCherry.isChecked = formData.field5
            binding.checkBoxElm.isChecked = formData.field6
            binding.checkBoxMaple.isChecked = formData.field7
            binding.checkBoxOak.isChecked = formData.field8
            binding.checkBoxPine.isChecked = formData.field9
            binding.checkBoxSpruce.isChecked = formData.field10
            // Update your views here
            Log.d("Tag", formData.field2.toString())

        })

        binding.checkBoxAsh.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field1 = binding.checkBoxAsh.isChecked
            viewModel.data.value = updatedData
        }

        binding.checkBoxAspen.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field2 = binding.checkBoxAspen.isChecked
            viewModel.data.value = updatedData
        }

        binding.checkBoxBirch.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field3 = binding.checkBoxBirch.isChecked
            viewModel.data.value = updatedData
        }

        binding.checkBoxCedar.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field4 = binding.checkBoxCedar.isChecked
            viewModel.data.value = updatedData
        }

        binding.checkBoxCherry.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field5 = binding.checkBoxCherry.isChecked
            viewModel.data.value = updatedData
        }
        binding.checkBoxElm.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field6 = binding.checkBoxElm.isChecked
            viewModel.data.value = updatedData
        }
        binding.checkBoxMaple.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field7 = binding.checkBoxMaple.isChecked
            viewModel.data.value = updatedData
        }
        binding.checkBoxOak.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field8 = binding.checkBoxOak.isChecked
            viewModel.data.value = updatedData
        }
        binding.checkBoxPine.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field9 = binding.checkBoxPine.isChecked
            viewModel.data.value = updatedData
        }
        binding.checkBoxSpruce.setOnClickListener {
            val updatedData = viewModel.data.value ?: MainViewModel.FormData()
            updatedData.field10 = binding.checkBoxSpruce.isChecked
            viewModel.data.value = updatedData
        }

        cardContainer = binding.containerLayout

        db = Firebase.firestore
        treelist.clear()
        db.collection("trees").get().addOnSuccessListener { result ->
            for (document in result) {
                val newDoc = document.data
                Log.d("TAG", newDoc.toString())
                newDoc.put("id", "${document.id}")
                treelist.add(newDoc)
            }
            filterRefresh()
        }

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

        val updatedData = viewModel.data.value ?: MainViewModel.FormData()

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

        updatedData.field2 = true
        updatedData.field1 = true
        updatedData.field3 = true
        updatedData.field4 = true
        updatedData.field5 = true
        updatedData.field6 = true
        updatedData.field7 = true
        updatedData.field8 = true
        updatedData.field9 = true
        updatedData.field10 = true
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

        updatedData.field2 = false
        updatedData.field1 = false
        updatedData.field3 = false
        updatedData.field4 = false
        updatedData.field5 = false
        updatedData.field6 = false
        updatedData.field7 = false
        updatedData.field8 = false
        updatedData.field9 = false
        updatedData.field10 = false
    }

    fun filterRefresh() {
        val treeCheck = mutableListOf<String>()
        cardContainer.removeAllViews()

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
                addCardItem(test.get("type").toString().lowercase().capitalize(), test.get("id").toString(), test.get("lat").toString(), test.get("lng").toString(), test.get("notes").toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}