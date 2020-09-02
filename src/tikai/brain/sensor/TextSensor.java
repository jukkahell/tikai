package tikai.brain.sensor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

import tikai.brain.Brain;

public class TextSensor implements Sensor {

  private Brain brain;
  private Scanner scanner;

  public TextSensor(final InputStream source) {
    scanner = new Scanner(new BufferedReader(new InputStreamReader(source, Charset.forName("IBM850")), 10240));
  }

  @Override
  public void send(final Signal signal) {
    brain.receive(signal);
  }

  @Override
  public void connectToBrain(final Brain brain) {
    this.brain = brain;
    var listener = new Thread(this);
    listener.start();
  }

  private void listen() {
    try {
      if (scanner.hasNextLine()) {
        final var text = scanner.nextLine();
        if (text != null) {
          final Signal signal = new Signal(text.getBytes("IBM850"), Signal.Source.TEXT);
          send(signal);
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    while (brain.isAlive()) {
      listen();
    }
  }
}
