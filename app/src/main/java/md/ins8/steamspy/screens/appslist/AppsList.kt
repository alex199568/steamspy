package md.ins8.steamspy.screens.appslist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.item_genre_steam_app.view.*
import kotlinx.android.synthetic.main.item_min_steam_app.view.*
import kotlinx.android.synthetic.main.item_top_steam_app.view.*
import md.ins8.steamspy.R
import md.ins8.steamspy.RawSteamApp

open class DefaultAppsListViewHolder(itemView: View?, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    open fun bind(steamApp: RawSteamApp) {
        Picasso.with(context)
                .load(steamApp.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.appThumbnail)
        itemView.appName.text = steamApp.name
    }
}

class TopAppsListViewHolder(itemView: View?, private val context: Context) : DefaultAppsListViewHolder(itemView, context) {
    override fun bind(steamApp: RawSteamApp) {
        Picasso.with(context)
                .load(steamApp.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.topAppThumbnail)
        itemView.topAppName.text = steamApp.name
        itemView.topAppPosition.text = "#${adapterPosition + 1}"

    }
}

class GenreAppsListViewHolder(itemView: View?, private val context: Context) : DefaultAppsListViewHolder(itemView, context) {
    override fun bind(steamApp: RawSteamApp) {
        Picasso.with(context)
                .load(steamApp.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.genreAppThumbnail)
        itemView.genreAppName.text = steamApp.name
        itemView.genreAppItemTags.text = steamApp.tagsText

    }
}

open class DefaultAppsListViewHolderProvider {
    open fun createViewHolder(parent: ViewGroup?, context: Context): DefaultAppsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_min_steam_app, parent, false)
        return DefaultAppsListViewHolder(view, context)
    }
}

class TopAppsListViewHolderProvider : DefaultAppsListViewHolderProvider() {
    override fun createViewHolder(parent: ViewGroup?, context: Context): DefaultAppsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_top_steam_app, parent, false)
        return TopAppsListViewHolder(view, context)
    }
}

class GenreAppsListViewHolderProvider : DefaultAppsListViewHolderProvider() {
    override fun createViewHolder(parent: ViewGroup?, context: Context): DefaultAppsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_genre_steam_app, parent, false)
        return GenreAppsListViewHolder(view, context)
    }
}

class AppsListAdapter(private val apps: List<RawSteamApp>, private val context: Context,
                      private val viewHolderProvider: DefaultAppsListViewHolderProvider)
    : RecyclerView.Adapter<DefaultAppsListViewHolder>() {
    private val itemClickSubject: Subject<Long> = PublishSubject.create<Long>()

    val itemClickObservable: Observable<Long>
        get() = itemClickSubject

    override fun getItemCount(): Int = apps.size

    override fun onBindViewHolder(viewHolder: DefaultAppsListViewHolder?, position: Int) {
        viewHolder?.bind(apps[position])
        viewHolder?.itemView?.setOnClickListener { itemClickSubject.onNext(apps[position].id) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DefaultAppsListViewHolder =
            viewHolderProvider.createViewHolder(parent, context)
}