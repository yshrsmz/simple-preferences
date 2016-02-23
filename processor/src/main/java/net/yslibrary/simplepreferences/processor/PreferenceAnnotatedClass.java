package net.yslibrary.simplepreferences.processor;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
      Map<String, KeyAnnotatedField> map = new LinkedHashMap<>();
      element.getEnclosedElements()
          .stream()
          .filter(variable -> variable.getAnnotation(Key.class) != null)
          .forEach(variable -> {
            try {
              KeyAnnotatedField field = new KeyAnnotatedField((VariableElement) variable);
              KeyAnnotatedField existing = map.get(field.preferenceKey);

              if (existing != null) {
                throw new ProcessingException(variable,
                    "Conflict: Preference key of the field %s annotated with @%s is declared as '%s', but %s already uses same value",
                    field.name, Key.class.getSimpleName(), field.preferenceKey, existing.name);
              }
              map.put(field.preferenceKey, field);
            } catch (ProcessingException e) {
              throw new RuntimeException(e);
            }
          });

      keys.addAll(map.values());
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
