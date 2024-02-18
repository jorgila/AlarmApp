package com.estholon.alarmapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.estholon.alarmapp.databinding.RvOnlyTextBinding

class StringsListAdapter: ListAdapter<String, StringsListAdapter.StringViewHolder>(DiffCallback) {

    // Listener Variable

    var clickListener : ClickListener? = null

    // Show data, Know what to do with the data

    class StringViewHolder(private var binding: RvOnlyTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String, clickListener: ClickListener?) {

            binding.tvRecyclerDescription.text = string

            // Listener

            binding.root.setOnClickListener {

                clickListener?.onClickListener(string)

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val viewHolder = StringViewHolder(

            // Inicializar el binding

            RvOnlyTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
        }

        return viewHolder
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            // Listener
            clickListener
        )
    }

    // Listener

    interface ClickListener {
        fun onClickListener (string: String)
    }

    fun setOnClickListener(clickListener : ClickListener){
        this.clickListener = clickListener
    }


}