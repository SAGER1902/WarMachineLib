package wmlib.common.bullet;

import com.google.common.base.MoreObjects; // MoreObjects.firstNonNull
import net.minecraft.core.BlockPos; // BlockPos
import net.minecraft.nbt.CompoundTag; // CompoundTag
import net.minecraft.network.protocol.Packet; // Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener; // ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket; // ClientboundAddEntityPacket
import net.minecraft.server.level.ServerLevel; // ServerLevel
import net.minecraft.util.Mth; // Mth
import net.minecraft.world.entity.Entity; // Entity
import net.minecraft.world.entity.EntityType; // EntityType
import net.minecraft.world.entity.TraceableEntity; // TraceableEntity
import net.minecraft.world.entity.player.Player; // Player
import net.minecraft.world.level.GameRules; // GameRules
import net.minecraft.world.level.Level; // Level
import net.minecraft.world.level.block.state.BlockState; // BlockState
import net.minecraft.world.level.gameevent.GameEvent; // GameEvent
import net.minecraft.world.phys.BlockHitResult; // BlockHitResult
import net.minecraft.world.phys.EntityHitResult; // EntityHitResult
import net.minecraft.world.phys.HitResult; // HitResult
import net.minecraft.world.phys.Vec3; // Vec3
import javax.annotation.Nullable; // @Nullable
import java.util.UUID; // UUID

public abstract class ProjectileBase extends Entity implements TraceableEntity {
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    private boolean leftOwner;
    private boolean hasBeenShot;

    ProjectileBase(EntityType<? extends ProjectileBase> entityType, Level level) {
        super(entityType, level);
    }
    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }
    @Override
    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        }
        if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            return this.cachedOwner;
        }
        return null;
    }
    public Entity getEffectSource() {
        return MoreObjects.firstNonNull(this.getOwner(), this);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            compound.putBoolean("LeftOwner", true);
        }
        compound.putBoolean("HasBeenShot", this.hasBeenShot);
    }

    protected boolean ownedBy(Entity entity) {
        return entity.getUUID().equals(this.ownerUUID);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
            this.cachedOwner = null;
        }
        this.leftOwner = compound.getBoolean("LeftOwner");
        this.hasBeenShot = compound.getBoolean("HasBeenShot");
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        if (!this.hasBeenShot) {
            this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        super.tick();
    }

    private boolean checkLeftOwner() {
        Entity $$02 = this.getOwner();
        if ($$02 != null) {
            for (Entity $$1 : this.level().getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), entity -> !entity.isSpectator() && entity.isPickable())) {
                if ($$1.getRootVehicle() != $$02.getRootVehicle()) continue;
                return false;
            }
        }
        return true;
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult result) {
        HitResult.Type $$1 = result.getType();
        if ($$1 == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)result);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        } else if ($$1 == HitResult.Type.BLOCK) {
            BlockHitResult $$2 = (BlockHitResult)result;
            this.onHitBlock($$2);
            BlockPos $$3 = $$2.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, $$3, GameEvent.Context.of(this, this.level().getBlockState($$3)));
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult result) {
    }

    protected void onHitBlock(BlockHitResult result) {
        //lockState $$1 = this.level().getBlockState(result.getBlockPos());
        //$$1.onProjectileHit(this.level(), $$1, result, this);
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            double $$3 = Math.sqrt(x * x + z * z);
            this.setXRot((float)(Mth.atan2(y, $$3) * 57.2957763671875));
            this.setYRot((float)(Mth.atan2(x, z) * 57.2957763671875));
            this.xRotO = this.getXRot();
            this.yRotO = this.getYRot();
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        }
    }

    protected boolean canHitEntity(Entity target) {
        if (!target.canBeHitByProjectile()) {
            return false;
        }
        Entity $$1 = this.getOwner();
        return $$1 == null || this.leftOwner || !$$1.isPassengerOfSameVehicle(target);
    }

    protected void updateRotation() {
        Vec3 $$0 = this.getDeltaMovement();
        double $$1 = $$0.horizontalDistance();
        this.setXRot(ProjectileBase.lerpRotation(this.xRotO, (float)(Mth.atan2($$0.y, $$1) * 57.2957763671875)));
        this.setYRot(ProjectileBase.lerpRotation(this.yRotO, (float)(Mth.atan2($$0.x, $$0.z) * 57.2957763671875)));
    }

    protected static float lerpRotation(float currentRotation, float targetRotation) {
        while (targetRotation - currentRotation < -180.0f) {
            currentRotation -= 360.0f;
        }
        while (targetRotation - currentRotation >= 180.0f) {
            currentRotation += 360.0f;
        }
        return Mth.lerp(0.2f, currentRotation, targetRotation);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity $$0 = this.getOwner();
        return new ClientboundAddEntityPacket(this, $$0 == null ? 0 : $$0.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Entity $$1 = this.level().getEntity(packet.getData());
        if ($$1 != null) {
            this.setOwner($$1);
        }
    }

    @Override
    public boolean mayInteract(Level level, BlockPos pos) {
        Entity $$2 = this.getOwner();
        if ($$2 instanceof Player) {
            return $$2.mayInteract(level, pos);
        }
        return $$2 == null || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }
}