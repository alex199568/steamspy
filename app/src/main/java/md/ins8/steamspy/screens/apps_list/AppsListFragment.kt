package md.ins8.steamspy.screens.apps_list

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_apps_list.*
import md.ins8.steamspy.GenreSteamAppItem
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamAppItem
import md.ins8.steamspy.app.SteamSpyApp
import md.ins8.steamspy.screens.apps_list.di.AppsListModule
import md.ins8.steamspy.screens.apps_list.di.DaggerAppsListComponent
import md.ins8.steamspy.screens.apps_list.mvp.AppsListPresenter
import timber.log.Timber
import javax.inject.Inject

private val APPS_LIST_TYPE_NAME_EXTRA = "AppsListTypeExtra"

enum class AppsListViewEvent {
    VIEW_CREATED
}

interface AppsListView {
    val eventBus: Observable<AppsListViewEvent>
    val itemClickObservable: Observable<Long>

    val appsListContext: Context

    fun showAppsList(apps: List<SteamAppItem>)
    fun showGenreAppsList(apps: List<GenreSteamAppItem>)
}


open class AppsListFragment : Fragment(), AppsListView {
    override val eventBus: Subject<AppsListViewEvent> = BehaviorSubject.create<AppsListViewEvent>()
    override val itemClickObservable: Subject<Long> = PublishSubject.create<Long>()

    override val appsListContext: Context
        get() = context

    override fun showAppsList(apps: List<SteamAppItem>) {
        val adapter = AppsListAdapter(apps, context)
        adapter.itemClickObservable.subscribe { itemClickObservable.onNext(it) }

        appsListRecyclerView.adapter = adapter
        appsListRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun showGenreAppsList(apps: List<GenreSteamAppItem>) {
        Timber.e("Should not be called")
    }

    @Inject
    lateinit var presenter: AppsListPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val appsListTypeName = arguments.getString(APPS_LIST_TYPE_NAME_EXTRA)
        val appsListType = AppsListType.valueOf(appsListTypeName)

        DaggerAppsListComponent.builder()
                .appComponent((activity.application as SteamSpyApp).appComponent)
                .appsListModule(AppsListModule(this, appsListType))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_apps_list, container, false) as View

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventBus.onNext(AppsListViewEvent.VIEW_CREATED)
    }
}

class TopListFragment : AppsListFragment() {
    override fun showAppsList(apps: List<SteamAppItem>) {
        val adapter = TopListAdapter(apps, context)
        adapter.itemClickObservable.subscribe { itemClickObservable.onNext(it) }

        appsListRecyclerView.adapter = adapter
        appsListRecyclerView.layoutManager = LinearLayoutManager(context)
    }
}

class GenreListFragment : AppsListFragment() {
    override fun showGenreAppsList(apps: List<GenreSteamAppItem>) {
        Timber.i(apps.toString())

        val adapter = GenreListAdapter(apps, context)
        adapter.itemClickObservable.subscribe { itemClickObservable.onNext(it) }

        appsListRecyclerView.adapter = adapter
        appsListRecyclerView.layoutManager = LinearLayoutManager(context)
    }
}


fun newAppsListFragmentInstance(appsListType: AppsListType): AppsListFragment {
    val fragment = AppsListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}

fun newTopListFragmentInstance(appsListType: AppsListType): TopListFragment {
    val fragment = TopListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}

fun newGenreListFragmentInstance(appsListType: AppsListType): GenreListFragment {
    val fragment = GenreListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}