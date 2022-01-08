package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.databinding.FragmentInstitutesPlotsListBinding

private const val ARG_PARAM1 = "recyclerType"

class InstitutesPlotsListFragment : Fragment() {
    private var recyclerType: RecyclerTypes? = null
    private var binding_ : FragmentInstitutesPlotsListBinding? = null
    private lateinit var binding : FragmentInstitutesPlotsListBinding
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
        binding_ = FragmentInstitutesPlotsListBinding.inflate(inflater)
        binding = binding_!!

        recyclerType?.let { binding.placeholder.text = it.name }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: RecyclerTypes) =
            InstitutesPlotsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1.name)
                }
            }
    }
}