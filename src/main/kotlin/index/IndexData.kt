package index

import kotlinx.serialization.Serializable
import kotlin.collections.HashMap

@Serializable
data class IndexData(val tfIndex: HashMap<String, HashMap<String, Int>>, val idfIndex: HashMap<String, Int>) {
}
