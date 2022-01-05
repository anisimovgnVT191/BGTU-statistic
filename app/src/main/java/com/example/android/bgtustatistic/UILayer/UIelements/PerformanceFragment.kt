package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.databinding.FragmentPerformanceBinding

class PerformanceFragment : Fragment() {
    private var binding_: FragmentPerformanceBinding? = null
    private lateinit var binding: FragmentPerformanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_ = FragmentPerformanceBinding.inflate(inflater)
        binding = binding_!!

        binding.updatePerfButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(binding.perfContainer.id, NoDataFragment())
                .commit()
        }
        childFragmentManager.beginTransaction()
            .replace(binding.perfContainer.id, PerformancePlotsFragment())
            .commit()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}