package md.ins8.steamspy.screens.appslist

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
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_apps_list.*
import md.ins8.steamspy.*
import timber.log.Timber
import javax.inject.Inject

private const val APPS_LIST_TYPE_NAME_EXTRA = "AppsListTypeExtra"
private const val LIST_TYPE_EXTRA = "ListTypeExtra"
private const val SEARCH_FOR_EXTRA = "SearchForExtra"


enum class AppsListViewEvent {
    VIEW_CREATED,
    VIEW_DESTROYED
}

enum class AppsListType(val listTypeId: Int) {
    DEFAULT(0),
    TOP(1),
    GENRE(2)
}

interface AppsListView {
    val eventBus: Observable<AppsListViewEvent>
    val itemClickObservable: Observable<Long>

    val appsListContext: Context

    fun showEmptyList(message: String)

    fun showTopAppsList(apps: RealmResults<RealmSteamApp>)
    fun showGenreAppsList(apps: RealmResults<RealmSteamApp>)
    fun showAppsList(apps: RealmResults<RealmSteamApp>)
}


open class AppsListFragment : Fragment(), AppsListView {

    override val eventBus: Subject<AppsListViewEvent> = BehaviorSubject.create<AppsListViewEvent>()
    override val itemClickObservable: Subject<Long> = PublishSubject.create<Long>()

    override val appsListContext: Context
        get() = context


    override fun showTopAppsList(apps: RealmResults<RealmSteamApp>) {
        actuallyShowAppsList(apps, TopAppsListViewHolderProvider())
    }

    override fun showGenreAppsList(apps: RealmResults<RealmSteamApp>) {
        actuallyShowAppsList(apps, GenreAppsListViewHolderProvider())
    }

    override fun showAppsList(apps: RealmResults<RealmSteamApp>) {
        actuallyShowAppsList(apps, DefaultAppsListViewHolderProvider())
    }

    override fun showEmptyList(message: String) {
        appsListProgressBar.visibility = View.GONE
        appsListRecyclerView.visibility = View.GONE

        noResultsTextView.visibility = View.VISIBLE
        noResultsTextView.text = message
    }

    @Inject
    lateinit var presenter: AppsListPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        var searchFor = ""
        if (arguments.containsKey(SEARCH_FOR_EXTRA)) {
            searchFor = arguments.getString(SEARCH_FOR_EXTRA)
        }

        var appsListTypeId = AppsListType.DEFAULT.listTypeId
        if (arguments.containsKey(APPS_LIST_TYPE_NAME_EXTRA)) {
            appsListTypeId = arguments.getInt(APPS_LIST_TYPE_NAME_EXTRA)
        }

        var listTypeId = ListTypes.ALL.listType.id
        if (arguments.containsKey(LIST_TYPE_EXTRA)) {
            listTypeId = arguments.getInt(LIST_TYPE_EXTRA)
        }

        DaggerAppsListComponent.builder()
                .appComponent((activity.application as SteamSpyApp).appComponent)
                .appsListModule(AppsListModule(this, appsListTypeId, listTypeId, searchFor))
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

    override fun onDestroyView() {
        eventBus.onNext(AppsListViewEvent.VIEW_DESTROYED)
        super.onDestroyView()
    }


    private fun actuallyShowAppsList(apps: RealmResults<RealmSteamApp>,
                                     defaultAppsListViewHolderProvider: DefaultAppsListViewHolderProvider) {
        try {
            val adapter = AppsListRealmAdapter(apps, defaultAppsListViewHolderProvider, context)
            adapter.itemClickObservable.subscribe { itemClickObservable.onNext(it) }

            appsListRecyclerView.visibility = View.VISIBLE
            appsListRecyclerView.adapter = adapter
            appsListRecyclerView.layoutManager = LinearLayoutManager(context)

            appsListProgressBar.visibility = View.GONE
            noResultsTextView.visibility = View.GONE
        } catch (e: NullPointerException) {
            Timber.e(e)
        }
    }

}


fun newAppsListFragmentInstance(appsListType: AppsListType, listType: ListType): AppsListFragment {
    val fragment = AppsListFragment()

    val args = Bundle()
    args.putInt(APPS_LIST_TYPE_NAME_EXTRA, appsListType.listTypeId)
    args.putInt(LIST_TYPE_EXTRA, listType.id)
    fragment.arguments = args

    return fragment
}

fun newAppsListFragmentInstance(searchFor: String): AppsListFragment {
    val fragment = AppsListFragment()

    val args = Bundle()
    args.putString(SEARCH_FOR_EXTRA, searchFor)
    args.putInt(APPS_LIST_TYPE_NAME_EXTRA, AppsListType.DEFAULT.listTypeId)
    args.putInt(LIST_TYPE_EXTRA, ListTypes.ALL.listType.id)
    fragment.arguments = args

    return fragment
}

