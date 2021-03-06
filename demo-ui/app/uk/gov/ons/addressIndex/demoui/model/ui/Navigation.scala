package uk.gov.ons.addressIndex.demoui.model.ui

import play.api.i18n.Messages

case class Navigation(links: Seq[Link])
case class Link(href: String, label: String)

object Links {
  def home(implicit messages: Messages) = {
    Link(
      href = uk.gov.ons.addressIndex.demoui.controllers.routes.ApplicationHomeController.indexPage.toString,
      label = messages("navbar.home")
    )
  }
  def single(implicit messages: Messages) = {
    Link(
      href = uk.gov.ons.addressIndex.demoui.controllers.routes.SingleMatchController.showSingleMatchPage.toString,
      label = messages("navbar.singlematch")
    )
  }
  def multi(implicit messages: Messages) = {
    Link(
      href = uk.gov.ons.addressIndex.demoui.controllers.routes.BulkMatchController.bulkMatchPage.toString,
      label = messages("navbar.multimatch")
    )
  }
  def clerical(implicit messages: Messages) = {
    Link(
      href = uk.gov.ons.addressIndex.demoui.controllers.routes.ClericalToolController.showSingleMatchPage.toString,
      label = messages("navbar.clerical")
    )
  }
}

object Navigation {
  def default(implicit messages: Messages) = {
    Navigation(
      links = Seq(
        Links.home,
        Links.single,
        Links.multi,
        Links.clerical
      )
    )
  }
}
