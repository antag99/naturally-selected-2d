package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.api.PassiveSystem;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;

/**
 * Wrapper for particles. we can make this more efficient later.
 *
 * @author Daan van Yperen
 */
@Wire
public class ParticleSystem extends PassiveSystem {

    public static final float EXPLOSION_FRAME_DURATION = 1/15f;

    ComponentMapper<Anim> am;
    AssetSystem assetSystem;
    private float rotation;

    public void spawnParticle(int x, int y, String particle) {

        switch ( particle )
        {
            case "explosion":
                createExplosion(x,y, 1f);
                break;
            case "tiny-explosion":
                createExplosion(x, y, 0.5f);
                break;
            case "muzzle-flare":
                createMuzzleFlare(x, y);
                break;
            case "bulletcasing":
                createBulletCasing(x, y);
                break;
            case "shellcasing":
                createShellCasing(x, y);
                break;
            case "debris":
                createDebris(x, y);
                break;
            case "alienblood":
                createAlienBlood(x, y);
                break;
            case "gasburn":
                createGasBurn(x, y);
                break;
            case "puff":
                createPuff(x, y);
                break;
            case "bile":
                createBile(x, y);
                break;
        }
    }

    private void createAlienBlood(int x, int y) {
        createWoundParticle(x, y, "particle-alienblood");
    }

    Vector2 vTmp = new Vector2();
    private void createDebris(int x, int y) {

        createWoundParticle(x, y, "particle-debris");
    }

    private void createWoundParticle(int x, int y, String animId) {
        vTmp.set(MathUtils.random(200, 500), 0).rotate(rotation);

        final Physics physics = new Physics();
        physics.vr = MathUtils.random(-90, 90)*10f;
        physics.vx = vTmp.x;
        physics.vy = vTmp.y;
        physics.friction = 0.02f;

        final TextureRegion frame = assetSystem.get(animId).getKeyFrame(0);

        Entity entity = basicCenteredParticle(x, y, animId, 1, 1)
                .addComponent(new Terminal(4f, 3.5f))
                .addComponent(physics)
                .addComponent(new Bounds(frame))
                .addComponent(new Gravity());

        Anim anim = am.get(entity);
        anim.speed = 0;
        anim.age = MathUtils.random(0,10f);

        entity
                .addToWorld();
    }

    private void createShellCasing(int x, int y) {
        Entity entity = basicShellParticle(x, y, "particle-shellcasing");
        am.get(entity).layer= Anim.Layer.DIRECTLY_BEHIND_PLAYER;
        entity
                .addToWorld();
    }

    private void createBulletCasing(int x, int y) {
        Entity entity = basicShellParticle(x, y, "particle-bulletcasing");
        am.get(entity).layer= Anim.Layer.DIRECTLY_BEHIND_PLAYER;
        entity
                .addToWorld();
    }


    private void createGasBurn(int x, int y) {
        vTmp.set(50, 0).rotate(rotation-90);
        final Physics physics = new Physics();
        physics.friction = 0.1f;
        physics.vx = vTmp.x;
        physics.vy = vTmp.y;
        physics.friction = 0.1f;

        Animation animation = assetSystem.get("particle-jetpack");
        final TextureRegion frame = animation.getKeyFrame(0);

        Entity entity = basicCenteredParticle(x, y, "particle-jetpack", 1, 1)
                .addComponent(new Terminal(animation.animationDuration, 0.1f))
                .addComponent(physics)
                .addComponent(new Bounds(frame));
        am.get(entity).layer = Anim.Layer.DIRECTLY_BEHIND_PLAYER;
        entity
                .addToWorld();
    }

    private void createPuff(int x, int y) {
        vTmp.set(100, 0).rotate(rotation-90);
        final Physics physics = new Physics();
        physics.friction = 0.1f;
        physics.vx = vTmp.x;
        physics.vy = vTmp.y;
        physics.friction = 0.1f;

        Animation animation = assetSystem.get("particle-puff");
        final TextureRegion frame = animation.getKeyFrame(0);

        Entity entity = basicCenteredParticle(x, y, "particle-puff", 1, 1)
                .addComponent(new Terminal(3,3))
                .addComponent(physics)
                .addComponent(new Bounds(frame));
        Anim anim = am.get(entity);
        anim.layer = Anim.Layer.DIRECTLY_BEHIND_BEHIND_PLAYER;
        anim.age = MathUtils.random(999f);
        anim.speed = 0;
        entity
                .addToWorld();
    }

    private Entity basicShellParticle(int x, int y, String animId) {
        final Physics physics = new Physics();
        physics.vr = MathUtils.random(-90, -80)*10f;
        physics.vx = MathUtils.random(-90, -80)*1.5f;
        physics.vy = MathUtils.random(100, 110)*1.5f;
        physics.friction = 0.1f;

        final TextureRegion frame = assetSystem.get(animId).getKeyFrame(0);

        return basicCenteredParticle(x, y, animId, 1, 1)
                .addComponent(new Terminal(1.5f,0.5f))
                .addComponent(physics)
                .addComponent(new Bounds(frame))
                .addComponent(new Gravity());
    }

    private void createBile(int x, int y) {
        final Physics physics = new Physics();
        physics.vr = MathUtils.random(-90, 90)*10f;
        physics.vx = MathUtils.random(-90, 90)*1.5f;
        physics.vy = MathUtils.random(100, 110)*1.5f;
        physics.friction = 0.1f;

        final TextureRegion frame = assetSystem.get("bile-droplet").getKeyFrame(0);

        Entity entity = basicCenteredParticle(x, y, "bile-droplet", 1, 1)
                .addComponent(new Terminal(4f, 3.5f))
                .addComponent(physics)
                .addComponent(new Bounds(frame))
                .addComponent(new Gravity());
        am.get(entity).layer = Anim.Layer.DIRECTLY_BEHIND_PLAYER;
        entity.addToWorld();
    }

    private void createMuzzleFlare(int x, int y) {
        basicCenteredParticle(x, y, "particle-muzzleflare", 1, 1)
                .addComponent(new Terminal(1 / 15f))
                .addToWorld();
    }

    private void createExplosion(int x, int y, float scale) {
        float speed = MathUtils.random(0.8f,1f);
        basicCenteredParticle(x, y, "particle-explosion", scale, speed)
                .addComponent(new Terminal(EXPLOSION_FRAME_DURATION * 5 * (1 / speed)))
                .addToWorld();

    }

    /**
     * Spawns a particle, animation centered on x,y.
     *
     * @param x
     * @param y
     * @param animId
     * @return
     */
    private Entity basicCenteredParticle(int x, int y, String animId, float scale, float speed) {
        Anim anim = new Anim(animId);
        anim.scale=scale;
        anim.speed=speed;
        anim.color.a= 0.9f;
        anim.rotation=rotation;

        TextureRegion frame = assetSystem.get(animId).getKeyFrame(0);

        return world.createEntity()
                .addComponent(new Pos(x - ((frame.getRegionWidth() * anim.scale) / 2), y - (frame.getRegionHeight() * anim.scale) / 2))
                .addComponent(anim);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
