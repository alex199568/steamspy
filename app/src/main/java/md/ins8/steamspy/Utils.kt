package md.ins8.steamspy

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.DateFormat
import java.util.*

fun retrieveVersionName(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName

fun pingHost(host: String, callback: (connected: Boolean) -> Unit, concurrent: Boolean = false) {
    var connectivityObserver = ReactiveNetwork.checkInternetConnectivity(SocketInternetObservingStrategy(), host)
    if (concurrent) {
        connectivityObserver = connectivityObserver.ioToMain()
    }
    connectivityObserver.subscribe({
        callback.invoke(it)
    }, {
        Timber.e(it)
        callback(false)
    }
    )
}

fun <T> Single<T>.ioToMain(): Single<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.ioToMain(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun retrieveCurrentTime(): String {
    val currentDate = Date()
    val format = DateFormat.getDateTimeInstance()
    return format.format(currentDate)
}