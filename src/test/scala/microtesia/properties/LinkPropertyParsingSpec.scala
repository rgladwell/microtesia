// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.{SaxElement, InvalidMicrodata, MicrodataLink}
import urimplicit._
import scala.xml._

object LinkPropertyParsingSpec extends Specification {

  "LinkPropertyParsing should" >> {

    class TestLinkPropertyParsing extends UndefinedPropertyParsing[Node]
                                     with LinkPropertyParsing[Node]
                                     with Scope

    "parse hyperlink elements" in new TestLinkPropertyParsing {
      val html = XML.loadString("""<a href="http://example.org">other</a>""")
      parseProperty(SaxElement(html,html)) must beSuccessfulTry(MicrodataLink(URI("http://example.org")))
    }

    "not parse non-hyperlink elements" in new TestLinkPropertyParsing {
      val html = XML.loadString("""<span href="http://example.org">value</span>""")
      parseProperty must not(beDefinedAt(SaxElement(html,html)))
    }

    "not parse empty hyperlink elements" in new TestLinkPropertyParsing {
      val html = XML.loadString("""<a/>""")
      parseProperty(SaxElement(html,html)) must beFailedTry(beAnInstanceOf[InvalidMicrodata])
    }

    "parse area elements" in new TestLinkPropertyParsing {
      val html = XML.loadString("""<area href="http://example.org">other</area>""")
      parseProperty(SaxElement(html,html)) must beSuccessfulTry(MicrodataLink(URI("http://example.org")))
    }

    "parse link elements" in new TestLinkPropertyParsing {
      val html = XML.loadString("""<link href="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beSuccessfulTry(MicrodataLink(URI("http://example.org")))
    }

  }
}
