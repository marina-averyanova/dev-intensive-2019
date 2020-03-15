package ru.skillbranch.devintensive.utils

import android.annotation.SuppressLint
import java.lang.IllegalStateException

object Utils {

    private val transliterationAccordance = mapOf(
        "а" to "a",
        "б" to "b",
        "в" to "v",
        "г" to "g",
        "д" to "d",
        "е" to "e",
        "ё" to "e",
        "ж" to "zh",
        "з" to "z",
        "и" to "i",
        "й" to "i",
        "к" to "k",
        "л" to "l",
        "м" to "m",
        "н" to "n",
        "о" to "o",
        "п" to "p",
        "р" to "r",
        "с" to "s",
        "т" to "t",
        "у" to "u",
        "ф" to "f",
        "х" to "h",
        "ц" to "c",
        "ч" to "ch",
        "ш" to "sh",
        "щ" to "sh'",
        "ъ" to "",
        "ы" to "i",
        "ь" to "",
        "э" to "e",
        "ю" to "yu",
        "я" to "ya"
    )

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val nameParts: List<String>? = clearOfSpaces(fullName)?.split(" ")

        return Pair(
            replaceEmptyStringToNull(nameParts?.getOrNull(0)),
            replaceEmptyStringToNull(nameParts?.getOrNull(1))
        )
    }

    @SuppressLint("DefaultLocale")
    fun toInitials(firstName: String?, lastName: String?): String? {
        return replaceEmptyStringToNull(
            ("${replaceNullToEmptyString(clearOfSpaces(firstName)?.take(1))}" +
                "${replaceNullToEmptyString(clearOfSpaces(lastName)?.take(1))}").toUpperCase()
        )
    }

    @SuppressLint("DefaultLocale")
    fun transliteration(entity: String, divider: String = " "): String {
        return clearOfSpaces(entity)?.toCharArray()?.joinToString("") {
            val isUpper = it.isUpperCase()
            val letter = it.toString()
            when {
                letter.matches(Regex("^[a-zA-Z0-9_-]$")) -> letter
                letter.matches(Regex("^[а-яА-Я0-9_-]$")) -> {
                    val mappedLetter = transliterationAccordance.get(letter.toLowerCase()) ?: letter
                    if (isUpper) mappedLetter.capitalize() else mappedLetter
                }
                letter == " " -> divider
                else -> throw IllegalStateException("invalid name format")
            }
        }.orEmpty()
    }

    private fun replaceEmptyStringToNull(entity: String?): String? {
       return if (entity.equals("")) null else entity
    }

    private fun replaceNullToEmptyString(entity: String?): String? {
        return if (entity.equals(null)) "" else entity
    }

    private fun clearOfSpaces(entity: String?): String? {
        return entity?.trim()?.replace(Regex("  +"), " ")
    }
}