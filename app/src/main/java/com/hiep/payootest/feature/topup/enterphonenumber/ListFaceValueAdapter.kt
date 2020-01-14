package com.hiep.payootest.feature.topup.enterphonenumber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.hiep.payootest.R
import com.hiep.payootest.model.FaceValue

class ListFaceValueAdapter(private val itemClickListener: (FaceValue, Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val faceValueList = mutableListOf<FaceValue>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return AmountHolder.create(
            inflater,
            parent
        )
    }

    override fun getItemCount() = faceValueList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AmountHolder -> holder.bind(faceValueList[position], itemClickListener, position)
        }
    }

    fun updateFaceValueState(position: Int, isSelected: Boolean) {
        if (faceValueList[position].isSelected == isSelected)
            return
        faceValueList[position].isSelected = isSelected
        notifyItemChanged(position)

        faceValueList.forEachIndexed { index, faceValue ->
            if (faceValue.isSelected && index!=position) {
                faceValueList[index].isSelected = false
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

    fun setList(newFaceValueList: List<FaceValue>) {
        faceValueList.clear()
        faceValueList.addAll(newFaceValueList)
        notifyDataSetChanged()
    }

    private class AmountHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val btnFaceValue: Button = itemView.findViewById(R.id.btn_face_value)

        fun bind(
            faceValue: FaceValue,
            itemClickListener: (FaceValue, Int) -> Unit,
            position: Int
        ) {
            faceValue.value?.let {
                btnFaceValue.text = faceValue.formattedValue ?: it.toString()
                btnFaceValue.isEnabled = !faceValue.isSelected
                btnFaceValue.setOnClickListener {
                    btnFaceValue.isEnabled = false
                    itemClickListener(faceValue, position)
                }
            }
        }

        companion object {
            fun create(inflater: LayoutInflater, parent: ViewGroup?) =
                AmountHolder(
                    inflater.inflate(
                        R.layout.item_face_value,
                        parent,
                        false
                    )
                )
        }

    }
}