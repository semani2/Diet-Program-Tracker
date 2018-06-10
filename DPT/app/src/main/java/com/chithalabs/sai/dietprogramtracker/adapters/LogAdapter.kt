package com.chithalabs.sai.dietprogramtracker.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chithalabs.sai.dietprogramtracker.*
import com.chithalabs.sai.dietprogramtracker.data.room.ILog
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog
import io.reactivex.subjects.PublishSubject

class LogAdapter(private var logList: MutableList<ILog>): RecyclerView.Adapter<LogAdapter.LogViewHolder>(){
    val mClickSubject: PublishSubject<ILog> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHolder(v)
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logList[position]

        holder.cardView.setOnClickListener({ mClickSubject.onNext(log) })

        if (log is Log) {
            when (log.type) {
                FOOD -> {
                    holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.meal))
                    holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_desc_food, log.desc)
                    holder.logTimeStampTextView.text = log.time
                }
                LIQUID -> {
                    holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.soup))
                    holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_desc_liquid, log.desc)
                    holder.logTimeStampTextView.text = log.time
                }
                FAT -> {
                    holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.olive_oil))
                    holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_desc_had, log.quantity, log.desc)
                    holder.logTimeStampTextView.text = log.time
                }
                WATER -> {
                    holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.water))
                    holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_drank_water, log.quantity)
                    holder.logTimeStampTextView.text = log.time
                }
                LIME -> {
                    holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.lime))
                    holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_had_lime, log.quantity)
                    holder.logTimeStampTextView.text = log.time
                }
                MULTIVITAMINS -> {
                    holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.proteins))
                    holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_had_multivitamin, log.quantity)
                    holder.logTimeStampTextView.text = log.time
                }
            }
        }
        else if (log is WeightLog){
            holder.logTypeImageView.setImageDrawable(holder.logTypeImageView.context.getDrawable(R.drawable.scale))
            holder.logDescTextView.text = holder.logDescTextView.context.getString(R.string.log_desc_weight, log.weightInKgs, "kgs")
            holder.logTimeStampTextView.text = log.time
        }
    }

    fun onItemClickEvent(): PublishSubject<ILog> {
        return mClickSubject
    }


    class LogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var logTypeImageView: ImageView
        var logDescTextView: TextView
        var logTimeStampTextView: TextView
        var cardView: CardView

        init {
            logTypeImageView = itemView.findViewById(R.id.log_type_image_view)
            logDescTextView = itemView.findViewById(R.id.log_desc_text_view)
            logTimeStampTextView = itemView.findViewById(R.id.log_time_stamp_text_view)
            cardView = itemView.findViewById(R.id.card_view)
        }
    }
}
