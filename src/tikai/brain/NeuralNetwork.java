package tikai.brain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NeuralNetwork implements Runnable {

  private final int SLEEP_CYCLE_MS = 1000;
  private final int NEURONS = 300;
  private final int MAX_CONNECTIONS = 5;

  private boolean conscious = true;

  private double[][] weights;
  private long[] activationTimes;
  private int[][] connections;
  private Map<Integer, Double> activeNeurons;
  
  public NeuralNetwork() {
    weights = new double[NEURONS][MAX_CONNECTIONS];
    activationTimes = new long[NEURONS];
    connections = new int[NEURONS][MAX_CONNECTIONS];
    activeNeurons = new HashMap<>();

    initValues();
  }

  public void connectToLayer(NeuralLayer layer) {
    
  }

  public void receiveSignals(double[][] signals) {
    for (int byteIdx = 0; byteIdx < signals.length; byteIdx++) {
      for (int neuron = 0; neuron < signals[byteIdx].length; neuron++) {
        receiveSignal(neuron, signals[byteIdx][neuron]);
      }
    }
  }

  public void receiveSignal(int neuron, double signal) {
    long now = Instant.now().toEpochMilli();
    if (!activeNeurons.containsKey(neuron)) {
      activeNeurons.put(neuron, signal);
    } else {
      double diff = Math.min(1d, (now - activationTimes[neuron]) / 100d);
      double newSignal = activeNeurons.get(neuron) + signal - diff;
      double sigmoid = 1 / (1 + Math.exp(-newSignal));
      activeNeurons.put(neuron, sigmoid);
    }
    activationTimes[neuron] = now;
  }

  public void think() {
    for (var activeNeuron : activeNeurons.entrySet()) {
      System.out.println(activeNeuron.getKey() + "/" + activeNeuron.getValue());
    }
  }

  public void stun() {
    this.conscious = false;
  }

  @Override
  public void run() {
    while (conscious) {
      this.think();
      try {
        Thread.sleep(SLEEP_CYCLE_MS);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  
  private void initValues() {
    for (int currentNeuron = 0; currentNeuron < NEURONS; currentNeuron++) {
      // Establish connection from neuron to 1-MAX_CONNECTIONS other neurons
      var random = new Random();
      int connectionCount = random.nextInt(MAX_CONNECTIONS) + 1;
      for (int connectionIdx = 0; connectionIdx < connectionCount; connectionIdx++) {
        // Pick a neuron to connect with
        int toNeuron = random.nextInt(NEURONS);
        if (toNeuron != currentNeuron && !connectionExists(currentNeuron, toNeuron)) {
          var weight = random.nextDouble();
          connections[currentNeuron][connectionIdx] = toNeuron;
          weights[currentNeuron][connectionIdx] = weight;
          //System.out.println("Established connection from neuron " + currentNeuron + " to " + toNeuron + " with weight " + weight);
        } else {
          connectionIdx--;
        }
      }
    }
  }

  private boolean connectionExists(int from, int to) {
    for (int i = 0; i < connections[from].length; i++) {
      if (connections[from][i] == to) {
        return true;
      }
    }
    return false;
  }
}