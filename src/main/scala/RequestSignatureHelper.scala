package scalapac

// Java
import java.net.URLEncoder
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

// Scala
import scala.collection.immutable.TreeMap

// commons-codec
import org.apache.commons.codec.binary.Base64

/**
 * A helper class to sign a given set of parameters ready for the Amazon Product API.
 */
class RequestSignatureHelper(awsAccessKeyId:  String,
                             awsSecretKey:    String,
                             endPoint:        String,
                             requestUri:      String,
                             requestMethod:   String = "GET"
                             )
{
  val UTF8_CHARSET = "UTF-8"
  val HMAC_SHA256_ALGORITHM = "HmacSHA256"

  /**
   * Takes an ordered map of parameters, signs it and returns the map
   * of parameters with the signature appended.
   */
  def sign(params: TreeMap[String, String]): TreeMap[String, String] = {

    val stringToSign = List(requestMethod,
                            endPoint,
                            requestUri,
                            canonicalize(params)
                            ).mkString("\n")

    params.insert("Signature", digest(stringToSign)) // Return TreeMap with Signature on the end
  }

  /**
   * Returns a canonicalized, escaped string of &key=value pairs from an ordered map of parameters
   */
  def canonicalize(params: TreeMap[String, String]): String = {

    params.map(
      (param) => escape( param._1 ) + "=" + escape(param._2)
    ).mkString("&")
  }

  /**
   * Returns the digest for a given string
   */
  protected def digest(x: String): String = {

    try {
      val mac = Mac.getInstance(HMAC_SHA256_ALGORITHM)
      val secretKeySpec = new SecretKeySpec(awsSecretKey.getBytes(UTF8_CHARSET), HMAC_SHA256_ALGORITHM);

      mac.init(secretKeySpec)

      val data = x.getBytes(UTF8_CHARSET)
      val rawHmac = mac.doFinal(data)

      val encoder = new Base64()
      return encoder.encodeToString(rawHmac).trim()

    } catch { // User has forgotten to include Base64()
      case e: NoClassDefFoundError => throw new RuntimeException("Cannot find apache.commons.codec.binary.Base64")
      case e: Throwable => throw new RuntimeException(UTF8_CHARSET + " is unsupported")
    }
  }

  /**
   * Returns an escaped string
   */
  protected def escape(s: String): String = {

    try {
      return URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20").replace("*", "%2A").replace("%7E", "~")
    } catch  {
      case e: Throwable => throw new RuntimeException(UTF8_CHARSET + " is unsupported")
    }
  }
}