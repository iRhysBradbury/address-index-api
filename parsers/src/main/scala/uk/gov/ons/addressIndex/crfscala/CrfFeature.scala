package uk.gov.ons.addressIndex.crfscala

import uk.gov.ons.addressIndex.crfscala.CrfFeatureAnalyser.CrfFeatureAnalyser

trait CrfFeature[T] extends CrfJniInputResolvable[T, CrfFeatureAnalyser[T]]