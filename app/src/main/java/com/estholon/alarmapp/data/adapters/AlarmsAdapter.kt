package com.estholon.alarmapp.data.adapters

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.estholon.alarmapp.databinding.RvAlarmsBinding
import com.estholon.alarmapp.databinding.RvOnlyTextBinding
import com.estholon.alarmapp.domain.model.Alarm

class AlarmsAdapter: ListAdapter<Alarm, AlarmsAdapter.AlarmsViewHolder>(DiffCallback) {

    // Listener Variable

    var clickListener : ClickListener? = null

    //

    class AlarmsViewHolder(private var binding: RvAlarmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm, clickListener: ClickListener?) {

            // SET VALUES

            binding.tvTitle.text = alarm.title
            binding.tvDetailsDate.text = alarm.start_date
            binding.tvDetailsHour.text = alarm.start_hour
            binding.tvHour.text = alarm.start_hour
            "${alarm.repetition_type} ${alarm.repetition_time}".also { binding.tvDetailsRepetitions.text = it }
            binding.tvDetailsMessage.text = alarm.message
            if(alarm.status){
                binding.swStatus.isChecked = true
            } else {
                binding.swStatus.isChecked = false
            }


            // DETAILS

            binding.llDetails.visibility = View.GONE
            var detailsVisibility = false

            binding.ivDetail.setOnClickListener {

                if(detailsVisibility){
                    binding.llDetails.visibility = View.GONE
                    detailsVisibility = false
                    binding.ivDetail.rotation = 0F
                } else {
                    binding.llDetails.visibility = View.VISIBLE
                    detailsVisibility = true
                    binding.ivDetail.rotation = 180F
                }

            }

            // DELETE ALARM

            binding.ivDelete.setOnClickListener {
                clickListener?.onClickListener("delete", alarm.id, !alarm.status)
            }
            // CHANGE STATUS
            binding.swStatus.setOnClickListener{
                clickListener?.onClickListener("status", alarm.id, !alarm.status)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmsViewHolder {
        val viewHolder = AlarmsViewHolder(

            // Inicializar el binding

            RvAlarmsBinding.inflate(
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
        private val DiffCallback = object : DiffUtil.ItemCallback<Alarm>(){
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onBindViewHolder(holder: AlarmsViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            // Listener
            clickListener
        )
    }

    // Listener

    interface ClickListener {
        fun onClickListener (string: String, position: Int, status: Boolean)
    }

    fun setOnClickListener(clickListener : ClickListener){
        this.clickListener = clickListener
    }


}