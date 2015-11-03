// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

import java.io.StringReader
import microtesia.properties._
import scala.xml.Node
import scala.language.implicitConversions
import scala.io.Source
import java.io.InputStreamReader
import java.io.InputStream

/**
 * Documentation for the Microtesia microdata parsing library.
 *
 * == Usage ==
 *
 * To use simply put the Microtesia API in scope and call the [[parse]]
 * method as follows:
 *
 * {{{
 * scala> import microtesia._
 * import microtesia._
 *
 * scala> parse("""&lt;div itemscope itemtype="http://schema.org/Movie">&lt;h1 itemprop="name">Avatar&lt;/h1>&lt;/div>""")
 * res0: Either[microtesia.InvalidMicrodata,microtesia.MicrodataDocument] = Right(MicrodataDocument(List(MicrodataItem(ArrayBuffer((name,MicrodataString(Avatar))),Some(http://schema.org/Movie),None))))
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

  private[microtesia] type Parsed[M, N] = Either[InvalidMicrodata, M]

  private[microtesia] implicit def toEitherSequence[A, B](seq: Seq[Either[A,B]]) = new EitherSequence(seq)

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
   * If there was an error parsing the microdata it returns a `Left[InvalidMicrodata]`.
   */
  def parse(html: String): Either[InvalidMicrodata, MicrodataDocument] = parser parse new StringReader(html)

  /**
   * Parses HTML and returns a structured representation of the microdata items in the document.
   * If there was an error parsing the microdata it returns a `Left[InvalidMicrodata]`.
   */
  def parse(input: InputStream): Either[InvalidMicrodata, MicrodataDocument] = parser parse new InputStreamReader(input)

}
