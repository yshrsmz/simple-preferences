package net.yslibrary.simplepreferences.processor.typewriter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class LongTypeWriter extends BaseTypeWriter {
  protected LongTypeWriter(KeyAnnotatedField keyAnnotatedField) {
    super(keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getLong($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(long.class).addStatement("$N.edit().putLong($S, value).apply()",
        prefs, annotatedField.preferenceKey).build();
  }
}
