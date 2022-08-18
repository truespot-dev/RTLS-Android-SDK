package com.example.truespotrtlsandroidsdk

import com.example.truespotrtlsandroid.TSBeacon
import com.example.truespotrtlsandroid.models.TSDevice
import com.example.truespotrtlsandroidsdk.databinding.ItemBeaconBinding

class BeaconsViewHolder(private val binding: ItemBeaconBinding, private val mType: String?, val clickListener: TagBottomSheetFragment.Listener) : BaseViewHolder(binding.root) {
    override fun bind(item: Any, position: Int) {
        var bID = item as TSDevice
        binding.textView64.text = bID.tagIdentifier

        binding.textView64.setOnClickListener{
            clickListener.onSelected(item)
        }

    }
}