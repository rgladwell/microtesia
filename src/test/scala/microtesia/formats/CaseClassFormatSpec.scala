package microtesia.formats

import org.specs2.mutable.Specification
import microtesia._
import shapeless._
import labelled._
import syntax.singleton._

object CaseClassFormatSpec extends Specification {

  "caseClassFormat should" >> {

    "convert flat case classes" in {
      case class Person(name: String, age: Int, adult: Boolean)
      val generic = LabelledGeneric[Person]

      val microdata =
        MicrodataItem(
          Seq(
            ("name", MicrodataString("hello")),
            ("age", MicrodataString("13")),
            ("adult", MicrodataString("true"))
          )
        )

      caseClassFormat[Person, generic.Repr].read(microdata) must beSuccessfulTry(Person("hello", 13, true))
    }

    "convert attribute with multiple values to" >> {
      val personWithMultipleNicknames =
        MicrodataItem(
          Seq(
            ("name", MicrodataString("hello")),
            ("nicknames", MicrodataString("nick1")),
            ("nicknames", MicrodataString("nick2"))
          )
        )

      "Seq" in {
        case class PersonWithNicknames(name: String, nicknames: Seq[String])
        val generic = LabelledGeneric[PersonWithNicknames]

        caseClassFormat[PersonWithNicknames, generic.Repr].read(personWithMultipleNicknames) must beSuccessfulTry.like { case p => p.nicknames must_== Seq("nick1", "nick2") }
      }

      "List" in {
        case class PersonWithNicknames(name: String, nicknames: List[String])
        val generic = LabelledGeneric[PersonWithNicknames]

        caseClassFormat[PersonWithNicknames, generic.Repr].read(personWithMultipleNicknames) must beSuccessfulTry.like { case p => p.nicknames must_== List("nick1", "nick2") }
      }

      "Set" in {
        case class PersonWithNicknames(name: String, nicknames: Set[String])
        val generic = LabelledGeneric[PersonWithNicknames]

        caseClassFormat[PersonWithNicknames, generic.Repr].read(personWithMultipleNicknames) must beSuccessfulTry.like { case p => p.nicknames must_== Set("nick1", "nick2") }
      }
    }

    "convert optional attributes to" >> {
      case class Person(name: String, gender: Option[String])
      val generic = LabelledGeneric[Person]

      "Some value if property exists" in {
        val personWithGender =
          MicrodataItem(
            Seq(
              ("name", MicrodataString("hello")),
              ("gender", MicrodataString("female"))
            )
          )

        caseClassFormat[Person, generic.Repr].read(personWithGender) must beSuccessfulTry.like { case p => p.gender must beSome("female") }
      }

      "None if property does not exist" in {
        val personWithoutGender = MicrodataItem(Seq(("name", MicrodataString("hello"))))
        caseClassFormat[Person, generic.Repr].read(personWithoutGender) must beSuccessfulTry.like { case p => p.gender must be(None) }
      }
    }

    "convert nested case classes" in {
      case class Address(address1: String)
      case class Person(name: String, address: Address)

      val generic = LabelledGeneric[Person]

      val microdata =
        MicrodataItem(
          Seq(
            ("name", MicrodataString("hello")),
            ("address", MicrodataItem(
                Seq(
                  ("address1", MicrodataString("10 World Road"))
                )
              )
            )
          )
        )

      caseClassFormat[Person, generic.Repr].read(microdata) must beSuccessfulTry(Person("hello", Address("10 World Road")))
    }

    "convert recursive case classes" in {
      case class Person(name: String, friend: Option[Person] = None)

      val generic = LabelledGeneric[Person]

      val microdata =
        MicrodataItem(
          Seq(
            ("name", MicrodataString("hello")),
            ("friend", MicrodataItem(
                Seq(
                  ("name", MicrodataString("Pally"))
                )
              )
            )
          )
        )

      caseClassFormat[Person, generic.Repr].read(microdata) must beSuccessfulTry(Person("hello", Some(Person("Pally"))))
    }

  }

}
