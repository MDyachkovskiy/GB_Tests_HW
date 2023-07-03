package com.geekbrains.tests.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.geekbrains.tests.R
import com.geekbrains.tests.databinding.FragmentDetailsBinding
import com.geekbrains.tests.presenter.details.PresenterDetailsContract
import org.koin.android.ext.android.inject
import java.util.Locale

class DetailsFragment : Fragment(), ViewDetailsContract {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val presenter: PresenterDetailsContract by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        setUI()
    }

    override fun setCount(count: Int) {
        setCountText(count)
    }

    private fun setCountText(count: Int) {
        binding.totalCountTextView.text =
            String.format(Locale.getDefault(), getString(R.string.results_count), count)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.onDetach()
    }

    private fun setUI() {
        arguments?.let {
            val counter = it.getInt(TOTAL_COUNT_EXTRA, 0)
            presenter.setCounter(counter)
            setCountText(counter)
        }
        binding.decrementButton.setOnClickListener {
            presenter.onDecrement()
        }
        binding.incrementButton.setOnClickListener {
            presenter.onIncrement()
        }
    }

    companion object {
        const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

        fun newInstance(totalCount: Int) = DetailsFragment().apply {
            arguments = bundleOf(TOTAL_COUNT_EXTRA to totalCount)
        }
    }
}