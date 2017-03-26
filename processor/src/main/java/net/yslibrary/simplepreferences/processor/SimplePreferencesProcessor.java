package net.yslibrary.simplepreferences.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.yslibrary.simplepreferences.annotation.Preferences;
import net.yslibrary.simplepreferences.processor.exception.ProcessingException;
import net.yslibrary.simplepreferences.processor.writer.PreferenceResourceWriter;
import net.yslibrary.simplepreferences.processor.writer.PreferenceWriter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
  private Path resourcePath;

  private Map<String, PreferenceAnnotatedClass> items = new LinkedHashMap<>();

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    elementUtils = processingEnv.getElementUtils();
    typeUtils = processingEnv.getTypeUtils();
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
    try {
      resourcePath = getResPath();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

    //Make xml
    List<PreferenceAnnotatedClass> classes = items.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    try {
      PreferenceResourceWriter writer = new PreferenceResourceWriter(classes);
      writer.write();
      writeXml(writer);
    } catch (ParserConfigurationException | TransformerException | IOException e) {
      error(null, e.getMessage());
    }

    items.clear();

    return true;
  }

  private void writeXml(PreferenceResourceWriter xml) throws IOException, TransformerException, ParserConfigurationException {

    String fileName = "simple_preferences.xml";

    if (!Files.exists(resourcePath)) {
      Files.createDirectories(resourcePath);
    }

    File resource = new File(resourcePath.toFile(), fileName);

    if (!resource.exists()) {
      resource.createNewFile();
    }

    xml.toFile(resource);
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

  /**
   * Get the path where the string resources should go.
   *
   * @return The path.
   *
   * @throws IOException An error.
   */
  private Path getResPath() throws IOException {
    FileObject dummy = filer.createResource(StandardLocation.SOURCE_OUTPUT,"","dummy");

    Path parent = Paths.get(dummy.toUri()).getParent();

    String variant = parent.getFileName().toString();

    // Find index of the generated folder
    Path basePath = Paths.get(parent.toUri());
    while(!basePath.endsWith("generated")) {
      basePath = basePath.getParent();
    }

    System.out.println("PATH: variant: " + variant);
    System.out.println("PATH: base: " + basePath);

    return Paths.get(basePath.toString(), "res", "resValues", variant, "values");
  }
}
