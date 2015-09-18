// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.xml.Elem
import scala.xml.factory.XMLLoader

private trait Sax {
  def saxParser: XMLLoader[Elem]
}
