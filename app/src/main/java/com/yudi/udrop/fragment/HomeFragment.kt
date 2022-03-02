package com.yudi.udrop.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.yudi.udrop.*
import com.yudi.udrop.adapter.HomeScheduleAdapter
import com.yudi.udrop.adapter.HomeTextAdapter
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.FragmentHomeBinding
import com.yudi.udrop.interfaces.OverviewInterface
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.interfaces.TabLayoutInterface
import com.yudi.udrop.model.data.ScheduleModel
import com.yudi.udrop.model.local.FunctionType
import com.yudi.udrop.model.local.TextDetail
import org.json.JSONArray

class HomeFragment : Fragment(), OverviewInterface, TabLayoutInterface, ProgressInterface {
    lateinit var binding: FragmentHomeBinding
    lateinit var localManager: SQLiteManager
    private val adapter = HomeScheduleAdapter(this, localManager)
    private val textAdapter by lazy {
        HomeTextAdapter(this)
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localManager = SQLiteManager(binding.root.context, "udrop.db", null, 1)
        setupRecyclerView()
        binding.homeScheduleViewpager.adapter = adapter
        setupTabLayout(view)
        binding.homeCollectionItem.setOnClickListener {
            activity?.let {
                startActivity(Intent(context, CollectionActivity::class.java))
            }
        }
        binding.homeGameItem.setOnClickListener {
            activity?.let {
                startActivity(Intent(context, UdropActivity::class.java).apply {
                    putExtra(UdropActivity.INTENT_EXTRA_TITLE, "游戏中心")
                    putExtra(UdropActivity.INTENT_EXTRA_TYPE, FunctionType.GAME.typeId)
                })
            }
        }
        binding.homeEnterSearch.setOnClickListener {
            activity?.let {
                startActivity(Intent(context, SearchActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData { new, review ->
            Handler(Looper.getMainLooper()).post {
                adapter.newSchedule =
                    ScheduleModel(localManager.countCompletedNewSchedule(), new.length())
                adapter.reviewSchedule =
                    ScheduleModel(localManager.countCompletedReviewSchedule(), review.length())
            }
        }
    }

    override fun clickTextItem(title: String) {
        activity?.let {
            startActivity(Intent(context, TextDetailActivity::class.java).apply {
                putExtra(TextDetailActivity.INTENT_EXTRA_TEXT_TITLE, title)
            })
        }
    }

    override fun checkProgress(buttonText: String) {
        activity?.let {
            startActivity(Intent(context, ProgressActivity::class.java).apply {
                putExtra(
                    ProgressActivity.INTENT_EXTRA_TITLE,
                    if (buttonText == getString(R.string.start_to_learn)) R.string.new_progress else R.string.review_progress
                )
            })
        }
    }

    override fun clickScheduleButton(buttonText: String) {
        activity?.let {
            startActivity(Intent(context, UdropActivity::class.java).apply {
                putExtra(
                    UdropActivity.INTENT_EXTRA_TITLE,
                    if (buttonText == getString(R.string.start_to_learn)) "学习计划" else "复习计划"
                )
                putExtra(
                    UdropActivity.INTENT_EXTRA_TYPE,
                    if (buttonText == getString(R.string.start_to_learn)) FunctionType.LEARN_NEW.typeId else FunctionType.REVIEW.typeId
                )
            })
        }
    }

    private fun getData(completion: (JSONArray, JSONArray) -> Unit) {
        localManager.getInfo()?.let {
            ServiceManager().getSchedule(it.id) { new, review ->
                localManager.updateNewSchedule(new)
                localManager.updateReviewSchedule(review)
                completion(new, review)
            }
        }
    }

    private fun getRecommendation(completion: (ArrayList<TextDetail>) -> Unit) {
        ServiceManager().getRecommendation(4) {
            val list = arrayListOf<TextDetail>()
            for (i in 0 until it.length()) {
                with(it.getJSONObject(i)) {
                    list.add(
                        TextDetail(
                            getString("title"),
                            getString("author"),
                            getString("content"),
                            ""
                        )
                    )
                }
            }
            completion(list)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.homeTextLearned
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = textAdapter
        getRecommendation { result ->
            Handler(Looper.getMainLooper()).post {
                textAdapter.recommendList = result
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setupTabLayout(view: View) {
        val tabLayout = binding.homeScheduleTab
        val viewPager = binding.homeScheduleViewpager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView =
                LayoutInflater.from(context)
                    .inflate(R.layout.home_schedule_tab_item, null, false)
            when (position) {
                0 -> tabView.findViewById<TextView>(R.id.home_schedule_tab_item_title).text =
                    getString(R.string.new_to_learn)
                1 -> tabView.findViewById<TextView>(R.id.home_schedule_tab_item_title).text =
                    getString(R.string.to_review)
            }
            tab.customView = tabView
        }.attach()
        super.setupTabLayout(view.context, tabLayout, R.id.home_schedule_tab_item_title)
    }
}