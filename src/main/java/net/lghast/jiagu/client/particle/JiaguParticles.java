package net.lghast.jiagu.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JiaguParticles extends TextureSheetParticle {
    private final int LIFETIME = 20;
    private final SpriteSet sprites;
    private final RandomSource random;
    protected JiaguParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet,
                             double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.8f;

        this.lifetime = LIFETIME;
        this.sprites = spriteSet;
        this.random = level.random;

        this.setSprite(this.sprites.get(this.random));

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.quadSize = 0.06f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age==LIFETIME || this.age % 6 == 0) {
            this.setSprite(this.sprites.get(this.random));
        }
        this.yd = Math.max(yd-0.04,-0.08);

        float progress = (float) this.age / this.lifetime;
        this.alpha = 1.0F - progress * progress;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new JiaguParticles(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
