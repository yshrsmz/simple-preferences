package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class BooleanTypeWriter extends BaseTypeWriter {

  protected BooleanTypeWriter(KeyAnnotatedField keyAnnotatedField) {
    super(keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getBoolean($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(boolean.class).addStatement(
        "$N.edit().putBoolean($S, value).apply()", prefs, annotatedField.preferenceKey).build();
  }

  @Override
  protected String getGetterPrefix() {
    return BaseTypeWriter.BOOLEAN_GETTER_PREFIX;
  }
}
