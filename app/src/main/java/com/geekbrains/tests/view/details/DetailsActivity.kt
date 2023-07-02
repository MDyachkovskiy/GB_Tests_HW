package com.geekbrains.tests.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.tests.R
import com.geekbrains.tests.databinding.ActivityDetailsBinding
import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.presenter.details.PresenterDetailsContract
import java.util.Locale

class DetailsActivity : AppCompatActivity(), ViewDetailsContract {

    private lateinit var binding: ActivityDetailsBinding
    val _binding get() = binding
    private val presenter: PresenterDetailsContract = DetailsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    private fun setUI() {
        val count = intent.getIntExtra(TOTAL_COUNT_EXTRA, 0)
        presenter.setCounter(count)
        setCountText(count)
        binding.decrementButton.setOnClickListener {
            presenter.onDecrement()
        }
        binding.incrementButton.setOnClickListener {
            presenter.onIncrement()
        }
    }

    companion object {
        const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

        fun getIntent(context: Context, totalCount: Int): Intent {
            return Intent(context, DetailsActivity::class.java).apply{
                putExtra(TOTAL_COUNT_EXTRA, totalCount)
            }
        }
    }

    override fun setCount(count: Int) {
        setCountText(count)
    }

    private fun setCountText(count: Int) {
        binding.totalCountTextView.text = String
            .format(Locale.getDefault(),getString(R.string.results_count), count)
    }
}