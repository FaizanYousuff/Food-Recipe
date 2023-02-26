package com.mfy.recipes.adpaters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter (
    private val resultBundle : Bundle,
    private val fragments : ArrayList<Fragment>,
    private val titles : ArrayList<String>,
    fragmentManager: FragmentManager

        ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,) {
    override fun getCount(): Int {
        return fragments.size

    }

    override fun getItem(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}