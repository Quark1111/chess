package window

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import game.MoveResult
import org.example.board.Board
import model.ReplacePawn
import org.example.figure.*
import org.example.model.*
import kotlin.system.exitProcess

class ChessWindow {
    var onModeSelected: ((String) -> Unit)? = null
    var onModeFigure: ((ReplacePawn) -> Unit)? = null
    var onMoveMode: ((String) -> Pair<MoveResult, String>)? = null
    var currentTurn = "Белые ходят"
    private var currentWindow by mutableStateOf("menu")
    private var currentBoard by mutableStateOf<Board?>(null)
    private val numberCell = listOf('1', '2', '3', '4', '5', '6', '7', '8')
    private val nameCell = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')

    fun updateBoard(board: Board) {
        currentBoard = board
    }

    fun createWindow() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                resizable = false,
                title = "Chess"
            ) {
                MaterialTheme {
                    runWindow()
                }
            }
        }
    }

    private fun changeMove(currentTurn: String): String {
        return if (currentTurn == "Белые ходят") "Чёрные ходят" else "Белые ходят"
    }

    @Composable
    fun turningPawn(color: Boolean, onReplacePawn: (ReplacePawn) -> Unit) {
        val colorPrefix = if (color) "Black" else "White"

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(500.dp)
                    .background(
                        color = Color(0xFF616161),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Выберите фигуру",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onReplacePawn(ReplacePawn.Rook) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource("images/${colorPrefix}Rook.png"),
                            contentDescription = "Ладья",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Button(
                        onClick = { onReplacePawn(ReplacePawn.Knight) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource("images/${colorPrefix}Knight.png"),
                            contentDescription = "Конь",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Button(
                        onClick = { onReplacePawn(ReplacePawn.Bishop) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource("images/${colorPrefix}Bishop.png"),
                            contentDescription = "Слон",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Button(
                        onClick = { onReplacePawn(ReplacePawn.Queen) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource("images/${colorPrefix}Queen.png"),
                            contentDescription = "Ферзь",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun runWindow() {
        when (currentWindow) {
            "menu" -> drawInitialWindow(
                onClassicClick = {
                    onModeSelected?.invoke("classic")
                    currentWindow = "game"
                },
                onFisherClick = {
                    onModeSelected?.invoke("fisher")
                    currentWindow = "game"
                }
            )
            "game" -> drawGameWindow()
        }
    }

    @Composable
    fun drawGameOverWindow(onRestart: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(500.dp)
                    .background(
                        color = Color(0xFF616161),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Игра окончена",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onRestart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.DarkGray
                        ),
                        modifier = Modifier.width(235.dp).height(50.dp)
                    ) {
                        Text("Начать заново")
                    }
                    Button(
                        onClick = { exitProcess(0) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.DarkGray
                        ),
                        modifier = Modifier.width(235.dp).height(50.dp)
                    ) {
                        Text("Закрыть приложение")
                    }
                }
            }
        }
    }

    @Composable
    fun drawInitialWindow(onClassicClick: () -> Unit, onFisherClick: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource("images/ChessFon.jpg"),
                contentDescription = "Расстановка",
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text("Выберите режим игры", fontSize = 30.sp)
                Spacer(modifier = Modifier.height(120.dp))
                Button(
                    onClick = onClassicClick,
                    modifier = Modifier.width(235.dp).height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.DarkGray
                    )
                ) {
                    Text("Классические шахматы", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onFisherClick,
                    modifier = Modifier.width(235.dp).height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.DarkGray
                    )
                ) {
                    Text("Шахматы Фишера", fontSize = 13.sp)
                }
            }
        }
    }

    @Composable
    fun drawGameWindow() {
        if (currentBoard == null) return

        var moveInput by remember { mutableStateOf("") }
        var history by remember { mutableStateOf(listOf<String>()) }
        var status by remember { mutableStateOf("") }
        var state by remember { mutableStateOf(true) }
        var showPawnDialog by remember { mutableStateOf(false) }
        var pendingMove by remember { mutableStateOf("") }
        var pendingColor by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Приложение для записи и проверки ходов в шахматах")
            Spacer(modifier = Modifier.height(20.dp))
            Text("Ход: $currentTurn")
            Spacer(modifier = Modifier.height(20.dp))
            if (status.isNotEmpty()) {
                Text("Состояние игры: $status")
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(top = 170.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                chessBoard()
                Column(
                    modifier = Modifier.width(250.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("История ходов:", fontSize = 18.sp)
                    Column(
                        modifier = Modifier
                            .height(210.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        history.forEach { move ->
                            Text(move, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    TextField(
                        value = moveInput,
                        onValueChange = { moveInput = it },
                        placeholder = { Text("Введите ход (Формат e2-e4 обычный ход, 0-0 обычная рокеровка, 0-0-0 длинная рокеровка") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFFAFAFA),
                            focusedIndicatorColor = Color(0xFF4CAF50),
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color(0xFF4CAF50),
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.LightGray
                        )
                    )

                    Button(
                        onClick = {
                            if (moveInput.isNotEmpty()) {
                                val result = onMoveMode?.invoke(moveInput) ?: Pair(MoveResult.Error, "")

                                when (result.first) {
                                    MoveResult.Accept -> {
                                        if (result.second == "Превращение пешки") {
                                            pendingMove = moveInput
                                            pendingColor = currentTurn == "Чёрные ходят"
                                            showPawnDialog = true
                                            moveInput = ""
                                            status = result.second
                                        } else {
                                            history = history + moveInput
                                            moveInput = ""
                                            currentTurn = changeMove(currentTurn)
                                            status = result.second
                                        }
                                    }
                                    MoveResult.Check -> {
                                        history = history + moveInput
                                        moveInput = ""
                                        currentTurn = changeMove(currentTurn)
                                        status = result.second
                                    }
                                    MoveResult.Checkmate, MoveResult.Pat, MoveResult.Draw -> {
                                        status = result.second
                                        state = false
                                    }
                                    else -> {
                                        status = result.second
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.DarkGray
                        )
                    ) {
                        Text("Записать ход")
                    }
                }
            }

            if (!state) {
                drawGameOverWindow {
                    currentWindow = "menu"
                }
            }

            if (showPawnDialog) {
                turningPawn(color = pendingColor) { figure ->
                    onModeFigure?.invoke(figure)
                    history = history + pendingMove
                    currentTurn = changeMove(currentTurn)
                    showPawnDialog = false
                    pendingMove = ""
                }
            }
        }
    }

    @Composable
    private fun drawTextCell(x: Int, y: Int) {
        if (x == 0) {
            Text(
                text = nameCell[y].toString(),
                fontSize = 17.sp,
                color = if (y % 2 == 0) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(start = 37.dp, top = 25.dp, end = 2.dp, bottom = 2.dp)
            )
        }
        if (y == 0) {
            Text(
                text = numberCell[x].toString(),
                fontSize = 17.sp,
                color = if (x % 2 == 0) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(start = 2.dp, top = 2.dp, end = 37.dp, bottom = 25.dp)
            )
        }
    }

    @Composable
    private fun chessBoard() {
        Box(
            modifier = Modifier.size(423.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource("images/Board.png"),
                modifier = Modifier.size(420.dp),
                contentDescription = "Board"
            )
            Column {
                for (j in 7 downTo 0) {
                    Row {
                        for (i in 0..7) {
                            val figure = currentBoard?.getFigure(Position(i, j))
                            val pathFigure = getPathFigure(figure)
                            drawFigureAndCell(pathFigure, i, j)
                        }
                    }
                }
            }
        }
    }

    private fun getPathFigure(figure: Figure?): String? {
        if (figure == null) return null

        val colorPrefix = when (figure.color) {
            org.example.model.Color.WHITE -> "White"
            org.example.model.Color.BLACK -> "Black"
        }

        val figureName = when (figure) {
            is Pawn -> "Pawn"
            is Rook -> "Rook"
            is Knight -> "Knight"
            is Bishop -> "Bishop"
            is Queen -> "Queen"
            is King -> "King"
            else -> return null
        }

        return "images/${colorPrefix}${figureName}.png"
    }

    private fun getColor(x: Int, y: Int): String {
        return if ((x + y) % 2 == 0) "images/WhiteCell.png" else "images/BlackCell.png"
    }

    @Composable
    private fun drawFigureAndCell(pathFigure: String?, x: Int, y: Int) {
        val color = getColor(x, y)
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(color),
                contentDescription = color,
                modifier = Modifier.fillMaxSize()
            )
            if (pathFigure != null) {
                Image(
                    painter = painterResource(pathFigure),
                    contentDescription = pathFigure,
                    modifier = Modifier.size(40.dp)
                )
            }
            drawTextCell(y, x)
        }
    }
}