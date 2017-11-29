package main.math;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.Random;

/** By Nicolas BOECKH */
public final class Ellipse extends Shape {
	private final float shortRadius;
	private final float longRadius;
	private final Vector center;

	/**
	 * Creates a new Ellipse.
	 * @param shortRadius : vertical size, non-negative
	 * @param longRadius : horizontal size, non-negative
	 * @param center origin, not null
	 */
	public Ellipse(float shortRadius, float longRadius, Vector center) {
		if (center == null)
			throw new NullPointerException();
		this.shortRadius = shortRadius;
		this.longRadius = longRadius;
		this.center = center;
	}

	/**
	 * Creates a new Ellipse.
	 * @param shortRadius : vertical size, non-negative
	 * @param longRadius : horizontal size, non-negative
	 */
	public Ellipse(float shortRadius, float longRadius) {
		this(shortRadius, longRadius, Vector.ZERO);
	}

	/** @return approximate size of circle */
	public float getRadius() {
		return (this.shortRadius + this.longRadius) / 2.f;
	}

	/** @return origin of circle, not null */
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public float getArea() {
		return (float)Math.PI * this.shortRadius * this.longRadius;
	}

	@Override
	public float getPerimeter() {
		float ramanujanH = (float) (Math.pow(this.longRadius - shortRadius, 2) / Math.pow(this.longRadius + this.shortRadius, 2));
		return (float) (Math.PI * (this.longRadius + this.shortRadius) * (1 + (3 * ramanujanH) / (10 + Math.sqrt(4 - 3 * ramanujanH))));

	}

	@Override
	public Vector sample(Random random) {

		// Sample random angle and distance (density increase quadratically)
		double distance = Math.sqrt(random.nextDouble()) * getRadius();
		double angle = random.nextDouble() * 2.0 * Math.PI;

		// Compute actual location
		return new Vector(
				center.x + (float)(distance * Math.cos(angle)),
				center.y + (float)(distance * Math.sin(angle))
		);
	}

	@Override
	public Path2D toPath() {
		Ellipse2D ellipse = new Ellipse2D.Float(
				center.x - this.longRadius,
				center.y - this.shortRadius,
				this.longRadius * 2.f,
				this.shortRadius * 2.f
		);
		return new Path2D.Float(ellipse);
	}

	@Override
	Part build(FixtureDef fixtureDef, Entity entity) {
		int vertexCount = 32; // Magic value for quick modification
		PolygonShape ellipseApproxShape = new PolygonShape();
		Vec2[] vertices = new Vec2[vertexCount];

		for (int i = 0; i < vertexCount; i++) {
			float angle = (float) (((Math.PI * 2) / vertexCount) * i);
			float radiusAtAngle = (float) ((this.longRadius * this.shortRadius) /
					Math.sqrt(Math.pow(this.longRadius * Math.sin(angle), 2) + Math.pow(this.shortRadius * Math.cos(angle), 2)));
			float xPos = (float) (radiusAtAngle * Math.cos(angle));
			float yPos = (float) (radiusAtAngle * Math.sin(angle));
			vertices[i] = new Vec2(xPos, yPos);
		}

		ellipseApproxShape.set(vertices, 8);
		fixtureDef.shape = ellipseApproxShape;

		// Instanciate the actual body part
		Part part = new Part();
		part.entity = entity;
		fixtureDef.userData = part;
		Fixture fixture = entity.body.createFixture(fixtureDef);
		part.fixtures = Arrays.asList(fixture);
		return part;
	}
}
