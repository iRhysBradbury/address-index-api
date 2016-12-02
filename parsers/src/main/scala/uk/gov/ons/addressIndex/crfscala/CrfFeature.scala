package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfFeatureAnalyser.CrfFeatureAnalyser

trait CrfFeature[T, R] extends CrfJniInputResolvable[T, R, CrfFeatureAnalyser[T]]