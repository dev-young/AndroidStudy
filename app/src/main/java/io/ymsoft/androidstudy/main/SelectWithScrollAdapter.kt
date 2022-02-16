package io.ymsoft.androidstudy.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.databinding.ItemCameraTypeBinding

open class SelectWithScrollAdapter(val recyclerView: RecyclerView, val listener: (Int) -> Unit) :
    RecyclerView.Adapter<SelectWithScrollAdapter.CameraTypeViewHolder>() {

    val itemList = arrayListOf<String>()


    private var textColor: Int = R.color.white

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraTypeViewHolder {
        return CameraTypeViewHolder(
            ItemCameraTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: CameraTypeViewHolder, position: Int) {
        holder.binding.name.text = itemList[position]
        holder.binding.root.setOnClickListener {
            holder.adapterPosition.let {
                listener.invoke(it)
            }

        }
        holder.binding.name.setTextColor(recyclerView.context.getColor(textColor))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CameraTypeViewHolder(val binding: ItemCameraTypeBinding) :
        RecyclerView.ViewHolder(binding.root)
}