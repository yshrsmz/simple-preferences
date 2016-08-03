package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class FloatTypeWriter extends BaseTypeWriter {

  protected FloatTypeWriter(TypeName enclosingClassName, KeyAnnotatedField keyAnnotatedField) {
    super(enclosingClassName, keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addStatement("return $N.getFloat($S, $L)", prefs, annotatedField.preferenceKey, annotatedField.name)
        .build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(float.class)
        .addStatement("$N.edit().putFloat($S, value).apply()", prefs, annotatedField.preferenceKey)
        .addStatement("return this")
        .build();
  }

  @Override
  public MethodSpec writeSetterWithCommit(FieldSpec prefs) {
    return getBaseSetterWithCommitBuilder(float.class)
        .addStatement("$N.edit().putFloat($S, value).commit()", prefs, annotatedField.preferenceKey)
        .addStatement("return this")
        .build();
  }
}