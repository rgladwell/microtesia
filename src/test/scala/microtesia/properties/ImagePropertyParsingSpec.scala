// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.{SaxElement, InvalidMicrodata, MicrodataLink}
import scala.xml._
import urimplicit._

object ImagePropertyParsingSpec extends Specification {
  
  "ImagePropertyParsing should" >> {

    class TestImagePropertyParsing extends UndefinedPropertyParsing[Node]
                                      with ImagePropertyParsing[Node]
                                      with Scope

    "parse hyperlink elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<img src="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }

    "not parse non-hyperlink elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<span src="http://example.org">value</span>""")
      parseProperty must not(beDefinedAt(SaxElement(html,html)))
    }

    "not parse empty hyperlink elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<img/>""")
      parseProperty(SaxElement(html,html)) must beLeft(beAnInstanceOf[InvalidMicrodata])
    }

    "parse audio elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<audio src="http://example.org">other</audio>""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }

    "parse embed elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<embed src="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }

    "parse iframe elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<iframe src="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }

    "parse source elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<source src="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }

    "parse track elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<track src="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }

    "parse video elements" in new TestImagePropertyParsing {
      val html = XML.loadString("""<video src="http://example.org" />""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataLink(URI("http://example.org")))
    }
  }
}