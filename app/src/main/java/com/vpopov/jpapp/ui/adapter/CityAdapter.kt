package com.vpopov.jpapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.CityItemViewBinding
import com.vpopov.jpapp.model.City

class CityAdapter(
    private val onItemClicked: (item: City, sharedView: View) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityVH>() {
    private val data: ArrayList<City> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.city_item_view, parent, false)
        return CityVH(view)
    }

    override fun onBindViewHolder(holder: CityVH, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onItemClicked(data[position], holder.sharedView)
        }
    }

    override fun getItemCount(): Int = data.size

    fun update(newData: List<City>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class CityVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CityItemViewBinding.bind(view)
        val sharedView: View = binding.image

        fun bind(city: City) {
            sharedView.transitionName = city.image
            Glide.with(itemView)
                .load(city.image)
                .into(binding.image)
            binding.title.text = city.name
            binding.description.text = city.description
        }
    }
}