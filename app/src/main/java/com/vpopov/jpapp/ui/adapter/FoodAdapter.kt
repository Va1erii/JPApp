package com.vpopov.jpapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.FoodItemViewBinding
import com.vpopov.jpapp.model.Food

class FoodAdapter(
    private val onItemClicked: (Food, sharedView: View) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodVH>() {
    private val data: ArrayList<Food> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.food_item_view, parent, false)
        return FoodVH(view)
    }

    override fun onBindViewHolder(holder: FoodVH, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { onItemClicked(data[position], holder.sharedView) }
    }

    override fun getItemCount(): Int = data.size

    fun update(newData: List<Food>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class FoodVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FoodItemViewBinding.bind(view)
        val sharedView: View = binding.image

        fun bind(food: Food) {
            sharedView.transitionName = food.name
            Glide.with(itemView)
                .load(food.image)
                .into(binding.image)
            binding.title.text = food.name
        }
    }
}