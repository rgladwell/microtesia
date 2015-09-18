// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import scala.xml._

class InvalidMicrodataSpec extends Specification {

  "InvalidMicrodata should" >> {

    val span = XML.loadString("""<span line="2" column="63">value</span>""")
    val error = InvalidMicrodata[Node]("TEST_ERROR", SaxElement(span, span))

    "return line number for malformed HTML element" >> {
      error.line must beSome(2)
    }

    "return tagsoup error adjusted column for malformed HTML element" >> {
      error.column must beSome(3)
    }

  }

}
