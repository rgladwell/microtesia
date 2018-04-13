// Copyright 2015, 2018 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.properties.{
  PropertiesParser,
  PropertyParsing,
  UndefinedPropertyParsing
}
import scala.xml._
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import urimplicit._
import scala.util.{Failure, Success, Try}

object ItemsParserSpec extends Specification {

  "ItemsParser should" >> {

    val testProperties = Seq("name" -> MicrodataString("Frank"))

    trait MockPropertiesParser extends PropertiesParser[Node] {
      this: PropertyParsing[Node] =>

      def mockProperties(element: Element[Node]): Try[Seq[MicrodataProperty]]
      override def parseProperties(element: Element[Node]) = mockProperties(element)
    }

    class TestSaxItemsParser(properties: Try[Seq[MicrodataProperty]] = Success(testProperties)) extends Scope {
      val parser = new UndefinedPropertyParsing[Node]
                         with MockPropertiesParser
                         with ItemsParser[Node] {

        override def mockProperties(element: Element[Node]) = properties

      }
    }

    "not parse non-items" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemprop="name">Frank</span>""")
      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry(empty)
    }

    "parse items" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true">Frank</span>""")
      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry(contain(MicrodataItem(properties = testProperties)))
    }

    "parse items nested" in new TestSaxItemsParser {
      val html = XML.loadString("""<div><span itemscope="true">Frank</span></div>""")
      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry(contain(MicrodataItem(properties = testProperties)))
    }

    "parse multiple items" in new TestSaxItemsParser {
      val html = XML.loadString("""<div><span itemscope="true">Frank</span>
                                   <span itemscope="true">John</span></div>""")

      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry((items: Seq[MicrodataItem]) => items must have size(2))
    }

    "parse item types" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true" itemtype="http://example.org">Frank</span>""")
      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry(contain{ (i: MicrodataItem) => i.itemtype must beSome(URI("http://example.org")) })
    }

    "parse item ids" in new TestSaxItemsParser {
      val html = XML.loadString("""<span itemscope="true" itemid="http://example.org">Frank</span>""")
      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry(contain{ (i: MicrodataItem) => i.id must beSome(URI("http://example.org")) })
    }

    val html = XML.loadString("""<span itemscope="true">Frank</span>""")
    val error = InvalidMicrodata[Node]("INVALID", SaxElement(html,html))

    "reports errors during property parsing" in new TestSaxItemsParser(Failure(error)) {
      parser.parseItems(SaxElement(html, html)) must beFailedTry(error)
    }

    val referenceProperties = Seq("name" -> MicrodataString("Barry"))

    "parse microdata item references" in new TestSaxItemsParser(Success(referenceProperties)) {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a"></div>""")
      val reference = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")

      // then
      parser.parseItems(SaxElement(item, html)) must beSuccessfulTry(contain(MicrodataItem(properties = referenceProperties)))
    }

    val aProperties = Seq("name" -> MicrodataString("Amanda"))
    val bProperties = Seq("name" -> MicrodataString("Barry"))

    class MultipleItemsTestSaxItemsParser extends Scope {
      val a = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")
      val b = XML.loadString("""<p id="b">Name: <span itemprop="name">Barry</span></p>""")

      val parser = new UndefinedPropertyParsing[Node]
                         with MockPropertiesParser
                         with ItemsParser[Node] {

        override def mockProperties(element: Element[Node]) = {
          if(element.attr("id") == Some("a")) Success(aProperties)
          else if(element.attr("id") == Some("b")) Success(bProperties)
          else Success(testProperties)
        }

      }
    }

    "parse microdata with multiple item references" in new MultipleItemsTestSaxItemsParser {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a b"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p>
                                       <p id="b">Name: <span itemprop="name">Barry</span></p></div>""")
      val item = XML.loadString("""<div itemscope="true" itemref="a b"></div>""")

      // then
      parser.parseItems(SaxElement(item, html)) must beSuccessfulTry(contain(MicrodataItem(properties = Seq(("name" -> MicrodataString("Amanda")), ("name" -> MicrodataString("Barry"))))))
    }

    val item = XML.loadString("""<div itemscope="true" itemref="a"></div>""")

    "report errors parsing item references properties" in new TestSaxItemsParser(Failure(InvalidMicrodata[Node]("INVALID", SaxElement(item, html)))) {
      // given
      val html = XML.loadString("""<div><div itemscope="true" itemref="a"></div>
                                       <p id="a">Name: <span itemprop="name">Amanda</span></p></div>""")

      val reference = XML.loadString("""<p id="a">Name: <span itemprop="name">Amanda</span></p>""")

      // then
      parser.parseItems(SaxElement(item, html)) must beFailedTry(beAnInstanceOf[InvalidMicrodata])
    }

    "parse empty itemref attribute" in new TestSaxItemsParser {
      // given
      val saxParser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
      val html = saxParser.loadString("""<div itemscope="true" itemref></div>""")

      // then
      parser.parseItems(SaxElement(html, html)) must beSuccessfulTry
    }

  }

}
