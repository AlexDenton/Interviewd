package io.github.alexdenton.interviewd.interview.conduct

import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.PublishRelay
import io.github.alexdenton.interviewd.api.repositories.InterviewRepository
import io.github.alexdenton.interviewd.entities.Interview
import io.github.rfonzi.rxaware.RxAwareViewModel
import io.reactivex.Observable

/**
 * Created by ryan on 9/27/17.
 */
class ConductInterviewViewModel : RxAwareViewModel() {

    private lateinit var interviewRepo: InterviewRepository
    lateinit var interview: Interview

    var currentPage = 0
    var inProgress = false
    private val nextQuestionString: PublishRelay<String> = PublishRelay.create()
    private val startSignal: PublishRelay<InterviewSignal> = PublishRelay.create()
    private val nextPageSignal: PublishRelay<Int> = PublishRelay.create()

    fun getNextQuestionStringObservable(): Observable<String> = nextQuestionString
    fun getNextPageSignal(): Observable<Int> = nextPageSignal
    fun getStartSignal(): Observable<InterviewSignal> = startSignal

    fun initWith(kodein: LazyKodein){
        interviewRepo = kodein.invoke().instance()
    }

    fun useInterview(interview: Interview) {
        this.interview = interview
    }

    fun exposeCurrentPage(pageSelections: Observable<Int>) = pageSelections
            .subscribe {
                when(it){
                    interview.questions.size - 1 -> nextQuestionString.accept("Finish interview")
                    else -> nextQuestionString.accept(interview.questions[it + 1].name)
                }
                currentPage = it
            }
            .lifecycleAware()

    fun exposeNextClicks(clicks: Observable<Unit>) = clicks
            .subscribe {
                if(currentPage == interview.questions.size - 1) {
                    toast("Finished the interview")
                    navigateUp()
                }
                else {
                    nextPageSignal.accept(currentPage + 1)
                }
            }
            .lifecycleAware()

    fun exposeStartClicks(clicks: Observable<Unit>) = clicks
            .subscribe {
                startSignal.accept(InterviewSignal.START)
                inProgress = true
            }
            .lifecycleAware()
}