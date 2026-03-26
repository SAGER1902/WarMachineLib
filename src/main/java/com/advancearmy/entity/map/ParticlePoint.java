package advancearmy.entity.map;
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

import safx.SagerFX;
import net.minecraft.core.particles.DustParticleOptions;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import wmlib.api.ITool;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
public class ParticlePoint extends Entity implements ITool{
	public ParticlePoint(EntityType<? extends ParticlePoint> type, Level worldIn) {
	  super(type, worldIn);
	}
	public ParticlePoint(PlayMessages.SpawnEntity packet, Level worldIn) { 
		super(ModEntities.ENTITY_P.get(), worldIn); 
	}
    /*public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1);
    }*/
	
	private static final EntityDataAccessor<Integer> particleType = SynchedEntityData.<Integer>defineId(ParticlePoint.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> stayTime = SynchedEntityData.<Integer>defineId(ParticlePoint.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		//super.addAdditionalSaveData(compound);
		compound.putInt("particleType", this.getPType());
		compound.putInt("stayTime", this.getSTime());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
		//super.readAdditionalSaveData(compound);
		this.setPType(compound.getInt("particleType"));
		this.setSTime(compound.getInt("stayTime"));
	}
	protected void defineSynchedData()
	{
		//super.defineSynchedData();
		this.entityData.define(particleType, Integer.valueOf(0));
		this.entityData.define(stayTime, Integer.valueOf(0));
	}
	public int getPType() {
		return ((this.entityData.get(particleType)).intValue());
	}
	public void setPType(int stack) {
		this.entityData.set(particleType, Integer.valueOf(stack));
	}
	public int getSTime() {
		return ((this.entityData.get(stayTime)).intValue());
	}
	public void setSTime(int stack) {
		this.entityData.set(stayTime, Integer.valueOf(stack));
	}
	public int time = 0;
	public float particleSize = 0;
	public float particleAlpha = 0;
	float size;
	public void tick() {
		int maxLife = getSTime();
		if (time < maxLife) {
			float progress = (float) time / maxLife;

			// 原有的透明度逻辑（前40%增加，后60%减少）
			if (progress < 0.4f) {
				particleAlpha = progress / 0.4f;
			} else {
				particleAlpha = 1.0f - (progress - 0.4f) / 0.6f;
			}

			// 修正后的粒子大小逻辑
			if (progress < 0.25f) {
				// 前25%：0 -> 1
				size = progress / 0.25f;
			} else if (progress < 0.7f) {
				//particleSize = 1.0f;
			} else {
				// 70% ~ 100%：1 -> 0
				size = 1.0f - (progress - 0.7f) / 0.3f;
			}
			if(getPType()==1)particleSize = size*0.5F;
			if(getPType()==2)particleSize = size*0.2F;
			time++; // 增加已存活时间
		} else {
			// 达到寿命上限，销毁粒子
			this.discard();
		}
		
		if(getPType()==0){
			if (this.level().isClientSide) {
				for (int $$0 = 0; $$0 < 2; ++$$0) {
					this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
				}
			}
		}		
		/*if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.02D, 0.0D));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}*/
	}
}
