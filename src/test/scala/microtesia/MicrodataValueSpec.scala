package microtesia

import org.specs2.mutable.Specification
import urimplicit._

object MicrodataValueSpec extends Specification {

  "MicrodataValue should" >> {

    "extract string values" >> {
      val strings = Seq(MicrodataString("test"))

      val s = for(MicrodataString(string) <- strings) yield string

      s must contain("test")
    }

    "extract link values" >> {
      val links = Seq(MicrodataLink(URI("http://example.org")))

      val uris = for(MicrodataLink(link) <- links) yield link

      uris must contain(URI("http://example.org"))
    }

    "extract item properties" >> {
      val items = Seq(MicrodataItem(properties = Seq("name" -> MicrodataString("test"))))

      val ps = for(MicrodataItem(properties, _, _) <- items) yield properties

      ps must contain(Seq("name" -> MicrodataString("test")))
    }

    "extract specific item property" >> {
      val items = Seq(MicrodataItem(properties = Seq("name" -> MicrodataString("test"))))

      val names =
        for { 
          MicrodataItem(properties, _, _) <- items
          MicrodataProperty("name", MicrodataString(string)) <- properties
        } yield string

      names must contain("test")
    }

    "extract specific item type property" >> {
      val items = Seq(
        MicrodataItem(properties = Seq("name" -> MicrodataString("test"))),
        MicrodataItem(
          itemtype = Some(URI("http://example.org")),
          properties = Seq("name" -> MicrodataString("test2"))
        )
      )

      val names =
        for{
          MicrodataItem(properties, Some(Uri("http://example.org", _)), _) <- items
          MicrodataProperty("name", MicrodataString(value)) <- properties
        } yield value

      names must contain("test2")
    }

    "retrieve item properties" >> {
      val items = MicrodataItem(Seq("name" -> MicrodataString("test")))
      items("name") must_== Seq(MicrodataString("test"))
    }

    "retrieve multiple item property" >> {
      val items = MicrodataItem(Seq("name" -> MicrodataString("test"), "name" -> MicrodataString("test2")))
      items("name") must_== Seq(MicrodataString("test"), MicrodataString("test2"))
    }

    "do not retrieve non-existent item property" >> {
      val items = MicrodataItem(Seq())
      items("name") must be empty
    }

  }

}
