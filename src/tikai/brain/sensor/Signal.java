package tikai.brain.sensor;

public class Signal {

    byte[] data;
    Source source;

    public Signal(byte[] data, Source source) {
        this.data = data;
        this.source = source;
    }

    public byte[] getData() {
        return data;
    }

    public Source getSource() {
        return source;
    }

    public enum Source {
        TEXT, AUDIO, SCREEN
    }
}
