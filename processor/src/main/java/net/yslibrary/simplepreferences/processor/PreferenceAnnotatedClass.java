package net.yslibrary.simplepreferences.processor;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

/**
 * Created by yshrsmz on 2016/02/21.
 */
public class PreferenceAnnotatedClass {

  public final TypeElement annotatedElement;
  public final String preferenceName;
  public final String preferenceClassName;
  public final String packageName;

  public final List<KeyAnnotatedField> keys = new ArrayList<>();

  public PreferenceAnnotatedClass(TypeElement element, Elements elementUtils)
      throws IllegalStateException {
    annotatedElement = element;
    Preferences annotation = annotatedElement.getAnnotation(Preferences.class);
    String value = annotation.value().trim();
    String simpleName = element.getSimpleName().toString();
    preferenceName =
        Strings.isNullOrEmpty(value) ? CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
            simpleName) : value;

    if (Strings.isNullOrEmpty(preferenceName)) {
      throw new IllegalStateException(String.format(
          "value() in @%s for Class %s is null, empty or target Class is anonymous class. You must specify value or class should be concrete.",
          Preferences.class.getSimpleName(), element.getQualifiedName()));
    }

    preferenceClassName = simpleName + "Prefs";

    packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();

    List<KeyAnnotatedField> annotatedVariables = element.getEnclosedElements()
        .stream()
        .filter(variable -> variable.getAnnotation(Key.class) != null)
        .map(variable -> new KeyAnnotatedField((VariableElement) variable))
        .collect(Collectors.toList());

    keys.addAll(annotatedVariables);
  }
}
