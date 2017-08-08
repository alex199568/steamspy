package md.ins8.steamspy.screens.apps_list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_min_steam_app.view.*
import kotlinx.android.synthetic.main.item_top_steam_app.view.*
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamAppItem

open class AppsListViewHolder(itemView: View?, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    fun bind(steamAppItem: SteamAppItem) {
        Picasso.with(context)
                .load(steamAppItem.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.appThumbnail)
        itemView.appName.text = steamAppItem.name
    }
}

class TopListViewHolder(itemView: View?, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    fun bind(steamAppItem: SteamAppItem, position: Int) {
        Picasso.with(context)
                .load(steamAppItem.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.topAppThumbnail)
        itemView.topAppName.text = steamAppItem.name
        itemView.topAppPosition.text = "#$position"
    }
}

class AppsListAdapter(private val apps: List<SteamAppItem>, private val context: Context) : RecyclerView.Adapter<AppsListViewHolder>() {
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

class TopListAdapter(private val apps: List<SteamAppItem>, private val context: Context) : RecyclerView.Adapter<TopListViewHolder>() {
    override fun onBindViewHolder(p0: TopListViewHolder?, p1: Int) {
        p0?.bind(apps[p1], p1 + 1)
    }

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): TopListViewHolder {
        return TopListViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_top_steam_app, p0, false),
                context
        )
    }

    override fun getItemCount(): Int {
        return apps.size
    }
}