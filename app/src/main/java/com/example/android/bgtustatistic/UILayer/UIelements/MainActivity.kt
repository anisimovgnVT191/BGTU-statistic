package com.example.android.bgtustatistic.UILayer.UIelements

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.UIelements.LoadingFeature.LoadingFragment
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen.ContingentFragment
import com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen.PerformanceFragment
import com.example.android.bgtustatistic.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private var binding_: ActivityMainBinding? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityMainBinding.inflate(layoutInflater)
        binding = binding_!!
        setContentView(binding.root)

        setLoadingFragment()
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
    private fun setLoadingFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoadingFragment())
            .commit()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStackImmediate()
        }
        when(item.itemId){
            R.id.movement -> {
                binding.topBar.apply {
                    title = resources.getString(R.string.movement_fragment_title)
                    menu.findItem(R.id.settings).isVisible = true

                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ContingentFragment())
                    .commit()
                return true
            }
            R.id.performance -> {
                binding.topBar.apply {
                    title = resources.getString(R.string.performance_fragment_title)
                    menu.findItem(R.id.settings).isVisible = false
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PerformanceFragment())
                    .commit()
                return true
            }
            else -> return false
        }
    }
}