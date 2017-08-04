package md.ins8.steamspy.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import md.ins8.steamspy.R
import md.ins8.steamspy.app.SteamSpyApp
import md.ins8.steamspy.main.di.DaggerMainComponent
import md.ins8.steamspy.main.di.MainModule
import md.ins8.steamspy.main.mvp.MainPresenter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerMainComponent.builder()
                .appComponent((application as SteamSpyApp).appComponent)
                .mainModule(MainModule(this)).build().inject(this)
    }
}

fun startMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}
