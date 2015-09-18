// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.net.URI

/**
 * Generic microdata value.
 */
sealed trait MicrodataValue

/**
 * Represents a microdata "item". An item consists of a set of
 * [[properties]] (name-value pairs) and an optional [[itemtype]] and [[id]].
 * 
 * @param itemtype Optional type of the item uniquely defined by a `URI`.
 * @param id Optional identifier of the item uniquely defined by a `URI`.
 * @param properties Map of name-value pairs.
 */
case class MicrodataItem(
  itemtype   : Option[URI] = None,
  properties : Properties,
  id         : Option[URI] = None
) extends MicrodataValue

/**
 * Represents a microdata string value.
 */
case class MicrodataString(value: String) extends MicrodataValue

/**
 * Represents a microdata link value.
 */
case class MicrodataLink(link: URI) extends MicrodataValue
