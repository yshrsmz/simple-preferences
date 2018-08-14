package net.yslibrary.simplepreferences.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.yslibrary.simplepreferences.annotation.Preferences;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;
import net.yslibrary.simplepreferences.processor.writer.PreferenceWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
    "net.yslibrary.simplepreferences.annotation.Key",
    "net.yslibrary.simplepreferences.annotation.Preferences"
})
@AutoService(Processor.class)
public class SimplePreferencesProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private Types typeUtils;
  private Filer filer;
  private Messager messager;

  private Map<String, PreferenceAnnotatedClass> items = new LinkedHashMap<>();

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    elementUtils = processingEnv.getElementUtils();
    typeUtils = processingEnv.getTypeUtils();
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    messager.printMessage(Diagnostic.Kind.NOTE, "start processor");
    if (annotations.isEmpty()) {
      return true;
    }

    // prepare
    roundEnv.getElementsAnnotatedWith(Preferences.class).forEach(element -> {
      if (!element.getKind().isClass()) {
        error(element, "Only classes can be annotated with @%s", Preferences.class.getSimpleName());
        return;
      }

      try {
        TypeElement typeElement = (TypeElement) element;

        PreferenceAnnotatedClass model = new PreferenceAnnotatedClass(typeElement, elementUtils);

        checkValid(model);

        PreferenceAnnotatedClass existing = items.get(model.preferenceName);

        if (existing != null) {
          throw new ProcessingException(typeElement,
              "Conflict: Class %s is annotated with @%s with value='%s', but %s already uses same value",
              model.annotatedElement, Preferences.class.getSimpleName(), model.preferenceName,
              existing.annotatedElement.getQualifiedName().toString());
        }

        items.put(model.preferenceName, model);
      } catch (ProcessingException e) {
        error(e.element, e.getMessage());
      } catch (IllegalStateException e) {
        error(null, e.getMessage());
      }
    });

    // generate
    items.forEach((key, preferenceAnnotatedClass) -> {
      try {
        TypeSpec clazz = (new PreferenceWriter(preferenceAnnotatedClass)).write();
        JavaFile.builder(preferenceAnnotatedClass.packageName, clazz).build().writeTo(filer);
      } catch (IOException e) {
        error(null, e.getMessage());
      }
    });

    items.clear();

    return true;
  }

  private void error(Element e, String msg, Object... args) {
    error(e, String.format(msg, args));
  }

  private void error(Element e, String msg) {
    messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
  }

  private void checkValid(PreferenceAnnotatedClass item) throws ProcessingException {
    TypeElement element = item.annotatedElement;

    if (element.getModifiers().contains(Modifier.PRIVATE)) {
      throw new ProcessingException(element, "The class %s is private",
          element.getQualifiedName().toString());
    }

    if (element.getModifiers().contains(Modifier.FINAL)) {
      throw new ProcessingException(element, "The class %s is define as final",
          element.getQualifiedName().toString());
    }
  }
}
