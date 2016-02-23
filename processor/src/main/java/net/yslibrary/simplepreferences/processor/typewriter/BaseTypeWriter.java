package net.yslibrary.simplepreferences.processor.typewriter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;
import net.yslibrary.simplepreferences.processor.Utils;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public abstract class BaseTypeWriter implements TypeWriter {
  protected final static String GETTER_PREFIX = "get";
  protected final static String BOOLEAN_GETTER_PREFIX = "is";
  protected final static String SETTER_PREFIX = "set";
  protected final static String EXIST_PREFIX = "has";
  protected final static String REMOVE_PREFIX = "remove";

  protected final KeyAnnotatedField annotatedField;

  protected BaseTypeWriter(KeyAnnotatedField annotatedField) {
    this.annotatedField = annotatedField;
  }

  public abstract MethodSpec writeGetter(FieldSpec prefs);

  public abstract MethodSpec writeSetter(FieldSpec prefs);

  public MethodSpec writeExists(FieldSpec prefs) {
    return MethodSpec.methodBuilder(EXIST_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .returns(boolean.class)
        .addStatement("return $N.contains($S)", prefs, annotatedField.preferenceKey)
        .build();
  }

  public MethodSpec writeRemover(FieldSpec prefs) {
    return MethodSpec.methodBuilder(REMOVE_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .addStatement("$N.edit().remove($S).apply()", prefs, annotatedField.preferenceKey)
        .build();
  }

  protected String getGetterPrefix() {
    return GETTER_PREFIX;
  }

  public List<MethodSpec> writeMethods(FieldSpec prefs) {
    return new ArrayList<MethodSpec>() {{
      add(writeSetter(prefs));
      add(writeGetter(prefs));
      add(writeExists(prefs));
      add(writeRemover(prefs));
    }};
  }

  protected MethodSpec.Builder getBaseGetterBuilder() {
    String methodName = annotatedField.omitGetterPrefix ? annotatedField.name
        : getGetterPrefix() + Utils.lowerToUpperCamel(annotatedField.name);
    return MethodSpec.methodBuilder(methodName)
        .addModifiers(Modifier.PUBLIC)
        .returns(annotatedField.type);
  }

  protected MethodSpec.Builder getBaseSetterBuilder(Class<?> parameterClass) {
    return MethodSpec.methodBuilder(SETTER_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .addParameter(parameterClass, "value");
  }
}
