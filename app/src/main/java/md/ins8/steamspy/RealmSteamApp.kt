package md.ins8.steamspy

import io.realm.*
import io.realm.annotations.PrimaryKey

open class RealmDev(var name: String) : RealmObject() {
    constructor() : this("")
}

open class RealmPub(var name: String) : RealmObject() {
    constructor() : this("")
}

open class RealmTag(
        var name: String,
        var votes: Int
) : RealmObject() {
    constructor(tag: Tag) : this(tag.name, tag.votes)

    constructor() : this("", 0)
}

open class RealmSteamApp(
        @PrimaryKey
        var id: Long,
        var name: String,
        var dev: RealmList<RealmDev> = RealmList(),
        var pub: RealmList<RealmPub> = RealmList(),
        var rank: Int,
        var numOwners: Int,
        var ownersVariance: Int,
        var playersTotal: Int,
        var playersTotalVariance: Int,
        var players2Weeks: Int,
        var players2WeeksVariance: Int,
        var averageTotal: Int,
        var average2Weeks: Int,
        var medianTotal: Int,
        var median2Weeks: Int,
        var ccu: Int,
        var price: String,
        var tags: RealmList<RealmTag> = RealmList(),
        var listTypeIds: RealmList<RealmListTypeId> = RealmList()
) : RealmObject() {
    constructor(steamApp: RawSteamApp) : this(
            id = steamApp.id,
            name = steamApp.name,
            rank = steamApp.rank,
            numOwners = steamApp.numOwners,
            ownersVariance = steamApp.ownersVariance,
            playersTotal = steamApp.playersTotal,
            playersTotalVariance = steamApp.playersTotalVariance,
            players2Weeks = steamApp.players2Weeks,
            players2WeeksVariance = steamApp.players2WeeksVariance,
            averageTotal = steamApp.averageTotal,
            average2Weeks = steamApp.average2Weeks,
            medianTotal = steamApp.medianTotal,
            median2Weeks = steamApp.median2Weeks,
            ccu = steamApp.ccu,
            price = steamApp.price
    ) {
        steamApp.dev.mapTo(dev, { RealmDev(it) })
        steamApp.pub.mapTo(pub, { RealmPub(it) })
        steamApp.tags.mapTo(tags, { RealmTag(it) })
    }

    constructor() : this(
            id = 0,
            name = "",
            rank = 0,
            numOwners = 0,
            ownersVariance = 0,
            playersTotal = 0,
            playersTotalVariance = 0,
            players2Weeks = 0,
            players2WeeksVariance = 0,
            averageTotal = 0,
            average2Weeks = 0,
            medianTotal = 0,
            median2Weeks = 0,
            ccu = 0,
            price = ""
    )
}

open class RealmListTypeId(var listTypeId: Int = -1) : RealmObject()


fun storeAppsList(appsResponse: SteamAppsResponse, listTypeId: Int) {
    val realm = Realm.getDefaultInstance()
    var query = realm.where(RealmSteamApp::class.java)
    appsResponse.apps.forEach { query = query.equalTo("id", it.id).or() }
    val results = query.findAll()
    realm.executeTransaction {
        results.forEach {
            it.listTypeIds.add(RealmListTypeId(listTypeId))
            realm.copyToRealmOrUpdate(it)
        }
    }
    realm.close()
}

fun storeAll(appsResponse: SteamAppsResponse) {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction {
        appsResponse.toRealm().forEach { realm.copyToRealm(it) }
    }
    realm.close()
}

fun deleteAllApps() {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction {
        it.delete(RealmSteamApp::class.java)
    }
    realm.close()
}

fun deleteAppsList(listTypeId: Int) {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction {
        realm.where(RealmSteamApp::class.java).equalTo("listTypeIds.listTypeId", listTypeId).findAll().forEach {
            it.listTypeIds.remove(RealmListTypeId(listTypeId))
            realm.copyToRealmOrUpdate(it)
        }
    }
    realm.close()
}


fun loadAllApps(realm: Realm): RealmResults<RealmSteamApp> = realm.where(RealmSteamApp::class.java)?.findAll()!!

fun loadAppsForName(realm: Realm, name: String): RealmResults<RealmSteamApp> =
        realm.where(RealmSteamApp::class.java).contains("name", name, Case.INSENSITIVE).findAll()

fun loadAppsList(realm: Realm, listTypeId: Int): RealmResults<RealmSteamApp> =
        realm.where(RealmSteamApp::class.java).equalTo("listTypeIds.listTypeId", listTypeId).findAll()

