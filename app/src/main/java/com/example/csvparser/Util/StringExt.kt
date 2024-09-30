package com.example.csvparser.Util

/**
 * CSV line correct splitting:
 * - commas inside quotation does not considered as delimiter
 * - escaped quotes does not considered as quotes
 * - value could be quoted or not
 * - quotation will be trimmed since it is not needed
 */

fun String.splitByComma(): List<String> {
    val list = mutableListOf<String>()

    val cArr: CharArray = this.toCharArray()

    var isInsideQuotes = false
    var sb = StringBuilder()
    var prev: Char = 0.toChar()

    for (c in cArr) {
        if(c == '"' && prev != '\\') { isInsideQuotes = !isInsideQuotes }
        if(c == ',' && !isInsideQuotes) {
            list.add(sb.toString())
            sb = StringBuilder()
        } else {
            sb.append(c)
            prev = c
        }
    }

    if (sb.length > 0) {
        list.add(sb.toString())
    }

    return list
}

fun String.trimQuotes():String =
    if(this.startsWith('\"') && this.endsWith('\"')) {
        this.substring(1, this.length-1)
    } else {
        this
    }