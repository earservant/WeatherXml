package com.example.weatherappxml.fragments

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Fragment.isPermissionGranted(p: String): Boolean {
    return ContextCompat.checkSelfPermission(
        activity as AppCompatActivity,
        p
    ) == PackageManager.PERMISSION_GRANTED
}

fun String.formatImageUrl(): String {
    return if (this.startsWith("http")) this else "https://$this"
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDisplayDate(): String =
    LocalDate.parse(this).format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru")))

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDisplayTime(): String =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        .format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru")))
