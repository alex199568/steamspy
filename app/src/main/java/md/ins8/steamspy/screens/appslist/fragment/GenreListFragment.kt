package md.ins8.steamspy.screens.appslist.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_apps_list.*
import md.ins8.steamspy.GenreSteamAppItem
import md.ins8.steamspy.screens.appslist.AppsListType
import md.ins8.steamspy.screens.appslist.list.GenreListAdapter

private const val APPS_LIST_TYPE_NAME_EXTRA = "AppsListTypeExtra"


class GenreListFragment : AppsListFragment() {
    override fun showGenreAppsList(apps: List<GenreSteamAppItem>) {
        val adapter = GenreListAdapter(apps, context)
        adapter.itemClickObservable.subscribe { itemClickObservable.onNext(it) }

        appsListRecyclerView.visibility = View.VISIBLE
        appsListRecyclerView.adapter = adapter
        appsListRecyclerView.layoutManager = LinearLayoutManager(context)

        appsListProgressBar.visibility = View.GONE
        noResultsTextView.visibility = View.GONE
    }
}

fun newGenreListFragmentInstance(appsListType: AppsListType): GenreListFragment {
    val fragment = GenreListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}