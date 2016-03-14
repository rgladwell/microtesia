// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import microtesia.{SaxElement, InvalidMicrodata, MicrodataString, MicrodataMatchers}
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import scala.xml._

object PropertiesParserSpec extends Specification with MicrodataMatchers {

  "PropertiesParser should" >> {

    class TestStringPropertyParsing extends UndefinedPropertyParsing[Node]
                                              with PropertiesParser[Node]
                                              with StringPropertyParsing[Node]
                                              with Scope

    "parse properties" in new TestStringPropertyParsing {
      val html = XML.loadString("""<span itemprop="name">Frank</span>""")
      parseProperties(SaxElement(html,html)) must beSuccessfulTry(haveProperty("name" -> MicrodataString("Frank")))
    }

    "report parse error on property without name" in new TestStringPropertyParsing {
      val saxParser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
      val html = saxParser.loadString("""<span itemprop>Frank</span>""")
      parseProperties(SaxElement(html,html)) must beFailedTry(beAnInstanceOf[InvalidMicrodata])
    }

    "parse properties with multiple names" in new TestStringPropertyParsing {
      val html = XML.loadString("""<span itemprop="name nickname">Frank</span>""")
      parseProperties(SaxElement(html,html)) must beSuccessfulTry(haveProperty("name" -> MicrodataString("Frank"))
                                              and haveProperty("nickname" -> MicrodataString("Frank")))
    }

  }

}
