package com.example.himapl.models

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class mahasiswa(
    val userId: String? = null,
    val nim: String? = null,
    val nama: String? = null,
    val email: String? = null,
    val angkatan: Int? = null,
    val password: String? =null,
    val role: String? =null,
    var imageUrl: String? = null,
    var jabatan: String?=null,
) : Serializable
