package net.yslibrary.simplepreferences.processor.typewriter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import java.util.Set;
import javax.lang.model.element.Modifier;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;
import net.yslibrary.simplepreferences.processor.Utils;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class StringSetTypeWriter extends BaseTypeWriter {

  protected StringSetTypeWriter(KeyAnnotatedField keyAnnotatedField) {
    super(keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder().addStatement("return $N.getStringSet($S, $L)", prefs,
        annotatedField.preferenceKey, annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return MethodSpec.methodBuilder(SETTER_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ParameterizedTypeName.get(Set.class, String.class), "value")
        .addStatement("$N.edit().putStringSet($S, value).apply()", prefs,
            annotatedField.preferenceKey)
        .build();
  }
}
