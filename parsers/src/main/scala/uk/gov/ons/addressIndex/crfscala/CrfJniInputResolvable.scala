package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfScala._
import uk.gov.ons.addressIndex.crfscala.jni.CrfScalaJni
import scala.util.control.NonFatal

//Higher kinded type
sealed trait AnalyserInput[T]
object FeatureAnalyserInput extends AnalyserInput[CrfToken]
object AggregateFeatureAnalyserInput extends AnalyserInput[(CrfTokens, CrfToken)]

trait CrfJniInputResolvable[T, AI, AnalyserInput[AI], A <: CrfFeaturable[AI, AnalyserInput[AI]]] {

  /**
    * @return a function which returns an instance of T
    */
  def analyser(): A

  /**
    * @return name
    */
  def name(): String

  /**
    * @param ai input
    * @return apply the analyser to i
    */
  def analyse[AI](ai: AnalyserInput[AI]): AI = analyser apply(ai)

  //TODO scaladoc
  /**
    *
    * @param input
    * @param next
    * @param previous
    * @return
    */
  def toCrfJniInput[T](
    input: AnalyserInput[T],
    next: Option[AnalyserInput[T]] = None,
    previous: Option[AnalyserInput[T]] = None
  ): CrfJniInput = {
    new StringBuilder()
      .append(CrfScalaJni.lineStart)
      .append(
        createCrfJniInput(
          prefix = name,
          someValue = analyse[T](input)
        )
      )
      .append(
        next map { n =>
          createCrfJniInput(
            prefix = CrfScalaJni.next,
            someValue = analyse[T](n)
          )
        } getOrElse ""
      )
      .append(
        previous map { p =>
          createCrfJniInput(
            prefix = CrfScalaJni.previous,
            someValue = analyse[T](p)
          )
        } getOrElse ""
      )
      .toString
  }

  //TODO scaladoc
  /**
    *
    * @param prefix
    * @param someValue
    * @return
    */
  def createCrfJniInput(prefix: String, someValue: Any): CrfJniInput = {
    def qualify(str: String): String = str.replace(":", "\\:")
    val qName = qualify(name)

    someValue match {
      case _: String =>
        s"$qName\\:${qualify(someValue.asInstanceOf[String])}:1.0"

      case _: Int =>
        s"$qName:$someValue.0"

      case _: Double =>
        s"$qName:$someValue"

      case _: Boolean =>
        s"$qName:${if(someValue.asInstanceOf[Boolean]) "1.0" else "0.0"}"

      case t : CrfType[_] =>
        createCrfJniInput(prefix, t.value)

      case NonFatal(e) =>
        throw e

      case _ =>
        throw new UnsupportedOperationException(
          s"Unsupported input to CrfJniInput: ${someValue.getClass.toString} or Feature with name: $name"
        )
    }
  }
}
