package org.example.model

data class Position(val x: Int, val y: Int)

fun isInRange(number: Int, min: Int, max: Int): Boolean = number in min..max

fun fromString(str: String): Position? {
    if (str.length != 2) {
        return null
    }
    val x = str[0] - 'a'
    val y = str[1].digitToInt() - 1
    if (!isInRange(x, 0, 7) || !isInRange(y, 0, 7)) {
        return null
    }
    return Position(x, y)
}