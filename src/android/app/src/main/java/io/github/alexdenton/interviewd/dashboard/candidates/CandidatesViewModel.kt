package io.github.alexdenton.interviewd.dashboard.candidates

import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxrelay2.PublishRelay
import io.github.alexdenton.interviewd.api.CandidateRepository
import io.github.alexdenton.interviewd.createcandidate.CreateCandidateActivity
import io.github.alexdenton.interviewd.interview.Candidate
import io.github.rfonzi.rxaware.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ryan on 9/25/17.
 */
class CandidatesViewModel : BaseViewModel() {

    lateinit var candidatesRepo: CandidateRepository
    private val candidates: PublishRelay<List<Candidate>> = PublishRelay.create()

    fun getCandidatesObservable(): Observable<List<Candidate>> = candidates

    fun initWith(kodein: LazyKodein){
        candidatesRepo = kodein.invoke().instance()
        fetchCandidates()
    }

    fun fetchCandidates() = candidatesRepo.getAllCandidates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ candidates.accept(it) },
                    { throwable -> throwable.printStackTrace() })

    fun exposeAddFab(clicks: Observable<Unit>) = clicks
            .subscribe { startActivity(CreateCandidateActivity::class.java) }

}