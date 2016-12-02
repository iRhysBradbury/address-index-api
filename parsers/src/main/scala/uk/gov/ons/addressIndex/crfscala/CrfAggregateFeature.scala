package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfAggregateFeatureAnalyser.CrfAggregateFeatureAnalyser

trait CrfAggregateFeature[T] extends CrfJniInputResolvable[T, CrfAggregateFeatureAnalyser[T]]