package md.ins8.steamspy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val navigationOptions = listOf(
            "All", "Top", "Genre", "Watchlist"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationList.adapter = ArrayAdapter<String>(this, R.layout.drawer_list_item, navigationOptions)
        navigationLayout.setStatusBarBackgroundColor(android.R.color.holo_red_dark)

        navigationList.onItemClickListener = AdapterView.OnItemClickListener {
            p0, p1, p2, p3 ->
            Toast.makeText(this, "Item clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
