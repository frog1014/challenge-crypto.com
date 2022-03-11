package com.mywork.apps.cryptodotcom.challenge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mywork.apps.cryptodotcom.challenge.databinding.ActivityMainBinding
import com.mywork.apps.cryptodotcom.challenge.utilities.MessageRes.UNKNOWN_ERROR_RES
import com.mywork.apps.cryptodotcom.challenge.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {

    private var backPressedTimeStamp: Long = 0L
    private val viewModel: MainViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val currencyListFragment = CurrencyListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, currencyListFragment).commit()
        }

        initLiveData()
        initView()
    }

    private fun initLiveData() {
        viewModel.listView.observe(this) { result ->
            if (result is MainViewModel.ListResult.Failed) {
                val msg = result.msg ?: getString(UNKNOWN_ERROR_RES)
                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.selectedCurrency.observe(this) { currencyInfo ->
            // reserved
        }
    }

    private fun initView() {
        with(binding) {
            loadDataBtn.setOnClickListener {
                loadData()
            }
            sortBtn.setOnClickListener {
                sortData()
            }
        }
    }

    private fun loadData() {
        viewModel.loadData()
    }

    private fun sortData() {
        viewModel.sortData()
    }

    override fun onBackPressed() {
        debounceOnBackPressed()
    }

    private fun debounceOnBackPressed() {
        if ((backPressedTimeStamp + 2000) > System.currentTimeMillis()) {
            finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0)
        } else {
            Toast.makeText(this, R.string.leave_app_hint, Toast.LENGTH_SHORT).show()
        }
        backPressedTimeStamp = System.currentTimeMillis()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}