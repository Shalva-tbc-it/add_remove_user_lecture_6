package com.example.addremoveuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class User(
    var id: UUID = UUID.randomUUID(),
    var firstName: String = "",
    var lastName: String = "",
    var age: String = "",
    var email: String = ""

): Parcelable
