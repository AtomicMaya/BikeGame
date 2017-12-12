package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Circle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ParticleEmitter implements Actor {
    /** The master {@linkplain ActorGame}. */
    private ActorGame game;

    /** A {@linkplain LinkedList} of {@linkplain Particle}s to be drawn by the {@linkplain ParticleEmitter}. */
    private LinkedList<Particle> particles;

    /** Given number of particles. */
    private int particlesPerSecond;

    /** The decay rate of the particles -> How many particles should be removed per second. */
    private int decayRate;

    /** The launch angle and by how much this angle can vary. */
    private float angle, angleVariation;

    /** The {@linkplain Particle}'s general life time and by how much this life time can vary. */
    private float particleLifeTime, particleLifeTimeVariation;

    /** The {@linkplain Particle}'s general speed and by how much this speed can vary. */
    private float speed, speedVariation;

    /** The 32-bit representation of an ARGB color. */
    private int startColor, endColor;

    /** Time Constraint, so that the {@linkplain ParticleEmitter} may expire. */
    private float deathStartTime, elapsedTime = 0;

    /** {@linkplain Vector}s representing semi-physical properties. */
    private Vector position, gravity;

    /**
     * Create a new {@linkplain ParticleEmitter}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The emission position {@linkplain Vector}.
     * @param gravity The assigned gravity {@linkplain Vector}.
     * @param particlesPerSecond The quantity of {@linkplain Particle}s per second.
     * @param angle The angle of emission.
     * @param angleVariation The variation of the {@param angle}.
     * @param speed The speed of the emission.
     * @param speedVariation The variation of the {@param speed}.
     * @param particleLifeTime The life time of each {@linkplain Particle}.
     * @param particleLifeTimeVariation The variation of the {@param particleLifeTime}.
     * @param startColor The beginning color.
     * @param endColor The end color.
     * @param deathStartTime When the {@linkplain ParticleEmitter} should start to decay.
     * @param decayRate At what rate (in {@linkplain Particle}s / second) the {@linkplain ParticleEmitter} should decay.
     */
    public ParticleEmitter(ActorGame game, Vector position, Vector gravity, int particlesPerSecond, float angle, float angleVariation,
                           float speed, float speedVariation, float particleLifeTime, float particleLifeTimeVariation,
                           int startColor, int endColor, float deathStartTime, int decayRate) {
        this.game = game;

        this.particlesPerSecond = particlesPerSecond;
        this.angle = angle;
        this.angleVariation = angleVariation;
        this.gravity = gravity == null ? this.game.getGravity().mul(0.003f) : gravity;

        this.speed = speed;
        this.speedVariation = speedVariation;

        this.particleLifeTime = particleLifeTime;
        this.particleLifeTimeVariation = particleLifeTimeVariation;

        this.startColor = startColor;
        this.endColor = endColor;

        this.deathStartTime = deathStartTime;
        this.decayRate = decayRate;

        this.particles = new LinkedList<>();

        this.position = position;
    }

    /**
     * Create a new {@linkplain ParticleEmitter} / Overloaded.
     * @param game The master {@linkplain ActorGame}.
     * @param position The emission position {@linkplain Vector}.
     * @param particlesPerSecond The quantity of {@linkplain Particle}s per second.
     * @param angle The angle of emission.
     * @param speed The speed of the emission.
     * @param particleLifeTime The life time of each {@linkplain Particle}.
     * @param startColor The beginning color.
     * @param endColor The end color.
     * @see #ParticleEmitter(ActorGame, Vector, Vector, int, float, float, float, float, float, float, int, int, float, int).
     */
    public ParticleEmitter(ActorGame game, Vector position, int particlesPerSecond, float angle, float speed, float particleLifeTime, int startColor, int endColor) {
        new ParticleEmitter(game, position, null, particlesPerSecond, angle, (float) Math.PI / 6f, speed, speed * 0.1f, particleLifeTime, particleLifeTime * 0.1f, startColor, endColor, -1, 0);
    }

    /**
     * Spawns a new {@linkplain Particle}.
     * @param offset The spawn offset so that all {@linkplain Particle}s don't spawn in the same place.
     */
    private void spawnParticle(float offset) {
        Random random = new Random();
        // The angle, speed and lifetime are selected randomly from the range [value-variation ; value+variation].
        float angle = (this.angle - this.angleVariation) + random.nextFloat() * ((this.angle + this.angleVariation) - (this.angle - this.angleVariation));
        float speed = (this.speed - this.speedVariation) + random.nextFloat() * ((this.speed + this.speedVariation) - (this.speed - this.speedVariation));
        float lifeTime = (this.particleLifeTime - this.particleLifeTimeVariation) + random.nextFloat() * ((this.particleLifeTime + this.particleLifeTimeVariation) - (this.particleLifeTime - this.particleLifeTimeVariation));
        Vector velocity = new Vector((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
        Vector position = this.position.add(velocity.mul(offset));

        this.particles.push(new Particle(position, new Circle(.05f),
                this.startColor, this.endColor, lifeTime, velocity, this.gravity));
    }

    @Override
    public void update(float deltaTime) {
        if (this.particles == null) return;

        if (this.deathStartTime >= 0) {
            this.elapsedTime += deltaTime;
            if(this.elapsedTime >= this.deathStartTime && this.particlesPerSecond > 0) {
                this.particlesPerSecond -= this.decayRate * deltaTime;
            }
            if (this.particlesPerSecond < 0) this.particlesPerSecond = 0;
        }

        ArrayList<Particle> particlesToRemove = new ArrayList<>();
        int newParticlesCount = (int) (this.particlesPerSecond * deltaTime);
        if (this.particlesPerSecond > 0) {
            for (int i = 0; i < newParticlesCount; i++) {
                this.spawnParticle((1f + i) / newParticlesCount * deltaTime);
            }
        }

        for (Particle particle : this.particles) {
            if(particle.isFlaggedForDestruction()) {
                particlesToRemove.add(particle);
            } else {
                particle.update(deltaTime);
            }
        }

        for (Particle particle : particlesToRemove)
            this.particles.remove(particle);


        if (this.particles.size() == 0)
            this.destroy(); // Auto-destructs when empty

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
        return this.position;
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    /**
     * Changes the default position.
     * @param position The new position {@linkplain Vector}.
     */
    public void setPosition(Vector position) {
        this.position = position;
    }
}
