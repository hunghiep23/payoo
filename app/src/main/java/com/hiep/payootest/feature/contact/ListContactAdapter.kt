package com.hiep.payootest.feature.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiep.payootest.R
import com.hiep.payootest.model.Contact

class ListContactAdapter(private val itemClickListener: (Contact) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val contactList = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return ContactHolder.create(
            inflater,
            parent
        )
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactHolder -> holder.bind(contactList[position], itemClickListener)
        }
    }

    fun setList(newContactList: List<Contact>) {
        contactList.clear()
        contactList.addAll(newContactList)
        notifyDataSetChanged()
    }


    fun clearList(){
        contactList.clear()
        notifyDataSetChanged()
    }

    private class ContactHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val tvContactName: TextView = itemView.findViewById(R.id.tv_contact_name)
        private val tvContactPhone: TextView = itemView.findViewById(R.id.tv_contact_phone)

        fun bind(contact: Contact, itemClickListener: (Contact) -> Unit) {
            tvContactName.text = contact.name
            tvContactPhone.text = contact.phone
            itemView.setOnClickListener {
                itemClickListener(contact)
            }
        }

        companion object {
            @JvmStatic
            fun create(inflater: LayoutInflater, parent: ViewGroup?) =
                ContactHolder(
                    inflater.inflate(
                        R.layout.item_contact,
                        parent,
                        false
                    )
                )
        }

    }
}