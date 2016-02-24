package net.yslibrary.simplepreferences.processor;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

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
}
