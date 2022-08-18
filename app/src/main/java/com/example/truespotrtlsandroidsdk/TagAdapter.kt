package com.example.truespotrtlsandroidsdk

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.truespotrtlsandroid.TSBeacon
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.TSDevice
import com.example.truespotrtlsandroidsdk.databinding.ItemBeaconBinding
import java.util.ArrayList

class TagAdapter(var tagsList: MutableList<TSBeaconSighting>, val mType: String?,
                 val clickListener: TagBottomSheetFragment.Listener) : BaseRecyclerViewAdapter() {

    private var filter: String? = null
    private var tagsItemsArrList: MutableList<TSBeaconSighting> = ArrayList()
    var filterDataList: MutableList<TSBeaconSighting> = ArrayList()
    var updatedFilteredList : MutableList<TSBeaconSighting> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemTagBinding = ItemBeaconBinding.inflate(layoutInflater, parent, false)
        return TagViewHolder(itemTagBinding, mType, clickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val tag = tagsList?.get(position)
        tag?.let { holder.bind(it,position) }
    }
    override fun getItemCount(): Int {
        return tagsList?.size ?: 0
    }
    fun setUpdatedList(l : Collection<TSBeaconSighting>?){
        tagsList = l as MutableList<TSBeaconSighting>;
        filterDataList = l
        notifyDataSetChanged()
    }

    fun setBeaconFilterItems(list: List<TSBeaconSighting>?) {
        tagsItemsArrList.clear()
        if (list != null) {
            tagsItemsArrList.addAll(list)
        }
        tagsList = tagsItemsArrList
        notifyDataSetChanged()
    }

}