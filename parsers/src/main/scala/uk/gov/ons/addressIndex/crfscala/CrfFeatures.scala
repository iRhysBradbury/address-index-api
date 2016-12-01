package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfScala._
import uk.gov.ons.addressIndex.crfscala.jni.CrfScalaJni

/**
  * scala wrapper of third_party.org.chokkan.crfsuite.Item
  */
trait CrfFeatures {

  /**
    * @return all the features
    */
  def all : Seq[CrfFeature[_]]

  def toCrfJniInput(input: CrfToken, next: Option[CrfToken] = None, previous: Option[CrfToken] = None): CrfJniInput = {
    all map(_.toCrfJniInput(input, next, previous)) mkString CrfScalaJni.lineEnd
  }

  /**
    * @param i the token to run against all feature analysers
    * @return the token and its results, as a pair
    */
  def analyse(i : CrfToken) : CrfTokenResult = {
    CrfTokenResult(
      token = i,
      results = all.map(f => f.name -> f.analyse(i)).toMap
    )
  }
}