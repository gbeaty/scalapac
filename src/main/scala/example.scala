import scalapac.OperationHelper

/**
 * Simple console example of an Amazon Product API call using scalapac
 */
object ExampleItemSearch {
  // Update this with your Amazon credentials before running
  val opHelper =
    new OperationHelper(
      awsAccessKeyId     = "[ACCESS KEY]",
      awsSecretKey       = "[SECRET KEY]",
      awsAssociateTagKey = "[ASSOCIATE TAG KEY]")

  def main(args: Array[String]) {
    opHelper.debugJSON(
      "ItemSearch",
      Map("SearchIndex"    -> "Books",
          "Keywords"       -> "harry potter",
          "ResponseGroup"  -> "ItemAttributes,Offers"))
  }
}
