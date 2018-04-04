package md.ins8.steamspy

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GsonTest {
    private lateinit var gson: Gson
    private lateinit var json: String

    @Before
    fun setup() {
        val apiModule = SteamSpyAPIModule()
        gson = apiModule.provideGson()

        val stream = javaClass.classLoader.getResourceAsStream("apps.json")
        json = stream.bufferedReader().readText()
    }

    @Test
    fun testGson() {
        val typeToken = TypeToken.get(SteamAppsResponse::class.java)
        val steamApps = gson.fromJson<SteamAppsResponse>(json, typeToken.type)

        assertEquals(2, steamApps.apps.size)
    }
}
