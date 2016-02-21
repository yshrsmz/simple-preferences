package net.yslibrary.simplepreferences.processor;

import com.google.auto.service.AutoService;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import net.yslibrary.simplepreferences.annotation.Preferences;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
    "ney.yslibrary.simplepreferences.annotation.*"
})
@AutoService(Processor.class)
public class SimplePreferencesProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private Types typeUtils;
  private Filer filer;
  private Messager messager;

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
    if (annotations.isEmpty()) {
      return true;
    }

    roundEnv.getElementsAnnotatedWith(Preferences.class).forEach(element -> {
      if (!element.getKind().isClass()) {
        error(element, "Only classes can be annotated with @%s", Preferences.class.getSimpleName());
        return;
      }


    });

    return true;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }
}
