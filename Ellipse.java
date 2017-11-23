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
				this.longRadius * 2,
				this.shortRadius * 2
		);
		return new Path2D.Float(ellipse);
	}

	@Override
	Part build(FixtureDef fixtureDef, Entity entity) {
		// Create Box2D polygonal shape
		PolygonShape ellipseApproxShape = new PolygonShape();

		ellipseApproxShape.set(new Vec2[]{
				new Vec2(center.x, center.y - this.shortRadius),
				new Vec2(center.x - .5f * this.longRadius, center.y - .9375f * this.shortRadius),   // 15/16th
				new Vec2(center.x - .75f * this.longRadius, center.y - .875f * this.shortRadius),   // 7/8th
				new Vec2(center.x - .875f * this.longRadius, center.y - .5f * this.shortRadius),    // 1/2th
				new Vec2(center.x - .9375f * this.longRadius, center.y - .25f * this.shortRadius),  // 1/4th
				new Vec2(center.x - this.longRadius, 0.f),
				new Vec2(center.x - .9375f * this.longRadius, center.y + .25f * this.shortRadius),
				new Vec2(center.x - .875f * this.longRadius, center.y + .5f * this.shortRadius),
				new Vec2(center.x - .75f * this.longRadius, center.y + .875f * this.shortRadius),
				new Vec2(center.x - .5f * this.longRadius, center.y + .9375f * this.shortRadius),
				new Vec2(center.x, center.y + this.shortRadius),
				new Vec2(center.x + .5f * this.longRadius, center.y + .9375f * this.shortRadius),
				new Vec2(center.x + .75f * this.longRadius, center.y + .875f * this.shortRadius),
				new Vec2(center.x + .875f * this.longRadius, center.y + .5f * this.shortRadius),
				new Vec2(center.x + .9375f * this.longRadius, center.y + .25f * this.shortRadius),
				new Vec2(center.x + this.longRadius, 0.f),
				new Vec2(center.x + .9375f * this.longRadius, center.y - .25f * this.shortRadius),
				new Vec2(center.x + .875f * this.longRadius, center.y - .5f * this.shortRadius),
				new Vec2(center.x + .75f * this.longRadius, center.y - .875f * this.shortRadius),
				new Vec2(center.x + .5f * this.longRadius, center.y - .9375f * this.shortRadius),
				new Vec2(center.x, center.y - this.shortRadius)
		}, 11);

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
