package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.Set;
import javax.lang.model.element.Modifier;
import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;
import net.yslibrary.simplepreferences.processor.Utils;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class StringSetTypeWriter extends BaseTypeWriter {

  protected StringSetTypeWriter(TypeName enclosingClassName, KeyAnnotatedField keyAnnotatedField) {
    super(enclosingClassName, keyAnnotatedField);
  }

  @Override
  public MethodSpec writeGetter(FieldSpec prefs) {
    return getBaseGetterBuilder()
        .addStatement("return $N.getStringSet($S, $L)", prefs, annotatedField.preferenceKey,
            annotatedField.name).build();
  }

  @Override
  public MethodSpec writeSetter(FieldSpec prefs) {
    return MethodSpec
        .methodBuilder(BaseTypeWriter.SETTER_PREFIX + Utils.lowerToUpperCamel(annotatedField.name))
        .addModifiers(Modifier.PUBLIC).returns(enclosingClassName)
        .addParameter(ParameterizedTypeName.get(Set.class, String.class), "value")
        .addStatement("$N.edit().putStringSet($S, value).apply()", prefs,
            annotatedField.preferenceKey).addStatement("return this").build();
  }
}
