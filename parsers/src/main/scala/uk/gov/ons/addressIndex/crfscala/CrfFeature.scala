package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfFeatureAnalyser.CrfFeatureAnalyser

trait CrfFeature[T, FeatureAnalyserInput] extends CrfJniInputResolvable[T, FeatureAnalyserInput, CrfFeatureAnalyser[T]]