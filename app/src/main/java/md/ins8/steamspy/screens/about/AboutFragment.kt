package md.ins8.steamspy.screens.about

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_about.*
import md.ins8.steamspy.R
import md.ins8.steamspy.retrieveVersionName
import javax.inject.Inject

enum class ViewEvent {
    VIEW_CREATED
}

interface AboutView {
    val viewObservable: Observable<ViewEvent>

    fun showLastDataUpdateTime(lastDataUpdate: String)
}

class AboutFragment : Fragment(), AboutView {
    override val viewObservable: Subject<ViewEvent> = PublishSubject.create<ViewEvent>()

    @Inject
    lateinit var presenter: AboutPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        version.text = retrieveVersionName(context)
        viewObservable.onNext(ViewEvent.VIEW_CREATED)
    }

    override fun showLastDataUpdateTime(lastDataUpdate: String) {
        lastDataUpdateTextView.text = lastDataUpdate
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerAboutComponent.builder().aboutModule(AboutModule(this)).build().inject(this)
    }
}

