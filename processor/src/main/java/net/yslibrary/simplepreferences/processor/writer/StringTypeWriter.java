package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class StringTypeWriter extends BaseTypeWriter {

  protected StringTypeWriter(TypeName enclosingClassName, KeyAnnotatedField annotatedField) {
    super(enclosingClassName, annotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getString($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(String.class)
        .addStatement("$N.edit().putString($S, value).apply()", prefs, annotatedField.preferenceKey)
        .addStatement("return this")
        .build();
  }

  @Override
  public MethodSpec writeSetterWithCommit(FieldSpec prefs) {
    return getBaseSetterWithCommitBuilder(String.class)
        .addStatement("$N.edit().putString($S, value).commit()", prefs, annotatedField.preferenceKey)
        .addStatement("return this")
        .build();
  }
}
