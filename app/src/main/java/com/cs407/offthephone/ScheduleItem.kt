package com.cs407.offthephone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ScheduleItem.kt - Data model for a schedule item
data class ScheduleItem(val eventName: String, val eventTime: String)

class ScheduleAdapter(private val scheduleList: List<ScheduleItem>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.eventName)
        val eventTime: TextView = view.findViewById(R.id.eventTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item = scheduleList[position]
        holder.eventName.text = item.eventName
        holder.eventTime.text = item.eventTime
    }

    override fun getItemCount(): Int = scheduleList.size
}