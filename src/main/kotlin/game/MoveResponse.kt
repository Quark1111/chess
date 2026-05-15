package game

data class MoveResponse(
    val result: MoveResult,
    val event: MoveEvent = MoveEvent.None
)