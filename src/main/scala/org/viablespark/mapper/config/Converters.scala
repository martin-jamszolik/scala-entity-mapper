package org.viablespark.mapper.config

trait Converter[T] {
  def convert(subject: AnyRef): T
}

class NoOpsConverter extends Converter[AnyRef] {
  def convert(subject:AnyRef ): AnyRef = subject
}

object NoOpsConverter {
  val instance = new NoOpsConverter()
}
class ToStringConverter extends Converter[String] {
  def convert(subject: AnyRef): String = {
    subject match {
      case null => ""
      case _ => subject.toString
    }
  }
}