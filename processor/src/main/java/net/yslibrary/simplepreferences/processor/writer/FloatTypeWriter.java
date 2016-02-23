package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class FloatTypeWriter extends BaseTypeWriter {

  protected FloatTypeWriter(KeyAnnotatedField keyAnnotatedField) {
    super(keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getFloat($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(float.class).addStatement("$N.edit().putFloat($S, value).apply()",
        prefs, annotatedField.preferenceKey).build();
  }
}