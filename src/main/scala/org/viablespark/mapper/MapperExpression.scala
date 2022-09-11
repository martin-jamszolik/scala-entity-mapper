package org.viablespark.mapper

import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.core.convert.converter.{ConditionalGenericConverter, GenericConverter}
import org.springframework.core.convert.support.{ConversionUtils, DefaultConversionService}
import org.springframework.core.convert.support.DefaultConversionService.*
import org.springframework.core.convert.{ConversionService, TypeDescriptor}
import org.springframework.expression.spel.SpelParserConfiguration
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.{StandardEvaluationContext, StandardTypeConverter}
import org.viablespark.mapper.MapperExpression.{expressionParser, getTypeConverter}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


class MapperExpression(val rootModel: AnyRef):

  private val expressionContext = StandardEvaluationContext(rootModel)
  //expressionContext.setTypeConverter( getTypeConverter )

  def getValue[T](exp: String, desiredType: Class[T]): T =
    val expression = expressionParser.parseExpression(exp)
    expression.getValue(expressionContext, desiredType)

  def setValue(exp: String, newValue: AnyRef): Unit =
    expressionParser.parseExpression(exp).setValue(expressionContext, newValue)


object MapperExpression:
  val config: SpelParserConfiguration = SpelParserConfiguration(true, false)
  val expressionParser: SpelExpressionParser = SpelExpressionParser(config)

  def getTypeConverter: StandardTypeConverter =
    val instance: DefaultConversionService = getSharedInstance.asInstanceOf[DefaultConversionService]
    val seqConverter = new MutableListToListConverter(instance)
    instance.addConverter(seqConverter)
    StandardTypeConverter(instance)


class MutableListToListConverter(private final val cvs: ConversionService) extends ConditionalGenericConverter :

  import org.springframework.core.convert.ConversionService
  import org.springframework.lang.Nullable
  import org.springframework.util.ClassUtils

  import java.util
  import java.util.Collections
  import scala.collection.mutable


  override def getConvertibleTypes: util.Set[ConvertiblePair] =
    Collections.singleton(GenericConverter.ConvertiblePair(classOf[mutable.Seq[_]], classOf[mutable.Seq[_]]))

  override def matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean =
    canConvertElements(sourceType.getElementTypeDescriptor, targetType.getElementTypeDescriptor, this.cvs)

  override def convert(source: AnyRef, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any =
    if (source == null) return null
    val sourceSeq = source.asInstanceOf[mutable.Seq[?]]
    var copyRequired = !targetType.getType.isInstance(source)

    if (!copyRequired && sourceSeq.isEmpty) return source

    val elementDesc = targetType.getElementTypeDescriptor
    if (elementDesc == null && !copyRequired) return source

    val target: mutable.ListBuffer[AnyRef] = new mutable.ListBuffer[AnyRef]

    if (elementDesc == null) then
      target.concat(sourceSeq)
    else
      sourceSeq.foreach(sourceElement => {
        val result = cvs.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), elementDesc)
        target.append(result)
        if (sourceElement != result) copyRequired = true
      }
      )
    if (copyRequired) target else source


  def canConvertElements(@Nullable sourceElementType: TypeDescriptor, @Nullable targetElementType: TypeDescriptor, conversionService: ConversionService): Boolean =
    if (targetElementType == null) then return true
    if (sourceElementType == null) then return true
    if (conversionService.canConvert(sourceElementType, targetElementType)) then return true
    if (ClassUtils.isAssignable(sourceElementType.getType, targetElementType.getType)) then return true
    false

