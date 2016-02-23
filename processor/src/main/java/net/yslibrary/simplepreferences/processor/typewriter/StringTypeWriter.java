package net.yslibrary.simplepreferences.processor.typewriter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class StringTypeWriter extends BaseTypeWriter {

  protected StringTypeWriter(KeyAnnotatedField annotatedField) {
    super(annotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getString($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(String.class).addStatement("$N.edit().putString($S, value).apply()",
        prefs, annotatedField.preferenceKey).build();
  }
}
