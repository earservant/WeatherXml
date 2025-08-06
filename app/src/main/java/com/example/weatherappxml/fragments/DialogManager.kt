package com.example.weatherappxml.fragments

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weatherappxml.R

object DialogManager {
    fun showLocationSettingsDialog(context: Context, onDefaultLocationSelected: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.current_location_on))
        builder.setMessage(context.getString(R.string.go_to_location_settings))
        builder.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
        builder.setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(
                context,
                context.getString(R.string.default_locations_settings),
                Toast.LENGTH_SHORT
            ).show()
            // Вызов обработчика для использования захардкоженных координат
            onDefaultLocationSelected()
        }
        builder.create().show()
    }

    fun searchByNameDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val edName = EditText(context)
        builder.setView(edName)
        val dialog = builder.create()
        dialog.setTitle(context.getString(R.string.city_name))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok)) { _, _ ->
            listener.onClick(edName.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel)) { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener {
        fun onClick(cityName: String)
    }
}