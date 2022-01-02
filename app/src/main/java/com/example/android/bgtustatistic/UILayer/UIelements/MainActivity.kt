package com.example.android.bgtustatistic.UILayer.UIelements

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding_: ActivityMainBinding? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityMainBinding.inflate(layoutInflater)
        binding = binding_!!
        setContentView(binding.root)

        savedInstanceState?:let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PerformanceFragment())
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.movement -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MovementFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.performance -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PerformanceFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }
}