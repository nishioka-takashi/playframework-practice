package controllers

import javax.inject._

import models._
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._

@Singleton
class ApiController @Inject()(cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) {

  val apiForm = Form(
    mapping(
      "area" -> text
    )(ApiForm.apply)(ApiForm.unapply)
  )

  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.api(
      apiForm
    ))
  }

  def call() = Action { implicit request: MessagesRequest[AnyContent] =>
    val form = apiForm.bindFromRequest
    form.fold(
      errorForm => {
        BadRequest(views.html.api(errorForm, "不正な入力です"))
      },
      area => {
        val r = requests.get("https://www.jma.go.jp/bosai/forecast/data/overview_forecast/" + area.area + ".json")
        val json: JsValue = Json.parse(r.text)
        val publishingOffice = (json \ "publishingOffice").asOpt[String]
        val reportDatetime = (json \ "reportDatetime").asOpt[String]
        val targetArea = (json \ "targetArea").asOpt[String]
        val text = (json \ "text").asOpt[String]
        val table = s"""
          <table>
            <tbody>
                <tr>
                    <td>publishingOffice</td>
                    <td>$publishingOffice</td>
                </tr>
                <tr>
                    <td>reportDatetime</td>
                    <td>$reportDatetime</td>
                </tr>
                <tr>
                    <td>targetArea</td>
                    <td>$targetArea</td>
                </tr>
                <tr>
                    <td>text</td>
                    <td>$text</td>
                </tr>
            </tbody>
          </table>
        """
        Ok(views.html.api(form, table))
      }
    )
  }
}