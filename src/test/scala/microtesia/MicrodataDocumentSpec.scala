// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import urimplicit._

object MicrodataDocumentSpec extends Specification {
  
  "MicrodataDocument should" >> {

    val person = MicrodataItem(Some(URI("http://example.org/person")), Map("name" -> Seq(MicrodataString("Frank"))))

    "find items based on item type" >> {
      val document = MicrodataDocument(Seq(person))
      document.findItems(URI("http://example.org/person")) must contain(person)
    }

    "does not find items for non-existent type" >> {
      val document = MicrodataDocument(Seq(person))
      document.findItems(URI("http://example.org/cat")) must not contain(person)
    }

    "find items based on item type" >> {
      val document = MicrodataDocument(Seq(person, person))
      document.findItems(URI("http://example.org/person")) must haveSize(2)
    }

    "find nested items" >> {
      val organisation = MicrodataItem(Some(URI("http://example.org/org")), Map("people" -> Seq(person)))
      val document = MicrodataDocument(Seq(organisation))

      document.findItems(URI("http://example.org/person")) must contain(person)
    }

  }

}
