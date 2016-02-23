package net.yslibrary.simplepreferences.processor;

import com.google.common.base.Strings;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.VariableElement;
import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;

/**
 * Created by yshrsmz on 2016/02/21.
 */
public class KeyAnnotatedField {

  public final VariableElement annotatedElement;
  public final TypeName type;
  public final String name;
  public final String preferenceKey;
  public final boolean omitGetterPrefix;

  public KeyAnnotatedField(VariableElement element) throws ProcessingException {
    annotatedElement = element;

    type = TypeName.get(element.asType());

    Key annotation = element.getAnnotation(Key.class);
    name = element.getSimpleName().toString();
    preferenceKey = Strings.isNullOrEmpty(annotation.name()) ? Utils.lowerCamelToLowerSnake(name)
        : annotation.name();
    omitGetterPrefix = annotation.omitGetterPrefix();
  }
}
