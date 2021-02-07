
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt
import scala.util.Random

class OtusHw3 extends Simulation {

  val allAlpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  val lowAlpha = "abcdefghijklmnopqrstuvwxyz"
  val nums = "0123456789"

  def randStr(n: Int, a: String): String = (1 to n).map(_ => a(Random.nextInt(a.length))).mkString

  val randFirstName: String = randStr(15, allAlpha)
  val randLastName: String = randStr(15, allAlpha)
  val randUser: String = randStr(10, lowAlpha)
  val randPhone: String = randStr(10, nums)

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://arch.homework:64902/otusapp/kkeker/api/v1")
    .disableAutoReferer
    .virtualHost("arch.homework")
    .contentTypeHeader("application/json")
    .disableWarmUp
    .disableCaching
  val scn: ScenarioBuilder = scenario("OTUS-HW3")
    .exec(http("CreateUser")
      .post("/user")
      .body(StringBody(
        s"""{
           |"username": "${randUser}",
           |"firstName": "${randFirstName}",
           |"lastName": "${randLastName}",
           |"email": "${randUser}@example.com",
           |"phone": "+7${randPhone}"
           |}""".stripMargin)).asJson
      .check(status.is(200))
      .check(jsonPath("$..id").saveAs("newUserId")))
    .pause(2)

    .exec(http("CreateIncorrectUser")
      .post("/user")
      .body(StringBody(
        """{}""")).asJson
      .check(status.is(500)))
    .pause(2)

    .exec(http("GetUnavailableUser")
      .get("/user/1")
      .check(status.is(404)))
    .pause(2)

    .exec(http("GetUser")
      .get("/user/${newUserId}")
      .check(status.is(200)))
    .pause(2)

    .exec(http("ChangeUnavailableUser")
      .put("/user/1")
      .body(StringBody(
        """{}""")).asJson
      .check(status.is(403)))
    .pause(2)

    .exec(http("ChangeUser")
      .put("/user/${newUserId}")
      .body(StringBody(
        """{
          |"username": "${newUserId}",
          |"firstName": "FiestNameTest",
          |"lastName": "LastNameTest",
          |"email": "test@example.com",
          |"phone": "+70000000000"
          |}""".stripMargin)).asJson
      .check(status.is(200)))
    .pause(2)

    .exec(http("ChangeIncorrectUser")
      .put("/user/${newUserId}")
      .body(StringBody(
        """{}""")).asJson
      .check(status.is(500)))
    .pause(2)

    .exec(http("DeleteUnavailableUser")
      .delete("/user/1")
      .check(status.is(403)))
    .pause(2)

    .exec(http("DeleteUser")
      .delete("/user/${newUserId}")
      .check(status.is(200)))
    .pause(2)

  setUp(scn.inject(constantUsersPerSec(10).during(10.minutes)).protocols(httpProtocol))
}
