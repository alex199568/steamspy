package md.ins8.steamspy.screens.apps_list.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_apps_list.*
import md.ins8.steamspy.SteamAppItem
import md.ins8.steamspy.screens.apps_list.AppsListType
import md.ins8.steamspy.screens.apps_list.list.TopListAdapter

private val APPS_LIST_TYPE_NAME_EXTRA = "AppsListTypeExtra"
private val SEARCH_FOR_EXTRA = "SearchForExtra"


class TopListFragment : AppsListFragment() {
    override fun showAppsList(apps: List<SteamAppItem>) {
        val adapter = TopListAdapter(apps, context)
        adapter.itemClickObservable.subscribe { itemClickObservable.onNext(it) }

        appsListRecyclerView.visibility = View.VISIBLE
        appsListRecyclerView.adapter = adapter
        appsListRecyclerView.layoutManager = LinearLayoutManager(context)

        appsListProgressBar.visibility = View.GONE
        noResultsTextView.visibility = View.GONE
    }
}


fun newTopListFragmentInstance(appsListType: AppsListType): TopListFragment {
    val fragment = TopListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}