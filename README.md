# Microtesia [![Build Status](https://travis-ci.org/rgladwell/microtesia.svg)](https://travis-ci.org/rgladwell/microtesia) [![Codacy Badge](https://api.codacy.com/project/badge/1af50d2ea130484a918a44a5ce0d1ce0)](https://www.codacy.com/app/ricardo_3/microtesia) [ ![Download](https://api.bintray.com/packages/rgladwell/maven/microtesia/images/download.svg) ](https://bintray.com/rgladwell/maven/microtesia/_latestVersion) [![Dependencies](https://app.updateimpact.com/badge/702556651743481856/microtesia.svg?config=runtime)](https://app.updateimpact.com/latest/702556651743481856/microtesia)

[Microdata](http://www.w3.org/TR/microdata/) parsing library for
Scala.

To install add the following line to your SBT configuration:

```
libraryDependencies += "me.gladwell.microtesia" %% "microtesia" % "0.2"
```

To use simply put the Microtesia API in scope and call the `parse`
method as follows:

```scala
scala> import microtesia._
import microtesia._

scala> parseMicrodata("""<div
     |                    itemscope
     |                    itemtype="http://schema.org/Movie">
     |                      <h1 itemprop="name">Avatar</h1>
     |                    </div>""")
res0: scala.util.Try[microtesia.MicrodataDocument] = Success(MicrodataDocument(List(MicrodataItem(List((name,MicrodataString(Avatar))),Some(http://schema.org/Movie),None))))
```

See the [API reference](http://rgladwell.github.io/microtesia/latest/api) for
more information.

Once the HTML has been parsed, you can extract microdata values using for-comprehensions:

```scala
scala> import microtesia._
import microtesia._

scala> val items = List(MicrodataItem(properties = Seq(("name", MicrodataString("Brian")))))

scala> for {
     |   MicrodataItem(properties, _, _) <- items
     |   MicrodataProperty("name", MicrodataString(string)) <- properties
     | } yield string
res1: List[String] = List(Brian)
```

See [MicrodataValueSpec.scala](https://github.com/rgladwell/microtesia/blob/master/src/test/scala/microtesia/MicrodataValueSpec.scala) for more examples of microdata for-comprehensions.

## Readers

While you can use the for-comprehensions to write custom parsers, microtesia provides a `formats` API (based on [https://github.com/milessabin/shapeless](shapeless)) to automatically de-serialise `MicrodataValue` instances into value types and case classes:

```scala
scala> import microtesia._, formats._
import microtesia._
import formats._

scala> case class Person(name: String, age: Int, adult: Boolean)
defined class Person

scala> MicrodataItem(
     |   Seq(
     |     ("name", MicrodataString("hello")),
     |     ("age", MicrodataString("13")),
     |     ("adult", MicrodataString("true"))
     |   )
     | ).convertTo[Person]
res0: scala.util.Try[Person] = Success(Person(hello,13,true))
```

## Releasing

To release a new, [tagged](https://git-scm.com/book/en/v2/Git-Basics-Tagging) version of Microtesia, execute the following:

```sh
$ sbt +publish
$ sbt ghpages-push-site
```

## License

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this program.  If not, see
<http://www.gnu.org/licenses/>.

Copyright 2015-2016 [Ricardo Gladwell](http://gladwell.me).
