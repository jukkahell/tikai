package tikai.brain.processor;

import java.nio.charset.Charset;

public class TalkProcessor implements Processor {

  public TalkProcessor() {}

  @Override
  public double[][] process(byte[] data) {
    var word = new String(data, Charset.forName("IBM850"));
    System.out.print(word);
    return null;
  }
}