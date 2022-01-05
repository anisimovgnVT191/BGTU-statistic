package com.example.android.bgtustatistic.UILayer.UIelements

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private var binding_: ActivityMainBinding? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityMainBinding.inflate(layoutInflater)
        binding = binding_!!
        setContentView(binding.root)

        savedInstanceState?:let {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, PerformanceFragment())
//                .commit()
            binding.bottomNavigation.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
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

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
    // make EditText loose focus on tap outside (applies to Activity and Fragments)
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus ?: let {
                return@dispatchTouchEvent super.dispatchTouchEvent(ev)
            }
            if (v is TextInputEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}