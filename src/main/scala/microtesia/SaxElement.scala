// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.xml._

private case class SaxElement(private val element: Node, private val document: Node) extends Element[Node] {

  private def nodeToAttribute(attribute: MetaData): (String, Option[String]) = {
    val value = attribute.value.text.trim

    (attribute.key, {
      if(attribute.key == value) None
      else Some(value)
    })
  }

  private lazy val attrs = element.attributes.map{ nodeToAttribute _ }.toMap

  override def doc = SaxElement(document, document)

  override def hasAttr(name: String): Boolean = attrs.contains(name)

  override def attr(name: String): Option[String] = attrs.get(name).getOrElse(None)

  override lazy val value: String = element.text

  override lazy val name = element.label

  override def findById(id: String): Option[SaxElement] = {
    val node = (element \\ "_" filter { _ \ "@id" exists (_.text == id) }).headOption
    node.map{ SaxElement(_, document) }
  }

  override def childMap[T](f: (Element[Node]) => T): Seq[T] =
    element
      .child
      .map{ (child: Node) => f(SaxElement(child, document)) }

}
