package tikai;

import tikai.brain.Brain;
import tikai.brain.sensor.TextSensor;

class Main {

  public static void main(String[] args) {
    var tikai = new Brain();
    var textSensor = new TextSensor(System.in);
    textSensor.connectToBrain(tikai);

    var life = new Thread(tikai);
    life.start();
  }
}