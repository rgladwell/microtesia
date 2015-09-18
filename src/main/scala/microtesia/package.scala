// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

import java.io.StringReader
import microtesia.properties._
import scala.xml.Node
import scala.language.implicitConversions

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
 * scala> parse("""&lt;div itemscope itemtype="http://schema.org/Movie"&gt;&lt;h1 itemprop="name"&gt;Avatar&lt;/h1&gt;&lt;/div&gt;""")
 * res0: Either[microtesia.InvalidMicrodata[scala.xml.Node],microtesia.MicrodataDocument] =
 * Right(MicrodataDocument(List(MicrodataItem(Some(http://schema.org/Movie),Map(name -> List(MicrodataString(Avatar))),None))))
 * }}}
 */
package object microtesia {

  /**
   * Represents collection of microdata name-value pairs. Values are represented by a
   * [[MicrodataValue]]. Names map to sequences of values because a name can have
   * multiple values in a microdata document.
   */
  type Properties = Map[String, Seq[MicrodataValue]]

  private[microtesia] type Parsed[M, N] = Either[InvalidMicrodata, M]

  private[microtesia] implicit def toEitherSequence[A, B](seq: Seq[Either[A,B]]) = new EitherSequence(seq)

  private[microtesia] implicit def toMicrodataProperties(p: Properties) = new MicrodataProperties(p)

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

}
