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
const val PING_URL = "www.steamspy.com"

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
    TAGS("tags")
}

enum class Genre(val paramName: String) {
    ACTION("Action"),
    STRATEGY("Strategy"),
    RPG("RPG"),
    INDIE("Indie"),
    ADVENTURE("Adventure"),
    SPORTS("Sports"),
    SIMULATION("Simulation"),
    EARLY_ACCESS("Early+Access"),
    EX_EARLY_ACCESS("Ex+Early+Access"),
    MMO("Massively+Multiplayer"),
    FREE("Free")
}


interface SteamSpyAPIService {
    @GET("api.php?")
    fun requestSteamApp(@Query("request") request: String = "appdetails", @Query("appid") appId: Long): Observable<RawSteamApp>

    @GET("api.php?")
    fun requestGenre(@Query("request") request: String = "genre", @Query("genre", encoded = true) genre: String): Observable<SteamAppsResponse>

    @GET("api.php?")
    fun requestTop2Weeks(@Query("request") request: String = "top100in2weeks"): Observable<SteamAppsResponse>

    @GET("api.php?")
    fun requestTopOwned(@Query("request") request: String = "top100owned"): Observable<SteamAppsResponse>

    @GET("api.php?")
    fun requestTopTotal(@Query("request") request: String = "top100forever"): Observable<SteamAppsResponse>

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
        val tokenRawSteamApp = object : TypeToken<RawSteamApp>() {}
        val tokenSteamAppsResponse = object : TypeToken<SteamAppsResponse>() {}
        return GsonBuilder()
                .registerTypeAdapter(tokenRawSteamApp.type, object : TypeAdapter<RawSteamApp>() {

                    @Throws(IOException::class)
                    override fun write(out: JsonWriter, value: RawSteamApp) {
                        //no-op
                    }

                    @Throws(IOException::class)
                    override fun read(`in`: JsonReader): RawSteamApp {
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
                        val apps: MutableList<RawSteamApp> = mutableListOf()
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

fun JsonReader.extractRawSteamApp(): RawSteamApp {
    val steamApp = RawSteamApp()
    while (hasNext()) {
        val name = nextName()
        when (name) {
            ResponseField.APP_ID.fieldName -> steamApp.id = nextLong()
            ResponseField.NAME.fieldName -> steamApp.name = nextString()
            ResponseField.AVERAGE.fieldName -> steamApp.averageTotal = nextInt()
            ResponseField.AVERAGE_2_WEEKS.fieldName -> steamApp.average2Weeks = nextInt()
            ResponseField.CCU.fieldName -> steamApp.ccu = nextInt()
            ResponseField.MEDIAN.fieldName -> steamApp.medianTotal = nextInt()
            ResponseField.MEDIAN_2_WEEKS.fieldName -> steamApp.median2Weeks = nextInt()
            ResponseField.NUM_OWNERS.fieldName -> steamApp.numOwners = nextInt()
            ResponseField.NUM_PLAYERS.fieldName -> steamApp.playersTotal = nextInt()
            ResponseField.NUM_PLAYERS_2_WEEKS.fieldName -> steamApp.players2Weeks = nextInt()
            ResponseField.OWNERS_VARIANCE.fieldName -> steamApp.ownersVariance = nextInt()
            ResponseField.PLAYERS_VARIANCE.fieldName -> steamApp.playersTotalVariance = nextInt()
            ResponseField.PLAYERS_2_WEEKS_VARIANCE.fieldName -> steamApp.players2WeeksVariance = nextInt()
            ResponseField.SCORE.fieldName -> steamApp.rank = safeNextInt()
            ResponseField.DEV.fieldName -> steamApp.dev = splitNextString()
            ResponseField.PUB.fieldName -> steamApp.pub = splitNextString()
            ResponseField.TAGS.fieldName -> steamApp.tags = extractTags()
            ResponseField.PRICE.fieldName -> steamApp.price = safeNextString()
        }
    }
    return steamApp
}

fun JsonReader.splitNextString(): MutableList<String> {
    val result: MutableList<String> = mutableListOf()
    result.addAll(safeNextString().split(","))
    val treamed: MutableList<String> = mutableListOf()
    result.mapTo(treamed, { it.trim(' ') })
    return treamed
}

fun JsonReader.extractTags(): MutableList<Tag> {
    return try {
        beginObject()
        val tags: MutableList<Tag> = mutableListOf()
        while (hasNext()) {
            tags.add(Tag(nextName(), nextInt()))
        }
        endObject()
        tags
    } catch (e: IllegalStateException) {
        beginArray()
        endArray()
        mutableListOf()
    }
}

fun JsonReader.safeNextInt(): Int {
    return try {
        nextInt()
    } catch (e: NumberFormatException) {
        nextString()
        -1
    }
}

fun JsonReader.safeNextString(): String {
    return try {
        nextString()
    } catch (e: IllegalStateException) {
        nextNull()
        ""
    }
}
