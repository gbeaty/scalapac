# scalapac - Amazon Product Advertising API Scala client #

## Overview

scalapac (Scala Amazon Product Advertising Client) allows you to access the Amazon Product Advertising API from Scala. [Learn more about the Amazon Product Advertising API](https://affiliate-program.amazon.com/gp/advertising/api/detail/main.html).

scalapac is a thin wrapper around Amazon's API: it takes care of the request signatures, performs the HTTP requests, processing Amazon's response and then returns the response code and XML in a tuple ready for you to work with in Scala. scalapac is "operation agnostic": you simply pass in the operation (as a String) and the operation's parameters (as a Map) and receive the response in XML. So it's like you're working directly with the API, but without the hassle of signing your requests, handling 403s errors from Amazon and so on.

## Credits

scalapac is a near-enough port of @dmcquay's excellent [node-apac](https://github.com/dmcquay/node-apac), an equivalent Amazon Product Advertising Client for node.js. It also leans heavily on Alex Parvulescu's [Amazon Product Api REST Client in Scala](http://blog.pfa-labs.com/2009/08/amazon-product-api-rest-client-in-scala.html) blog post for the Scala request signing code. Many thanks Dustin and Alex.

## Quick Start

Launch an SBT console from the git repository root directory with 'sbt console.' This project is currently configured to run with Scala 2.11.5 and version 2013-08-01 of the Amazon API.

Now you can import the OperationHelper class:

    scala> import co.orderly.scalapac.OperationHelper

Create an OperationHelper (changing your Amazon credentials):

    val opHelper = new OperationHelper(
        awsAccessKeyId     = "[YOUR AWS ID HERE]",
        awsSecretKey       = "[YOUR AWS SECRET HERE]",
        awsAssociateTagKey = "[YOUR ASSOCIATE TAG HERE]")

And now you can run a test against the Amazon Product Advertising API and receive your results as either XML:

    opHelper.debug(
        "ItemSearch", 
        Map("SearchIndex"    -> "Books",
            "Keywords"       -> "harry potter",
            "ResponseGroup"  -> "ItemAttributes,Offers"))

or JSON:
    opHelper.debugJSON(
        "ItemSearch", 
        Map("SearchIndex"    -> "Books",
            "Keywords"       -> "harry potter",
            "ResponseGroup"  -> "ItemAttributes,Offers"))

If everything is working correctly, you should see an XML or JSON print-out of results from the Amazon API, along with a 200 response code.

When using scalapac in production, instead of the debug() (or debugJSON) method you should use execute() (or executeJSON). execute() takes exactly the same arguments as debug() but returns a tuple containing the response code (as an Int in _1) and the XML or JSON response (as an Elem or JValue in _2):

    val (code, xml) = opHelper.execute("ItemSearch", Map(...

## API Documentation

All Amazon Product Advertising API documentation can be found on the AWS website:
[API Reference](http://docs.amazonwebservices.com/AWSECommerceService/latest/DG/index.html?ProgrammingGuide.html)

#Forking 

Forking from GitHub and making changes to the code is easy if you use sbt:

    git clone git://github.com/orderly/scalapac
    cd scalapac/lib
    wget http://apache.mirrors.timporter.net//commons/codec/binaries/commons-codec-1.5-bin.tar.gz
    tar -xzvf commons-code-1.5-bin.tar.gz
    vi src/main/scala/example.scala

Now update example.scala to include your own Amazon credentials, save and finish up with:

    cd ..    
    sbt
    > run

You should see scalapac Compiling main sources, running ExampleItemSearch and then displaying some XML results and a 200 response code.

## Copyright and License

scalapac is copyright (c) 2011-2015 Orderly Ltd

scalapac is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

scalapac is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public
License along with scalapac. If not, see [GNU licenses](http://www.gnu.org/licenses/).
