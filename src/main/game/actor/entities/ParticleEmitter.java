package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ParticleEmitter implements Actor{
    private ActorGame game;

    private ShapeGraphics graphics;
    private LinkedList<Particle> particles;
    private int particlesPerSecond;

    private float angle, angleVariation, particleLifeTime, particleLifeTimeVariation;
    private float speed, speedVariation;

    private int startColor, endColor;

    private Vector position, gravity;

    public ParticleEmitter(ActorGame game, Vector position, int particlesPerSecond, float angle, float angleVariation, float speed, float speedVariation, float particleLifeTime, float particleLifeTimeVariation, int startColor, int endColor) {
        this.game = game;

        this.particlesPerSecond = particlesPerSecond;
        this.angle = angle;
        this.angleVariation = angleVariation;
        this.gravity = this.game.getGravity().mul(0.003f);

        this.speed = speed;
        this.speedVariation = speedVariation;

        this.particleLifeTime = particleLifeTime;
        this.particleLifeTimeVariation = particleLifeTimeVariation;

        this.startColor = startColor;
        this.endColor = endColor;

        this.particles = new LinkedList<>();

        this.position = position;
        this.game.addActor(this);
    }

    public ParticleEmitter(ActorGame game, Vector position, int particlesPerSecond, float angle, float speed, float particleLifeTime, int startColor, int endColor) {
        new ParticleEmitter(game, position, particlesPerSecond, angle, (float) Math.PI / 6f, speed, speed * 0.1f, particleLifeTime, particleLifeTime * 0.1f, startColor, endColor);
    }

    private void spawnParticle(float offset) {
        Random random = new Random();
        float angle = (this.angle - this.angleVariation) + random.nextFloat() * ((this.angle + this.angleVariation) - (this.angle - this.angleVariation));
        float speed = (this.speed - this.speedVariation) + random.nextFloat() * ((this.speed + this.speedVariation) - (this.speed - this.speedVariation));
        float lifeTime = (this.particleLifeTime - this.particleLifeTimeVariation) + random.nextFloat() * ((this.particleLifeTime + this.particleLifeTimeVariation) - (this.particleLifeTime - this.particleLifeTimeVariation));
        Vector velocity = new Vector((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
        Vector position = this.position.add(velocity.mul(offset));

        this.particles.push(new Particle(position, new Circle(.1f),
                this.startColor, this.endColor, lifeTime, velocity, this.gravity));
    }

    @Override
    public void update(float deltaTime) {
        ArrayList<Particle> particlesToRemove = new ArrayList<>();
        int newParticlesCount = (int) (this.particlesPerSecond * deltaTime);
        for (int i = 0; i < newParticlesCount; i++) {
            this.spawnParticle((1f + i) / newParticlesCount * deltaTime);
        }

        for(Particle particle : this.particles) {
            if(particle.isFlaggedForDestruction()) {
                particlesToRemove.add(particle);
            } else {
                particle.update(deltaTime);
            }
        }

        for(Particle particle :  particlesToRemove) {
            this.particles.remove(particle);
        }

        if(this.particles.size() == 0) {
            this.destroy();
        }
    }

    @Override
    public void destroy() {
        this.game.destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        for(Particle particle : this.particles)
            particle.draw(canvas);
    }

    @Override
    public Vector getVelocity() {
        return null;
    }

    @Override
    public Vector getPosition() {
        return null;
    }

    @Override
    public Transform getTransform() {
        return null;
    }
}
