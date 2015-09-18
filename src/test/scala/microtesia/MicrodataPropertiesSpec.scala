// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification

object MicrodataPropertiesSpec extends Specification {

  "MicrodataProperties should" >> {

    "correctly merge properies" >> {
      val properties1 = Map("name" -> Seq(MicrodataString("name1"), MicrodataString("name2")))
      val properties2 = Map("name" -> Seq(MicrodataString("name3")))

      properties1 merge properties2 must_== Map("name" -> Seq(MicrodataString("name1"),
          MicrodataString("name2"), MicrodataString("name3")))
    }

  }

}
