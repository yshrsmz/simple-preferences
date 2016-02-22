package net.yslibrary.simplepreferences.processor;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import net.yslibrary.simplepreferences.annotation.Key;

/**
 * Created by yshrsmz on 2016/02/21.
 */
public class KeyAnnotatedField {

  private final static String GETTER_PREFIX = "get";
  private final static String BOOLEAN_GETTER_PREFIX = "is";
  private final static String SETTER_PREFIX = "set";
  private final static String EXIST_PREFIX = "has";
  private final static String REMOVE_PREFIX = "remove";

  public final VariableElement annotatedElement;
  public final TypeName type;
  public final String name;
  public final boolean omitGetterPrefix;

  public KeyAnnotatedField(VariableElement element) {
    annotatedElement = element;
    type = TypeName.get(element.asType());
    name = element.getSimpleName().toString();
    omitGetterPrefix = element.getAnnotation(Key.class).omitGetterPrefix();
  }

  public List<MethodSpec> generate(FieldSpec prefsField, Elements elementUtils) {
    List<MethodSpec> methods = new ArrayList<>();
    MethodSpec.Builder getterBuilder;
    MethodSpec.Builder setterBuilder;
    MethodSpec.Builder removeBuilder;
    MethodSpec.Builder existBuilder;

    if (omitGetterPrefix) {
      getterBuilder = MethodSpec.methodBuilder(name);
    } else {
      String prefix = type.equals(TypeName.BOOLEAN) ? BOOLEAN_GETTER_PREFIX : GETTER_PREFIX;
      getterBuilder = MethodSpec.methodBuilder(
          prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name));
    }

    return methods;
  }

  public enum Type {
    BOOLEAN(TypeName.BOOLEAN, BOOLEAN_GETTER_PREFIX),
    STRING(TypeName.get(String.class), GETTER_PREFIX),
    INT(TypeName.INT, GETTER_PREFIX),
    FLOAT(TypeName.FLOAT, GETTER_PREFIX),
    LONG(TypeName.LONG, GETTER_PREFIX),
    STRING_SET(ParameterizedTypeName.get(Set.class, String.class), GETTER_PREFIX);

    public final TypeName type;
    public final String getterPrefix;

    Type(TypeName type, String getterPrefix) {
      this.type = type;
      this.getterPrefix = getterPrefix;
    }
  }
}
