package com.vpopov.jpapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.MainFragmentBinding
import com.vpopov.jpapp.extension.addTo
import com.vpopov.jpapp.ui.adapter.CityAdapter
import com.vpopov.jpapp.ui.adapter.decoration.VerticalMarginDecoration
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {
    private val compositeDisposable = CompositeDisposable()
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)
        val adapter = CityAdapter()
        binding.cities.adapter = adapter
        binding.cities.addItemDecoration(VerticalMarginDecoration((8 * resources.displayMetrics.density).toInt()))
        viewModel.cities.subscribe {
            adapter.update(it)
        }.addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}