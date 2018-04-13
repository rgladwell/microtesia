// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia._
import scala.xml._
import scala.util.{Failure, Success, Try}

object NestedItemPropertyParsingSpec extends Specification {

  "NestedItemPropertyParsing should" >> {

    trait MockPropertiesParser extends PropertiesParser[Node] {
      this: PropertyParsing[Node] =>
      override def parseProperties(element: Element[Node]) = ???
    }

    trait MockItemsParser extends ItemsParser[Node] with MockPropertiesParser {
      this: PropertyParsing[Node] =>

      override def parseItems(element: Element[Node]) = ???

      val mockParseItem: Try[MicrodataItem]
      override def parseItem(element: Element[Node]) = mockParseItem
    }

    class TestNestedItemPropertyParsing(item: Try[MicrodataItem]) extends Scope {
      val parser = new UndefinedPropertyParsing[Node]
                         with NestedItemPropertyParsing[Node]
                         with MockItemsParser {

        override val mockParseItem = item

      }
    }

    val testItem = MicrodataItem(properties = Seq("testprop" -> MicrodataString("")))

    "parse items as properties" in new TestNestedItemPropertyParsing(Success(testItem)) {
      val html = XML.loadString("""<span itemprop="name" itemscope="true">Frank</span>""")
      parser.parseProperty(SaxElement(html,html)) must beSuccessfulTry(testItem)
    }

    val html = XML.loadString("""<span itemprop="name" itemscope="true">Frank</span>""")
    val error = InvalidMicrodata[Node]("INVALID", SaxElement(html,html))

    "reports errors during sub-item parsing" in new TestNestedItemPropertyParsing(Failure(error)) {
      parser.parseProperty(SaxElement(html,html)) must beFailedTry(error)
    }

  }

}
