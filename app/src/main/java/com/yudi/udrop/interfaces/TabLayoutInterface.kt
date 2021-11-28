package com.yudi.udrop.interfaces

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.yudi.udrop.R
import com.yudi.udrop.fragment.HomeFragment

interface TabLayoutInterface {
    fun setupTabLayout(context: Context, tabLayout: TabLayout, textId: Int) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textView =
                    tab?.customView?.findViewById<TextView>(textId)
                textView?.setTextColor(context.getColor(R.color.udrop_text_2))
                textView?.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                textView?.invalidate()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val textView =
                    tab?.customView?.findViewById<TextView>(textId)
                textView?.setTextColor(context.getColor(R.color.udrop_text_1))
                textView?.typeface = Typeface.DEFAULT
                textView?.invalidate()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        tabLayout.post {
            tabLayout.tabMode = TabLayout.MODE_FIXED
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            val textView =
                tabLayout.getTabAt(0)?.customView?.findViewById<TextView>(textId)
            textView?.setTextColor(context.getColor(R.color.udrop_text_2))
            textView?.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            textView?.invalidate()
        }
    }
}