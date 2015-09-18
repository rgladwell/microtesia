// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

private object Tag {

  def unapply(element: SaxElement): Option[String] = Option(element.name)

}
