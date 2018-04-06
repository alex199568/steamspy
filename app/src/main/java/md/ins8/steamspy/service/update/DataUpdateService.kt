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

const val LAST_UPDATE_TIME_KEY = "LastUpdateTimeKey"
const val TO_CHECK_PARAM_EXTRA = "ToCheckParamExtra"
const val OK_TO_START_KEY = "OkToStart"

private const val DOWNLOAD_PART = 0.6
private const val STORE_PART = 0.4
private const val ALL_APPS_PART = 30

class DataUpdateService : IntentService(INTENT_SERVICE_NAME) {
    @Inject
    lateinit var steamAppsAPIService: SteamSpyAPIService
    @Inject
    lateinit var autoUpdateChecker: AutoUpdateConditionsChecker

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        DaggerDataUpdateComponent.builder().appComponent((application as SteamSpyApp).appComponent).build().inject(this)

        setupNotifications()
    }

    override fun onHandleIntent(workIntent: Intent?) {
        saveRunning(true)
        var toCheck = false
        if (workIntent != null) {
            toCheck = workIntent.getBooleanExtra(TO_CHECK_PARAM_EXTRA, false)
        }
        if (toCheck) {
            if (autoUpdateChecker.check()) {
                doUpdate()
            }
        }
        doUpdate()
        saveRunning(false)
    }

    private fun doUpdate() {
        updateNotification()

        downloadAll()
        val observables = downloadListTypes()
        startDownloading(observables)

        saveUpdateTime()

        notificationManager.cancel(NOTIFICATION_ID)

        val intent = Intent()
        intent.action = LOCAL_ACTION
        val lbm = LocalBroadcastManager.getInstance(this)
        lbm.sendBroadcast(intent)
    }

    private fun downloadAll() {
        steamAppsAPIService.requestAll()
                .subscribe({
                    updateNotification((ALL_APPS_PART * DOWNLOAD_PART).toInt())
                    deleteRealm()
                    storeAll(it)
                    updateNotification((ALL_APPS_PART * DOWNLOAD_PART + ALL_APPS_PART * STORE_PART).toInt())
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
        val totalLists = observables.size
        var currentPercent = ALL_APPS_PART
        val left = 100 - ALL_APPS_PART
        val downloadPercent = (left / totalLists) * DOWNLOAD_PART
        val storePercent = (left / totalLists) * STORE_PART
        for ((key, value) in observables) {
            value.subscribe({
                currentPercent += downloadPercent.toInt()
                updateNotification(currentPercent)
                storeAppsList(it, key.id)
                currentPercent += storePercent.toInt()
                updateNotification(currentPercent)
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
                .setProgress(100, 0, false)
                .setSmallIcon(R.drawable.ic_stat_spyglass_white)
    }

    private fun updateNotification(percent: Int = 0, message: String = getString(R.string.dataUpdateNotificationText)) {
        notificationBuilder.setContentText(message)
        notificationBuilder.setProgress(100, percent, false)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun saveUpdateTime() {
        val sharedPrefs = getSharedPreferences(getString(R.string.sharedPreferencesFileKey), Context.MODE_PRIVATE)
        val currentTime = retrieveCurrentTime()
        sharedPrefs.edit().putLong(LAST_UPDATE_TIME_KEY, currentTime).apply()
    }

    private fun saveRunning(running: Boolean) {
        val prefs = getSharedPreferences(getString(R.string.sharedPreferencesFileKey), Context.MODE_PRIVATE)
        prefs.edit().putBoolean(OK_TO_START_KEY, !running).apply()
    }
}
