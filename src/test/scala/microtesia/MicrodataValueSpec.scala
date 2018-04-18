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

    "query item properties" >> {
      val items = MicrodataItem(Seq("name" -> MicrodataString("test")))
      (items \ "name" results) must_== Seq(MicrodataString("test"))
    }

    "query multiple item property" >> {
      val items = MicrodataItem(Seq("name" -> MicrodataString("test"), "name" -> MicrodataString("test2")))
      (items \ "name" results) must_== Seq(MicrodataString("test"), MicrodataString("test2"))
    }

    "do not retrieve non-existent item property" >> {
      val items = MicrodataItem(Seq())
      (items \ "name" results) must be empty
    }

    "query nested item properties" >> {
      val item =
        MicrodataItem(
          Seq("subitem" ->
            MicrodataItem(
              Seq("name" -> MicrodataString("test name"))
            )
          )
        )

      val results: Seq[MicrodataValue] = (item \ "subitem" \ "name").results
      results must_== Seq(MicrodataString("test name"))
    }

    "query nested item properties across muliple trees" >> {
      val item =
        MicrodataItem(
          Seq("subitem" ->
            MicrodataItem(
              Seq("name" -> MicrodataString("test name"))
            ),
            "subitem" ->
            MicrodataItem(
              Seq("name" -> MicrodataString("test name 2"))
            )
          )
        )

      (item \ "subitem" \ "name" results) must_== Seq(MicrodataString("test name"), MicrodataString("test name 2"))
    }

    "reucursively query nested item properties" >> {
      val item =
        MicrodataItem(
          Seq("subitem" ->
            MicrodataItem(
              Seq("subsubitem" ->
                MicrodataItem(
                  Seq("name" -> MicrodataString("test name"))
                )
              )
            )
          )
        )

      (item \\ "name").results must_== Seq(MicrodataString("test name"))
    }

  }

}
