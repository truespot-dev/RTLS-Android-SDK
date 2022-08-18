package com.example.truespotrtlsandroidsdk

public interface CompletionCallBack {
    fun completion(completion: (exception: Exception?) -> Unit)
}