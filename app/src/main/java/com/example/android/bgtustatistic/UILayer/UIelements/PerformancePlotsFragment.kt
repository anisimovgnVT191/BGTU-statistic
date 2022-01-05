package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.StateHolders.RecyclerTypes
import com.example.android.bgtustatistic.databinding.FragmentPerformancePlotsBinding

class PerformancePlotsFragment : Fragment() {

    private lateinit var binding : FragmentPerformancePlotsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerformancePlotsBinding.inflate(inflater)

        binding.arrearsCard.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InstitutesPlotsFragment.newInstance(RecyclerTypes.Performance))
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }
}