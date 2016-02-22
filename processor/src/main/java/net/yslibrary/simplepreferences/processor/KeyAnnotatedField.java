package net.yslibrary.simplepreferences.processor;

import com.google.common.base.Strings;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;

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
  public final Type type;
  public final String name;
  public final String preferenceKey;
  public final boolean omitGetterPrefix;

  public KeyAnnotatedField(VariableElement element) throws ProcessingException {
    annotatedElement = element;

    try {
      type = Type.from(TypeName.get(element.asType()));
    } catch (IllegalArgumentException e) {
      throw new ProcessingException(element, e.getMessage());
    }

    Key annotation = element.getAnnotation(Key.class);
    name = element.getSimpleName().toString();
    preferenceKey = Strings.isNullOrEmpty(annotation.name()) ? Utils.lowerCamelToLowerSnake(name)
        : annotation.name();
    omitGetterPrefix = annotation.omitGetterPrefix();
  }

  public List<MethodSpec> generate(FieldSpec prefsField, Elements elementUtils) {
    List<MethodSpec> methods = new ArrayList<>();

    methods.add(type.generateGetter(prefsField, name, preferenceKey, omitGetterPrefix));
    methods.add(type.generateSetter(prefsField, name, preferenceKey));

    return methods;
  }

  public enum Type {
    BOOLEAN(TypeName.BOOLEAN, BOOLEAN_GETTER_PREFIX) {
      @Override
      public MethodSpec generateGetter(FieldSpec prefs, String name, String key,
          boolean omitPrefix) {
        return getGetterBuilder(name, omitPrefix).addStatement("return $N.getBoolean($S, $L)",
            prefs, key, name).build();
      }

      @Override
      public MethodSpec generateSetter(FieldSpec prefs, String name, String key) {
        return getSetterBuilder(name, boolean.class).addStatement(
            "$N.edit().putBoolean($S, value).apply()", prefs, key).build();
      }
    },
    STRING(TypeName.get(String.class), GETTER_PREFIX) {
      @Override
      public MethodSpec generateGetter(FieldSpec prefs, String name, String key,
          boolean omitPrefix) {
        return getGetterBuilder(name, omitPrefix).addStatement("return $N.getString($S, $L)", prefs,
            key, name).build();
      }

      @Override
      public MethodSpec generateSetter(FieldSpec prefs, String name, String key) {
        return getSetterBuilder(name, String.class).addStatement(
            "$N.edit().putString($S, value).apply()", prefs, key).build();
      }
    },
    INT(TypeName.INT, GETTER_PREFIX) {
      @Override
      public MethodSpec generateGetter(FieldSpec prefs, String name, String key,
          boolean omitPrefix) {
        return getGetterBuilder(name, omitPrefix).addStatement("return $N.getInt($S, $L)", prefs,
            key, name).build();
      }

      @Override
      public MethodSpec generateSetter(FieldSpec prefs, String name, String key) {
        return getSetterBuilder(name, int.class).addStatement("$N.edit().putInt($S, value).apply()",
            prefs, key).build();
      }
    },
    FLOAT(TypeName.FLOAT, GETTER_PREFIX) {
      @Override
      public MethodSpec generateGetter(FieldSpec prefs, String name, String key,
          boolean omitPrefix) {
        return getGetterBuilder(name, omitPrefix).addStatement("return $N.getFloat($S, $L)", prefs,
            key, name).build();
      }

      @Override
      public MethodSpec generateSetter(FieldSpec prefs, String name, String key) {
        return getSetterBuilder(name, float.class).addStatement(
            "$N.edit().putFloat($S, value).apply()", prefs, key).build();
      }
    },
    LONG(TypeName.LONG, GETTER_PREFIX) {
      @Override
      public MethodSpec generateGetter(FieldSpec prefs, String name, String key,
          boolean omitPrefix) {
        return getGetterBuilder(name, omitPrefix).addStatement("return $N.getLong($S, $L)", prefs,
            key, name).build();
      }

      @Override
      public MethodSpec generateSetter(FieldSpec prefs, String name, String key) {
        return getSetterBuilder(name, long.class).addStatement(
            "$N.edit().putLong($S, value).apply()", prefs, key).build();
      }
    },
    STRING_SET(ParameterizedTypeName.get(Set.class, String.class), GETTER_PREFIX) {
      @Override
      public MethodSpec generateGetter(FieldSpec prefs, String name, String key,
          boolean omitPrefix) {
        return getGetterBuilder(name, omitPrefix).addStatement("return $N.getStringSet($S, $L)",
            prefs, key, name).build();
      }

      @Override
      public MethodSpec generateSetter(FieldSpec prefs, String name, String key) {
        MethodSpec.Builder builder =
            MethodSpec.methodBuilder(SETTER_PREFIX + Utils.lowerToUpperCamel(name))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(Set.class, String.class), "value");
        return builder.addStatement("$N.edit().putStringSet($S, value).apply()", prefs, key)
            .build();
      }
    };

    public final TypeName type;
    public final String getterPrefix;

    Type(TypeName type, String getterPrefix) {
      this.type = type;
      this.getterPrefix = getterPrefix;
    }

    public static Type from(TypeName typeName) throws IllegalStateException {

      if (TypeName.BOOLEAN.equals(typeName)) {
        return BOOLEAN;
      } else if (TypeName.get(String.class).equals(typeName)) {
        return STRING;
      } else if (TypeName.INT.equals(typeName)) {
        return INT;
      } else if (TypeName.FLOAT.equals(typeName)) {
        return FLOAT;
      } else if (TypeName.LONG.equals(typeName)) {
        return LONG;
      } else if (ParameterizedTypeName.get(Set.class, String.class).equals(typeName)) {
        return STRING_SET;
      } else {
        throw new IllegalArgumentException(
            String.format("TypeName: %s is not allowed", typeName.toString()));
      }
    }

    public abstract MethodSpec generateGetter(FieldSpec prefs, String name, String key,
        boolean omitPrefix);

    public abstract MethodSpec generateSetter(FieldSpec prefs, String name, String key);

    protected MethodSpec.Builder getGetterBuilder(String name, boolean omitPrefix) {
      String methodName = omitPrefix ? name : getterPrefix + Utils.lowerToUpperCamel(name);
      return MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC).returns(type);
    }

    protected MethodSpec.Builder getSetterBuilder(String name, Class<?> valueClass) {
      return MethodSpec.methodBuilder(SETTER_PREFIX + Utils.lowerToUpperCamel(name))
          .addModifiers(Modifier.PUBLIC)
          .addParameter(valueClass, "value");
    }
  }
}
