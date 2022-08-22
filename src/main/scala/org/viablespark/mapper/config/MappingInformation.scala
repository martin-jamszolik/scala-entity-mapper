
package org.viablespark.mapper.config

import scala.collection.mutable
import scala.collection.mutable.*

class MappingInformation(mappingData: mutable.Seq[MappingData],
                         postProcessors: mutable.Seq[PostProcessor]) {
  def getData: mutable.Seq[MappingData] = mappingData

  def getProcessors: mutable.Seq[PostProcessor] = postProcessors
}


case class MappingData(source: String,
                       target: String,
                       converter: Option[Converter[_]] = None,
                       collection: mutable.Seq[MappingData] = ListBuffer.empty[MappingData],
                       objType: String = null) {
}


trait PostProcessor {
  def process(source: AnyRef, target: AnyRef): Unit
}