package net.yslibrary.simplepreferences.processor.exception;

import javax.lang.model.element.Element;

/**
 * Created by yshrsmz on 2016/02/22.
 */
public class ProcessingException extends Exception {

  public final Element element;

  public ProcessingException(Element element, String message, Object... args) {
    super(String.format(message, args));

    this.element = element;
  }
}
