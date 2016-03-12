// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.net.URI

/**
 * Generic microdata value. You can extract microdata values using for-comprehensions:
 * 
 * {{{
 * scala> import microtesia._
 * import microtesia._
 * 
 * scala> val items = List(MicrodataItem(List(MicrodataProperty("name",MicrodataString("Avatar")))))
 * items: List[microtesia.MicrodataItem] = List(MicrodataItem(List((name,MicrodataString(Avatar))),None,None))
 * 
 * scala> for {
 *          MicrodataItem(properties, _, _) <- items
 *          MicrodataProperty("name", MicrodataString(string)) <- properties
 *        } yield string
 * res0: Seq[String] = List(Avatar)
 * }}}
 */
sealed abstract class MicrodataValue

/**
 * Represents a microdata "item". An item consists of a set of
 * [[properties]] (name-value pairs) and an optional [[itemtype]] and unique [[id]].
 * 
 * <strong>Note</strong>  Properties are represented by a sequence of [[MicrodataProperty]]
 * and not a map as a property name can have multiple values in a microdata document.
 * 
 * @param itemtype Optional type of the item defined by a `URI`.
 * @param id Optional unique identifier of the item defined by a `URI`.
 * @param properties Map of name-value pairs.
 */
case class MicrodataItem(
  properties : Seq[MicrodataProperty],
  itemtype   : Option[URI] = None,
  id         : Option[URI] = None
) extends MicrodataValue {

  /**
   * Utility helper to retrieve the item properties for a particular property name.
   */
  def apply(key: String): Seq[MicrodataValue] = for(p <- properties if p._1 == key) yield p._2

}

/**
 * Represents a microdata string value.
 */
case class MicrodataString(value: String) extends MicrodataValue

/**
 * Represents a microdata link value.
 */
case class MicrodataLink(link: URI) extends MicrodataValue
