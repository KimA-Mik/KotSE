import kotlinx.serialization.Serializable
import kotlin.collections.HashMap

@Serializable
data class Index(val tfIndex: HashMap<String, HashMap<String, Int>>, val idfIndex: HashMap<String, Int>) {
}
