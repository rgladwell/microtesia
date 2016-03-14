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
import scala.util.{Failure, Success, Try}

object ItemsParserSpec extends Specification with Mockito {

  "ItemsParser should" >> {

    val mockProperties = Seq("name" -> MicrodataString("Frank"))

     trait MockPropertiesParser extends PropertiesParser[Node] {
      this: PropertyParsing[Node] =>
      val mockParseProperties = mock[(Element[Node]) => Try[Seq[MicrodataProperty]]]
      override def parseProperties(element: Element[Node]) = mockParseProperties(element)
      mockParseProperties.apply(any[Element[Node]]) returns Success(mockProperties)
    }

    class TestSaxItemsParser extends UndefinedPropertyParsing[Node]
                                        with ItemsParser[Node]
                                        with MockPropertiesParser
                                        with Scope

    "not parse non-items" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemprop="name">Frank</span>""")
      parseItems(SaxElement(html, html)) must beSuccessfulTry(empty)
    }

    "parse items" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true">Frank</span>""")
      parseItems(SaxElement(html, html)) must beSuccessfulTry(contain(MicrodataItem(properties = mockProperties)))
    }

    "parse items nested" in new TestSaxItemsParser {
      val html = XML.loadString("""<div><span itemscope="true">Frank</span></div>""")
      parseItems(SaxElement(html, html)) must beSuccessfulTry(contain(MicrodataItem(properties = mockProperties)))
    }

    "parse multiple items" in new TestSaxItemsParser {
      val html = XML.loadString("""<div><span itemscope="true">Frank</span>
                                   <span itemscope="true">John</span></div>""")

      parseItems(SaxElement(html, html)) must beSuccessfulTry((items: Seq[MicrodataItem]) => items must have size(2))
    }

    "parse item types" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true" itemtype="http://example.org">Frank</span>""")
      parseItems(SaxElement(html, html)) must beSuccessfulTry(contain{ (i: MicrodataItem) => i.itemtype must beSome(URI("http://example.org")) })
    }

    "parse item ids" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true" itemid="http://example.org">Frank</span>""")
      parseItems(SaxElement(html, html)) must beSuccessfulTry(contain{ (i: MicrodataItem) => i.id must beSome(URI("http://example.org")) })
    }

    "reports errors during property parsing" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<span itemscope="true">Frank</span>""")
      val error = InvalidMicrodata[Node]("INVALID", SaxElement(html,html))
      mockParseProperties.apply(any[SaxElement]) returns Failure(error)

      // then
      parseItems(SaxElement(html, html)) must beFailedTry(error)
    }

    "parse microdata item references" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a"></div>""")
      val reference = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")

      val referenceProperties = Seq("name" -> MicrodataString("Barry"))
      mockParseProperties.apply(SaxElement(reference, html)) returns Success(referenceProperties)

      // then
      parseItems(SaxElement(item, html)) must beSuccessfulTry(contain(MicrodataItem(properties = referenceProperties)))
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
      mockParseProperties.apply(SaxElement(a, html)) returns Success(aProperties)
      mockParseProperties.apply(SaxElement(b, html)) returns Success(bProperties)

      // then
      parseItems(SaxElement(item, html)) must beSuccessfulTry(contain(MicrodataItem(properties = Seq(("name" -> MicrodataString("Amanda")), ("name" -> MicrodataString("Barry"))))))
    }

    "report errors parsing item references properties" in new TestSaxItemsParser {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a"></div>""")
      val reference = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")

      mockParseProperties.apply(SaxElement(reference, html)) returns Failure(InvalidMicrodata[Node]("INVALID", SaxElement(item, html)))

      // then
      parseItems(SaxElement(item, html)) must beFailedTry(beAnInstanceOf[InvalidMicrodata])
    }

    "parse empty itemref attribute" in new TestSaxItemsParser {
      // given
      val saxParser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
      val html = saxParser.loadString("""<div itemscope="true" itemref></div>""")

      // then
      parseItems(SaxElement(html, html)) must beSuccessfulTry
    }

  }

}
