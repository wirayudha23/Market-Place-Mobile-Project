package com.example.ichoosecoffetoday.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ichoosecoffetoday.Domain.Product
import com.example.ichoosecoffetoday.R

class UserProductAdapter(
    private val context: Context,
    private var dataList: List<Product>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<UserProductAdapter.HolderJual>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderJual {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_jual, parent, false)
        return HolderJual(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderJual, position: Int) {
        Glide.with(context).load(dataList[position].imageUrl).into(holder.jualGambar)
        holder.jualNama.text = dataList[position].nama
        holder.jualHarga.text = dataList[position].harga.toString()
        holder.jualStock.text = dataList[position].jumlah.toString()

        holder.itemView.setOnClickListener {
            onItemClick.invoke(position)
        }
    }

    fun setData(newList: List<Product>) {
        dataList = newList
        notifyDataSetChanged()
    }

    class HolderJual(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var jualGambar: ImageView = itemView.findViewById(R.id.gambarJual)
        var jualNama: TextView = itemView.findViewById(R.id.namaJual)
        var jualHarga: TextView = itemView.findViewById(R.id.hargaJual)
        var jualStock: TextView = itemView.findViewById(R.id.stockJual)
    }
}
