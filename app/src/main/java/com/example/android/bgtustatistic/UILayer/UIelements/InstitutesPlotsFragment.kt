package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.StateHolders.RecyclerTypes
import com.example.android.bgtustatistic.databinding.FragmentInstitutesPlotsBinding

private const val ARG_PARAM1 = "recyclerType"


class InstitutesPlotsFragment : Fragment() {
    private var recyclerType: RecyclerTypes? = null
    private var binding_ : FragmentInstitutesPlotsBinding? = null
    private lateinit var binding : FragmentInstitutesPlotsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(ARG_PARAM1)?.let { str ->
                recyclerType = RecyclerTypes.valueOf(str)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentInstitutesPlotsBinding.inflate(inflater)
        binding = binding_!!

        recyclerType?.let {
            when(it){
                RecyclerTypes.Deducted -> {
                    binding.backButton.text = getString(R.string.movement_fragment_title)
                    binding.movementType.text = getString(R.string.deducted)
                }
                RecyclerTypes.Enrolled -> {
                    binding.backButton.text = getString(R.string.movement_fragment_title)
                    binding.movementType.text = getString(R.string.enrolled)
                }
                RecyclerTypes.Performance -> {
                    binding.backButton.text = getString(R.string.performance_fragment_title)
                    binding.movementType.visibility = View.GONE
                }
            }

            childFragmentManager.beginTransaction()
                .replace(binding.listContainer.id, InstitutesPlotsListFragment.newInstance(it))
                .commit()
        }?:let {
            childFragmentManager.beginTransaction()
                .replace(binding.listContainer.id, NoDataFragment())
                .commit()
        }

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: RecyclerTypes) =
            InstitutesPlotsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1.name)
                }
            }
    }
}