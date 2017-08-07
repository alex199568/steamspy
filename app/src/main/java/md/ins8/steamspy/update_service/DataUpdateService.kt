package md.ins8.steamspy.update_service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import io.realm.Realm
import md.ins8.steamspy.RealmGenreFree
import md.ins8.steamspy.app.SteamSpyApp
import md.ins8.steamspy.app.di.Genre
import md.ins8.steamspy.app.di.SteamSpyAPIService
import javax.inject.Inject

class DataUpdateService : IntentService("DataUpdateService") {
    @Inject
    lateinit var steamAppsAPIService: SteamSpyAPIService

    override fun onCreate() {
        super.onCreate()
        DaggerDataUpdateComponent.builder().appComponent((application as SteamSpyApp).appComponent).build().inject(this)
    }

    override fun onHandleIntent(workIntent: Intent?) {
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "my_channel_id"
        val channelName = "SteamSpy Notifications"
        val description = "This is the channel for steam spy"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = description
            notifyManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setContentTitle("SteamSpy: Data Update")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentText("Downloading all apps")
        notifyManager.notify(1, builder.build())

        steamAppsAPIService.requestAll()
                .subscribe { data ->
                    builder.setContentText("Data arrived")
                    notifyManager.notify(1, builder.build())

                    Realm.deleteRealm(Realm.getDefaultConfiguration())
                    var realm = Realm.getDefaultInstance()
                    realm.executeTransaction {
                        data.toRealm().forEach { realm.copyToRealm(it) }
                    }
                    realm.close()

                    builder.setContentText("Data saved")
                    notifyManager.notify(1, builder.build())

                    steamAppsAPIService.requestGenre(genre = Genre.FREE.paramName)
                            .subscribe { data1 ->
                                builder.setContentText("Genre data arrived")
                                notifyManager.notify(1, builder.build())

                                realm = Realm.getDefaultInstance()
                                realm.executeTransaction {
                                    realm.copyToRealm(RealmGenreFree(data1.apps))
                                }
                                realm.close()

                                builder.setContentText("Genre data saved")
                                notifyManager.notify(1, builder.build())

                            }
                }
    }
}
