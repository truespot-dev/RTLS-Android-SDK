package com.example.truespotrtlsandroidsdk

import com.example.truespotrtlsandroid.TSBeacon
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.TSDevice
import com.example.truespotrtlsandroidsdk.databinding.ItemBeaconBinding

class TagViewHolder(private val binding: ItemBeaconBinding, private val mType: String?, val clickListener: TagBottomSheetFragment.Listener) : BaseViewHolder(binding.root)  {
    override fun bind(item: Any, position: Int) {
        var bID = item as TSBeaconSighting
        binding.textView64.text = bID.beaconIdentifier

        binding.textView64.setOnClickListener{
            clickListener.onTagSelected(item)
        }

    }
}