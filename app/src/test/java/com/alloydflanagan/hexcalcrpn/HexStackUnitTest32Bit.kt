package com.alloydflanagan.hexcalcrpn

import com.alloydflanagan.hexcalcrpn.model.BitsMode
import com.alloydflanagan.hexcalcrpn.model.HexStack
import java.math.BigInteger
import kotlin.test.*

/**
 * Test the utility class HexStack.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HexStackUnitTest32Bit {

    private lateinit var stack: HexStack

    @BeforeTest
    fun setup() {
        stack = HexStack(BitsMode.THIRTY_TWO)
    }

    @Test
    fun collectionConstructor_isCorrect() {
        val list = arrayListOf(10, 0xBADBEEF,
                0xBADFFFFFFFF)
        val hs = HexStack(list, BitsMode.THIRTY_TWO)
        assertEquals(BigInteger.TEN, hs.pop())
        assertEquals(0xBADBEEF, hs.pop().toInt())
        assertEquals(0xFFFFFFFF, hs.pop().toLong())
        assertEquals(0, hs.size)
    }

    @Test
    fun copyConstructor_isCorrect() {
        stack.push(0xFFFFFFFF)
        stack.push(7)
        stack.push(23)
        val copy = HexStack(stack)

        assertEquals(stack.bits, copy.bits)

        while (!stack.isEmpty) {
            val expected = stack.pop()
            val actual = copy.pop()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun addition_isCorrect() {
        stack.push(7)
        stack.push(3)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(10, stack.pop().toInt())
        stack.push(0xFFFFFFFF)
        stack.push(1)
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(0, stack.pop().toInt())
        stack.push(53)
        stack.push(0xFFFFFFE5)  // 2's comp of 27
        stack.add()
        assertEquals(1, stack.size)
        assertEquals(26, stack.pop().toInt())
    }

    @Test
    fun subtraction_isCorrect() {
        stack.push(7)
        stack.push(3)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(4, stack.pop().toInt())
        stack.push(4)
        stack.push(23)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(0xFFFF_FFFF - 19 + 1, stack.pop().toLong())
        stack.push(0xFFFF_FFFF)
        stack.push(0xABCD)
        stack.subtract()
        assertEquals(1, stack.size)
        assertEquals(0xFFFF_5432, stack.pop().toLong())
    }

    @Test
    fun multiply_isCorrect() {
        stack.push(3)
        stack.push(5)
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(15, stack.peek()?.toInt())
        stack.push(-5)
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(0x1_0000_0000 - 75, stack.pop().toLong())
        stack.push(0xBADBEEF)
        stack.push(0xFFF)
        // == BAD0413111 ==> D0413111
        stack.multiply()
        assertEquals(1, stack.size)
        assertEquals(0xD0413111, stack.pop().toLong())
    }

    @Test
    fun divide_isCorrect() {
        stack.push(8)
        stack.push(5)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(1, stack.pop().toInt())
        stack.push(0xD0413111)
        stack.push(0xFFF)
        stack.divide()
        assertEquals(1, stack.size)
        assertEquals(0xD04E3, stack.pop().toInt())
    }

    @Test
    fun mod_isCorrect() {
        stack.push(43)
        stack.push(28)
        stack.mod()
        assertEquals(15, stack.pop().toInt())
        stack.push(BigInteger("12341435134312", 10)) // 0x76DB7168
        stack.push(BigInteger("1324322342", 10)) // 0x4EEF8E26
        stack.mod()
        assertEquals(BigInteger.valueOf(0x27EBE342), stack.pop())
    }

    @Test
    fun toString_isCorrect() {
        stack.push(0x76DB7168)
        stack.push(0xD0413111)
        stack.push(0xFFF)
        assertEquals("76DB7168\nD0413111\nFFF", stack.toString())
        // verify stack not damaged
        assertEquals(0xFFF, stack.pop().toInt())
        assertEquals(0xD0413111, stack.pop().toLong())
        assertEquals(0x76DB7168, stack.pop().toInt())
        assertEquals(0, stack.size)
        assertEquals("", stack.toString())

        stack.push(0x1234_FACE_0FF1)
        stack.push(0x5678_BEEF_FACE)
        assertEquals("FACE0FF1\nBEEFFACE", stack.toString())
    }

    @Test
    fun contains_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        assert(stack.contains(15))
        assert(stack.contains(15L))
        assert(stack.contains(BigInteger.valueOf(15L)))
        assertFalse { stack.contains(12) }
        assertFalse { stack.contains(12L) }
        assertFalse { stack.contains(BigInteger.valueOf(12L)) }
    }

    @Test
    fun in_isCorrect() {
        stack.push(15)
        stack.push(7)
        stack.push(23)
        assert(15 in stack)
        assert(15L in stack)
        assert(BigInteger.valueOf(15L) in stack)
        assertFalse { 12 in stack }
        assertFalse { 12L in stack }
        assertFalse { BigInteger.valueOf(12L) in stack }
    }

    @Test
    fun pop_throwsOnUnderflow() {
        stack.push(7)
        assertEquals(7, stack.pop().toInt())
        assertFailsWith(NoSuchElementException::class) { stack.pop() }
    }
}
