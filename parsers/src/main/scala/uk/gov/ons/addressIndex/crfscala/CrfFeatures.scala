package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfScala._
import uk.gov.ons.addressIndex.crfscala.jni.CrfScalaJni

//TODO scaladoc
trait CrfFeatures[CrfToken, CrfTokens] {

  /**
    * @return all the features
    */
  def features: Seq[CrfFeature[_, CrfToken]]

  def aggregateFeatures: Seq[CrfAggregateFeature[_, (CrfTokens, CrfToken)]]

  //TODO scaladoc
  def toCrfJniInput[T, CrfToken[]](input: CrfToken, next: Option[CrfToken] = None, previous: Option[CrfToken] = None, all: CrfTokens): CrfJniInput = {
    (
      (
        features
          map(
            _.toCrfJniInput[CrfToken](
              input,
              next,
              previous
            )
          )
      ) ++ (
        aggregateFeatures
          map(
            _.toCrfJniInput[CrfToken](
              all -> input,
              next map(n => all -> n),
              previous map(p => all -> p)
            )
          )
        )
      mkString
    ) + CrfScalaJni.lineEnd
  }

  /**
    * @param i the token to run against all feature analysers
    * @return the token and its results, as a pair
    */
  def analyse(i : CrfToken, next: Option[CrfToken] = None, previous: Option[CrfToken] = None): CrfTokenResult = {
    CrfTokenResult(
      token = i,
      next = next,
      previous = previous,
      results = features.map(f => f.name -> f.analyse(i)).toMap
    )
  }
}
