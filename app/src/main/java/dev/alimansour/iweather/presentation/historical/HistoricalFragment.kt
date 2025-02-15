package dev.alimansour.iweather.presentation.historical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.alimansour.iweather.databinding.FragmentHistoricalBinding
import dev.alimansour.iweather.presentation.MainActivity
import dev.alimansour.iweather.util.Resource
import timber.log.Timber
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@AndroidEntryPoint
class HistoricalFragment : Fragment() {

    private var _binding: FragmentHistoricalBinding? = null

    @Inject
    lateinit var historicalAdapter: HistoricalAdapter

    @Inject
    lateinit var viewModel: HistoricalViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = HistoricalFragmentArgs.fromBundle(requireArguments())
        val city = args.city

        (requireActivity() as MainActivity).toolbarTitle = "${city.name}, Historical"

        _binding = FragmentHistoricalBinding.inflate(inflater, container, false)
        historicalAdapter.differ.submitList(listOf())

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = historicalAdapter

            viewModel.historicalData.observe(viewLifecycleOwner, { resource ->
                if (resource is Resource.Success) {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            historicalAdapter.differ.submitList(list)
                            adapter = historicalAdapter
                        }
                    }
                }
            })
        }

        viewModel.historicalDataUpdated.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Loading ->
                    binding.swipeRefresh.post { binding.swipeRefresh.isRefreshing = true }

                is Resource.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    viewModel.getHistoricalDataList(city.id)
                }
                is Resource.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    Timber.e(resource.message.toString())
                    Snackbar.make(binding.root, resource.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            }

        })
        binding.swipeRefresh.setOnRefreshListener {
            Timber.v("Refreshing historical data of saved cities")
            viewModel.updateHistoricalData()
        }

        viewModel.getHistoricalDataList(city.id)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}