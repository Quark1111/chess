package org.example.board

import org.example.model.*
import kotlin.math.sign
import org.example.figure.*
import org.example.rules.*
import rules.*

class Board(val cells: Array<Array<Figure?>>) {

    fun getFigure(position: Position): Figure? {
        if (position.x !in 0..7 || position.y !in 0..7) {
            throw IndexOutOfBoundsException()
        }
        return cells[position.x][position.y]
    }

    fun setFigure(position: Position, figure: Figure?) {
        if (position.x !in 0..7 || position.y !in 0..7) {
            throw IndexOutOfBoundsException()
        }
        cells[position.x][position.y] = figure
    }

    fun deleteFigure(position: Position) {
        if (position.x !in 0..7 || position.y !in 0..7) {
            throw IndexOutOfBoundsException()
        }
        cells[position.x][position.y] = null
    }

    fun findKing(color: Color): Position? {
        for (x in 0..7) {
            for (y in 0..7) {
                val piece = getFigure(Position(x, y))
                if (piece is King && piece.color == color) {
                    return Position(x, y)
                }
            }
        }
        return null
    }

    private fun checkPermutation(arrangement: MutableList<Char>): Boolean {
        var bishop1 = -1
        var bishop2 = -1
        var king = -1
        var rook1 = -1
        var rook2 = -1
        for (i in 0..7) {
            if (arrangement[i] == '0') {
                if (rook1 == -1) {
                    rook1 = i
                } else {
                    rook2 = i
                }
            } else if (arrangement[i] == '2') {
                if (bishop1 == -1) {
                    bishop1 = i
                } else {
                    bishop2 = i
                }
            } else if (arrangement[i] == '4') {
                king = i
            }
        }
        return bishop1 % 2 != bishop2 % 2 && rook1 < king && king < rook2
    }

    private fun permutations(
        arrangement: MutableList<Char>,
        len: Int,
        availableSymbols: String,
        flag: MutableList<Boolean>,
        arrayPermutations: MutableList<List<Char>>
    ) {
        if (len == 8) {
            if (checkPermutation(arrangement)) {
                arrayPermutations.add(arrangement.toList())
            }
        } else {
            for (i in availableSymbols.indices) {
                if (!flag[i]) {
                    arrangement.add(availableSymbols[i])
                    flag[i] = true
                    permutations(arrangement, len + 1, availableSymbols, flag, arrayPermutations)
                    flag[i] = false
                    arrangement.removeAt(arrangement.size - 1)
                }
            }
        }
    }

    fun initialize(rules: Rules) {
        if (rules == Rules.Classic) {
            setFigure(Position(0, 0), Rook(Color.WHITE, Position(0, 0)))
            setFigure(Position(1, 0), Knight(Color.WHITE, Position(1, 0)))
            setFigure(Position(2, 0), Bishop(Color.WHITE, Position(2, 0)))
            setFigure(Position(3, 0), Queen(Color.WHITE, Position(3, 0)))
            setFigure(Position(4, 0), King(Color.WHITE, Position(4, 0)))
            setFigure(Position(5, 0), Bishop(Color.WHITE, Position(5, 0)))
            setFigure(Position(6, 0), Knight(Color.WHITE, Position(6, 0)))
            setFigure(Position(7, 0), Rook(Color.WHITE, Position(7, 0)))

            for (x in 0..7) {
                setFigure(Position(x, 1), Pawn(Color.WHITE, Position(x, 1)))
            }

            setFigure(Position(0, 7), Rook(Color.BLACK, Position(0, 7)))
            setFigure(Position(1, 7), Knight(Color.BLACK, Position(1, 7)))
            setFigure(Position(2, 7), Bishop(Color.BLACK, Position(2, 7)))
            setFigure(Position(3, 7), Queen(Color.BLACK, Position(3, 7)))
            setFigure(Position(4, 7), King(Color.BLACK, Position(4, 7)))
            setFigure(Position(5, 7), Bishop(Color.BLACK, Position(5, 7)))
            setFigure(Position(6, 7), Knight(Color.BLACK, Position(6, 7)))
            setFigure(Position(7, 7), Rook(Color.BLACK, Position(7, 7)))

            for (x in 0..7) {
                setFigure(Position(x, 6), Pawn(Color.BLACK, Position(x, 6)))
            }
        } else {
            val availableSymbols = "00112234"
            val flag = MutableList(8) { false }
            val arrayPermutations = mutableListOf<List<Char>>()

            permutations(mutableListOf(), 0, availableSymbols, flag, arrayPermutations)

            val randomPosition = arrayPermutations.random()

            for (x in 0..7) {
                val piece = when (randomPosition[x]) {
                    '0' -> Rook(Color.WHITE, Position(x, 0))
                    '1' -> Knight(Color.WHITE, Position(x, 0))
                    '2' -> Bishop(Color.WHITE, Position(x, 0))
                    '3' -> Queen(Color.WHITE, Position(x, 0))
                    '4' -> King(Color.WHITE, Position(x, 0))
                    else -> null
                }
                piece?.let { setFigure(Position(x, 0), it) }
            }

            for (x in 0..7) {
                val piece = when (randomPosition[x]) {
                    '0' -> Rook(Color.BLACK, Position(x, 7))
                    '1' -> Knight(Color.BLACK, Position(x, 7))
                    '2' -> Bishop(Color.BLACK, Position(x, 7))
                    '3' -> Queen(Color.BLACK, Position(x, 7))
                    '4' -> King(Color.BLACK, Position(x, 7))
                    else -> null
                }
                piece?.let { setFigure(Position(x, 7), it) }
            }

            for (x in 0..7) {
                setFigure(Position(x, 1), Pawn(Color.WHITE, Position(x, 1)))
                setFigure(Position(x, 6), Pawn(Color.BLACK, Position(x, 6)))
            }
        }
    }

    fun copy(): Board {
        val newCells = Array(8) { arrayOfNulls<Figure>(8) }
        for (x in 0..7) {
            for (y in 0..7) {
                val figure = getFigure(Position(x, y))
                newCells[x][y] = figure?.copy(figure.position)
            }
        }
        return Board(newCells)
    }

    fun isPathClear(from: Position, to: Position): Boolean {
        val dx = (to.x - from.x).sign
        val dy = (to.y - from.y).sign
        var x = from.x + dx
        var y = from.y + dy
        while (x != to.x || y != to.y) {
            if (cells[x][y] != null) {
                return false
            }
            x += dx
            y += dy
        }
        return true
    }

    fun moveFigure(from: Position, to: Position) {
        val figure = cells[from.x][from.y]
        setFigure(from, null)
        figure?.position = to
        figure?.hasMoved = true
        setFigure(to, figure)
    }

    fun checkPosition(position: Position, color: Color): Boolean {
        val figure = getFigure(position)
        if (figure is King) {
            return false
        }
        if (figure?.color != color) {
            return true
        }
        return false
    }
}