package md.ins8.steamspy.screens.apps_list

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.item_genre_steam_app.view.*
import kotlinx.android.synthetic.main.item_top_steam_app.view.*
import md.ins8.steamspy.GenreSteamAppItem
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamAppItem
import org.jetbrains.anko.*

private enum class MinItemViewIds {
    APP_THUMBNAIL,
    APP_NAME
}

class AppsListViewHolder(itemView: View?, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    fun bind(steamAppItem: SteamAppItem) {
        Picasso.with(context)
                .load(steamAppItem.imgUrl)
                .placeholder(R.drawable.app_thumbnail_placeholder)
                .fit().centerInside()
                .into(itemView.find<ImageView>(MinItemViewIds.APP_THUMBNAIL.ordinal))
        itemView.find<TextView>(MinItemViewIds.APP_NAME.ordinal).text = steamAppItem.name
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

class AppsListAdapter(private val apps: List<SteamAppItem>, private val context: Context) : RecyclerView.Adapter<AppsListViewHolder>() {
    private val itemClickSubject: Subject<Long> = PublishSubject.create<Long>()

    val itemClickObservable: Observable<Long>
        get() = itemClickSubject

    override fun getItemCount(): Int {
        return apps.size
    }

    override fun onBindViewHolder(viewHolder: AppsListViewHolder?, position: Int) {
        viewHolder?.bind(apps[position])
        viewHolder?.itemView?.setOnClickListener { itemClickSubject.onNext(apps[position].id) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppsListViewHolder {

        val itemView: View = context.linearLayout {
            imageView {
                id = MinItemViewIds.APP_THUMBNAIL.ordinal
            }.lparams(width = dimen(R.dimen.steamAppCapsuleItemWidth), height = dimen(R.dimen.steamAppCapsuleItemHeight)) {
                leftMargin = dimen(R.dimen.listItemSideMargin)
                verticalGravity = Gravity.CENTER_VERTICAL
            }

            verticalLayout {
                textView {
                    id = MinItemViewIds.APP_NAME.ordinal
                    textSize = 16f
                    ellipsize = TextUtils.TruncateAt.END
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = ContextCompat.getColor(context, R.color.onBackgroundPrimaryTextColor)
                }.lparams(width = matchParent, height = 0, weight = 99f)

                view {
                    backgroundColor = R.color.colorListItemDivider
                }.lparams(width = matchParent, height = 1)
            }.lparams(width = matchParent, height = matchParent) {
                leftMargin = dimen(R.dimen.listItemSingleLineTextMargin)
                rightMargin = dimen(R.dimen.listItemSideMargin)
            }

            lparams(height = dimen(R.dimen.listItemSingleLineHeight), width = matchParent)
        }

        return AppsListViewHolder(
                itemView,
                context
        )
    }
}

class TopListAdapter(private val apps: List<SteamAppItem>, private val context: Context) : RecyclerView.Adapter<TopListViewHolder>() {
    private val itemClickSubject: Subject<Long> = PublishSubject.create<Long>()

    val itemClickObservable: Observable<Long>
        get() = itemClickSubject

    override fun onBindViewHolder(viewHolder: TopListViewHolder?, position: Int) {
        viewHolder?.bind(apps[position], position + 1)
        viewHolder?.itemView?.setOnClickListener { itemClickSubject.onNext(apps[position].id) }
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

class GenreListAdapter(private val apps: List<GenreSteamAppItem>, private val context: Context) : RecyclerView.Adapter<GenreListViewHolder>() {
    private val itemClickSubject: Subject<Long> = PublishSubject.create<Long>()

    val itemClickObservable: Observable<Long>
        get() = itemClickSubject

    override fun onBindViewHolder(p0: GenreListViewHolder?, p1: Int) {
        p0?.bind(apps[p1])
        p0?.itemView?.setOnClickListener { itemClickSubject.onNext(apps[p1].steamAppItem.id) }
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): GenreListViewHolder {
        return GenreListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_genre_steam_app, p0, false), context)
    }

}