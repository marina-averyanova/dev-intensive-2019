package ru.skillbranch.devintensive.models

import java.util.Date

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {

    abstract fun formatMessage(): String

    companion object AbstractFactory {
        private var lastId = -1;

        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            type: String,
            payload: Any?,
            isIncoming: Boolean = false
        ): BaseMessage {
            lastId++

            return when (type) {
                MessageType.IMAGE.type -> ImageMessage(
                    id = "$lastId",
                    from = from,
                    chat = chat,
                    isIncoming = isIncoming,
                    date = date,
                    imageUrl = "$payload"
                )
                else -> TextMessage(
                    id = "$lastId",
                    from = from,
                    chat = chat,
                    isIncoming = isIncoming,
                    date = date,
                    text = "$payload"
                )
            }
        }
    }
}