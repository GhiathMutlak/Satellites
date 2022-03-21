package com.satellites.app.satelliteModule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.satellites.app.R
import com.satellites.app.entities.Satellite

class SatellitesAdapter :
    ListAdapter<Satellite, SatellitesAdapter.SatelliteViewHolder>(SatelliteComparator()) {
    var mSatelliteList: List<Satellite>? = null
    var satelliteListFiltered: List<Satellite>? = null
    var interaction: SatelliteCardInteraction? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        return SatelliteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        val current = satelliteListFiltered?.get(position)
        holder.bind(current, currentList.size, interaction)
    }

    fun setList(satelliteList: List<Satellite>) {
      this.mSatelliteList = satelliteList
      this.satelliteListFiltered = satelliteList
    }

    override fun getItemCount(): Int {
        return satelliteListFiltered?.size!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class SatelliteViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        private var ctx = context
        private val satelliteNameTV: TextView = itemView.findViewById(R.id.satelliteNameTV)
        private val satelliteStateTV: TextView = itemView.findViewById(R.id.satelliteStateTV)
        private val satelliteStateIV: ImageView = itemView.findViewById(R.id.satelliteStateIV)
        private val lineV: View = itemView.findViewById(R.id.lineV)
        fun bind(satellite: Satellite?, size: Int, interaction: SatelliteCardInteraction?) {
            satelliteNameTV.text = satellite?.name
            if (satellite?.active!!) {
                satelliteStateTV.text = ctx.getString(R.string.active)
                satelliteStateIV.setBackgroundResource(R.drawable.bg_round_green)
            } else {
                satelliteStateTV.text = ctx.getString(R.string.passive)
                satelliteStateIV.setBackgroundResource(R.drawable.bg_round_red)
            }


            if (adapterPosition==size-1){
                lineV.visibility = View.GONE
            } else {
                lineV.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {

                interaction?.onSatelliteClicked(satellite)

            }


        }


        companion object {
            fun create(parent: ViewGroup): SatelliteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.each_satellite_row, parent, false)
                return SatelliteViewHolder(view, parent.context)
            }
        }
    }

    class SatelliteComparator : DiffUtil.ItemCallback<Satellite>() {
        override fun areItemsTheSame(oldItem: Satellite, newItem: Satellite): Boolean {
            return oldItem === newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Satellite, newItem: Satellite): Boolean {
            return oldItem == newItem
        }
    }


    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    satelliteListFiltered = mSatelliteList
                } else {
                    val filteredList: MutableList<Satellite> =
                        ArrayList<Satellite>()
                    for (row in mSatelliteList!!) {
                        if (row?.name?.toLowerCase()?.contains(charString.toLowerCase()) == true
                        ) {
                            filteredList.add(row)
                        }
                    }
                    satelliteListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = satelliteListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                satelliteListFiltered = filterResults.values as List<Satellite>?
                notifyDataSetChanged()
            }
        }
    }

    interface SatelliteCardInteraction {
        fun onSatelliteClicked(satelliteId: Satellite)
    }
}