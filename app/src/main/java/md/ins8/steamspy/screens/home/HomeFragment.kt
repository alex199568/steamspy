package md.ins8.steamspy.screens.home

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_home.*
import md.ins8.steamspy.R
import md.ins8.steamspy.formatDecimal
import javax.inject.Inject

enum class HomeViewEvent {
    VIEW_CREATED
}

interface HomeView {
    val eventsObservable: Observable<HomeViewEvent>
    fun showAppsCount(count: Long)
}

class HomeFragment : Fragment(), HomeView {
    @Inject
    lateinit var presenter: HomePresenter

    override val eventsObservable: Subject<HomeViewEvent> = PublishSubject.create<HomeViewEvent>()

    override fun showAppsCount(count: Long) {
        val message = getString(R.string.appsCountMessage)
        val number = formatDecimal(count)
        appsCount.text = "$number $message"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerHomeComponent.builder().homeModule(HomeModule(this)).build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventsObservable.onNext(HomeViewEvent.VIEW_CREATED)
    }
}
