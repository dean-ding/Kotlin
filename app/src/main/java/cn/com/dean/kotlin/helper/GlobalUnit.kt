package cn.com.dean.kotlin.helper

/**
 * Created: tvt on 17/10/10 15:12
 */
class GlobalUnit {

    companion object {
        val HTTP_URL_BASE: String = "http://192.168.32.6:8080"

        public class NewsItemType {
            val IMAGE_TYPE = 0x1000
            val TEXT_TYPE = 0x1001
        }
    }

}