package md.ins8.steamspy.app_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialcab.MaterialCab
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_app_details.*
import kotlinx.android.synthetic.main.layout_dev_pub.*
import kotlinx.android.synthetic.main.layout_price.*
import kotlinx.android.synthetic.main.layout_rank.*
import kotlinx.android.synthetic.main.layout_statistics.*
import md.ins8.steamspy.R
import md.ins8.steamspy.RawSteamApp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


private val APP_ID_EXTRA = "AppIdExtra"

private val URL_START = "http://cdn.akamai.steamstatic.com/steam/apps/"
private val URL_END = "/header.jpg"

private val PLUS_MINUS = "\u00B1"

interface AppDetailsView {
    fun showApp(app: RawSteamApp)
}

fun formatDecimal(decimal: Int): String {
    val formatter = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val symbols = formatter.decimalFormatSymbols

    formatter.decimalFormatSymbols = symbols
    return formatter.format(decimal)
}

class AppDetailsActivity : AppCompatActivity(), AppDetailsView {
    @Inject
    lateinit var presenter: AppDetailsPresenter

    private lateinit var materialCab: MaterialCab

    override fun showApp(app: RawSteamApp) {
        Picasso.with(this)
                .load("$URL_START${app.id}$URL_END")
                .fit()
                .placeholder(R.drawable.app_placeholder)
                .into(appImage)
        appTitle.text = app.name

        developersList.text = app.dev.joinToString(", ")

        publishersList.text = app.pub.joinToString(", ")


        ownedRange.text = formatDecimal(app.numOwners)
        ownersVariance.text = "$PLUS_MINUS${formatDecimal(app.ownersVariance)}"

        totalPlayers.text = formatDecimal(app.playersTotal)
        playersTotalVariance.text = "$PLUS_MINUS${formatDecimal(app.playersTotalVariance)}"

        players2Weeks.text = formatDecimal(app.players2Weeks)
        players2WeeksVariance.text = "$PLUS_MINUS${formatDecimal(app.players2WeeksVariance)}"

        totalAverage.text = formatDecimal(app.averageTotal)
        average2Weeks.text = formatDecimal(app.average2Weeks)
        totalMedian.text = formatDecimal(app.medianTotal)
        median2Weeks.text = formatDecimal(app.median2Weeks)
        ccu.text = formatDecimal(app.ccu)

        tagsList.text = app.tags.joinToString(separator = ", ", transform = { it.name })

        rank.text = app.rank.toString()
        price.text = app.price
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_details)

        val appId = intent.getLongExtra(APP_ID_EXTRA, 0)
        DaggerAppDetailsComponent.builder().appDetailsModule(AppDetailsModule(this, appId)).build().inject(this)

        materialCab = MaterialCab(this, R.id.cab_stub)
                .start(object : MaterialCab.Callback {
                    override fun onCabFinished(cab: MaterialCab?): Boolean {
                        return true
                    }

                    override fun onCabItemClicked(item: MenuItem?): Boolean {
                        return true
                    }

                    override fun onCabCreated(cab: MaterialCab?, menu: Menu?): Boolean {
                        return true
                    }
                })
        materialCab.toolbar.setNavigationOnClickListener { onBackPressed() }
        materialCab.toolbar.title = "App Details"
    }
}

fun startAppDetailsActivity(context: Context, appId: Long) {
    val intent = Intent(context, AppDetailsActivity::class.java)
    intent.putExtra(APP_ID_EXTRA, appId)
    context.startActivity(intent)
}
