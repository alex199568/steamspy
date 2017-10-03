package md.ins8.steamspy.screens.appslist.list

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
import md.ins8.steamspy.GenreSteamAppItem
import md.ins8.steamspy.R

private enum class GenreListItemViewId {
    APP_THUMBNAIL,
    APP_NAME,
    GENRE_TAGS
}

class GenreListViewHolder(itemView: View?, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    fun bind(genreSteamAppItem: GenreSteamAppItem) {
        Picasso.with(context)
                .load(genreSteamAppItem.steamAppItem.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.genreAppThumbnail)
        itemView.genreAppName.text = genreSteamAppItem.steamAppItem.name
        itemView.genreAppItemTags.text = genreSteamAppItem.tags
    }
}


class GenreListAdapter(private val apps: List<GenreSteamAppItem>, private val context: Context) : RecyclerView.Adapter<GenreListViewHolder>() {
    private val itemClickSubject: Subject<Long> = PublishSubject.create<Long>()

    val itemClickObservable: Observable<Long>
        get() = itemClickSubject

    override fun onBindViewHolder(p0: GenreListViewHolder?, p1: Int) {
        p0?.bind(apps[p1])
        p0?.itemView?.setOnClickListener { itemClickSubject.onNext(apps[p1].steamAppItem.id) }
    }

    override fun getItemCount(): Int = apps.size

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): GenreListViewHolder {
        return GenreListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_genre_steam_app, p0, false), context)
    }

}