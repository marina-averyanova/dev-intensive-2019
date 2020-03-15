package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    this.trimEnd().also {
        return if (it.length > length)  {
            "${this.take(length).trimEnd()}..."
        } else it
    }
}