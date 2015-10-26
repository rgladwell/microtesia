// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import microtesia.properties.{PropertiesParser, PropertyParsing, UndefinedPropertyParsing}
import scala.xml._
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import urimplicit._

object ItemsParserSpec extends Specification with Mockito {

  "ItemsParser should" >> {

    val mockProperties = Seq("name" -> MicrodataString("Frank"))

     trait MockPropertiesParser extends PropertiesParser[Node] {
      this: PropertyParsing[Node] =>
      val mockParseProperties = mock[(Element[Node]) => Parsed[Seq[MicrodataProperty], Node]]
      override def parseProperties(element: Element[Node]) = mockParseProperties(element)
      mockParseProperties.apply(any[Element[Node]]) returns Right(mockProperties)
    }

    class TestSaxItemsParser extends UndefinedPropertyParsing[Node]
                                        with ItemsParser[Node]
                                        with MockPropertiesParser
                                        with Scope

    "not parse non-items" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemprop="name">Frank</span>""")
      parseItems(SaxElement(html, html)) must beRight(empty)
    }

    "parse items" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true">Frank</span>""")
      parseItems(SaxElement(html, html)) must beRight(contain(MicrodataItem(properties = mockProperties)))
    }

    "parse items nested" in new TestSaxItemsParser {
      val html = XML.loadString("""<div><span itemscope="true">Frank</span></div>""")
      parseItems(SaxElement(html, html)) must beRight(contain(MicrodataItem(properties = mockProperties)))
    }

    "parse multiple items" in new TestSaxItemsParser {
      val html = XML.loadString("""<div><span itemscope="true">Frank</span>
                                   <span itemscope="true">John</span></div>""")

      parseItems(SaxElement(html, html)) must beRight((items: Seq[MicrodataItem]) => items must have size(2))
    }

    "parse item types" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true" itemtype="http://example.org">Frank</span>""")
      parseItems(SaxElement(html, html)) must beRight(contain{ (i: MicrodataItem) => i.itemtype must beSome(URI("http://example.org")) })
    }

    "parse item ids" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true" itemid="http://example.org">Frank</span>""")
      parseItems(SaxElement(html, html)) must beRight(contain{ (i: MicrodataItem) => i.id must beSome(URI("http://example.org")) })
    }

    "reports errors during property parsing" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<span itemscope="true">Frank</span>""")
      val error = InvalidMicrodata[Node]("INVALID", SaxElement(html,html))
      mockParseProperties.apply(any[SaxElement]) returns Left(error)

      // then
      parseItems(SaxElement(html, html)) must beLeft(error)
    }

    "parse microdata item references" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a"></div>""")
      val reference = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")

      val referenceProperties = Seq("name" -> MicrodataString("Barry"))
      mockParseProperties.apply(SaxElement(reference, html)) returns Right(referenceProperties)

      // then
      parseItems(SaxElement(item, html)) must beRight(contain(MicrodataItem(properties = referenceProperties)))
    }

    "parse microdata with multiple item references" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a b"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p>
                                       <p id="b">Name: <span itemprop="name">Barry</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a b"></div>""")
      val a = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")
      val b = XML.loadString("""<p id="b">Name: <span itemprop="name">Barry</span></p>""")

      val aProperties = Seq("name" -> MicrodataString("Amanda"))
      val bProperties = Seq("name" -> MicrodataString("Barry"))
      mockParseProperties.apply(SaxElement(a, html)) returns Right(aProperties)
      mockParseProperties.apply(SaxElement(b, html)) returns Right(bProperties)

      // then
      parseItems(SaxElement(item, html)) must beRight(contain(MicrodataItem(properties = Seq(("name" -> MicrodataString("Amanda")), ("name" -> MicrodataString("Barry"))))))
    }

    "report errors parsing item references properties" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a"></div>""")
      val reference = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")

      mockParseProperties.apply(SaxElement(reference, html)) returns Left(InvalidMicrodata[Node]("INVALID", SaxElement(item, html)))

      // then
      parseItems(SaxElement(item, html)) must beLeft(beAnInstanceOf[InvalidMicrodata])
    }

    "parse empty itemref attribute" in new TestSaxItemsParser {
      // given
      val saxParser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
      val html = saxParser.loadString("""<div itemscope="true" itemref></div>""")

      // then
      parseItems(SaxElement(html, html)) must beRight
    }

  }

}
