package org.viablespark.mapper.config

import scala.collection.mutable.*
import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.parsing.ConstructingParser

class SimpleXmlMapperConfig
(val file: String, val sourceType: Class[_], val targetType: Class[_]) extends MapperConfigTrait :
  lazy val mi: MappingInformation = parse()

  override def getMappingInformation: MappingInformation = mi

  private def parse(): MappingInformation =
    val resource = classOf[SimpleXmlMapperConfig].getResourceAsStream(file)
    val parser = ConstructingParser.fromSource(Source.fromInputStream(resource), preserveWS = false)
    val doc = parser.document()

    var list = new ListBuffer[MappingData]()
    for (element <- doc \ "bind")
      list += getBindData(element)
    for (element <- doc \ "collection")
      var b = new ListBuffer[MappingData]()
      element.child.foreach(bind => b += getBindData(bind))
      list += MappingData((element \ "@source").text,
        (element \ "@target").text, null, b, (element \ "@type").text)
    val processorList = new ListBuffer[PostProcessor]()
    for (processors <- doc \ "post-processors")
      processors.child.foreach(p => processorList += newInstance((p \ "@value").text, classOf[PostProcessor]))
    new MappingInformation(list, processorList)

  private def getBindData(el: NodeSeq): MappingData =
    MappingData((el \ "@source").text,
      (el \ "@target").text,
      getConverter((el \ "@converter").text))
