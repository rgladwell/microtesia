// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

private trait Element[N] {

  def doc: Element[N]

  def hasAttr(name: String): Boolean

  def attr(name: String): Option[String]

  def value: String

  def name: String

  def findById(id: String): Option[Element[N]]

  def childMap[T](f: (Element[N]) => T): Seq[T]

}
