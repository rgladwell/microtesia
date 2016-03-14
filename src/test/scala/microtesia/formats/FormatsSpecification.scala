package microtesia.formats

import org.specs2.mutable.Specification
import urimplicit._
import microtesia._

object FormatsSpecification extends Specification {
  
  "Microtesia formats should" >> {

    "convert microdata strings" in {
      MicrodataString("test").convertTo[String] must beSuccessfulTry("test")
    }

    "convert microdata links" in {
      MicrodataLink(URI("http://example.org")).convertTo[URI] must beSuccessfulTry(URI("http://example.org"))
    }

    "convert integers" in {
      MicrodataString("10").convertTo[Int] must beSuccessfulTry(10)
    }

    "convert doubles" in {
      MicrodataString("10.01").convertTo[Double] must beSuccessfulTry(10.01)
    }

    "convert floats" in {
      MicrodataString("10.01").convertTo[Float] must beSuccessfulTry(10.01f)
    }

    "convert long" in {
      MicrodataString("10").convertTo[Long] must beSuccessfulTry(10)
    }

    "convert short" in {
      MicrodataString("10").convertTo[Short] must beSuccessfulTry(10)
    }

    "convert boolean" in {
      MicrodataString("true").convertTo[Boolean] must beSuccessfulTry(true)
    }

    "convert flat case classes" in {
      case class Person(name: String, age: Int, adult: Boolean)

      val microdata =
        MicrodataItem(
          Seq(
            ("name", MicrodataString("hello")),
            ("age", MicrodataString("13")),
            ("adult", MicrodataString("true"))
          )
        )

      microdata.convertTo[Person] must beSuccessfulTry(Person("hello", 13, true))
    }

  }

}
