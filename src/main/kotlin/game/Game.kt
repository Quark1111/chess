package game

import org.example.model.*
import org.example.figure.*
import org.example.board.Board
import rules.ChessRules
import rules.Rules

class Game(
    val whitePlayer: Player,
    val blackPlayer: Player,
    val rules: ChessRules,
    val rulesType: Rules
) {
    val board = Board(Array(8) { arrayOfNulls<Figure>(8) })
    val moves = mutableListOf<String>()
    var currentTurn = Color.WHITE
    private var pendingPromotionMove: String? = null

    init {
        board.initialize(rulesType)
    }

    fun makeMove(userMove: String): MoveResponse {
        if (userMove == "0-0" || userMove == "0-0-0") {
            val isKingSide = userMove == "0-0"
            val kingFrom = board.findKing(currentTurn) ?: return MoveResponse(MoveResult.Error)
            val targetKingX = if (isKingSide) 6 else 2
            val kingTo = Position(targetKingX, kingFrom.y)

            if (!rules.canCastle(kingTo, kingFrom, board, currentTurn)) {
                return MoveResponse(MoveResult.Error)
            }

            if (!executeCastling(isKingSide, kingFrom, kingTo)) {
                return MoveResponse(MoveResult.Error)
            }

            moves.add(userMove)
            switchTurn()
            return evaluatePosition()
        }

        if (userMove.length != 5) {
            return MoveResponse(MoveResult.Error)
        }

        val from = fromString(userMove.substring(0, 2))
        val to = fromString(userMove.substring(3, 5))

        if (from == null || to == null) {
            return MoveResponse(MoveResult.Error)
        }

        val figure = board.getFigure(from) ?: return MoveResponse(MoveResult.Error)

        if (figure is Pawn && kotlin.math.abs(to.x - from.x) == 1 && board.getFigure(to) == null) {
            val previousMove = moves.lastOrNull() ?: return MoveResponse(MoveResult.Error)
            if (!rules.canEnPassant(previousMove, userMove, board, currentTurn)) {
                return MoveResponse(MoveResult.Error)
            }

            if (!executeEnPassant(from, to)) {
                return MoveResponse(MoveResult.Error)
            }

            moves.add(userMove)
            switchTurn()
            return evaluatePosition()
        }

        if (!rules.isCorrect(board, from, to, currentTurn)) {
            return MoveResponse(MoveResult.Error)
        }

        val isPromotion = figure is Pawn && (to.y == 7 || to.y == 0)

        board.moveFigure(from, to)
        moves.add(userMove)

        if (isPromotion) {
            pendingPromotionMove = userMove
            return MoveResponse(MoveResult.Accept, MoveEvent.NeedPromotion)
        }

        switchTurn()
        return evaluatePosition()
    }

    fun completePromotion(piece: PromotionPiece): MoveResponse {
        val userMove = pendingPromotionMove ?: return MoveResponse(MoveResult.Error)
        val to = fromString(userMove.substring(3, 5)) ?: return MoveResponse(MoveResult.Error)

        val pawn = board.getFigure(to)
        if (pawn !is Pawn) return MoveResponse(MoveResult.Error)

        val newFigure = when (piece) {
            PromotionPiece.QUEEN -> Queen(pawn.color, to, true)
            PromotionPiece.ROOK -> Rook(pawn.color, to, true)
            PromotionPiece.BISHOP -> Bishop(pawn.color, to, true)
            PromotionPiece.KNIGHT -> Knight(pawn.color, to, true)
        }

        board.setFigure(to, newFigure)
        pendingPromotionMove = null
        switchTurn()
        return evaluatePosition()
    }

    private fun evaluatePosition(): MoveResponse {
        return when {
            rules.isCheckmate(board, currentTurn) -> MoveResponse(MoveResult.Checkmate)
            rules.isStalemate(board, currentTurn) -> MoveResponse(MoveResult.Pat)
            rules.isCheck(board, currentTurn) -> MoveResponse(MoveResult.Check)
            else -> MoveResponse(MoveResult.Accept)
        }
    }

    private fun switchTurn() {
        currentTurn = if (currentTurn == Color.WHITE) Color.BLACK else Color.WHITE
    }

    private fun executeCastling(isKingSide: Boolean, kingFrom: Position, kingTo: Position): Boolean {
        val y = if (currentTurn == Color.WHITE) 0 else 7
        val targetRookX = if (isKingSide) 5 else 3
        val step = if (isKingSide) 1 else -1

        var rookX = kingFrom.x + step
        var rook: Rook? = null

        while (rookX in 0..7) {
            val piece = board.getFigure(Position(rookX, y))
            if (piece is Rook && piece.color == currentTurn) {
                rook = piece
                break
            }
            rookX += step
        }

        val rookFrom = rook?.position ?: return false

        board.moveFigure(kingFrom, kingTo)
        board.moveFigure(rookFrom, Position(targetRookX, y))
        return true
    }

    private fun executeEnPassant(from: Position, to: Position): Boolean {
        val previousMove = moves.lastOrNull() ?: return false
        val previousTo = fromString(previousMove.substring(3, 5)) ?: return false

        board.moveFigure(from, to)
        board.deleteFigure(previousTo)
        return true
    }
}
