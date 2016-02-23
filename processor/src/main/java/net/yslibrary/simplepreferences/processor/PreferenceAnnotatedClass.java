package net.yslibrary.simplepreferences.processor;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;

/**
 * Created by yshrsmz on 2016/02/21.
 */
public class PreferenceAnnotatedClass {

  public final static String DEFAULT_PREFS = "default";
  private final static String SUFFIX = "Prefs";

  public final TypeElement annotatedElement;

  // used as SharedPreferences' name
  public final String preferenceName;

  // generated class name
  public final String preferenceClassName;
  public final String qualifiedPreferenceClassName;

  public final String packageName;

  public final List<KeyAnnotatedField> keys = new ArrayList<>();

  public PreferenceAnnotatedClass(TypeElement element, Elements elementUtils)
      throws IllegalStateException, ProcessingException {
    annotatedElement = element;
    Preferences annotation = annotatedElement.getAnnotation(Preferences.class);
    String value = annotation.value().trim();
    String simpleName = element.getSimpleName().toString();
    preferenceName = Strings.isNullOrEmpty(value) ? DEFAULT_PREFS : value;

    if (Strings.isNullOrEmpty(preferenceName)) {
      throw new IllegalStateException(String.format(
          "value() in @%s for Class %s is null, empty or target Class is anonymous class. You must specify value or class should be concrete.",
          Preferences.class.getSimpleName(), element.getQualifiedName()));
    }

    preferenceClassName = simpleName + SUFFIX;
    qualifiedPreferenceClassName = element.getQualifiedName().toString() + SUFFIX;

    PackageElement pkg = elementUtils.getPackageOf(element);
    packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

    try {
      List<KeyAnnotatedField> annotatedVariables = element.getEnclosedElements()
          .stream()
          .filter(variable -> variable.getAnnotation(Key.class) != null)
          .map(variable -> {
            try {
              return new KeyAnnotatedField((VariableElement) variable);
            } catch (ProcessingException e) {
              throw new RuntimeException(e);
            }
          })
          .collect(Collectors.toList());

      keys.addAll(annotatedVariables);
    } catch (RuntimeException e) {
      if (e.getCause() instanceof ProcessingException) {
        throw (ProcessingException) e.getCause();
      }
    }
  }

  public boolean useDefaultPreferences() {
    return preferenceName.equals(DEFAULT_PREFS);
  }
}
