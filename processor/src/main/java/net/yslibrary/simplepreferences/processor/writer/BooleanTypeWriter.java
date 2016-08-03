package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class BooleanTypeWriter extends BaseTypeWriter {

  protected BooleanTypeWriter(TypeName enclosingClassName, KeyAnnotatedField keyAnnotatedField) {
    super(enclosingClassName, keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addStatement("return $N.getBoolean($S, $L)", prefs, annotatedField.preferenceKey, annotatedField.name)
        .build();
  }

  @Override
  public MethodSpec writeGetterWithDefaultValue(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addParameter(boolean.class, PARAM_DEFAULT_VALUE)
        .addStatement("return $N.getBoolean($S, $L)", prefs, annotatedField.preferenceKey, PARAM_DEFAULT_VALUE)
        .build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(boolean.class)
        .addStatement("$N.edit().putBoolean($S, $L).apply()", prefs, annotatedField.preferenceKey, PARAM_VALUE)
        .addStatement("return this")
        .build();
  }

  @Override
  public MethodSpec writeSetterWithCommit(FieldSpec prefs) {
    return getBaseSetterWithCommitBuilder(boolean.class)
        .addStatement("$N.edit().putBoolean($S, $L).commit()", prefs, annotatedField.preferenceKey, PARAM_VALUE)
        .addStatement("return this")
        .build();
  }

  @Override
  protected String getGetterPrefix() {
    return BaseTypeWriter.BOOLEAN_GETTER_PREFIX;
  }
}
