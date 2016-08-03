package net.yslibrary.simplepreferences.processor.writer;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;

import java.util.List;
import java.util.Set;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public interface TypeWriter {

  static TypeWriter create(TypeName enclosingClassName, KeyAnnotatedField field) {
    TypeName typeName = field.type;

    if (TypeName.BOOLEAN.equals(typeName)) {
      return new BooleanTypeWriter(enclosingClassName, field);
    } else if (TypeName.get(String.class).equals(typeName)) {
      return new StringTypeWriter(enclosingClassName, field);
    } else if (TypeName.INT.equals(typeName)) {
      return new IntTypeWriter(enclosingClassName, field);
    } else if (TypeName.FLOAT.equals(typeName)) {
      return new FloatTypeWriter(enclosingClassName, field);
    } else if (TypeName.LONG.equals(typeName)) {
      return new LongTypeWriter(enclosingClassName, field);
    } else if (ParameterizedTypeName.get(Set.class, String.class).equals(typeName)) {
      return new StringSetTypeWriter(enclosingClassName, field);
    } else {
      throw new IllegalArgumentException(
          String.format("TypeName: %s is not allowed", typeName.toString()));
    }
  }

  MethodSpec writeGetter(FieldSpec prefs);

  MethodSpec writeGetterWithDefaultValue(FieldSpec prefs);

  MethodSpec writeSetter(FieldSpec prefs);

  MethodSpec writeSetterWithCommit(FieldSpec prefs);

  MethodSpec writeExists(FieldSpec prefs);

  MethodSpec writeRemover(FieldSpec prefs);

  MethodSpec writeRemoverWithCommit(FieldSpec prefs);

  List<MethodSpec> writeMethods(FieldSpec prefs);
}
