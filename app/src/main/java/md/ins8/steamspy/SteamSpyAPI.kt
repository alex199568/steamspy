package md.ins8.steamspy

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.realm.RealmList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://steamspy.com/"

private const val CONNECT_TIMEOUT = 60L
private const val READ_TIMEOUT = 300L

enum class ResponseField(val fieldName: String) {
    APP_ID("appid"),
    NAME("name"),
    DEV("developer"),
    PUB("publisher"),
    SCORE("score_rank"),
    NUM_OWNERS("owners"),
    OWNERS_VARIANCE("owners_variance"),
    NUM_PLAYERS("players_forever"),
    PLAYERS_VARIANCE("players_forever_variance"),
    NUM_PLAYERS_2_WEEKS("players_2weeks"),
    PLAYERS_2_WEEKS_VARIANCE("players_2weeks_variance"),
    AVERAGE("average_forever"),
    AVERAGE_2_WEEKS("average_2weeks"),
    MEDIAN("median_forever"),
    MEDIAN_2_WEEKS("median_2weeks"),
    CCU("ccu"),
    PRICE("price"),
}

interface SteamSpyAPIService {
    @GET("api.php?")
    fun requestSteamApp(@Query("request") request: String = "appdetails", @Query("appid") appId: Long): Observable<RealmSteamApp>

    @GET("api.php?")
    fun requestGenre(@Query("request") request: String = "genre", @Query("genre", encoded = true) genre: String): Observable<SteamAppsResponse>

    @GET("api.php?")
    fun requestTop(@Query("request") request: String): Observable<SteamAppsResponse>

    @GET("api.php?")
    fun requestAll(@Query("request") request: String = "all"): Observable<SteamAppsResponse>
}

@Module
class SteamSpyAPIModule {
    @AppScope
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(BASE_URL)
                .build()
    }

    @AppScope
    @Provides
    fun provideSteamSpyAPIService(retrofit: Retrofit): SteamSpyAPIService =
            retrofit.create(SteamSpyAPIService::class.java)

    @AppScope
    @Provides
    fun provideLogginInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE }

    @AppScope
    @Provides
    fun provideClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build()
    }

    @AppScope
    @Provides
    fun provideGson(): Gson {
        val tokenRealmSteamApp = object : TypeToken<RealmSteamApp>() {}
        val tokenSteamAppsResponse = object : TypeToken<SteamAppsResponse>() {}
        return GsonBuilder()
                .registerTypeAdapter(tokenRealmSteamApp.type, object : TypeAdapter<RealmSteamApp>() {

                    @Throws(IOException::class)
                    override fun write(out: JsonWriter, value: RealmSteamApp) {
                        //no-op
                    }

                    @Throws(IOException::class)
                    override fun read(`in`: JsonReader): RealmSteamApp {
                        `in`.beginObject()
                        val steamApp = `in`.extractRawSteamApp()
                        `in`.endObject()
                        return steamApp
                    }
                })
                .registerTypeAdapter(tokenSteamAppsResponse.type, object : TypeAdapter<SteamAppsResponse>() {
                    @Throws(IOException::class)
                    override fun write(out: JsonWriter, value: SteamAppsResponse) {
                        //no op
                    }

                    override fun read(`in`: JsonReader): SteamAppsResponse {
                        `in`.beginObject()
                        val apps = RealmList<RealmSteamApp>()
                        while (`in`.hasNext()) {
                            `in`.nextName()
                            `in`.beginObject()
                            apps.add(`in`.extractRawSteamApp())
                            `in`.endObject()
                        }
                        `in`.endObject()
                        return SteamAppsResponse(apps)
                    }
                })
                .create()
    }
}

fun JsonReader.extractRawSteamApp(): RealmSteamApp {
    val steamApp = RealmSteamApp()
    while (hasNext()) {
        val name = nextName()
        when (name) {
            ResponseField.APP_ID.fieldName -> steamApp.id = safeNextLong()
            ResponseField.NAME.fieldName -> steamApp.name = safeNextString()
            ResponseField.AVERAGE.fieldName -> steamApp.averageTotal = safeNextInt()
            ResponseField.AVERAGE_2_WEEKS.fieldName -> steamApp.average2Weeks = safeNextInt()
            ResponseField.CCU.fieldName -> steamApp.ccu = safeNextInt()
            ResponseField.MEDIAN.fieldName -> steamApp.medianTotal = safeNextInt()
            ResponseField.MEDIAN_2_WEEKS.fieldName -> steamApp.median2Weeks = safeNextInt()
            ResponseField.NUM_OWNERS.fieldName -> steamApp.numOwners = safeNextInt()
            ResponseField.NUM_PLAYERS.fieldName -> steamApp.playersTotal = safeNextInt()
            ResponseField.NUM_PLAYERS_2_WEEKS.fieldName -> steamApp.players2Weeks = safeNextInt()
            ResponseField.OWNERS_VARIANCE.fieldName -> steamApp.ownersVariance = safeNextInt()
            ResponseField.PLAYERS_VARIANCE.fieldName -> steamApp.playersTotalVariance = safeNextInt()
            ResponseField.PLAYERS_2_WEEKS_VARIANCE.fieldName -> steamApp.players2WeeksVariance = safeNextInt()
            ResponseField.SCORE.fieldName -> steamApp.rank = safeNextInt()
            ResponseField.DEV.fieldName -> steamApp.dev = extractDevs()
            ResponseField.PUB.fieldName -> steamApp.pub = extractPubs()
            ResponseField.PRICE.fieldName -> steamApp.price = safeNextString()
            else -> skipValue()
        }
    }
    return steamApp
}

fun JsonReader.extractDevs(): RealmList<RealmDev> {
    val devs = RealmList<RealmDev>()
    val raw = splitNextString()
    return raw.mapTo(devs, { RealmDev(it) })
}

fun JsonReader.extractPubs(): RealmList<RealmPub> {
    val pubs = RealmList<RealmPub>()
    val raw = splitNextString()
    return raw.mapTo(pubs, { RealmPub(it) })
}

fun JsonReader.splitNextString(): MutableList<String> {
    val result: MutableList<String> = mutableListOf()
    result.addAll(safeNextString().split(","))
    val treamed: MutableList<String> = mutableListOf()
    result.mapTo(treamed, { it.trim(' ') })
    return treamed
}

fun JsonReader.extractTags(): RealmList<RealmTag> {
    return try {
        beginObject()
        val tags = RealmList<RealmTag>()
        while (hasNext()) {
            tags.add(RealmTag(nextName(), nextInt()))
        }
        endObject()
        tags
    } catch (e: IllegalStateException) {
        beginArray()
        endArray()
        RealmList()
    }
}

fun JsonReader.safeNextInt(): Int = try {
    nextInt()
} catch (e: NumberFormatException) {
    nextString()
    -1
}

fun JsonReader.safeNextString(): String = try {
    nextString()
} catch (e: IllegalStateException) {
    nextNull()
    ""
}

fun JsonReader.safeNextLong(): Long = try {
    nextLong()
} catch (e: NumberFormatException) {
    nextString()
    -1
}
