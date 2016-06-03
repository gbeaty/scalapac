/**
 * Provides a pair of classes for interacting with the Amazon Product API from Scala.
 *
 * ==Overview==
 * The main class to use is [[main.scala.co.orderly.scalapac.scalapac.OperationHelper]]. Create an instance
 * with your Amazon API connection details like so:
 *
 * val opHelper = new OperationHelper(awsAccessKeyId     = "Enter here",
 *                                    awsSecretKey       = "Enter here",
 *                                    awsAssociateTagKey = "Enter here"
 *                                    )
 *
 * Once you have created an opHelper, you can then run operations against the Amazon Product
 * API using the execute command:
 *
 * val (code, xml) = opHelper.execute("ItemSearch", Map("SearchIndex"    -> "Books",
 *                                                      "Keywords"       -> "harry potter",
 *                                                      "ResponseGroup"  -> "ItemAttributes,Offers"
 *                                                     ))
 *
 * For testing queries in the Scala console there is also a helper method, use like so:
 *
 * scala> opHelper.debug("ItemSearch", ...)
 */
package object scalapac {
}
