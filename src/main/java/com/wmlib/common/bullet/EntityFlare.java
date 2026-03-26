package wmlib.common.bullet;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import wmlib.WarMachineLib;
import safx.SagerFX;
import net.minecraft.core.particles.DustParticleOptions;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import wmlib.api.ITool;
import wmlib.init.WMModEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
public class EntityFlare extends Mob implements ITool{
	private int fuse = 80;
	public EntityFlare(EntityType<? extends EntityFlare> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	}
	public EntityFlare(PlayMessages.SpawnEntity packet, Level worldIn) { 
		super(WMModEntities.ENTITY_FLARE.get(), worldIn); 
	}
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1);
    }
	public void shootFromRotation(Entity entity, float pitch, float yaw, float count, float speed, float bure) {
		float f = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
		float f1 = -Mth.sin((pitch + count) * ((float)Math.PI / 180F));
		float f2 = Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
		this.shoot((double)f, (double)f1, (double)f2, speed, bure);
	}
	public void shoot(double movex, double movey, double movez, float speed, float spread) {
		Vec3 direction = new Vec3(movex, movey, movez).normalize();
		direction = direction.add(
			this.random.nextGaussian() * 0.0075 * spread,
			this.random.nextGaussian() * 0.0075 * spread,
			this.random.nextGaussian() * 0.0075 * spread
		).scale(speed);
		this.setDeltaMovement(direction);
		double horizontalDistance = direction.horizontalDistance();
		float pitch = (float) Math.toDegrees(Mth.atan2(direction.y, horizontalDistance));
		float yaw = (float) Math.toDegrees(Mth.atan2(direction.x, direction.z));
		this.setYRot(yaw);
		this.setXRot(pitch);
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}
   /**
    * Gets the amount of gravity to apply to the thrown entity with each tick.
    */
   /*protected float getGravityVelocity() {
      return 0.03F;
   }*/
	/*public static AttributeModifierMap.MutableAttribute createAttributes() {
		return EntitySA_SoldierBase.createMonsterAttributes();
	}*/
   /**
    * Called to update the entity's position/logic.
    */
	public int time = 100;
	public void tick() {
		if(time>0)--time;
		if(time<=0)this.discard();
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.02D, 0.0D));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}
		if(ModList.get().isLoaded("safx")){
			if(time==99)SagerFX.proxy.createFXOnEntity("FlamethrowerTrail", this, 1);
		}else{
			if (this.level().isClientSide) {
				this.level().addParticle(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
   
	/*@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}*/
}
