package main.game.RRT;

import main.math.Polyline;

import java.util.ArrayList;
import java.util.Random;

class LGenPoint {
    private ArrayList<Float> coordinates;

    public LGenPoint() {
        this.coordinates = new ArrayList<>();
    }

    public void setCoordinate(Float value, int index) {
        this.coordinates.add(index, value);
    }

    public Float getCoordinate(int index) {
        return this.coordinates.get(index);
    }

    public int getSize() {
        return this.coordinates.size();
    }
}

/**
 * Generates "Lightning"
 */
public class LightningGen {
    private static LGenPoint newRandomPoint(int dimension, int bound) {
        LGenPoint point = new LGenPoint();
        for (int i = 0; i < dimension; i++) {
            Random generator = new Random();
            point.setCoordinate((float) generator.nextInt(bound), i);
        }
        return point;
    }

    public static ArrayList<Polyline> getPolylines(int size) {
        int dimensions = 2;
        int iterations = 15;

        ArrayList<Float> xValues = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            LGenPoint point = newRandomPoint(dimensions, size);
            xValues.add(point.getCoordinate(0));
            yValues.add(point.getCoordinate(1));
        }

        ArrayList<Polyline> lines = new ArrayList<>();
        int x[] = new int[xValues.size()], y[] = x.clone(), j;
        for (int i = 0; i < xValues.size() / 2; i++) {
            for (j = 0, x[i] = xValues.get(new Random().nextInt(xValues.size() - 1)).intValue(),
                         y[i] = yValues.get(new Random().nextInt(yValues.size() - 1)).intValue(); j < i; lines.add(new Polyline(x[i], y[i], x[j], y[j++])));
        }
        return lines;
    }
}


