package md.ins8.steamspy.screens.apps_list

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_apps_list.*
import md.ins8.steamspy.R
import md.ins8.steamspy.screens.apps_list.di.AppsListModule
import md.ins8.steamspy.screens.apps_list.di.DaggerAppsListComponent
import md.ins8.steamspy.screens.apps_list.mvp.AppsListPresenter
import javax.inject.Inject

private val APPS_LIST_TYPE_NAME_EXTRA = "AppsListTypeExtra"

class AppsListFragment : Fragment() {
    private lateinit var appsListType: AppsListType

    @Inject
    lateinit var presenter: AppsListPresenter

    val viewEventBus = BehaviorSubject.create<Boolean>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerAppsListComponent.builder().appsListModule(AppsListModule(this)).build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_apps_list, container, false) as View

        val appsListTypeName = arguments.getString(APPS_LIST_TYPE_NAME_EXTRA)
        appsListType = AppsListType.valueOf(appsListTypeName)
        presenter.start(appsListType)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewEventBus.onNext(true)
    }

    fun setData(data: String) {
        tempData.text = data
    }
}

fun newAppsListFragmentInstance(appsListType: AppsListType): AppsListFragment {
    val fragment = AppsListFragment()

    val args = Bundle()
    args.putString(APPS_LIST_TYPE_NAME_EXTRA, appsListType.name)
    fragment.arguments = args

    return fragment
}
