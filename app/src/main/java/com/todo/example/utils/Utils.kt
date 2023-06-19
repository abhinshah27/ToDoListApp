package com.todo.example.utils

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.todo.example.utils.Constants.AAA
import com.todo.example.utils.Constants.DD_MMM_YYYY
import com.todo.example.utils.Constants.DD_MMM_YYYY_HH_MM_AAA
import com.todo.example.utils.Constants.HH_MM
import com.todo.example.utils.Constants.HH_MM_AAA
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * class is used to declare methods inside an object or use package-level functions
 */

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

enum class TaskStatus {
    PENDING,
    COMPLETED,
    UNCOMPLETED,
}

fun getDateToTimestamp(value: String): Long {
    return SimpleDateFormat(DD_MMM_YYYY_HH_MM_AAA, Locale.getDefault()).parse(value)?.time ?: 0
}

fun getCurrentDate(): String {
    val c: Date = Calendar.getInstance().time
    val df = SimpleDateFormat(DD_MMM_YYYY, Locale.getDefault())
    return df.format(c)
}

fun getCurrentTime(): String {
    val c: Date = Calendar.getInstance().time
    val df = SimpleDateFormat(HH_MM, Locale.getDefault())
    return df.format(c)
}

fun getCurrentMeridiem(): String {
    val c: Date = Calendar.getInstance().time
    val df = SimpleDateFormat(AAA, Locale.getDefault())
    return df.format(c).uppercase()
}

fun Context.timePickerDialog(
    time: (String, String, String) -> Unit,
): TimePickerDialog {
    val currentTime = Calendar.getInstance()
    val hour = currentTime[Calendar.HOUR_OF_DAY]
    val minute = currentTime[Calendar.MINUTE]
    val picker = TimePickerDialog(this, { _, mHour, mMin ->
        val finalHour = (if (mHour > 12) mHour % 12 else mHour).toString()
        val finalMinute = if (mMin < 10) "0$mMin" else mMin.toString()
        val meridiem = if (mHour < 12) "AM" else "PM"
        time.invoke(finalHour, finalMinute, meridiem)
    }, hour, minute, false)
    return picker
}

fun isTimeInPast(time: String): Boolean {
    val formatter = SimpleDateFormat(DD_MMM_YYYY_HH_MM_AAA, Locale.getDefault())
    val mDate = getCurrentDate()
    val text = "$mDate $time"
    val date = formatter.parse(text)
    val currentDate = Calendar.getInstance().time
    return date?.before(currentDate) ?: true
}

fun Activity.showSnackBar(msg: String?, view: View) {
    if (!TextUtils.isEmpty(msg)) {
        this.hideKeyboard()
        val snack = Snackbar.make(view, msg.toString(), Snackbar.LENGTH_LONG)
        if (!snack.isShown) {
            snack.show()
        }
    }
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) view = View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun changeDataFormat(dateInput: String?, inputPattern: String?, outputPattern: String?): String {
    return if (!dateInput.isNullOrEmpty()) {
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
        val date: Date?
        var str: String
        try {
            date = inputFormat.parse(dateInput)
            str = outputFormat.format(date ?: "")
        } catch (e: ParseException) {
            e.printStackTrace()
            str = ""
        }
        str
    } else {
        ""
    }
}

@BindingAdapter("time")
fun TextView.time(value: String?) {
    value?.let {
        if (it.isNotEmpty()) {
            this.text = changeDataFormat(
                dateInput = it,
                inputPattern = DD_MMM_YYYY_HH_MM_AAA,
                outputPattern = HH_MM_AAA,
            )
        }
    }
}

val isRunningTest: Boolean by lazy {
    try {
        Class.forName("androidx.test.espresso.Espresso")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

fun isDateInPast(value: String): Boolean {
    val formatter = SimpleDateFormat(DD_MMM_YYYY_HH_MM_AAA, Locale.getDefault())
    val date = formatter.parse(value)
    val currentDate = Calendar.getInstance().time
    return date?.before(currentDate) ?: true
}
