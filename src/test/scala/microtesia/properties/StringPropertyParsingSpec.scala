// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import scala.xml.XML
import microtesia.{SaxElement, MicrodataString}
import org.specs2.specification.Scope
import scala.xml.Node

object StringPropertyParsingSpec extends Specification {

  "StringPropertyParsing should" >> {

    class TestStringPropertyParsing extends UndefinedPropertyParsing[Node]
                                       with StringPropertyParsing[Node]
                                       with Scope

    "parse microdata string properties" in new TestStringPropertyParsing {
      val html = XML.loadString("""<name>value</name>""")
      parseProperty(SaxElement(html,html)) must beRight(MicrodataString("value"))
    }

  }

}
