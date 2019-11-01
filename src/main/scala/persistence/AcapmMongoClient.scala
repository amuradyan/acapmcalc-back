package persistence

import com.mongodb.ConnectionString
import com.typesafe.config.ConfigFactory
import helpers.Expression
import helpers.Helpers._
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection, MongoCredential}

object AcapmMongoClient {

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val conf = ConfigFactory.load()

  val db = conf.getString("mongodb.db")
  val user = conf.getString("mongodb.user")
  val password = conf.getString("mongodb.password")
  val host = conf.getString("mongodb.host")
  val port = conf.getString("mongodb.port")

  val clientSettingsBuilder = MongoClientSettings.builder()
  val mongoConnectionString = new ConnectionString(s"mongodb://$host:$port")

  val credential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray)
  clientSettingsBuilder.credential(credential)
  clientSettingsBuilder.applyConnectionString(mongoConnectionString)

  val mongoClient = MongoClient(clientSettingsBuilder.build())
  val codecRegistry = fromRegistries(
    fromProviders(
      classOf[Expression]),
    DEFAULT_CODEC_REGISTRY)
  val acapmDB = mongoClient.getDatabase(db).withCodecRegistry(codecRegistry)

  lazy val expressions: MongoCollection[Expression] = acapmDB.getCollection("expressions")

  def storeExpression(expression: String, value: Int) = {
    expressions.insertOne(Expression(s"$expression = $value")).results()
  }
}
