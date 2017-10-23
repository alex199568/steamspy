package md.ins8.steamspy

import android.content.Context
import android.net.wifi.WifiManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun retrieveVersionName(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName

fun <T> Single<T>.ioToMain(): Single<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.ioToMain(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun retrieveCurrentTime(): Long {
    val currentDate = Date()
    return currentDate.time
}

fun formatDate(date: Long): String {
    val format = DateFormat.getDateTimeInstance()
    val d = Date(date)
    return format.format(d)
}

fun onWifi(context: Context): Boolean {
    val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifi.isWifiEnabled
}

fun <T : Number> formatDecimal(decimal: T): String {
    val formatter = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val symbols = formatter.decimalFormatSymbols

    formatter.decimalFormatSymbols = symbols
    return formatter.format(decimal)
}
