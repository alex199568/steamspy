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

        downloadTop2Weeks().subscribe { storeTop2Weeks(it); endProcess() }
        downloadTopOwned().subscribe { storeTopOwned(it); endProcess() }
        downloadTopTotal().subscribe { storeTopTotal(it); endProcess() }

        downloadGenre(Genre.ACTION).subscribe { storeGenreAction(it); endProcess() }
        downloadGenre(Genre.RPG).subscribe { storeGenreRPG(it); endProcess() }
        downloadGenre(Genre.STRATEGY).subscribe { storeGenreStrategy(it); endProcess() }
        downloadGenre(Genre.SIMULATION).subscribe { storeGenreSimulation(it); endProcess() }
        downloadGenre(Genre.ADVENTURE).subscribe { storeGenreAdventure(it); endProcess() }
        downloadGenre(Genre.INDIE).subscribe { storeGenreIndie(it); endProcess() }
        downloadGenre(Genre.SPORTS).subscribe { storeGenreSports(it); endProcess() }
        downloadGenre(Genre.EARLY_ACCESS).subscribe { storeGenreEarlyAccess(it); endProcess() }
        downloadGenre(Genre.EX_EARLY_ACCESS).subscribe { storeGenreExEarlyAccess(it); endProcess() }
        downloadGenre(Genre.MMO).subscribe { storeGenreMMO(it); endProcess() }
        downloadGenre(Genre.FREE).subscribe { storeGenreFree(it); endProcess() }
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

    private fun storeAll(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            appsResponse.toRealm().forEach { realm.copyToRealm(it) }
        }
        realm.close()
    }

    private fun storeTop2Weeks(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmTop2Weeks(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeTopOwned(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmTopOwned(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeTopTotal(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmTopTotal(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreAction(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreAction(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreStrategy(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreStrategy(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreRPG(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreRPG(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreAdventure(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreAdventure(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreIndie(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreIndie(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreSports(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreSports(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreSimulation(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreSimulation(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreEarlyAccess(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreEarlyAccess(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreExEarlyAccess(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreExEarlyAccess(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreMMO(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreMMO(appsResponse.apps))
        }
        realm.close()
    }

    private fun storeGenreFree(appsResponse: SteamAppsResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.copyToRealm(RealmGenreFree(appsResponse.apps))
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
