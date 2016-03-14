// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.{SaxElement, MicrodataString}
import scala.xml._

object MeterPropertyParsingSpec extends Specification {
  
  "SaxMeterPropertyParsing should" >> {

    class TestMeterPropertyParsing extends UndefinedPropertyParsing[Node]
                                      with MeterPropertyParsing[Node]
                                      with Scope

    "parse meter elements" in new TestMeterPropertyParsing {
      val html = XML.loadString("""<meter value="1">One</meter>""")
      parseProperty(SaxElement(html,html)) must beSuccessfulTry(MicrodataString("1"))
    }

    "not parse non-meter elements" in new TestMeterPropertyParsing {
      val html = XML.loadString("""<span>value</span>""")
      parseProperty must not(beDefinedAt(SaxElement(html,html)))
    }

    "parse empty meter elements" in new TestMeterPropertyParsing {
      val html = XML.loadString("""<meter/>""")
      parseProperty(SaxElement(html,html)) must beSuccessfulTry(MicrodataString(""))
    }

  }

}
