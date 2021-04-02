package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.NearEarthObjectItemBinding
import com.udacity.asteroidradar.domain.NearEarthObject
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

        binding.asteroidRecycler.adapter = NearEarthObjectsAdapter()

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    /**
     * RecyclerView Adapter that is used for Near Earth Object population
     */
    class NearEarthObjectsAdapter() : RecyclerView.Adapter<NearEarthObjectViewHolder>() {

        var nearEarthObjects: List<NearEarthObject> = emptyList()
        set(value) {
            // On initial population, invalidate the entire list
            field = value
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearEarthObjectViewHolder {
            val dataBinding: NearEarthObjectItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.near_earth_object_item,
                parent,
                false)

            return NearEarthObjectViewHolder(dataBinding)
        }

        override fun getItemCount(): Int {
           return nearEarthObjects.size
        }

        override fun onBindViewHolder(holder: NearEarthObjectViewHolder, position: Int) {
            holder.viewDataBinding.also {
                it.asteroid = nearEarthObjects[position]
            }
        }

    }

    class NearEarthObjectViewHolder(val viewDataBinding: NearEarthObjectItemBinding) :
            RecyclerView.ViewHolder(viewDataBinding.root)
}
