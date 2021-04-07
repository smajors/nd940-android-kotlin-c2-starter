package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.NearEarthObjectsAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.NearEarthObjectItemBinding
import com.udacity.asteroidradar.domain.NearEarthObject
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        Timber.d("Setting up fragment in onCreateView()")
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        /**
         * Handle item clicks
         */
        val adapter = NearEarthObjectsAdapter(NearEarthObjectsAdapter.AsteroidListener { asteroid ->
            Timber.d("onClick for asteroid ${asteroid.codeName}")
            viewModel.onObjectClick(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            /**
             * Used to scroll the list to the top on data change
             */
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                asteroid_recycler.smoothScrollToPosition(0)

            }
        })

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("Adapter has new items. Submitting new list.")
                adapter.submitList(it)
            }
        })

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                Timber.d("Navigation for asteroid ${it.codeName}")
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onObjectClickNavigation()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item clicks
        when (item.itemId) {
            R.id.show_all_menu -> viewModel.setFilter(MainViewModel.ObjectFilter.WEEKLY)
            R.id.show_rent_menu -> viewModel.setFilter(MainViewModel.ObjectFilter.TODAY)
            R.id.show_buy_menu -> viewModel.setFilter(MainViewModel.ObjectFilter.ALL)
        }

        return true
    }

}
