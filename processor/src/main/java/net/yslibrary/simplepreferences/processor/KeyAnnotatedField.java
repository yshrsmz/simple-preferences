package net.yslibrary.simplepreferences.processor;

import com.google.common.base.Strings;
import com.squareup.javapoet.TypeName;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by yshrsmz on 2016/02/21.
 */
public class KeyAnnotatedField {

  public final VariableElement annotatedElement;
  public final TypeName type;
  public final String name;
  public final String preferenceKey;
  public final boolean omitGetterPrefix;
  public final boolean needCommitMethod;
  public final boolean needGetterMethod;
  public final boolean needObservableMethod;

  public KeyAnnotatedField(VariableElement element) throws ProcessingException {
    if (element.getModifiers().contains(Modifier.PRIVATE)) {
      throw new ProcessingException(element,
          "Field %s is private, must be accessible from inherited class", element.getSimpleName());
    }
    annotatedElement = element;

    type = TypeName.get(element.asType());

    Key annotation = element.getAnnotation(Key.class);
    name = element.getSimpleName().toString();
    preferenceKey = Strings.isNullOrEmpty(annotation.name()) ? Utils.lowerCamelToLowerSnake(name)
        : annotation.name();
    omitGetterPrefix = annotation.omitGetterPrefix();
    needCommitMethod = annotation.needCommitMethod();
    needGetterMethod = annotation.getterType() == Key.GetterType.GETTER || annotation.getterType() == Key.GetterType.GETTER_AND_OBSERVABLE;
    needObservableMethod = annotation.getterType() == Key.GetterType.OBSERVABLE || annotation.getterType() == Key.GetterType.GETTER_AND_OBSERVABLE;
  }
}
