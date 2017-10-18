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
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_genre_steam_app.view.*
import kotlinx.android.synthetic.main.item_min_steam_app.view.*
import kotlinx.android.synthetic.main.item_top_steam_app.view.*
import md.ins8.steamspy.R
import md.ins8.steamspy.RawSteamApp
import md.ins8.steamspy.RealmSteamApp

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

class AppsListRealmAdapter(
        data: OrderedRealmCollection<RealmSteamApp>?,
        private val viewHolderProvider: DefaultAppsListViewHolderProvider,
        private val context: Context)
    : RealmRecyclerViewAdapter<RealmSteamApp, DefaultAppsListViewHolder>(data, true) {
    private val itemClickSubject: Subject<Long> = PublishSubject.create<Long>()

    val itemClickObservable: Observable<Long>
        get() = itemClickSubject

    override fun onBindViewHolder(viewHolder: DefaultAppsListViewHolder?, position: Int) {
        val realmSteamApp = getItem(position)
        if (realmSteamApp != null) {
            val steamApp = RawSteamApp(realmSteamApp)
            viewHolder?.bind(steamApp)
            viewHolder?.itemView?.setOnClickListener { itemClickSubject.onNext(steamApp.id) }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): DefaultAppsListViewHolder =
            viewHolderProvider.createViewHolder(p0, context)
}