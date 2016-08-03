package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;
import net.yslibrary.simplepreferences.processor.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public abstract class BaseTypeWriter implements TypeWriter {
  protected final static String GETTER_PREFIX = "get";
  protected final static String BOOLEAN_GETTER_PREFIX = "is";
  protected final static String SETTER_PREFIX = "set";
  protected final static String EXIST_PREFIX = "has";
  protected final static String REMOVE_PREFIX = "remove";
  protected final static String WITH_COMMIT_SUFFIX = "WithCommit";

  protected final TypeName enclosingClassName;
  protected final KeyAnnotatedField annotatedField;

  protected BaseTypeWriter(TypeName enclosingClassName, KeyAnnotatedField annotatedField) {
    this.enclosingClassName = enclosingClassName;
    this.annotatedField = annotatedField;
  }

  public abstract MethodSpec writeGetter(FieldSpec prefs);

  public abstract MethodSpec writeSetter(FieldSpec prefs);

  @Override
  public MethodSpec writeExists(FieldSpec prefs) {
    return MethodSpec.methodBuilder(EXIST_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .returns(boolean.class)
        .addStatement("return $N.contains($S)", prefs, annotatedField.preferenceKey)
        .build();
  }

  @Override
  public MethodSpec writeRemover(FieldSpec prefs) {
    return MethodSpec.methodBuilder(REMOVE_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .returns(enclosingClassName)
        .addStatement("$N.edit().remove($S).apply()", prefs, annotatedField.preferenceKey)
        .addStatement("return this")
        .build();
  }

  @Override
  public MethodSpec writeRemoverWithCommit(FieldSpec prefs) {
    return MethodSpec.methodBuilder(REMOVE_PREFIX + Utils.lowerToUpperCamel(annotatedField.name) + WITH_COMMIT_SUFFIX)
        .addModifiers(Modifier.PUBLIC)
        .returns(enclosingClassName)
        .addStatement("$N.edit().remove($S).commit()", prefs, annotatedField.preferenceKey)
        .addStatement("return this")
        .build();
  }

  protected String getGetterPrefix() {
    return GETTER_PREFIX;
  }

  @Override
  public List<MethodSpec> writeMethods(FieldSpec prefs) {
    List<MethodSpec> methods = new ArrayList<MethodSpec>() {{
      add(writeSetter(prefs));
      add(writeGetter(prefs));
      add(writeExists(prefs));
      add(writeRemover(prefs));
    }};

    if (annotatedField.needCommitMethod) {
      methods.add(writeSetterWithCommit(prefs));
      methods.add(writeRemoverWithCommit(prefs));
    }

    return methods;
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
        .returns(enclosingClassName)
        .addParameter(parameterClass, "value");
  }

  protected MethodSpec.Builder getBaseSetterWithCommitBuilder(Class<?> parameterClass) {
    return MethodSpec.methodBuilder(SETTER_PREFIX + Utils.lowerToUpperCamel(annotatedField.name) + WITH_COMMIT_SUFFIX)
        .addModifiers(Modifier.PUBLIC)
        .returns(enclosingClassName)
        .addParameter(parameterClass, "value");
  }
}
