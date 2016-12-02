package uk.gov.ons.addressIndex.parsers

import uk.gov.ons.addressIndex.crfscala.CrfAggregateFeatureAnalyser.CrfAggregateFeatureAnalyser
import uk.gov.ons.addressIndex.crfscala.CrfFeatureAnalyser.CrfFeatureAnalyser
import uk.gov.ons.addressIndex.crfscala.{CrfAggregateFeature, CrfFeature, CrfFeatures, CrfParser}
import uk.gov.ons.addressIndex.crfscala.CrfScala._

//TODO scaladoc
/**
  * AddressParser
  */
object AddressParser extends CrfParser {
  //can remove
  def parse(i: Input, fa: Features[CrfToken, CrfTokens], tokenable: CrfTokenable): CrfParserResults = {
    super.parse(i, fa, tokenable)
  }
}

/**
  * Feature collection
  *
  * @param features the features of this feature collection
  */
case class Features[CrfToken, CrfTokens](override val features : Feature[_]*)(override val aggregateFeatures: FeatureAggregate[_]*)
  extends CrfFeatures[CrfToken, CrfTokens]

/**
* @param name the feature's key which is referenced in them jcrfsuite model
*
  * @param analyser feature analyser
  *
  * @tparam T the return type of this analyser; used for the conversion to an Item
  */
case class Feature[T](override val name: String)(override val analyser: CrfFeatureAnalyser[T])
  extends CrfFeature[T, CrfToken]

/**
  *
  * @param name
  * @param analyser
  * @tparam T
  */
case class FeatureAggregate[T](override val name: String)(override val analyser: CrfAggregateFeatureAnalyser[T])
  extends CrfAggregateFeature[T, (CrfTokens, CrfToken)]