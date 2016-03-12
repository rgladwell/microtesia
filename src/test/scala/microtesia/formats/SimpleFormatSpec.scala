package microtesia.formats

import org.specs2.mutable.Specification
import microtesia._
import urimplicit._

object SimpleFormatSpec extends Specification {

  "SimpleFormat should" >> {

    import ValueReads._

    "convert integers" in {
      val format = new SimpleFormat[Int]
      format.read(MicrodataString("10")) must_== Right(10)
    }

    "convert double" in {
      val format = new SimpleFormat[Double]
      format.read(MicrodataString("10.01")) must_== Right(10.01)
    }

    "not convert non-values" in {
      val format = new SimpleFormat[Int]
      format.read(MicrodataLink(URI("http://example.org"))) must beLeft
    }

  }

}
