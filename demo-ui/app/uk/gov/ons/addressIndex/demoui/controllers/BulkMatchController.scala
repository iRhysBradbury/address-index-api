package uk.gov.ons.addressIndex.demoui.controllers

import java.io.File
import java.util.UUID
import javax.inject.{Inject, Singleton}
import play.api.libs.iteratee.Iteratee
import java.io.ByteArrayOutputStream
import play.api.mvc.MultipartFormData.FilePart
import akka.stream.IOResult
import akka.stream.scaladsl.FileIO
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.Files
import play.api.libs.Files.TemporaryFile
import play.api.libs.streams.Accumulator
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.core.parsers.Multipart
import play.core.parsers.Multipart.{FileInfo, FilePartHandler}
import uk.gov.ons.addressIndex.crfscala.CrfScala.CrfTokenResult
import uk.gov.ons.addressIndex.demoui.client.AddressIndexClientInstance
import uk.gov.ons.addressIndex.demoui.model.ui.Navigation
import uk.gov.ons.addressIndex.demoui.modules.DemouiConfigModule
import uk.gov.ons.addressIndex.demoui.utils.ClassHierarchy
import uk.gov.ons.addressIndex.model.AddressIndexSearchRequest
import uk.gov.ons.addressIndex.model.server.response.AddressBySearchResponseContainer

import scala.concurrent.duration.Duration
import scala.io.Source
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

/**
  * Controller class for a multiple addresses to be matched
  *
  * @param messagesApi
  * @param conf
  * @param apiClient
  * @param ec
  */
@Singleton
class BulkMatchController @Inject()(
  val messagesApi: MessagesApi,
  conf: DemouiConfigModule,
  apiClient: AddressIndexClientInstance,
  classHierarchy: ClassHierarchy
 )(
  implicit
  ec: ExecutionContext,
  mat: akka.stream.Materializer
) extends Controller with I18nSupport {

  private val multiMatchFormName = "file"

  def bulkMatchPage(): Action[AnyContent] = Action.async { implicit request =>
    Future successful Ok(
      uk.gov.ons.addressIndex.demoui.views.html.multiMatch(
        nav = Navigation.default,
        fileFormName = multiMatchFormName
      )
    )
  }

  def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType) =>
      val path = Files.TemporaryFile("multipartBody", "tempFile")
      val file = path.file
      val fileSink = FileIO.toFile(file)
      val accumulator = Accumulator(fileSink)
      accumulator.map { case IOResult(count, status) =>
        FilePart(partName, filename, contentType, file)
      }(play.api.libs.concurrent.Execution.defaultContext)
  }

  def bulkQuery(): Action[MultipartFormData[File]] = Action.async(
    parse.multipartFormData(
      filePartHandler = handleFilePartAsFile,
      maxLength = 10 * 1024 * 1024
    )
  ) { implicit req =>
    val x = req.body.file(multiMatchFormName).map { file =>
      apiClient.bulk(
        file = file.ref,
        headers = req.headers
      ).map(x => Ok(x.body.toString))
    }

    x.getOrElse(Future.successful(BadRequest))
  }

  def uploadFile(): Action[Either[MaxSizeExceeded, MultipartFormData[TemporaryFile]]] = Action.async(
    parse.maxLength(
      maxLength = 10 * 1024 * 1024, //10MB
      parser = parse.multipartFormData
    )
  ) { implicit request => {
    request.body match {
      case Right(file) => {
        file.file(multiMatchFormName) map { file =>
          apiClient.bulk(
            file = file.ref.file,
            headers = request.headers
          ) map (x => Ok(x.body))
        }
      }
      case Left(maxSizeExceeded) => {
        Some(Future.successful(EntityTooLarge))
      }
    }
  }.getOrElse(Future.successful(InternalServerError))
  }

  def newOne(): Action[_] = Action.async(
    bodyParser = parse.multipartFormData(
      filePartHandler = Multipart.handleFilePartAsTemporaryFile,
      maxLength = 10 * 1024 * 1024 //10MB
    )
  ) { implicit request => {
    request.body.file(multiMatchFormName) map { file =>
      apiClient.bulk3(
        file = file.ref.file,
        headers = request.headers
      ) map (x => Ok(x.body))
    }
  }.getOrElse(Future.successful(InternalServerError))
  }


  def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType) =>
      val path = Files.TemporaryFile("multipartBody", "tempFile")
      val file = path.file
      val fileSink = FileIO.toFile(file)
      val accumulator = Accumulator(fileSink)
      accumulator.map { case IOResult(count, status) =>
        FilePart(partName, filename, contentType, file)
      }(play.api.libs.concurrent.Execution.defaultContext)
  }

  def handleFilePartAsByteArray: FilePartHandler[Array[Byte]] = {
    case FileInfo(partName, filename, contentType) =>
      // simply write the data to the a ByteArrayOutputStream

      val i = Iteratee.fold[Array[Byte], ByteArrayOutputStream](new ByteArrayOutputStream) { (os, data) =>
        os.write(data)
        os
      } map { os =>
        os.close()
        os.toByteArray
      }

      val accumulator = Accumulator()
      accumulator.map { case IOResult(count, status) =>
        FilePart(partName, filename, contentType, file)
      }(play.api.libs.concurrent.Execution.defaultContext)


  }

  def multipartFormDataAsBytes:BodyParser[MultipartFormData[Array[Byte]]] =
    parse.multipartFormData(filePartHandler = handleFilePartAsByteArray, maxLength = 10 *1024 *1024)

}