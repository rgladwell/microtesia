// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import microtesia.{Element, SaxElement, InvalidMicrodata, MicrodataItem, Parsed}
import microtesia.ItemsParser
import scala.xml._

object NestedItemPropertyParsingSpec extends Specification with Mockito {

  "NestedItemPropertyParsing should" >> {

    trait MockPropertiesParser extends PropertiesParser[Node] {
      this: PropertyParsing[Node] =>
      override def parseProperties(element: Element[Node]) = ???
    }

    trait MockItemsParser extends ItemsParser[Node] with MockPropertiesParser {
      this: PropertyParsing[Node] =>

      override def parseItems(element: Element[Node]) = ???

      val mockParseItem = mock[(Element[Node]) => Parsed[MicrodataItem, Node]]
      override def parseItem(element: Element[Node]) = mockParseItem(element)
    }

    class TestNestedItemPropertyParsing extends UndefinedPropertyParsing[Node]
                                                   with NestedItemPropertyParsing[Node]
                                                   with MockItemsParser
                                                   with Scope

    "parse items as properties" in new TestNestedItemPropertyParsing {
      // given
      val html = XML.loadString("""<span itemprop="name" itemscope="true">Frank</span>""")
      val mockItem = MicrodataItem(properties = Map("testprop" -> Seq()))
      mockParseItem.apply(any[Element[Node]]) returns Right(mockItem)

      // then
      println(s"parseProperty=${parseProperty.getClass}")
      parseProperty(SaxElement(html,html)) must beRight(mockItem)
    }

    "reports errors during sub-item parsing" in new TestNestedItemPropertyParsing {
      // given
      val html = XML.loadString("""<span itemprop="name" itemscope="true">Frank</span>""")
      val error = InvalidMicrodata[Node]("INVALID", SaxElement(html,html))
      mockParseItem.apply(any[Element[Node]]) returns Left(error)

      // then
      parseProperty(SaxElement(html,html)) must beLeft(error)
    }

  }

}
