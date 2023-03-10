import java.util.*

class Lexer(val src: String) : Iterator<String>{
    private var pos = 0
    override fun hasNext(): Boolean {
        return pos < src.length - 1
    }

    private val locale = Locale.getDefault()
    override fun next(): String {

        while ((pos < src.length - 1) and src[pos].isWhitespace()) {
            pos++
        }

        if ((pos < src.length) and src[pos].isDigit()) {
            var tail = pos
            while ((tail < src.length) and src[tail].isDigit()) tail++
            val res =  src.slice(pos until tail)
            pos = tail + 1
            return res
        }

        if ((pos < src.length) and src.get(pos).isLetter()) {
            var tail = pos
            while ((tail < src.length) and src.get(tail).isLetterOrDigit()) tail++
            val res = src.slice(pos until tail).uppercase(locale)
            pos = tail + 1
            return res
        }

        if ((pos < src.length) and !src[pos].isWhitespace()) {
            val res = src[pos].toString()
            pos++
            return res
        }
        return String()
    }
}