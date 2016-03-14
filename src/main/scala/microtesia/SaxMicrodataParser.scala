// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.io.Reader
import java.net.URI
import scala.xml._
import microtesia.properties.{PropertiesParser, UndefinedPropertyParsing}
import scala.util.Try

private class SaxMicrodataParser extends UndefinedPropertyParsing[Node] with MicrodataParser {
  this: Sax with ItemsParser[Node] =>

  override def parse(input: Reader): Try[MicrodataDocument] = {
    val document = html(input)

    parseItems(SaxElement(document, document))
      .map(MicrodataDocument(_))
  }

  private def html(input: Reader) = saxParser load input

}
