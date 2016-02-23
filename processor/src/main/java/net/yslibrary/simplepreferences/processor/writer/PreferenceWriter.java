package net.yslibrary.simplepreferences.processor.writer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import net.yslibrary.simplepreferences.processor.PreferenceAnnotatedClass;

/**
 * Created by yshrsmz on 2016/02/23.
 */
public class PreferenceWriter {

  private final PreferenceAnnotatedClass annotatedClass;

  public PreferenceWriter(PreferenceAnnotatedClass annotatedClass) {
    this.annotatedClass = annotatedClass;
  }

  public TypeSpec write() {
    boolean useDefaultPreferences = annotatedClass.useDefaultPreferences();
    String preferenceName = annotatedClass.preferenceName;
    String preferenceClassName = annotatedClass.preferenceClassName;
    String packageName = annotatedClass.packageName;
    TypeElement annotatedElement = annotatedClass.annotatedElement;

    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(preferenceClassName)
        .addModifiers(Modifier.PUBLIC)
        .superclass(ClassName.get(annotatedElement));

    TypeName generatingClass = ClassName.get(packageName, preferenceClassName);

    // SharedPreferences field ---
    FieldSpec prefsField =
        FieldSpec.builder(SharedPreferences.class, "prefs", Modifier.PRIVATE, Modifier.FINAL)
            .build();
    classBuilder.addField(prefsField);

    // constructor
    MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PRIVATE)
        .addParameter(Context.class, "context");
    constructorBuilder.beginControlFlow("if (context == null)")
        .addStatement("throw new NullPointerException($S)", "Context is Null!")
        .endControlFlow();
    if (useDefaultPreferences) {
      constructorBuilder.addStatement("prefs = $T.getDefaultSharedPreferences(context)",
          PreferenceManager.class);
    } else {
      constructorBuilder.addStatement(
          "prefs = context.getSharedPreferences($S, Context.MODE_PRIVATE)", preferenceName);
    }
    MethodSpec constructor = constructorBuilder.build();
    classBuilder.addMethod(constructor);

    // create method ---
    MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(Context.class, "context")
        .returns(generatingClass);

    createMethod.beginControlFlow("if (context == null)")
        .addStatement("throw new NullPointerException($S)", "Context is Null!")
        .endControlFlow();
    createMethod.addStatement("return new $T(context)", generatingClass);

    classBuilder.addMethod(createMethod.build());

    // clear method ---
    MethodSpec.Builder clearMethod =
        MethodSpec.methodBuilder("clear").addModifiers(Modifier.PUBLIC);
    clearMethod.addStatement("prefs.edit().clear().apply()");

    classBuilder.addMethod(clearMethod.build());

    // keys
    annotatedClass.keys.forEach(keyAnnotatedField -> {
      List<MethodSpec> methods = TypeWriter.create(keyAnnotatedField).writeMethods(prefsField);

      classBuilder.addMethods(methods);
    });

    return classBuilder.build();
  }
}
