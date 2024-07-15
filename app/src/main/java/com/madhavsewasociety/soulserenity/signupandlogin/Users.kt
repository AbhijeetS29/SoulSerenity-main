package com.madhavsewasociety.soulserenity.signupandlogin

import android.provider.ContactsContract.CommonDataKinds.Email

data class Users(
    var userName: String = "",
    var userId: String = "",
    var email: String = "",
    var photoUrl :String=""
)
