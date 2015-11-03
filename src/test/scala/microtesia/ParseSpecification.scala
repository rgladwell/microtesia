// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import urimplicit._
import scala.xml.Node
import scala.io.Source
import java.io.ByteArrayInputStream

/**
 * Test fixtures taken from http://www.w3.org/TR/microdata/
 */
object ParseSpecification extends Specification with MicrodataMatchers {

  "Microtesia should" >> {

    "not parse non-microdata HTML" >> {
      val html = """<div><p>My name is Elizabeth.</p></div>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must be empty }
    }

    "parse top-level microdata item and" >> {

      val html = """<div itemscope>
                      <p>My name is <span itemprop="name">Elizabeth</span>.</p>
                    </div>"""

      val microdata = parse(html)

      "return item" >> {
        microdata must beDocument{ _.items must contain(beAnInstanceOf[MicrodataItem]) }
      }

      "return item name" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must haveProperty("name" -> MicrodataString("Elizabeth"))
        )}
      }

      "not return item type" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) => item.itemtype must beNone) }
      }

    }

    "parse mutiple microdata items" >> {

      val html = """<div>
                      <div itemscope>
                        <p>My name is <span itemprop="name">Elizabeth</span>.</p>
                      </div>
  
                      <div itemscope>
                        <p>My <em>name</em> is <span itemprop="name">Daniel</span>.</p>
                      </div>
                    </div>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain(beAnInstanceOf[MicrodataItem]).exactly(2.times) }

    }

