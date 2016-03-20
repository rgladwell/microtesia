// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

import java.io.InputStreamReader
import java.io.InputStream
import java.io.StringReader
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.xml.Node
import microtesia.properties._
import scala.language.implicitConversions

/**
 * Documentation for the Microtesia microdata parsing library.
 *
 * == Usage ==
 *
 * To use simply put the Microtesia API in scope and call the [[parseMicrodata]]
 * function as follows:
 *
 * {{{
 * scala> import microtesia._
 * import microtesia.
 * 
 * scala> import scala.util.Try
 * import scala.util.Try
 *
 * scala> parseMicrodata("""&lt;div itemscope itemtype="http://schema.org/Movie">&lt;h1 itemprop="name">Avatar&lt;/h1>&lt;/div>""")
 * res0: Try[microtesia.MicrodataDocument] = Success(MicrodataDocument(List(MicrodataItem(ArrayBuffer((name,MicrodataString(Avatar))),Some(http://schema.org/Movie),None))))
 * }}}
 */
package object microtesia {

  /**
   * Represents a microdata name-value pair.
   */
  type MicrodataProperty = (String, MicrodataValue)

  /**
   * Utility object to help extract microdata property values in for-comprehensions.
   */
  object MicrodataProperty {
    def apply(name: String, value: MicrodataValue) = (name, value)
    def unapply(p: MicrodataProperty): Option[(String, MicrodataValue)] = Some(p)
  }

  private[microtesia] implicit def toTrySequence[A](seq: Seq[Try[A]]) = new TrySequence(seq)

  private val parser = new SaxMicrodataParser
                            with TagSoupSax
                            with ItemsParser[Node]
                            with PropertiesParser[Node]
                            with NestedItemPropertyParsing[Node]
                            with LinkPropertyParsing[Node]
                            with ImagePropertyParsing[Node]
                            with TimePropertyParsing[Node]
                            with DataPropertyParsing[Node]
                            with MeterPropertyParsing[Node]
                            with StringPropertyParsing[Node]

  /**
   * Parses HTML and returns a structured representation of the microdata items in the document.
   * If there was an error parsing the microdata it returns a `Failed(InvalidMicrodata)`.
   */
  def parseMicrodata(html: String): Try[MicrodataDocument] = parser parse new StringReader(html)

  /**
   * Parses HTML and returns a structured representation of the microdata items in the document.
   * If there was an error parsing the microdata it returns a `Left[InvalidMicrodata]`.
   */
  def parseMicrodata(input: InputStream)(implicit c: ExecutionContext): Future[MicrodataDocument] =
    Future(parser parse new InputStreamReader(input))
      .flatMap {
         case Success(result) => Future.successful(result)
         case Failure(error)  => Future.failed(error)
      }

}
