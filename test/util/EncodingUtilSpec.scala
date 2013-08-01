package test.util

import org.specs2.mutable.Specification

import util.{EncodingUtil => EU}

class EncodingUtilSpec extends Specification {
  "EncodingUtil" should {
    "Compress different text differently" in {
      EU.compressText("The quick brown fox jumped over the lazy dog.") mustNotEqual(EU.compressText("What a great test sentence!"))
    }
    "Compress the same text the same" in {
      val sentence = "How much did HBO pay Led Zeppelin to license In The Evening for one episode of True Blood?"
      EU.compressText(sentence) mustEqual(EU.compressText(sentence))
    }
    "Inflate compressed text" in {
      val inflated = EU.inflateText("H4sIAAAAAAAAAAvJSFUoLM1MzlZIKsovz1NIy69QyCrNLUhNUcgvSy1SKAHK5yRWVSqk5KfrAQBCRqOCLQAAAA==")
      inflated must beSome
      inflated.get mustEqual("The quick brown fox jumped over the lazy dog.")
    }
  }
}
