package md.ins8.steamspy.update_service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmModel
import md.ins8.steamspy.*
import md.ins8.steamspy.app.SteamSpyApp
import md.ins8.steamspy.app.di.Genre
import md.ins8.steamspy.app.di.SteamSpyAPIService
import javax.inject.Inject

private val CHANNEL_ID = "steam_spy_channel_id"
private val NOTIFICATION_ID = 1
private val INTENT_SERVICE_NAME = "DataUpdateIntentService"
private val TOTAL_PROCESSES = 15

class DataUpdateService : IntentService(INTENT_SERVICE_NAME) {
    @Inject
    lateinit var steamAppsAPIService: SteamSpyAPIService

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private var processesEnded = 0

    override fun onCreate() {
        super.onCreate()
        DaggerDataUpdateComponent.builder().appComponent((application as SteamSpyApp).appComponent).build().inject(this)

        setupNotifications()
    }

    override fun onHandleIntent(workIntent: Intent?) {
        updateNotification(getString(R.string.dataUpdateNotificationText))
        deleteApps()

        downloadAll().subscribe { storeAll(it); endProcess() }

        downloadTop2Weeks().subscribe { storeTyped(it, RealmTop2Weeks::class.java); endProcess() }
        downloadTopOwned().subscribe { storeTyped(it, RealmTopOwned::class.java); endProcess() }
        downloadTopTotal().subscribe { storeTyped(it, RealmTopTotal::class.java); endProcess() }

        downloadGenre(Genre.ACTION).subscribe { storeTyped(it, RealmGenreAction::class.java); endProcess() }
        downloadGenre(Genre.RPG).subscribe { storeTyped(it, RealmGenreRPG::class.java); endProcess() }
        downloadGenre(Genre.STRATEGY).subscribe { storeTyped(it, RealmGenreStrategy::class.java); endProcess() }
        downloadGenre(Genre.SIMULATION).subscribe { storeTyped(it, RealmGenreSimulation::class.java); endProcess() }
        downloadGenre(Genre.ADVENTURE).subscribe { storeTyped(it, RealmGenreAdventure::class.java); endProcess() }
        downloadGenre(Genre.INDIE).subscribe { storeTyped(it, RealmGenreIndie::class.java); endProcess() }
        downloadGenre(Genre.SPORTS).subscribe { storeTyped(it, RealmGenreEarlyAccess::class.java); endProcess() }
        downloadGenre(Genre.EARLY_ACCESS).subscribe { storeTyped(it, RealmGenreEarlyAccess::class.java); endProcess() }
        downloadGenre(Genre.EX_EARLY_ACCESS).subscribe { storeTyped(it, RealmGenreExEarlyAccess::class.java); endProcess() }
        downloadGenre(Genre.MMO).subscribe { storeTyped(it, RealmGenreMMO::class.java); endProcess() }
        downloadGenre(Genre.FREE).subscribe { storeTyped(it, RealmGenreFree::class.java); endProcess() }
    }

    private fun endProcess() {
        ++processesEnded
        if (processesEnded == TOTAL_PROCESSES) {
            notificationManager.cancel(NOTIFICATION_ID)
        }
    }

    private fun downloadAll(): Observable<SteamAppsResponse> {
        return steamAppsAPIService.requestAll()
    }

    private fun downloadTop2Weeks(): Observable<SteamAppsResponse> {
        return steamAppsAPIService.requestTop2Weeks()
    }

    private fun downloadTopOwned(): Observable<SteamAppsResponse> {
        return steamAppsAPIService.requestTopOwned()
    }

    private fun downloadTopTotal(): Observable<SteamAppsResponse> {
        return steamAppsAPIService.requestTopTotal()
    }

    private fun downloadGenre(genre: Genre): Observable<SteamAppsResponse> {
        return steamAppsAPIService.requestGenre(genre = genre.paramName)
    }

    private fun deleteApps() {
        Realm.deleteRealm(Realm.getDefaultConfiguration())
    }

    private fun <T> storeTyped(appsResponse: SteamAppsResponse, clazz: Class<T>) where T : RealmModel, T : CustomRealmList {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val list = clazz.newInstance()
            appsResponse.apps.forEach {
                list.apps.add(RealmAppId(it.id))
            }
            realm.copyToRealm(list)
        }
        realm.close()
    }

    private fun storeAll(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            appsResponse.toRealm().forEach { realm.copyToRealm(it) }
        }
        realm.close()
    }

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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true)
    }

    private fun updateNotification(message: String) {
        notificationBuilder.setContentText(message)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

}
