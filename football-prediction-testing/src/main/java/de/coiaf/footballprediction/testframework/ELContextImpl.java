package de.coiaf.footballprediction.testframework;

import org.apache.el.ValueExpressionLiteral;

import javax.el.*;
import java.util.HashMap;
import java.util.Map;

class ELContextImpl extends ELContext {

    private final BeanELResolver resolver = new BeanELResolver();
    private final FunctionMapper functionMapper = new ELFunctionMapperImpl();
    private final VariableMapper variableMapper = new VariableMapperImpl();
    private final Map<String, ValueExpression> variables =  new HashMap<>();
    private final ExpressionFactory factory;

    ELContextImpl() {
        final String factoryClass = "org.apache.el.ExpressionFactoryImpl";
        System.setProperty("javax.el.ExpressionFactory", factoryClass);
        this.factory = ExpressionFactory.newInstance();
        if ( this.factory == null ) {
            throw new RuntimeException( "could not get instance of factory class " + factoryClass );
        }
    }

    ExpressionFactory getFactory() {
        return this.factory;
    }

    @Override
    public ELResolver getELResolver() {
        return this.resolver;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return this.functionMapper;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return this.variableMapper;
    }

    void bind( String variable, Object obj ) {
        this.variables.put( variable, new ValueExpressionLiteral(obj,Object.class) );
    }

    private class VariableMapperImpl extends VariableMapper {
        public ValueExpression resolveVariable(String s) {
            return ELContextImpl.this.variables.get(s);
        }
        public ValueExpression setVariable(String s, ValueExpression valueExpression) {
            return (ELContextImpl.this.variables.put(s, valueExpression));
        }
    }
}
