package md.ins8.steamspy

import android.content.Context
import android.net.wifi.WifiManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
import java.util.*

fun retrieveVersionName(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName

fun <T> Single<T>.ioToMain(): Single<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.ioToMain(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun retrieveCurrentTime(): String {
    val currentDate = Date()
    val format = DateFormat.getDateTimeInstance()
    return format.format(currentDate)
}

fun onWifi(context: Context): Boolean {
    val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifi.isWifiEnabled
}