    "ignore non-microdata elements" >> {

      val html = """<div itemscope>
                      <p>My name is <span itemprop="name">E<strong>liz</strong>abeth</span>.</p>
                    </div>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must haveProperty("name" -> MicrodataString("Elizabeth"))
      )}

    }

    "parse microdata links" >> {

      val html = """<div itemscope>
                      <img itemprop="image" src="google-logo.png" alt="Google">
                      <a itemprop="url" href="http://google.com">Google</a>
                    </div>"""

      val microdata = parse(html)

      "for image sources" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must haveProperty("image" -> MicrodataLink(URI("google-logo.png")))
        )}
      }

      "for hyperlink references" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must haveProperty("url" -> MicrodataLink(URI("http://google.com")))
        )}
      }

    }

    "parse machine-readable formats" >> {

      val html = """<h1 itemscope>
                      <data itemprop="product-id" value="9678AOU879">The Instigator 2000</data>
                    </h1>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must haveProperty("product-id" -> MicrodataString("9678AOU879"))
      )}

    }

    "parse numeric data" >> {

      val html = """<h1 itemscope>
                      <meter itemprop="ratingValue" min=0 value=3.5 max=5>Rated 3.5/5</meter>
                    </h1>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must haveProperty("ratingValue" -> MicrodataString("3.5"))
      )}

    }

    "parse date- and time-related data" >> {

      val html = """<h1 itemscope>
                      I was born on <time itemprop="birthday" datetime="2009-05-10">May 10th 2009</time>.
                    </h1>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must haveProperty("birthday" -> MicrodataString("2009-05-10"))
      )}

    }

    "parse nested microdata item" >> {

      val html = """<div itemscope>
                      <p>Name: <span itemprop="name">Amanda</span></p>
                      <p>Band: <span itemprop="band" itemscope> <span itemprop="name">Jazz Band</span>
                          (<span itemprop="size">12</span> players)</span></p>
                    </div>"""

      val microdata = parse(html)

      "as a property" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) => item.properties must haveKey("band")) }
      }

      "as an item" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must havePropertyMatching("band" -> beAnInstanceOf[MicrodataItem])
        )}
      }

      "with nested properties" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must havePropertyMatching("band" -> beLike {
            case subitem: MicrodataItem => subitem.properties must haveProperty("name" -> MicrodataString("Jazz Band"))
          })
        )}
      }

      "without properties from parent item" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must not(havePropertyMatching("band" -> beLike {
            case subitem: MicrodataItem => subitem.properties must haveProperty("name" -> MicrodataString("Amanda"))
          }))
        )}
      }

    }

    "parse multiple properties with same name" >> {

      val html = """<div itemscope>
                      <p>Flavors in my favorite ice cream:</p>
                      <ul>
                        <li itemprop="flavor">Lemon sorbet</li>
                        <li itemprop="flavor">Apricot sorbet</li>
                      </ul>
                    </div>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must haveProperty("flavor" -> MicrodataString("Lemon sorbet"))
          and haveProperty("flavor" -> MicrodataString("Apricot sorbet"))
      )}
    }

    "parse property with multiple names" >> {

      val html = """<div itemscope>
                      <span itemprop="favorite-color favorite-fruit">orange</span>
                    </div>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must haveProperty("favorite-color" -> MicrodataString("orange"))
          and haveProperty("favorite-fruit" -> MicrodataString("orange"))
      )}
    }

    "parse typed items" >> {

      val html = """<section itemscope itemtype="http://example.org/animals#cat">
                      <h1 itemprop="name">Hedral</h1>
                    </section>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.itemtype must beSome(URI("http://example.org/animals#cat"))
      )}
    }

    "parse nexted typed items" >> {

      val html = """<section itemscope itemtype="http://example.org/animals#cat">
                      <h1 itemprop="owner" itemscope itemtype="http://example.org/animals#person">Hedral</h1>
                    </section>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must havePropertyMatching("owner" -> beLike {
          case subitem: MicrodataItem => subitem.itemtype must beSome(URI("http://example.org/animals#person"))
        })
      )}
    }

    "parse global identifiers" >> {
      val html = """<dl itemscope
                      itemtype="http://vocab.example.net/book"
                      itemid="urn:isbn:0-330-34032-8">
                       <dt>Title
                       <dd itemprop="title">The Reality Dysfunction
                       <dt>Author
                       <dd itemprop="author">Peter F. Hamilton
                       <dt>Publication date
                       <dd><time itemprop="pubdate" datetime="1996-01-26">26 January 1996</time>
                      </dl>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.id must beSome(URI("urn:isbn:0-330-34032-8"))
      )}
    }

    "parse global identifiers for nested items" >> {
      val html = """<div itemscope>
                      <dl itemscope
                      itemprop="owner"
                      itemtype="http://vocab.example.net/book"
                      itemid="urn:isbn:0-330-34032-8">
                       <dt>Title
                       <dd itemprop="title">The Reality Dysfunction
                       <dt>Author
                       <dd itemprop="author">Peter F. Hamilton
                       <dt>Publication date
                       <dd><time itemprop="pubdate" datetime="1996-01-26">26 January 1996</time>
                      </dl>
                     </div>"""

      val microdata = parse(html)

      microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
        item.properties must havePropertyMatching("owner" -> beLike {
          case subitem: MicrodataItem => subitem.id must beSome(URI("urn:isbn:0-330-34032-8"))
        })
      )}
    }

    "on invalid microdata" >> {

      val html = """<div itemscope>
                      <p>My name is <span itemprop>Elizabeth</span>.</p>
                    </div>"""

      val microdata = parse(html)

      "invalidate" >> {
        microdata must beLeft(beAnInstanceOf[InvalidMicrodata])
      }

      "report invalid line" >> {
        microdata must beLeft.like{ case error: InvalidMicrodata => error.line must beSome(1) }
      }

      "report invalid column" >> {
        microdata must beLeft.like{ case error: InvalidMicrodata => error.column must beSome(1) }
      }

    }

    "parse item referenced" >> {

      val html = """<div itemscope id="amanda" itemref="a b"></div>
                    <p id="a">Name: <span itemprop="name">Amanda</span></p>
                    <div id="b" itemprop="band" itemscope itemref="c"></div>
                    <div id="c">
                       <p>Band: <span itemprop="name">Jazz Band</span></p>
                       <p>Size: <span itemprop="size">12</span> players</p>
                    </div>"""

      val microdata = parse(html)

      "properties" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must haveProperty("name" -> MicrodataString("Amanda"))
        )}
      }

      "nested item" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must havePropertyMatching("band" -> beAnInstanceOf[MicrodataItem])
        )}
      }

      "nested item properties" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must havePropertyMatching("band" -> beLike {
            case subitem: MicrodataItem => subitem.properties must haveProperty("size" -> MicrodataString("12"))
          })
        )}
      }

    }

    "parse sources and" >> {

      val html = """<div itemscope>
                      <p>My name is <span itemprop="name">Elizabeth</span>.</p>
                    </div>"""

      val microdata = parse(new ByteArrayInputStream(html.getBytes))

      "return item name" >> {
        microdata must beDocument{ _.items must contain((item: MicrodataItem) =>
          item.properties must haveProperty("name" -> MicrodataString("Elizabeth"))
        )}
      }

    }
  }

}
