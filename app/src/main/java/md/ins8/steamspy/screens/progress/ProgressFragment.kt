package md.ins8.steamspy.screens.progress

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.R

enum class ProgressEvent {
    VIEW_CREATED
}

class ProgressFragment : Fragment() {
    val eventBus: Subject<ProgressEvent> = PublishSubject.create<ProgressEvent>()

    fun setMessage(message: String) {
        //progressText.text = message
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventBus.onNext(ProgressEvent.VIEW_CREATED)
    }
}