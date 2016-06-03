package scalapac

// Java
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}
import java.net.{URL, URLConnection, HttpURLConnection}
import java.io.{InputStream, IOException}

// Scala
import scala.collection.immutable.TreeMap
import scala.xml._

// JSON support
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.Xml.toJson

/**
 * Instantiate an OperationHelper to start executing operations against the Amazon Product API
 */
class OperationHelper(
  awsAccessKeyId:       String,
  awsSecretKey:         String,
  awsAssociateTagKey:   String,
  endPoint:             String = "ecs.amazonaws.com")
{
  // Definitions we use in the standard params
  val VERSION  = "2013-08-01"
  val SERVICE  = "AWSECommerceService"
  val BASE_URI = "/onca/xml"

  /** Execute an operation against the Amazon Product API with the supplied arguments
    *
    * Returns a tuple containing the return code and converted JSON contents from the
    * Amazon Product API
    */
  def executeJSON(operation: String, args: Map[String, String]): (Int, JValue) = execute(operation, args) match {
    case (code, elem) =>
      val json = toJson(elem)
      (code, json)
  }

  /**
   * Execute an operation against the Amazon Product API with the supplied arguments
   *
   * Returns a tuple containing the return code and XML contents from the Amazon
   * Product API.
   */
  def execute(operation: String, args: Map[String, String]): (Int, Elem) = {

    // Put together the standard params with args for this operation
    val params = generateParams(operation, args)

    // Set up the request signature helper, sign the params and canonicalize
    val helper = new RequestSignatureHelper(awsAccessKeyId = awsAccessKeyId,
                                            awsSecretKey = awsSecretKey,
                                            endPoint = endPoint,
                                            requestUri = BASE_URI)
    val signedParams = helper.sign(params)
    val queryString = helper.canonicalize(signedParams)

    // Construct the API URL from the above components
    val url = new URL("http://" + endPoint + BASE_URI + "?" + queryString)

    // Because Amazon can throw 403s which XML.load doesn't like, load in a two-stage process
    val connection = url.openConnection() match {
      case x: HttpURLConnection => x
      case _ => throw new ClassCastException
    }

    // Get the return value
    val responseCode = connection.getResponseCode();

    // Now fetch the XML and return a tuple of the return code and XML
    val data = try {
      connection.getInputStream()
    } catch {
      case (e: Throwable) => connection.getErrorStream()
    }

    val xml = XML.load(data)
    (responseCode, xml)
  }


  /**
   * A wrapper which pretty-prints the JSON and prints out the return code.
   * Useful for checking API responses in the console
   */
  def debugJSON(operation: String, args: Map[String, String]) {
    val (code, json) = executeJSON(operation, args)
    pretty(render(json))
    println(pretty(render(json)))
    println("Response code: " + code)
  }

  /**
   * A wrapper which pretty-prints the XML and prints out the return code.
   * Useful for checking API responses in the console
   */
  def debug(operation: String, args: Map[String, String]) {

    val (code, xml) = execute(operation, args)

    // Now pretty print the XML
    val ppr = new scala.xml.PrettyPrinter(80,2)
    val out = ppr.format(xml)
    Console.println(out)

    // Append the response code
    Console.println("Response code: " + code)
  }

  /**
   * Returns an ordered list of parameters ready for Amazon
   */
  protected def generateParams(operation: String, args: Map[String, String]): TreeMap[String, String] = {

    var params = TreeMap.empty[String, String]

    params += "Version" -> VERSION
    params += "Operation" -> operation
    params += "AWSAccessKeyId" -> awsAccessKeyId
    params += "AssociateTag" -> awsAssociateTagKey
    params += "Timestamp" -> generateTimestamp()
    params += "Service" -> SERVICE

    // Copy in all the args passed into this function
    args foreach ( (arg) => params += arg._1 -> arg._2)
    params
  }

  /**
   * Returns a timestamp in the format required by Amazon
   */
  protected def generateTimestamp(): String = {

      val dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      dfm.setTimeZone(TimeZone.getTimeZone("GMT"))
      dfm.format(Calendar.getInstance().getTime())
  }
}
