package index

import java.util.*

class Lexer(private val src: String) : Iterator<String>{
    private var pos = 0
    override fun hasNext(): Boolean {
        return pos < src.length - 1
    }

    private val locale = Locale.getDefault()
    override fun next(): String {
        while ((pos < src.length - 1) and src[pos].isWhitespace())
            pos++

        if ((pos < src.length) and src[pos].isDigit())
            return chopWhile { x -> x.isDigit() }

        if ((pos < src.length) and src[pos].isLetter())
            return chopWhile { x -> x.isLetterOrDigit() }

        if ((pos < src.length) and !src[pos].isWhitespace())
            return chop(pos + 1)

        return String()
    }

    private fun chopWhile(func: (Char) -> Boolean): String {
        var tail = pos
        while ((tail < src.length) and func(src[tail]))
            tail++
        return chop(tail)
    }

    private fun chop(n: Int): String {
        val res = src.slice(pos until n)
        pos = n
        return res.uppercase(locale)
    }
}