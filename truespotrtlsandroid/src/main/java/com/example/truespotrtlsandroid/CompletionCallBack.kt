package com.example.truespotrtlsandroid

public interface CompletionCallBack {
    fun completion(completion: (exception: Exception?) -> Unit){}
}