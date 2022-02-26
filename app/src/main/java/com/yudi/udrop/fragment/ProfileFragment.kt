package com.yudi.udrop.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yudi.udrop.LaunchActivity
import com.yudi.udrop.R
import com.yudi.udrop.adapter.ProfileFeatureAdapter
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.FragmentProfileBinding
import com.yudi.udrop.interfaces.InputInterface
import com.yudi.udrop.model.data.ProfileModel

class ProfileFragment : Fragment(), InputInterface {
    lateinit var binding: FragmentProfileBinding
    lateinit var SQLiteManager: SQLiteManager

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SQLiteManager = SQLiteManager(binding.root.context, "udrop.db", null, 1)
        setupBinding()
        setupRecyclerView()
        refreshPage()
    }

    private fun refreshPage() {
        SQLiteManager.getInfo()?.let {
            binding.model =
                ProfileModel(it.id, it.name, if (it.motto == "") "点击修改个性签名" else it.motto, it.days)
        }
    }

    private fun setupBinding() {
        binding.profileSetting.setOnClickListener {
            binding.model?.let {
                it.showSettingList.set(!it.showSettingList.get())
            }
        }
        binding.profileSettingList.settingSignOut.let {
            it.setOnClickListener {
                showConfirmDialog {
                    SQLiteManager.deleteUser()
                    activity?.let { it ->
                        startActivity(Intent(context, LaunchActivity::class.java))
                        it.finish()
                    }
                }
            }
        }
        binding.profileSignature.setOnClickListener {
            binding.model?.doEdit?.set(true)
        }
        binding.profileSignatureEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.model?.editMotto?.set(s.toString())
            }
        })
        binding.profileSignatureEditIcon.setOnClickListener { v ->
            binding.model?.let { it ->
                if (it.motto.get() != it.editMotto.get().toString()) {
                    ServiceManager().changeUserInfo(
                        it.id,
                        it.Name,
                        it.editMotto.get().toString()
                    ) { result ->
                        if (result) {
                            SQLiteManager.updateInfo(
                                it.Name,
                                it.editMotto.get().toString(),
                                it.DaysNum
                            )
                            refreshPage()
                        } else
                            Snackbar.make(binding.root, "更新失败", Snackbar.LENGTH_SHORT).show()
                    }
                }
                it.doEdit.set(false)
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerviewLayoutManager = LinearLayoutManager(context)
        recyclerviewLayoutManager.orientation = RecyclerView.VERTICAL
        with(binding.profileFeatures) {
            adapter = ProfileFeatureAdapter(this@ProfileFragment.activity)
            layoutManager = recyclerviewLayoutManager
        }
    }

    private fun showConfirmDialog(completion: () -> Unit) {
        val builder = AlertDialog.Builder(binding.root.context)
        builder.apply {
            setPositiveButton(R.string.button_confirm) { _, _ ->
                completion()
            }
            setNegativeButton(R.string.cancel) { _, _ -> }
        }
        builder.setMessage(R.string.sign_out_message)
            .setTitle(R.string.sign_out)
        builder.show()
    }
}