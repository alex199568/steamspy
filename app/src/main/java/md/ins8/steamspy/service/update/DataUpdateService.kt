package md.ins8.steamspy.service.update

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import io.reactivex.Observable
import md.ins8.steamspy.*
import timber.log.Timber
import javax.inject.Inject

private const val CHANNEL_ID = "steam_spy_channel_id"
private const val NOTIFICATION_ID = 1
private const val INTENT_SERVICE_NAME = "DataUpdateIntentService"

class DataUpdateService : IntentService(INTENT_SERVICE_NAME) {
    @Inject
    lateinit var steamAppsAPIService: SteamSpyAPIService

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder


    override fun onCreate() {
        super.onCreate()
        DaggerDataUpdateComponent.builder().appComponent((application as SteamSpyApp).appComponent).build().inject(this)

        setupNotifications()
    }

    override fun onHandleIntent(workIntent: Intent?) {
        pingHost(PING_URL, {
            if (it) {
                doUpdate()
            }
        })
    }

    private fun doUpdate() {
        updateNotification(getString(R.string.dataUpdateNotificationText))

        downloadAll()
        val observables = downloadListTypes()
        startDownloading(observables)

        notificationManager.cancel(NOTIFICATION_ID)

        val intent = Intent()
        intent.action = LOCAL_ACTION
        val lbm = LocalBroadcastManager.getInstance(this)
        lbm.sendBroadcast(intent)
    }

    private fun downloadAll() {
        steamAppsAPIService.requestAll()
                .subscribe({
                    deleteAllApps()
                    storeAll(it)
                }, {
                    Timber.e(it)
                })
    }

    private fun downloadListTypes(): Map<ListType, Observable<SteamAppsResponse>> {
        val result = hashMapOf<ListType, Observable<SteamAppsResponse>>()
        TopListTypes.values().forEach {
            result[it.listType] = downloadTop(it.listType.paramName)
        }
        GenreListTypes.values().forEach {
            result[it.listType] = downloadGenre(it.listType.paramName)
        }
        return result
    }

    private fun startDownloading(observables: Map<ListType, Observable<SteamAppsResponse>>) {
        for ((key, value) in observables) {
            value.subscribe({
                deleteAppsList(key.id)
                storeAppsList(it, key.id)
            }, {
                Timber.e(it)
            })
        }
    }

    private fun downloadTop(param: String): Observable<SteamAppsResponse> =
            steamAppsAPIService.requestTop(param)

    private fun downloadGenre(genre: String): Observable<SteamAppsResponse> =
            steamAppsAPIService.requestGenre(genre = genre)

    private fun setupNotifications() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, getString(R.string.notificationChannelName), importance)
            channel.description = getString(R.string.notificationChannelDescription)
            notificationManager.createNotificationChannel(channel)
        }

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setContentTitle(getString(R.string.notificationDataUpdateTitle))
                .setProgress(0, 0, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.spyglass_white)
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_square)
        }
    }

    private fun updateNotification(message: String) {
        notificationBuilder.setContentText(message)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}
