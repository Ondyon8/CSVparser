package com.example.csvparser

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.csvparser.Data.Api
import com.example.csvparser.Data.MainRepository
import com.example.csvparser.UI.MainActivity
import com.example.csvparser.UI.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @Rule
    @JvmField
    var mActivityTestRule : ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var api: Api
    lateinit var repository: MainRepository
    lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val intent = Intent()
        mActivityTestRule.launchActivity(intent)
    }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.csvparser", appContext.packageName)
    }

    @Test
    fun checkUI() {
        onView(withId(R.id.spinner)).check(ViewAssertions.matches(not(isDisplayed())))
        onView(withId(R.id.list)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.list)).check(ViewAssertions.matches(hasDescendant(withText("Title1"))))
        onView(withId(R.id.list)).check(ViewAssertions.matches(hasDescendant(withText("Title2"))))
        onView(withId(R.id.list)).check(ViewAssertions.matches(hasDescendant(withText("Value1"))))
        onView(withId(R.id.list)).check(ViewAssertions.matches(hasDescendant(withText("Value2"))))
    }
}