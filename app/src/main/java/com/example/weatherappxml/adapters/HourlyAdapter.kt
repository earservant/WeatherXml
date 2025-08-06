package com.example.weatherappxml.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappxml.R
import com.example.weatherappxml.adapters.DaysAdapter.Listener
import com.example.weatherappxml.databinding.ListItemBinding
import com.example.weatherappxml.fragments.formatImageUrl
import com.example.weatherappxml.fragments.toDisplayTime
import com.squareup.picasso.Picasso

class HourlyAdapter(val listener: Listener?) :
    ListAdapter<HourlyWeather, HourlyAdapter.Holder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: HourlyWeather) = with(binding) {
            cardDate.text = item.time.toDisplayTime()
            cardCondition.text = item.condition.text
            cardTemp.text = itemView.context.getString(R.string.c, item.tempC)

            Picasso.get()
                .load(item.condition.icon.formatImageUrl())
                .into(im)
        }
    }

    class Comparator : DiffUtil.ItemCallback<HourlyWeather>() {
        override fun areItemsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
            return oldItem == newItem
        }
    }
}
