package com.alloydflanagan.hexcalcrpn.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity8Test:  MainActivityUI(), MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * Sets bit mode, preferences to consistent values.
     */
    @BeforeTest
    fun setup() {
        enterKeys("z") // 8-bit mode
        testSetup()
    }

    /**
     * Verify that pressing a series of digits creates a number in the current value register,
     * and that pressing "clear" resets that number to 0.
     */
    @Suppress("SpellCheckingInspection")
    @Test
    override fun testEntry() {
        enterKeys("8")
        checkCurrentIs("8")
        enterKeys("FA6540123456789ABCDEF")

        checkCurrentIs("8F A654 0123 4567 89AB CDEF")

        enterKeys("c")
        checkCurrentIs("0")
    }

    /**
     * Verify that we can do simple addition.
     */
    @Suppress("SpellCheckingInspection")
    @Test
    override fun testAdd() {
        enterKeys("1234")
        enter()
        checkOutputIs("34")

        enterKeys("ABCD")  // "CD"
        enter()

        enterKeys("+")

        checkOutputIs("1") // 101 truncated
    }

    /**
     * Verify that we can do simple subtraction.
     */
    @Test
    override fun testSubtract() {
        enterKeys("ABC")  // BC
        enter()

        enterKeys("12")
        enter()
        enterKeys("-")

        checkOutputIs("AA")

        enterKeys("BC")
        enter()
        enterKeys("-")
        // result is -18
        // 18 ==> 10010
        // 8-bit complement ==> 11101101
        // two's complement ==> 11101110
        checkOutputIs("EE")
    }

    @Test
    override fun testMultiply() {
        enterKeys("AC57")
        enter()

        enterKeys("FACE")
        enter()

        checkOutputIs("57\nCE")

        enterKeys("*")
        checkOutputIs("2")
    }

    @Test
    override fun testDivide() {
        enterKeys("A488")
        enter()
        enterKeys("2")
        enter()

        checkOutputIs("88\n2")

        enterKeys("/")
        checkOutputIs("44")
    }

    @Test
    override fun testAND() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("CE\n57")

        enterKeys("&")
        checkOutputIs("46")
    }

    @Test
    override fun testOR() {
        enterKeys("FACE")
        enter()
        enterKeys("AC57")
        enter()

        checkOutputIs("CE\n57")

        enterKeys("|")
        checkOutputIs("DF")
    }

    /**
     * Verify that pressing "c" clears the current value if any, otherwise removes one entry from
     * stack.
     */
    @Test
    override fun testClear() {
        enterKeys("1234")
        enter()
        checkCurrentIs("0")
        checkOutputIs("34")

        enterKeys("ABC")
        enter()
        checkCurrentIs("0")
        checkOutputIs("34\nBC")

        enterKeys("747")
        checkCurrentIs("747")

        enterKeys("c")

        checkCurrentIs("0")
        checkOutputIs("34\nBC")

        enterKeys("c")

        checkOutputIs("34")

        enterKeys("c")

        checkOutputIs("")
    }

    /**
     * Verify pressing "clear" with no input and no stack does nothing.
     */
    @Test
    override fun testClearNull() {
        enterKeys("c") // should do nothing
        checkOutputIs("")
        checkCurrentIs("0")
    }

    @Suppress("SpellCheckingInspection")
    @Test
    override fun testInvert() {
        enterKeys("ABCD")
        enter()
        checkOutputIs("CD")
        enterKeys("~")
        checkOutputIs("32")
        enterKeys("~")
        checkOutputIs("CD")
        enterKeys("c")
        enter()
        checkOutputIs("0")
        enterKeys("~")
        checkOutputIs("FF")
    }

    @Test
    override fun test2sComplement() {
        @Suppress("SpellCheckingInspection")
        enterKeys("ABCD")
        enter()
        checkOutputIs("CD")
        enterKeys("s")
        checkOutputIs("33")
        enterKeys("s")
        checkOutputIs("CD")
        enterKeys("c")
        enter()
        checkOutputIs("0")
        enterKeys("s")
        checkOutputIs("0")
    }

    @Test
    override fun testInvertCurrent() {
        @Suppress("SpellCheckingInspection")
        enterKeys("ABCD")
        enterKeys("~")
        checkCurrentIs("32")
        checkOutputIs("")
        enterKeys("~")
        checkCurrentIs("CD")
        checkOutputIs("")
        enterKeys("c")
        checkCurrentIs("0")
        checkOutputIs("")
        enterKeys("0~")
        checkCurrentIs("FF")
        checkOutputIs("")
    }

    @Test
    override fun test2sCompCurrent() {
        @Suppress("SpellCheckingInspection")
        enterKeys("ABCD")
        enterKeys("s")
        checkCurrentIs("33")
        enterKeys("s")
        checkCurrentIs("CD")
        enterKeys("c")
        checkCurrentIs("0")
        enterKeys("s")
        checkCurrentIs("0")
        checkOutputIs("")
    }
}
