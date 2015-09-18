// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import scala.xml._

object TagSpec extends Specification {

  "Tag should" >> {
    val span = XML.loadString("""<span class="name p-nickname">test-value</span>""")
    val element = SaxElement(span, span)

    "match an HTML element" >> {
      element must beLike{ case tag @ Tag(_) => tag.name must_== "span" }
    }

    "match an HTML element with label" >> {
      element must beLike{ case tag @ Tag("span") => tag.name must_== "span" }
    }

  }

}
