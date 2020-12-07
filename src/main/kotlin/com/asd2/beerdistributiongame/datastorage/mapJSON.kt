import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

// Reads JSON file and adds data to the given object
inline fun <reified T: Any> mapJSONFile(path: String) : T = ObjectMapper().registerModule(KotlinModule()).readValue(File(path))
// Reads JSON and adds data to the given object
inline fun <reified T: Any> mapJSON(string: String) : T = ObjectMapper().registerModule(KotlinModule()).readValue(string)

fun <T:Any> mapJSONToFile(fileName: String, path: String, obj: T){
    ObjectMapper().registerModule(KotlinModule()).writerWithDefaultPrettyPrinter().writeValue(File(path + fileName), obj)
}

// For Usage of the JSONMappers see MapJSONUnitTest