package io.github.alexdenton.interviewd.dashboard.interviews

import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.PublishRelay
import io.github.alexdenton.interviewd.api.InterviewRepository
import io.github.alexdenton.interviewd.createinterview.CreateInterviewActivity
import io.github.alexdenton.interviewd.interview.Interview
import io.github.rfonzi.rxaware.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ryan on 9/25/17.
 */
class InterviewsViewModel : BaseViewModel() {

    private lateinit var interviewRepo: InterviewRepository
    private val interviews: PublishRelay<List<Interview>> = PublishRelay.create()

    fun getInterviewsObservable(): Observable<List<Interview>> = interviews

    fun initWith(kodein: LazyKodein) {
        interviewRepo = kodein.invoke().instance()
        fetchInterviews()
    }

    fun fetchInterviews() = interviewRepo.getAllInterviews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list -> interviews.accept(list) },
                    { throwable -> throwable.printStackTrace() })
            .lifecycleAware()

    fun exposeAddFab(clicks: Observable<Unit>) = clicks
            .subscribe { startActivity(CreateInterviewActivity::class.java) }
}