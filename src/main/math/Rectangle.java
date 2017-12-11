package main.math;

public class Rectangle {
    private Polygon shape;
    private float length, height;

    public Rectangle(float length, float height) {
        this.length = length;
        this.height = height;
        this.shape = new Polygon(0, 0, this.length, 0, this.length, this.height, 0, this.height);
    }

    public Rectangle(Polygon shape) {
        int size = shape.getPoints().size();
        if (size != 4) this.shape = new Polygon(0, 0, 1, 0, 1, 1, 0, 1);
        else this.shape = shape;
        this.length = this.shape.getPoints().get(2).x;
        this.height = this.shape.getPoints().get(2).y;
    }

    public float getHeight() {
        return this.height;
    }

    public float getLength() {
        return this.length;
    }
}
