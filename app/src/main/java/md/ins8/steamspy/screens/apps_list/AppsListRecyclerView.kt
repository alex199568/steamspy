package md.ins8.steamspy.screens.apps_list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_min_steam_app.view.*
import md.ins8.steamspy.MinSteamApp
import md.ins8.steamspy.R

class AppsListViewHolder(itemView: View?, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    fun bind(minSteamApp: MinSteamApp) {
        Picasso.with(context)
                .load(minSteamApp.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.appThumbnail)
        itemView.minAppName.text = minSteamApp.name
    }
}

class AppsListAdapter(private val apps: List<MinSteamApp>, private val context: Context) : RecyclerView.Adapter<AppsListViewHolder>() {
    override fun getItemCount(): Int {
        return apps.size
    }

    override fun onBindViewHolder(viewHolder: AppsListViewHolder?, position: Int) {
        viewHolder?.bind(apps[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppsListViewHolder {
        return AppsListViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_min_steam_app, parent, false),
                context
        )
    }
}