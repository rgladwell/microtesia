// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

private class MicrodataProperties(properties: Properties) {

  def merge(tomerge: Properties): Properties = {
    val merged = Seq(properties, tomerge).map(_.toSeq).fold(Nil)(_ ++ _)
    val grouped = merged.groupBy(_._1)
    grouped.mapValues(_.map(_._2).toList.flatten)
  }

}
