package io.github.alexdenton.interviewd.entities

/**
 * Created by ryan on 7/24/17.
 */
data class Interview(
        val id: Long = 0,
        val candidate: Candidate,
        val name: String,
        val questions: List<Question>,
        var status: InterviewStatus = InterviewStatus.NOT_COMPLETE
)