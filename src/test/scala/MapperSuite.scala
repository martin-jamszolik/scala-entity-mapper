

import org.viablespark.mapper.*
import org.viablespark.mapper.config.*

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class MapperSuite extends munit.FunSuite:
  test("should map BeanA to BeanB correctly") {
    val entityMapper = getEntityMapper
    val a = BeanA("martin", "some description", 28,Phone("123452032","Home"))
    val b = BeanB("", "", 0,"")
    var result = entityMapper.map(a, b)
    assert(result != null)
    assertEquals("martin", result.title)
    assertEquals(28, result.years)
    a.desc ="second time desc"
    a.age = 31
    result = entityMapper.map(a, b)
    assertEquals("martin", result.title)
    assertEquals(31, result.years)
    assertEquals("second time desc", result.description)
  }


  private def getEntityMapper: EntityMapper =
    SimpleConfigFactory.add(
      SimpleXmlMapperConfig("/sample/beanA-to-beanB.xml", classOf[BeanA], classOf[BeanB]),
    )
    EntityMapperImpl(SimpleConfigFactory)


case class BeanA(@BeanProperty var name: String,
                 @BeanProperty var desc: String,
                 @BeanProperty var age: Int,
                 @BeanProperty var phone: Phone):
  @BeanProperty
  val groups: mutable.Seq[Group] = new ArrayBuffer()


case class BeanB(@BeanProperty var title: String,
                 @BeanProperty var description: String,
                 @BeanProperty var years: Int,
                 @BeanProperty var phoneNumber: String):

  val entityGroups: mutable.Seq[Group] = new ArrayBuffer()

case class Group(@BeanProperty var name: String,
                 @BeanProperty var rank: String)

case class Phone(@BeanProperty var number: String,
                 @BeanProperty var where: String)


class ReportResultPostProcessor extends PostProcessor:
  override def process(source: AnyRef, target: AnyRef): Unit =
    println( s"Post Process Invoked. Processing source: $source, target: $target")



