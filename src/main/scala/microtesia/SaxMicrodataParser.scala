// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.io.Reader
import java.net.URI
import scala.xml._
import microtesia.properties.{PropertiesParser, UndefinedPropertyParsing}

private class SaxMicrodataParser extends UndefinedPropertyParsing[Node] with MicrodataParser[Node] {
  this: Sax with ItemsParser[Node] =>

  override def parse(input: Reader): Parsed[MicrodataDocument, Node] = {
    val document = html(input)
    parseItems(SaxElement(document, document))
     .right
     .map(MicrodataDocument(_))
  }

  private def html(input: Reader) = saxParser load input

}
