package org.viablespark.mapper

import java.lang.reflect.Method
import scala.collection.mutable
import jakarta.el._

class BeanMapperELContext extends ELContext {
  val READ_WRITE: CompositeELResolver = new CompositeELResolver() {
    add(new ArrayELResolver(false))
    add(new ListELResolver(false))
    add(new MapELResolver(false))
    add(new ResourceBundleELResolver())
    add(new BeanELResolver(false))
  }
  val variableMapper: VariableMapper = new VariableMapper() {
    val map = new mutable.HashMap[String, ValueExpression]

    def resolveVariable(variable: String): ValueExpression = map(variable)

    def setVariable(variable: String, expr: ValueExpression): ValueExpression = {
      map.put(variable, expr) match {
        case None => null
        case Some(x) => x
      }
    }
  }
  val functionMapper: FunctionMapper = new FunctionMapper() {
    def resolveFunction(prefix: String, localName: String): Method = {
      throw new UnsupportedOperationException("Function Not supported yet.")
    }
  }

  override def getELResolver: ELResolver = READ_WRITE

  override def getVariableMapper: VariableMapper = variableMapper

  override def getFunctionMapper: FunctionMapper = functionMapper

  def setSource(expr: ValueExpression): ValueExpression = variableMapper setVariable("source", expr)

  def setTarget(expr: ValueExpression): ValueExpression = variableMapper setVariable("target", expr)


}