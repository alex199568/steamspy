package md.ins8.steamspy

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun retrieveVersionName(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName

fun pingHost(host: String, callback: (connected: Boolean) -> Unit, concurrent: Boolean = false) {
    var connectivityObserver = ReactiveNetwork.checkInternetConnectivity(SocketInternetObservingStrategy(), host)
    if (concurrent) {
        connectivityObserver = connectivityObserver
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
    connectivityObserver.subscribe { result -> callback.invoke(result) }
}