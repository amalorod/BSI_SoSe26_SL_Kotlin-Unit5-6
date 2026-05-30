package com.example.busscheduleapp

import android.app.Application
import com.example.busscheduleapp.data.AppDatabase

class BusScheduleApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}