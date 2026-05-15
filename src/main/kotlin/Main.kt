import window.ChessWindow
import game.*
import org.example.model.Player
import model.ReplacePawn
import org.example.rules.ClassicRules
import org.example.rules.FischerRules
import rules.Rules

fun main() {
    val window = ChessWindow()
    var game: Game? = null
    var currentMode = "classic"

    fun createNewGame() {
        val newGame = if (currentMode == "classic") {
            Game(Player("white"), Player("black"), ClassicRules(), Rules.Classic)
        } else {
            Game(Player("white"), Player("black"), FischerRules(), Rules.Fisher)
        }
        game = newGame
        window.updateBoard(newGame.board)
    }

    window.onModeSelected = { mode ->
        currentMode = mode
        createNewGame()
    }

    window.onMoveMode = { move ->
        val currentGame = game
        if (currentGame == null) {
            Pair(MoveResult.Error, "Игра не создана")
        } else {
            val response = currentGame.makeMove(move)
            window.updateBoard(currentGame.board)

            val message = when (response.result) {
                MoveResult.Accept -> if (response.event == MoveEvent.NeedPromotion) "Превращение пешки" else "Ход принят"
                MoveResult.Check -> "Шах!"
                MoveResult.Checkmate -> "Мат! Игра окончена"
                MoveResult.Pat -> "Пат! Ничья"
                MoveResult.Draw -> "Ничья"
                MoveResult.Error -> "Невозможный ход"
            }

            Pair(response.result, message)
        }
    }

    window.onModeFigure = { figure ->
        val currentGame = game
        if (currentGame != null) {
            val promotionPiece = when (figure) {
                ReplacePawn.Queen -> PromotionPiece.QUEEN
                ReplacePawn.Rook -> PromotionPiece.ROOK
                ReplacePawn.Bishop -> PromotionPiece.BISHOP
                ReplacePawn.Knight -> PromotionPiece.KNIGHT
            }
            currentGame.completePromotion(promotionPiece)
            window.updateBoard(currentGame.board)
        }
    }

    window.createWindow()
}