// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.{SaxElement, MicrodataString}
import scala.xml._

object TimePropertyParsingSpec extends Specification {
  
  "TimePropertyParsing should" >> {

    class TestNestedItemPropertyParsing extends UndefinedPropertyParsing[Node]
                                           with TimePropertyParsing[Node]
                                           with Scope

    "parse time element values" in new TestNestedItemPropertyParsing {
      val html = XML.loadString("""<time>2005-01-01</time>""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataString("2005-01-01"))
    }

    "parse datetime element attributes" in new TestNestedItemPropertyParsing {
      val html = XML.loadString("""<time datetime="2005-01-01">1st January 2005</time>""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataString("2005-01-01"))
    }

    "not parse non-time elements" in new TestNestedItemPropertyParsing {
      val html = XML.loadString("""<span>value</span>""")
      parseProperty must not(beDefinedAt(SaxElement(html,html)))
    }

    "parse empty time elements" in new TestNestedItemPropertyParsing {
      val html = XML.loadString("""<time/>""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataString(""))
    }

  }
}
