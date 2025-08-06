package com.example.weatherappxml.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappxml.R
import com.example.weatherappxml.databinding.ListItemBinding
import com.example.weatherappxml.fragments.formatImageUrl
import com.example.weatherappxml.fragments.toDisplayDate
import com.squareup.picasso.Picasso

class DaysAdapter(val listener: Listener?) :
    ListAdapter<ForecastDay, DaysAdapter.Holder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(
            view,
            listener
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listener {
        fun onClick(item: ForecastDay)
    }

    class Holder(view: View, val listener: Listener?) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)
        var itemTemp: ForecastDay? = null

        init {
            itemView.setOnClickListener {
                itemTemp?.let { item ->
                    // Анимация при нажатии
                    itemView.animate().apply {
                        duration = 150
                        scaleX(0.95f)
                        scaleY(0.95f)
                    }.withEndAction {
                        itemView.animate().apply {
                            duration = 150
                            scaleX(1f)
                            scaleY(1f)
                        }.start()
                    }.start()

                    // Вызов метода интерфейса
                    listener?.onClick(item)

                    // Показ тоста
                    val context = itemView.context
                    Toast.makeText(
                        context,
                        context.getString(R.string.update_for, item.date),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: ForecastDay) = with(binding) {
            itemTemp = item
            cardDate.text = item.date.toDisplayDate()
            cardCondition.text = item.day.condition.text
            cardTemp.text = itemView.context.getString(
                R.string.temperature_range,
                item.day.maxTempC.toInt(),
                item.day.minTempC.toInt()
            )

            Picasso.get()
                .load(item.day.condition.icon.formatImageUrl()) // Используется форматирование при помощи функции в Extensions
                .into(im)
        }
    }

    class Comparator : DiffUtil.ItemCallback<ForecastDay>() {
        override fun areItemsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
            return oldItem == newItem
        }
    }
}

