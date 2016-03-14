// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import microtesia._
import scala.xml._
import scala.util.{Failure, Success, Try}

object NestedItemPropertyParsingSpec extends Specification with Mockito {

  "NestedItemPropertyParsing should" >> {

    trait MockPropertiesParser extends PropertiesParser[Node] {
      this: PropertyParsing[Node] =>
      override def parseProperties(element: Element[Node]) = ???
    }

    trait MockItemsParser extends ItemsParser[Node] with MockPropertiesParser {
      this: PropertyParsing[Node] =>

      override def parseItems(element: Element[Node]) = ???

      val mockParseItem = mock[(Element[Node]) => Try[MicrodataItem]]
      override def parseItem(element: Element[Node]) = mockParseItem(element)
    }

    class TestNestedItemPropertyParsing extends UndefinedPropertyParsing[Node]
                                                   with NestedItemPropertyParsing[Node]
                                                   with MockItemsParser
                                                   with Scope

    "parse items as properties" in new TestNestedItemPropertyParsing {
      // given
      val html = XML.loadString("""<span itemprop="name" itemscope="true">Frank</span>""")
      val mockItem = MicrodataItem(properties = Seq("testprop" -> MicrodataString("")))
      mockParseItem.apply(any[Element[Node]]) returns Success(mockItem)

      // then
      parseProperty(SaxElement(html,html)) must beSuccessfulTry(mockItem)
    }

    "reports errors during sub-item parsing" in new TestNestedItemPropertyParsing {
      // given
      val html = XML.loadString("""<span itemprop="name" itemscope="true">Frank</span>""")
      val error = InvalidMicrodata[Node]("INVALID", SaxElement(html,html))
      mockParseItem.apply(any[Element[Node]]) returns Failure(error)

      // then
      parseProperty(SaxElement(html,html)) must beFailedTry(error)
    }

  }

}
