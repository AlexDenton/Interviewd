package io.github.alexdenton.interviewd.dashboard

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import io.github.alexdenton.interviewd.dashboard.questions.QuestionsFragment
import io.github.alexdenton.interviewd.R
import io.github.alexdenton.interviewd.dashboard.candidates.CandidatesFragment
import io.github.alexdenton.interviewd.dashboard.templates.TemplatesFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_questions -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.dashboard_fragmentContainer, QuestionsFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_templates -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.dashboard_fragmentContainer, TemplatesFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_candidates -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.dashboard_fragmentContainer, CandidatesFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.dashboard_fragmentContainer, PlaceholderFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportFragmentManager.beginTransaction()
                .add(R.id.dashboard_fragmentContainer, PlaceholderFragment())
                .commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_dashboard
    }
}