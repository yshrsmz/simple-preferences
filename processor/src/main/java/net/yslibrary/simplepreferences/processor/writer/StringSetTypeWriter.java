package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;
import net.yslibrary.simplepreferences.processor.Utils;

import javax.lang.model.element.Modifier;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class StringSetTypeWriter extends BaseTypeWriter {

  protected StringSetTypeWriter(TypeName enclosingClassName, KeyAnnotatedField keyAnnotatedField) {
    super(enclosingClassName, keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addStatement("return $N.getStringSet($S, $L)", prefs, annotatedField.preferenceKey, annotatedField.name)
        .build();
  }

  @Override
  public MethodSpec writeGetterWithDefaultValue(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addParameter(annotatedField.type, PARAM_DEFAULT_VALUE)
        .addStatement("return $N.getStringSet($S, $L)", prefs, annotatedField.preferenceKey, PARAM_DEFAULT_VALUE)
        .build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return MethodSpec
        .methodBuilder(BaseTypeWriter.SETTER_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .returns(enclosingClassName)
        .addParameter(annotatedField.type, PARAM_VALUE)
        .addStatement("$N.edit().putStringSet($S, $L).apply()", prefs, annotatedField.preferenceKey, PARAM_VALUE)
        .addStatement("return this")
        .build();
  }

  @Override
  public MethodSpec writeSetterWithCommit(FieldSpec prefs) {
    return MethodSpec
        .methodBuilder(BaseTypeWriter.SETTER_PREFIX + Utils.lowerToUpperCamel(annotatedField.name) + WITH_COMMIT_SUFFIX)
        .addModifiers(Modifier.PUBLIC)
        .returns(enclosingClassName)
        .addParameter(annotatedField.type, PARAM_VALUE)
        .addStatement("$N.edit().putStringSet($S, $L).commit()", prefs, annotatedField.preferenceKey, PARAM_VALUE)
        .addStatement("return this")
        .build();
  }
}
