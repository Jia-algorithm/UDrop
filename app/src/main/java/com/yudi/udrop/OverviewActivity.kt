package com.yudi.udrop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.fragment.HomeFragment
import com.yudi.udrop.fragment.ProfileFragment
import com.yudi.udrop.fragment.UdropFragment


class OverviewActivity : WakeupActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val SQLiteManager = com.yudi.udrop.data.SQLiteManager(this, "udrop.db", null, 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initializeTabBar()
        SQLiteManager.getInfo()?.let {
            ServiceManager().getUserInfo(it.id) { user ->
                SQLiteManager.updateInfo(user.name, user.motto, user.days)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.tabBar_item0 -> fragment = HomeFragment()
            R.id.tabBar_item1 -> fragment = UdropFragment()
            R.id.tabBar_item2 -> fragment = ProfileFragment()
        }
        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            return true
        }
        return false
    }

    @SuppressLint("ResourceType")
    private fun initializeTabBar() {
        val tabBar = findViewById<BottomNavigationView>(R.id.tabBar)
        tabBar.setOnNavigationItemSelectedListener(this)
        tabBar.selectedItemId = R.id.tabBar_item0
        tabBar.itemIconTintList = null

    }

    companion object {
        const val TAG = "OverviewActivity"
    }
}