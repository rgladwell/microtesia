// Copyright 2015, 2018 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import scala.xml._

object ElementSpec extends Specification {

  "Element should" >> {

    "parse an HTML element" >> {
      val span = XML.loadString("""<span class="name p-nickname">test-value</span>""")
      val element = SaxElement(span, span)

      "name" >> {
        element.name must_== "span"
      }

      "value" >> {
        element.value must_== "test-value"
      }

      "attributes" >> {
        element.hasAttr("class") must beTrue
      }

      "attribute values" >> {
        element.attr("class") must beSome("name p-nickname")
      }

      "attributes and trim whitespace" >> {
        val spanWithWhitespace = XML.loadString("""<span class="   name p-nickname     " />""")
        val elementWithWhitespace = SaxElement(spanWithWhitespace, span)

        elementWithWhitespace.attr("class") must beSome("name p-nickname")
      }

      "ignoring empty attributes" >> {
        val saxParser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
        val spanWithEmptyAttribute = saxParser.loadString("""<span class />""").child.head.child.head
        val elementWithEmptyAttribute = SaxElement(spanWithEmptyAttribute, span)

        elementWithEmptyAttribute.hasAttr("class") must beTrue
      }

      "parse empty attributes" >> {
        val saxParser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
        val spanWithEmptyAttribute = saxParser.loadString("""<span class />""").child.head.child.head
        val elementWithEmptyAttribute = SaxElement(spanWithEmptyAttribute, span)

        elementWithEmptyAttribute.attr("class") must beNone
      }
    }

    "find elements in document" >> {
      val document = XML.loadString("""<span class="name p-nickname"><div id="test">TEST-VALUE</div></span>""")
      val div = XML.loadString("""<div id="test">TEST-VALUE</div>""")
      val element = SaxElement(document, document)
      element.doc.findById("test") must beSome(SaxElement(div, document))
    }

    "not find non-existent elements in document" >> {
      val document = XML.loadString("""<span class="name p-nickname"></span>""")
      val element = SaxElement(document, document)
      element.doc.findById("test") must beNone
    }

    "map children" >> {
      val document = XML.loadString("""<span class="name p-nickname"><div id="test"/></span>""")
      val element = SaxElement(document, document)

      def function(element: Element[Node]) = element.attr("id") == Some("test")

      element.childMap { function } must_== Seq(true)
    }
  }

}
