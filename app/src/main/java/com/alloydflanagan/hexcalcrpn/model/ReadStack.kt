package com.alloydflanagan.hexcalcrpn.model

import java.lang.Exception

enum class BitsMode(private val numBits: Int) {
    EIGHT(8), SIXTEEN(16), THIRTY_TWO(32), SIXTY_FOUR(64), INFINITE(0);

    // required: toString() value should match button text (see [MainActivity.onCreate])
    override fun toString(): String {
        return if (numBits == 0) "\u221E" else numBits.toString()
    }

    /// Convert to a preferences setting
    fun toPreference(): BitsPreference = when(this) {
        EIGHT -> BitsPreference.EIGHT
        SIXTEEN -> BitsPreference.SIXTEEN
        THIRTY_TWO -> BitsPreference.THIRTY_TWO
        SIXTY_FOUR -> BitsPreference.SIXTY_FOUR
        INFINITE -> BitsPreference.INFINITE
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun fromString(mode: String): BitsMode = when (mode) {
            EIGHT.toString() -> EIGHT
            SIXTEEN.toString() -> SIXTEEN
            THIRTY_TWO.toString() -> THIRTY_TWO
            SIXTY_FOUR.toString() -> SIXTY_FOUR
            INFINITE.toString() -> INFINITE
            else -> throw Exception("BitsMode.fromString() got invalid argument $mode")
        }

        @JvmStatic
        fun fromPreference(pref: BitsPreference): BitsMode = when (pref) {
            BitsPreference.EIGHT -> EIGHT
            BitsPreference.SIXTEEN -> SIXTEEN
            BitsPreference.THIRTY_TWO -> THIRTY_TWO
            BitsPreference.SIXTY_FOUR -> SIXTY_FOUR
            BitsPreference.INFINITE -> INFINITE
            // if we get here some other code screwed up
            BitsPreference.PREVIOUS -> throw Exception("Don't know how to handle pref PREVIOUS")
        }
    }
}

/**
 * An interface to a stack of values. This implements only read operations.
 *
 * A [ReadStack] cannot change its contents (as long as [ReadStack.contents] is correctly
 * implemented).
 */

interface ReadStack<T> {
    val isEmpty: Boolean

    var bits: BitsMode

    /**
     * Returns the _current top of the stack, without altering the stack.
     */
    fun peek(): T?

    /**
     * True if the stack contains `o`.
     */
    operator fun contains(o: T): Boolean

    /**
     * True if the stack contains the value of o converted to type [T].
     *
     * Always false if no such conversion is possible.
     */
    operator fun contains(o: Int): Boolean

    /**
     * Number of elements on the stack.
     */
    val size: Int

    /**
     * A list of the contents of the stack, such that index 0 is top of stack.
     * Elements are copies, or references to immutable objects.
     */
    val contents: List<T>

    /**
     * Like [toString], but puts spaces between each group of four digits
     */
    fun toSpacedString(): String
}
