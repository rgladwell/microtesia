// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import urimplicit._

object MicrodataDocumentSpec extends Specification {
  
  "MicrodataDocument" >> {

    val person = MicrodataItem(
      itemtype   = Some(URI("http://example.org/person")),
      properties = Seq("name" -> MicrodataString("Frank"))
    )

    val organisation = MicrodataItem(
      itemtype   = Some(URI("http://example.org/org")),
      properties = Seq("people" -> person)
    )

    "items should" >> {
      "find all items based on item type" >> {
        val document = MicrodataDocument(Seq(organisation))
        document.items(URI("http://example.org/person")) must contain(person)
      }
  
      "does not find items for non-existent type" >> {
        val document = MicrodataDocument(Seq(organisation))
        document.items(URI("http://example.org/cat")) must not contain(person)
      }
  
      "find items based on item type" >> {
        val document = MicrodataDocument(Seq(organisation, organisation))
        document.items(URI("http://example.org/person")) must haveSize(2)
      }

      "find root items" >> {
        val document = MicrodataDocument(Seq(organisation))
        document.items(URI("http://example.org/org")) must contain(organisation)
      }
    }

    "rootItems should" >> {
      "find items based on item type" >> {
        val document = MicrodataDocument(Seq(person))
        document.rootItems(URI("http://example.org/person")) must contain(person)
      }
  
      "does not find items for non-existent type" >> {
        val document = MicrodataDocument(Seq(person))
        document.rootItems(URI("http://example.org/cat")) must not contain(person)
      }
  
      "find items based on item type" >> {
        val document = MicrodataDocument(Seq(person, person))
        document.rootItems(URI("http://example.org/person")) must haveSize(2)
      }
  
      "ignore nested items" >> {
        val organisation = MicrodataItem(
          itemtype   = Some(URI("http://example.org/org")),
          properties = Seq("people" -> person)
        )
        val document = MicrodataDocument(Seq(organisation))
  
        document.rootItems(URI("http://example.org/person")) must not contain(person)
      }
    }

  }

}
