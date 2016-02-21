package net.yslibrary.simplepreferences.processor;

import com.squareup.javapoet.TypeName;
import javax.lang.model.element.VariableElement;

/**
 * Created by yshrsmz on 2016/02/21.
 */
public class KeyAnnotatedField {

  public final VariableElement annotatedElement;
  public final TypeName type;

  public KeyAnnotatedField(VariableElement element) {
    annotatedElement = element;
    type = TypeName.get(element.asType());
  }
}
