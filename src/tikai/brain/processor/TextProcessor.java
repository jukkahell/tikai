package tikai.brain.processor;

public class TextProcessor implements Processor {

  private final int LAYER_SIZE = 8;
  public TextProcessor() {}

  @Override
  public double[][] process(byte[] data) {
    var result = new double[data.length][LAYER_SIZE];
    for (int i = 0; i < data.length; i++) {
      String bits = String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(' ', '0');
      for (int j = 0; j < bits.length(); j++) {
        result[i][j] = Integer.parseInt(String.valueOf(bits.charAt(j)));
      }
    }
    return result;
  }
}