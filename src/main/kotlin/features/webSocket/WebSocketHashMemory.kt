package features.webSocket

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

// Хранилище активных соединений (потокобезопасное)
object WebSocketManager {
    private val connections = ConcurrentHashMap<String, WebSocketSession>() // Key: phoneNumber

    fun addConnection(phoneNumber: String, session: WebSocketSession) {
        connections[phoneNumber] = session
        println("Добавлено соединение для $phoneNumber. Всего: ${connections.size}")
    }

    fun removeConnection(phoneNumber: String) {
        connections.remove(phoneNumber)
        println("Удалено соединение $phoneNumber. Осталось: ${connections.size}")
    }

    fun getConnection(phoneNumber: String): WebSocketSession? {
        return connections[phoneNumber]
    }


    private fun getKeyByValue(session: WebSocketSession): String? {
        return connections.entries.find { it.value == session }?.key
    }
}