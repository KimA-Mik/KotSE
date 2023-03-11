package index

import org.apache.pdfbox.io.RandomAccessFile
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.openxml4j.util.ZipSecureFile
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class Parser() {

    companion object{
        const val TOTAL_TERMS_KEY = "total"
    }

    fun ParseDir(dirPath: String) : IndexData {
        ZipSecureFile.setMinInflateRatio(0.0)
        val tfIndex = HashMap<String, HashMap<String, Int>>()
        File(dirPath).walk(FileWalkDirection.TOP_DOWN).forEach {
            val ext = it.extension.lowercase(Locale.getDefault())
            var content = String()
            try {
                if (ext == "pdf") {
                    println(it.path)
                    content = parsePdf(it)
                } else if (ext == "docx") {
                    println(it.path)
                    content = parseDocx(it)
                }
            }catch (Ex: Exception){
                println(Ex.localizedMessage)
            }

            if (content.isNotEmpty()) {
                val curMap = HashMap<String, Int>()
                val lexer = Lexer(content)
                var totalTerms = 0
                for (token in lexer) {
                    totalTerms += 1
                    if (curMap.containsKey(token)) {
                        curMap[token] = curMap[token]!! + 1
                    } else {
                        curMap[token] = 1
                    }
                }
                if (totalTerms > 0) {
                    curMap[TOTAL_TERMS_KEY] = totalTerms
                    tfIndex[it.path] = curMap
                }
            }
        }
        val idfIndex = computeIdfIndex(tfIndex)
        return IndexData(tfIndex, idfIndex)
    }

    private fun computeIdfIndex(data: HashMap<String, HashMap<String, Int>>): HashMap<String, Int> {
        val res = HashMap<String, Int>()

        for (doc in data.values){
            for (term in doc.keys){
                if (res.containsKey(term)) {
                    res[term] = res[term]!! + 1
                }else{
                    res[term] = 1
                }
            }
        }
        return res
    }

    private fun parsePdf(file: File): String {
        val parser = PDFParser(RandomAccessFile(file, "r"))
        parser.parse()

        val cosDoc = parser.document
        val pdfStripper = PDFTextStripper()
        val pdDoc = PDDocument(cosDoc)
        val result = pdfStripper.getText(pdDoc)
        parser.document.close()
        return result
    }

    private fun parseDocx(file: File) : String {
        val docxFile = XWPFDocument(OPCPackage.open(file))
        val extractor = XWPFWordExtractor(docxFile)
        docxFile.close()
        return extractor.text
    }
}