package com.sello.wherethereis4cast.data

class DataOrException<T, E : Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var exception: E? = null,
    var isConnected: Boolean? = null,
    var isSearchedFromTextFieldLocationFound: Boolean? = null
)