// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.matcher.{Matcher, MatchResult, Matchers, MustMatchers}
import scala.xml.Node

trait MicrodataMatchers extends Matchers with MustMatchers {

  def beDocument(matcher: (MicrodataDocument) => MatchResult[Any]): Matcher[Either[InvalidMicrodata, MicrodataDocument]] =
    beRight.like{ case doc: MicrodataDocument => matcher(doc) }

  def haveProperty(p: (String, MicrodataValue)): Matcher[MicrodataItem] = { item: MicrodataItem =>
    item.properties must haveKey(p._1)
    item.properties(p._1) must contain(p._2)
  }

  def havePropertyMatching(p: (String, Matcher[MicrodataValue])): Matcher[MicrodataItem] = { item: MicrodataItem =>
    item.properties must haveKey(p._1)
    item.properties(p._1) must contain(p._2)
  }

}
