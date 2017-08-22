package md.ins8.steamspy.app_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialcab.MaterialCab
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_app_details.*
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

private val INFO_VIEWPAGER_PADDING = 16

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
        appName.text = app.name

        val infoAdapter = AppInfoPagerAdapter(supportFragmentManager, this, app)
        appDetailsViewPager.apply {
            adapter = infoAdapter
            clipToPadding = false
            setPadding(INFO_VIEWPAGER_PADDING, INFO_VIEWPAGER_PADDING, INFO_VIEWPAGER_PADDING, INFO_VIEWPAGER_PADDING)
            pageMargin = INFO_VIEWPAGER_PADDING
        }
        tabs.setupWithViewPager(appDetailsViewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_details)

        val appId = intent.getLongExtra(APP_ID_EXTRA, 0)
        DaggerAppDetailsComponent.builder().appDetailsModule(AppDetailsModule(this, appId)).build().inject(this)

        materialCab = MaterialCab(this, R.id.cab_stub)
                .start(object : MaterialCab.Callback {
                    override fun onCabFinished(cab: MaterialCab?): Boolean = true

                    override fun onCabItemClicked(item: MenuItem?): Boolean = true

                    override fun onCabCreated(cab: MaterialCab?, menu: Menu?): Boolean = true
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
