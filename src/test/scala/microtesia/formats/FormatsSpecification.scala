package microtesia.formats

import org.specs2.mutable.Specification
import urimplicit._
import microtesia._

object FormatsSpecification extends Specification {
  
  "Microtesia formats should" >> {

    "convert microdata strings" in {
      MicrodataString("test").convertTo[String] must_== Right("test")
    }

    "convert microdata links" in {
      MicrodataLink(URI("http://example.org")).convertTo[URI] must_== Right(URI("http://example.org"))
    }

    "convert integers" in {
      MicrodataString("10").convertTo[Int] must_== Right(10)
    }

    "convert doubles" in {
      MicrodataString("10.01").convertTo[Double] must_== Right(10.01)
    }

    "convert floats" in {
      MicrodataString("10.01").convertTo[Float] must_== Right(10.01f)
    }

    "convert long" in {
      MicrodataString("10").convertTo[Long] must_== Right(10)
    }

    "convert short" in {
      MicrodataString("10").convertTo[Short] must_== Right(10)
    }

    "convert boolean" in {
      MicrodataString("true").convertTo[Boolean] must_== Right(true)
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

      microdata.convertTo[Person] must_== Right(Person("hello", 13, true))
    }

  }

}
