package tikai.brain.sensor;

import tikai.brain.Brain;

public interface Sensor extends Runnable {
    void send(Signal signal);
    void connectToBrain(Brain brain);
}
