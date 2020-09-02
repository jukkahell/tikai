package tikai.brain;

import tikai.brain.processor.Processor;

public class TalkLayer implements NeuralLayer {

  private final double THRESHOLD = 0.8d;
  private Processor talkProcessor;

  public TalkLayer(Processor talkProcessor) {
    this.talkProcessor = talkProcessor;
  }

  @Override
  public void receive(double[] signals) {
    // We have 255 characters which can be represented with 8 bits (2^8)
    // So there should be 8 signals which we'll encode to a character and put into a buffer
    var bits = new StringBuilder();
    for (int i = 0; i < signals.length; i++) {
      bits.append(signals[i] > THRESHOLD ? 1 : 0);
    }
    byte b = Byte.parseByte(bits.toString(), 2);
    talkProcessor.process(new byte[] { b });
  }
}