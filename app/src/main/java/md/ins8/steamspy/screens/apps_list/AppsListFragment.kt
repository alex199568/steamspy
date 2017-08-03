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
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_apps_list.*
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamAppItem
import md.ins8.steamspy.screens.apps_list.di.AppsListModule
import md.ins8.steamspy.screens.apps_list.di.DaggerAppsListComponent
import md.ins8.steamspy.screens.apps_list.mvp.AppsListPresenter
import javax.inject.Inject

private val APPS_LIST_TYPE_NAME_EXTRA = "AppsListTypeExtra"

enum class AppsListViewEvent {
    VIEW_CREATED
}

interface AppsListView {
    val eventBus: Observable<AppsListViewEvent>

    fun showAppsList(apps: List<SteamAppItem>)
}


class AppsListFragment : Fragment(), AppsListView {
    override val eventBus: Subject<AppsListViewEvent> = BehaviorSubject.create<AppsListViewEvent>()

    override fun showAppsList(apps: List<SteamAppItem>) {
        val adapter = AppsListAdapter(apps, context)

        appsListRecyclerView.adapter = adapter
        appsListRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    @Inject
    lateinit var presenter: AppsListPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val appsListTypeName = arguments.getString(APPS_LIST_TYPE_NAME_EXTRA)
        val appsListType = AppsListType.valueOf(appsListTypeName)

        DaggerAppsListComponent.builder().appsListModule(AppsListModule(this, appsListType)).build().inject(this)
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


fun newAppsListFragmentInstance(appsListType: AppsListType): AppsListFragment {
    val fragment = AppsListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}
