import org.apache.pdfbox.io.RandomAccessFile
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.util.*


class Parser() {

    fun ParseDir(dirPath: String) : HashMap<String, HashMap<String, Int>> {
        val map = HashMap<String, HashMap<String, Int>>()
        File(dirPath).walk(FileWalkDirection.TOP_DOWN).forEach {
            val ext = it.extension.lowercase(Locale.getDefault())
            val curMap = HashMap<String, Int>()
            if ( (ext == "pdf")){
                println(it)
//                try {

                    val content = parsePdf(it)
                    val lexer = Lexer(content)

                    for (token in lexer){
                        if (curMap.containsKey(token)){
                            curMap[token] = curMap[token]!! + 1
                        }else {
                            curMap[token] = 1
                        }
                    }
//                }
//                catch (e: Exception){
//                    println("Some pdf parsing error {e.localizedMessage}")
//                }
                map[it.path] = curMap
            }
        }
        return map
    }

    private fun parsePdf(file: File) : String {
        val parser = PDFParser(RandomAccessFile(file, "r"))
        parser.parse()

        val cosDoc = parser.document
        val pdfStripper = PDFTextStripper()
        val pdDoc = PDDocument(cosDoc)
        val parsedText = pdfStripper.getText(pdDoc)
        return parsedText
    }
}