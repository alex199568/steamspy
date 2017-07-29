package md.ins8.steamspy.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import md.ins8.steamspy.R
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

        DaggerMainComponent.builder().mainModule(MainModule(this)).build().inject(this)
    }
}
