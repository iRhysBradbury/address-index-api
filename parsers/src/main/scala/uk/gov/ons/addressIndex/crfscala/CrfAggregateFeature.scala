package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfAggregateFeatureAnalyser.CrfAggregateFeatureAnalyser

trait CrfAggregateFeature[T, AggregateFeatureAnalyserInput] extends CrfJniInputResolvable[T, AggregateFeatureAnalyserInput, CrfAggregateFeatureAnalyser[T]]