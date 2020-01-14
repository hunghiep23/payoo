package com.hiep.payootest.feature.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hiep.payootest.R
import com.hiep.payootest.model.Contact
import com.hiep.payootest.model.State
import kotlinx.android.synthetic.main.activity_list_contact.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import kotlinx.android.synthetic.main.layout_actionbar_search.*
import kotlinx.android.synthetic.main.layout_empty.*

class ListContactActivity : AppCompatActivity() {
    lateinit var viewModel: ListContactViewModel
    lateinit var contactAdapter: ListContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_contact)
        initView()
        initViewModel()

        viewModel.getContacts(applicationContext)
    }

    private fun initView() {
        setSupportActionBar(toolbarContact)
        actionbarBack.setOnClickListener(onClickListener)
        searchViewContact.setOnQueryTextListener(onQueryTextListener)
        initContactAdapter()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[ListContactViewModel::class.java]
        subscribeLiveData()
    }

    private fun initContactAdapter() {
        contactAdapter = ListContactAdapter {
            it.phone?.let { phoneNumber ->
                val dataIntent = Intent()
                dataIntent.data = Uri.parse(phoneNumber)
                setResult(RESULT_OK, dataIntent)
                finish()
            }
        }
        rvListContact.adapter = contactAdapter
    }

    private val onClickListener = View.OnClickListener {
        when (it?.id) {
            R.id.actionbarBack -> finish()
        }
    }
    private val onQueryTextListener = object :
        OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { viewModel.searchContacts(it) }
            return true
        }

    }

    private fun subscribeLiveData() {
        viewModel.contactsLiveData.observe(this, Observer {
            val (state, data, _) = it
            when (state) {
                State.SUCCEED -> {
                    if (data != null) {
                        bindData(data)
                    }
                }
                State.EMPTY -> {
                    bindEmpty()
                }
                else -> {
                }
            }
        })
    }

    fun bindData(data: List<Contact>) {
        vEmptyContact.visibility = View.GONE
        rvListContact.visibility = View.VISIBLE
        contactAdapter.setList(data)
    }

    fun bindEmpty() {
        contactAdapter.clearList()
        rvListContact.visibility = View.GONE
        vEmptyContact.visibility = View.VISIBLE
        imvEmpty.setImageResource(R.drawable.ic_contact)
        tvEmpty.text = "Danh bạ trống. Hãy thêm vào danh bạ"
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, ListContactActivity::class.java)
    }
}
