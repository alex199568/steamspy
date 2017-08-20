package md.ins8.steamspy.launch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamSpyApp
import md.ins8.steamspy.main.startMainActivity
import javax.inject.Inject

interface LaunchView {
    fun startMainActivity()
}


class LaunchActivity : AppCompatActivity(), LaunchView {
    @Inject
    lateinit var presenter: LaunchPresenter

    override fun startMainActivity() {
        startMainActivity(this)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    private fun inject() {
        DaggerLaunchComponent.builder()
                .appComponent((application as SteamSpyApp).appComponent)
                .launchModule(LaunchModule(this))
                .build().inject(this)
    }
}
