package tikai.brain;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import tikai.brain.processor.Processor;
import tikai.brain.processor.TalkProcessor;
import tikai.brain.processor.TextProcessor;
import tikai.brain.sensor.Signal;

public class Brain implements Runnable {

  private final int MAX_SIGNAL_BUFFER = 5;
  private final int SLEEP_CYCLE_MS = 1000;

  private Queue<Signal> signals;
  private boolean sleeping = false;
  private boolean alive = true;

  private NeuralNetwork neuralNetwork;
  private Processor textProcessor;

  public Brain() {
    this.signals = new ConcurrentLinkedQueue<>();
    var talkProcessor = new TalkProcessor();
    var talkLayer = new TalkLayer(talkProcessor);
    this.textProcessor = new TextProcessor();
    
    this.neuralNetwork = new NeuralNetwork();
    this.neuralNetwork.connectToLayer(talkLayer);

    Thread consciousness = new Thread(neuralNetwork);
    consciousness.start();
  }

  public boolean isAlive() {
    return alive;
  }

  public void kill() {
    this.alive = false;
    this.neuralNetwork.stun();
  }

  public boolean isAwake() {
    return !sleeping;
  }
  
  @Override
  public void run() {
    while (isAlive()) {
      var signal = signals.poll();
      if (signal != null && this.isAwake()) {
        switch (signal.getSource()) {
          case TEXT:
            var encodedSignals = textProcessor.process(signal.getData());
            neuralNetwork.receiveSignals(encodedSignals);
            break;
          case AUDIO:
          case SCREEN:
          default:
            break;
        }
      }
      try {
          Thread.sleep(SLEEP_CYCLE_MS);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
  }

  public void receive(Signal signal) {
    if (signals.size() < MAX_SIGNAL_BUFFER || signal.getSource() == Signal.Source.TEXT) {
      signals.add(signal);
    }
  }
}