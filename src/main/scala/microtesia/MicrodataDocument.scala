// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.net.URI

/**
 * Representation of a document containing microdata mark-up. Returned by the
 * [[parseMicrodata]] function. A [[MicrodataDocument]] can contain multiple
 * [[MicrodataItem]] instances which can be accessed via the `rootItems` property
 * as follows:
 * 
 * {{{
 * scala> document.rootItems.foreach{ (m: MicrodataItem) => println(m.itemtype) }
 * Some(http://schema.org/Movie)
 * }}}
 */
case class MicrodataDocument private[microtesia] (val rootItems: Seq[MicrodataItem]) {

  private lazy val allItems: Seq[MicrodataItem] = {

    def isMicrodataItem(value: MicrodataValue): Option[MicrodataItem] = value match {
      case item: MicrodataItem => Some(item)
      case _                   => None
    }

    def recurse(items: Seq[MicrodataItem]): Seq[MicrodataItem] = {
      val subvalues: Seq[MicrodataValue] = items.map{ _.properties.toMap.values }.flatten
      val subitems = subvalues.map{ isMicrodataItem(_) }.flatten

      if(subitems.isEmpty) items
      else items ++ recurse(subitems)
    }

    recurse(rootItems)

  }

  private def matchingItemType(item: MicrodataItem, itemtype: URI) = item.itemtype.exists { _ == itemtype }

  /**
   * Utility method to help collect all items by their type URI within a document.
   */
  def items(itemtype: URI): Seq[MicrodataItem] = allItems.filter { matchingItemType(_, itemtype) }

  /**
   * Utility method to help collect root items by their type URI within a document.
   */
  def rootItems(itemtype: URI): Seq[MicrodataItem] = rootItems.filter { matchingItemType(_, itemtype) }

}
