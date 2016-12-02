package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfAggregateFeatureAnalyser.CrfAggregateFeatureAnalyser

trait CrfAggregateFeature[T, R] extends CrfJniInputResolvable[T, R, CrfAggregateFeatureAnalyser[T]]