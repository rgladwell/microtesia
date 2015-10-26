// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.net.URI

/**
 * Representation of a document containing microdata mark-up. Returned by the
 * [[parse]] function. A [[MicrodataDocument]] can contain multiple
 * [[MicrodataItem]] instances which can be accessed via the `items` property
 * as follows:
 * 
 * {{{
 * scala> document.items.foreach{ (m: MicrodataItem) => println(m.itemtype) }
 * Some(http://schema.org/Movie)
 * }}}
 */
case class MicrodataDocument private[microtesia] (val items: Seq[MicrodataItem]) {

  private lazy val defAllItems: Seq[MicrodataItem] = {

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

    recurse(items)

  }

  /**
   * Utility method to help collect items by their type URI within a document.
   */
  def findItems(itemtype: URI) = defAllItems.filter { _.itemtype.exists { _ == itemtype } }

}
