package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.RecyclerTypes
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutesPlotsFragment
import com.example.android.bgtustatistic.databinding.FragmentContingentPlotsBinding

class ContingentPlotsFragment : Fragment() {
    private var binding_ : FragmentContingentPlotsBinding? = null
    private lateinit var bindig : FragmentContingentPlotsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentContingentPlotsBinding.inflate(inflater)
        bindig = binding_!!

        bindig.apply {
            deductedDetailsButton.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        InstitutesPlotsFragment.newInstance(RecyclerTypes.Deducted)
                    )
                    .addToBackStack(null)
                    .commit()
            }
            enrolledDetailsButton.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        InstitutesPlotsFragment.newInstance(RecyclerTypes.Enrolled)
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
        return bindig.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}