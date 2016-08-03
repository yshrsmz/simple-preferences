package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class IntTypeWriter extends BaseTypeWriter {
  protected IntTypeWriter(TypeName enclosingClassName, KeyAnnotatedField annotatedField) {
    super(enclosingClassName, annotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addStatement("return $N.getInt($S, $L)", prefs, annotatedField.preferenceKey, annotatedField.name)
        .build();
  }

  @Override
  public MethodSpec writeGetterWithDefaultValue(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addParameter(int.class, PARAM_DEFAULT_VALUE)
        .addStatement("return $N.getInt($S, $L)", prefs, annotatedField.preferenceKey, PARAM_DEFAULT_VALUE)
        .build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(int.class)
        .addStatement("$N.edit().putInt($S, $L).apply()", prefs, annotatedField.preferenceKey, PARAM_VALUE)
        .addStatement("return this")
        .build();
  }

  @Override
  public MethodSpec writeSetterWithCommit(FieldSpec prefs) {
    return getBaseSetterWithCommitBuilder(int.class)
        .addStatement("$N.edit().putInt($S, $L).commit()", prefs, annotatedField.preferenceKey, PARAM_VALUE)
        .addStatement("return this")
        .build();
  }
}
