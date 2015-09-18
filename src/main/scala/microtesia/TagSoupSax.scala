// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.xml._
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import scala.xml.parsing.NoBindingFactoryAdapter
import scala.xml.factory.XMLLoader
import org.xml.sax.Locator

private trait TagSoupSax extends Sax {

  /**
   * Location code adapted from solution proposed here:
   * http://stackoverflow.com/a/4449703/48611
   */
  private trait Location extends NoBindingFactoryAdapter {

    var locator: org.xml.sax.Locator = _

    abstract override def setDocumentLocator(locator: Locator) {
      this.locator = locator
      super.setDocumentLocator(locator)
    }

    abstract override def createNode(pre: String, label: String, attrs: MetaData, scope: NamespaceBinding, children: List[Node]): Elem = (
      super.createNode(pre, label, attrs, scope, children)
        % Attribute("line", Text(locator.getLineNumber.toString), Null) 
        % Attribute("column", Text(locator.getColumnNumber.toString), Null)
    )

  }

  private class TagSoupXmlLoader extends XMLLoader[Elem] {
    override def adapter = new NoBindingFactoryAdapter with Location
    override def parser = new SAXFactoryImpl().newSAXParser()
  }

  override def saxParser: XMLLoader[Elem] = new TagSoupXmlLoader

}
