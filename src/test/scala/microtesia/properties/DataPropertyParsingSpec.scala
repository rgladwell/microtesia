// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.{SaxElement, MicrodataString}
import scala.xml._

object DataPropertyParsingSpec extends Specification {

  "DataPropertyParsing should" >> {

    class TestDataPropertyParsing extends UndefinedPropertyParsing[Node]
                                     with DataPropertyParsing[Node]
                                     with Scope

    "parse data elements" in new TestDataPropertyParsing {
      val html = XML.loadString("""<data value="test-value">other</data>""")
      parseProperty(SaxElement(html, html)) must beSuccessfulTry(MicrodataString("test-value"))
    }

    "not parse non-data elements" in new TestDataPropertyParsing {
      val html = XML.loadString("""<span>value</span>""")
      parseProperty must not(beDefinedAt(SaxElement(html, html)))
    }

    "parse empty data elements" in new TestDataPropertyParsing {
      val html = XML.loadString("""<data/>""")
      parseProperty(SaxElement(html, html)) must beSuccessfulTry(MicrodataString(""))
    }

  }
}
