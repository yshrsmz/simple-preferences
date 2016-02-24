package net.yslibrary.simplepreferences.processor;

import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

/**
 * Created by yshrsmz on 2016/02/24.
 */
public class SimplePreferencesProcessorTest {

  @Test
  public void testValidClassWithDefaultPref() {
    JavaFileObject modelFile = JavaFileObjects.forResource("ValidPrefsWithDefaultName.java");

    assert_().about(javaSource())
        .that(modelFile)
        .processedWith(new SimplePreferencesProcessor())
        .compilesWithoutError();
  }

  @Test
  public void testValidClassWithCustomPref() {
    JavaFileObject modelFile = JavaFileObjects.forResource("ValidPrefsWithCustomName.java");

    assert_().about(javaSource())
        .that(modelFile)
        .processedWith(new SimplePreferencesProcessor())
        .compilesWithoutError();
  }

  @Test
  public void testDuplicatePreferenceNames() {
    JavaFileObject modelFileA = JavaFileObjects.forResource("PrefsWithSameNameA.java");
    JavaFileObject modelFileB = JavaFileObjects.forResource("PrefsWithSameNameB.java");

    assert_().about(javaSources())
        .that(Arrays.asList(modelFileA, modelFileB))
        .processedWith(new SimplePreferencesProcessor())
        .failsToCompile()
        .withErrorCount(1)
        .withErrorContaining(
            "Conflict: Class net.yslibrary.simplepreferences.processor.PrefsWithSameNameB is annotated with @Preferences with value='custom_name', but net.yslibrary.simplepreferences.processor.PrefsWithSameNameA already uses same value");
  }
}
