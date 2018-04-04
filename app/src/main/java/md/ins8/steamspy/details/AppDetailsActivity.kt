package md.ins8.steamspy.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialcab.MaterialCab
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.layout_app_main_info.*
import kotlinx.android.synthetic.main.layout_average.*
import kotlinx.android.synthetic.main.layout_average_2w.*
import kotlinx.android.synthetic.main.layout_ccu.*
import kotlinx.android.synthetic.main.layout_developers.*
import kotlinx.android.synthetic.main.layout_median.*
import kotlinx.android.synthetic.main.layout_median_2w.*
import kotlinx.android.synthetic.main.layout_owners.*
import kotlinx.android.synthetic.main.layout_players.*
import kotlinx.android.synthetic.main.layout_players_2w.*
import kotlinx.android.synthetic.main.layout_price.*
import kotlinx.android.synthetic.main.layout_publishers.*
import kotlinx.android.synthetic.main.layout_rank.*
import md.ins8.steamspy.BaseActivity
import md.ins8.steamspy.R
import md.ins8.steamspy.RawSteamApp
import md.ins8.steamspy.formatDecimal
import javax.inject.Inject


private val APP_ID_EXTRA = "AppIdExtra"

private val URL_START = "http://cdn.akamai.steamstatic.com/steam/apps/"
private val URL_END = "/header.jpg"

private val PLUS_MINUS = "\u00B1"

enum class ViewEvent {
    EXPAND_BTN_CLICK, CREATED
}

interface AppDetailsView {
    val viewEvents: Observable<ViewEvent>

    fun showApp(app: RawSteamApp)
}

fun choosePriceUnits(): String = "Units"

class AppDetailsActivity : BaseActivity(), AppDetailsView {
    @Inject
    lateinit var presenter: AppDetailsPresenter

    private lateinit var materialCab: MaterialCab

    override val viewEvents: Subject<ViewEvent> = PublishSubject.create<ViewEvent>()

    override fun showApp(app: RawSteamApp) {
        Picasso.with(this)
                .load("$URL_START${app.id}$URL_END")
                .fit()
                .placeholder(R.drawable.app_placeholder)
                .into(appImage)
        appName.text = app.name
        developersTextView.text = app.dev.joinToString()
        publishersTextView.text = app.pub.joinToString()
        rankTextView.text = "${app.rank} %"
        priceTextView.text = "${app.price} ${choosePriceUnits()}"

        ownersNumber.text = formatDecimal(app.numOwners)
        ownersVariance.text = "$PLUS_MINUS${formatDecimal(app.ownersVariance)}"

        ccuTextView.text = formatDecimal(app.ccu)

        playersNumber.text = formatDecimal(app.playersTotal)
        playersVariance.text = "$PLUS_MINUS${formatDecimal(app.playersTotalVariance)}"

        players2WNumber.text = formatDecimal(app.players2Weeks)
        players2WVariance.text = "$PLUS_MINUS${formatDecimal(app.players2WeeksVariance)}"

        averageTextView.text = formatDecimal(app.averageTotal)
        average2WTextView.text = formatDecimal(app.average2Weeks)

        medianTextView.text = formatDecimal(app.medianTotal)
        median2WTextView.text = formatDecimal(app.median2Weeks)
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
        materialCab.toolbar.title = getString(R.string.appDetailsTitle)

        viewEvents.onNext(ViewEvent.CREATED)
    }
}

fun startAppDetailsActivity(context: Context, appId: Long) {
    val intent = Intent(context, AppDetailsActivity::class.java)
    intent.putExtra(APP_ID_EXTRA, appId)
    context.startActivity(intent)
}
