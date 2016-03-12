package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class LongTypeWriter extends BaseTypeWriter {
  protected LongTypeWriter(TypeName enclosingClassName, KeyAnnotatedField keyAnnotatedField) {
    super(enclosingClassName, keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getLong($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return getBaseSetterBuilder(long.class).addStatement("$N.edit().putLong($S, value).apply()",
        prefs, annotatedField.preferenceKey).addStatement("return this").build();
  }
}
