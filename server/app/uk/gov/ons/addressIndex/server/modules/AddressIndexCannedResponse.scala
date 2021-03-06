package uk.gov.ons.addressIndex.server.modules

import uk.gov.ons.addressIndex.model.server.response._

trait AddressIndexCannedResponse {

  def searchUprnContainerTemplate(optAddresses: Option[AddressResponseAddress]): AddressByUprnResponseContainer = {
    AddressByUprnResponseContainer(
      response = AddressByUprnResponse(
        address = optAddresses
      ),
      status = OkAddressResponseStatus
    )
  }

  def NoAddressFoundUprn: AddressByUprnResponseContainer = {
    AddressByUprnResponseContainer(
      response = AddressByUprnResponse(
        address = None
      ),
      status = NotFoundAddressResponseStatus,
      errors = Seq(NotFoundAddressResponseError)
    )
  }

  def UnsupportedFormatUprn: AddressByUprnResponseContainer = {
    AddressByUprnResponseContainer(
      response = AddressByUprnResponse(
        address = None
      ),
      status = BadRequestAddressResponseStatus,
      errors = Seq(FormatNotSupportedAddressResponseError)
    )
  }

  private def BadRequestTemplate(errors: AddressResponseError*): AddressBySearchResponseContainer = {
    AddressBySearchResponseContainer(
      response = Error,
      status = BadRequestAddressResponseStatus,
      errors = errors
    )
  }

  def OffsetNotNumeric: AddressBySearchResponseContainer = {
    BadRequestTemplate(OffsetNotNumericAddressResponseError)
  }

  def LimitNotNumeric: AddressBySearchResponseContainer = {
    BadRequestTemplate(LimitNotNumericAddressResponseError)
  }

  def LimitTooSmall: AddressBySearchResponseContainer = {
      BadRequestTemplate(LimitTooSmallAddressResponseError)
  }

  def OffsetTooSmall: AddressBySearchResponseContainer = {
      BadRequestTemplate(OffsetTooSmallAddressResponseError)
  }

  def LimitTooLarge: AddressBySearchResponseContainer = {
      BadRequestTemplate(LimitTooLargeAddressResponseError)
  }

  def OffsetTooLarge: AddressBySearchResponseContainer = {
      BadRequestTemplate(OffsetTooLargeAddressResponseError)
  }

  def UnsupportedFormat: AddressBySearchResponseContainer = {
      BadRequestTemplate(FormatNotSupportedAddressResponseError)
  }

  def EmptySearch: AddressBySearchResponseContainer = {
      BadRequestTemplate(EmptyQueryAddressResponseError)
  }

  def FailedRequestToEs: AddressBySearchResponseContainer = {
    AddressBySearchResponseContainer(
      response = Error,
      status = InternalServerErrorAddressResponseStatus,
      errors = Seq(FailedRequestToEsError)
    )
  }

  def Error: AddressBySearchResponse = {
    AddressBySearchResponse(
      Map.empty,
      addresses = Seq.empty,
      limit = 10,
      offset = 0,
      total = 0,
      maxScore = 0f
    )
  }
}