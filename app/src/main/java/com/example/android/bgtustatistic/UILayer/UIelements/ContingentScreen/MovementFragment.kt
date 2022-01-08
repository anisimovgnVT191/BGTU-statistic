package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.UILayer.UIelements.NoDataFragment
import com.example.android.bgtustatistic.databinding.FragmentMovementBinding

class MovementFragment : Fragment() {
    private var binding_: FragmentMovementBinding? = null
    private lateinit var binding: FragmentMovementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentMovementBinding.inflate(inflater)
        binding = binding_!!

        binding.apply {
            updateMovButton.setOnClickListener {
                childFragmentManager.beginTransaction()
                    .replace(binding.movContainer.id, NoDataFragment())
                    .commit()
            }
            settingsButton.setOnClickListener {
                showSettingsBottomSheet()
            }
        }

        childFragmentManager.beginTransaction()
            .replace(binding.movContainer.id, ContingentPlotsFragment())
            .commit()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
    private fun showSettingsBottomSheet(){
        val bottomSheet = SettingsBottomSheet()

        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

}