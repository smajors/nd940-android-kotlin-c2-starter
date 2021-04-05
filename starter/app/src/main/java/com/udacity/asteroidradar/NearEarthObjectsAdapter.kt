package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.NearEarthObjectItemBinding
import com.udacity.asteroidradar.domain.NearEarthObject
import timber.log.Timber

/**
 * RecyclerView Adapter that is used for Near Earth Object population
 */
class NearEarthObjectsAdapter(val listener: AsteroidListener) : ListAdapter<NearEarthObject, NearEarthObjectsAdapter.NearEarthObjectViewHolder>(NearEarthObjectDiffCallback()) {


    class NearEarthObjectDiffCallback : DiffUtil.ItemCallback<NearEarthObject>() {
        override fun areItemsTheSame(oldItem: NearEarthObject, newItem: NearEarthObject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NearEarthObject, newItem: NearEarthObject): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearEarthObjectViewHolder {
        return NearEarthObjectViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: NearEarthObjectViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid!!, listener)
    }

    class NearEarthObjectViewHolder(val viewDataBinding: NearEarthObjectItemBinding) :
            RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(asteroid: NearEarthObject, listener: AsteroidListener) {
            viewDataBinding.asteroid = asteroid
            viewDataBinding.listener = listener
            Timber.d("Item: ${asteroid.codeName} / ${asteroid.date} / ${asteroid.isPotentiallyHazardousAsteroid}")
            viewDataBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : NearEarthObjectViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NearEarthObjectItemBinding.inflate(layoutInflater, parent, false)
                return NearEarthObjectViewHolder(binding)
            }
        }
    }

    /**
     * Handles all click events related to presses on Items handled by this adapter
     */
    class AsteroidListener(val listener: (asteroid: NearEarthObject) -> Unit) {
        fun onClick(asteroid: NearEarthObject) = listener(asteroid)
    }

}

