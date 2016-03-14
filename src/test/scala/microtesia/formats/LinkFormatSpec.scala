package microtesia.formats

import org.specs2.mutable.Specification
import microtesia._
import urimplicit._

object LinkFormatSpec extends Specification {

  "LinkFormat should" >> {

    "convert microdata strings" in {
      LinkFormat.read(MicrodataLink(URI("http://example.org"))) must beSuccessfulTry(URI("http://example.org"))
    }

    "not convert non-string microdata" in {
      LinkFormat.read(MicrodataString("test")) must beFailedTry
    }

  }

}
