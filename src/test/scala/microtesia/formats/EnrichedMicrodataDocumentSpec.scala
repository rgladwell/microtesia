package microtesia.formats

import org.specs2.mutable.Specification
import microtesia._
import urimplicit._

object EnrichedMicrodataDocumentSpec extends Specification {

  "EnrichedMicrodataDocument should" >> {

    val itemtype = URI("http://example.org/schemas/person")

    case class Person(name: String)

    "convert root items" >> {
      val document =
        MicrodataDocument(
          Seq(
            MicrodataItem(
              itemtype = Some(itemtype),
              properties = Seq(
                "name" -> MicrodataString("Emily")
              )
            )
          )
        )

      document.convertRootsTo[Person](itemtype) should beSuccessfulTry.withValue{ contain (Person("Emily")) }
    }

    "ignore non-root items" >> {
      val document =
        MicrodataDocument(
          Seq(
            MicrodataItem(
              itemtype = Some(itemtype),
              properties = Seq(
                "name" -> MicrodataString("Emily"),
                "friend" ->
                  MicrodataItem(
                    itemtype = Some(itemtype),
                    properties = Seq(
                      "name" -> MicrodataString("Sally")
                    )
                  )
              )
            )
          )
        )

      document.convertRootsTo[Person](itemtype) should beSuccessfulTry.withValue{ not (contain (Person("Sally"))) }
    }

    "ignore un-typed items" >> {
      val document =
        MicrodataDocument(
          Seq(
            MicrodataItem(
              properties = Seq(
                "name" -> MicrodataString("Emily")
              )
            )
          )
        )

      document.convertRootsTo[Person](itemtype) should beSuccessfulTry.withValue{ not (contain (Person("Emily"))) }
    }

  }

}
