package net.yslibrary.simplepreferences.processor;

import java.util.LinkedHashMap;
import java.util.Map;
import net.yslibrary.simplepreferences.annotation.Preferences;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;

/**
 * Created by yshrsmz on 2016/02/22.
 */
public class PreferenceGroupedClasses {

  public final Map<String, PreferenceAnnotatedClass> map = new LinkedHashMap<>();

  public PreferenceGroupedClasses() {

  }

  public void add(PreferenceAnnotatedClass toInsert) throws ProcessingException {
    PreferenceAnnotatedClass existing = map.get(toInsert.preferenceName);

    if (existing != null) {
      throw new ProcessingException(toInsert.annotatedElement,
          "Conflict: Class %s is annotated with @%s with value='%s', but %s already uses same value",
          toInsert.annotatedElement.getQualifiedName().toString(),
          Preferences.class.getSimpleName(), toInsert.preferenceName,
          existing.annotatedElement.getQualifiedName().toString());
    }

    map.put(toInsert.preferenceName, toInsert);
  }
}
