package wmlib.common.living;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.ModList;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
//import wmlib.common.event.WMSoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import safx.SagerFX;

public class AI_TankSet {
	public static void set(EntityWMVehicleBase entity, SoundEvent sound, float sound_speed, float f1, float sp, float brake, boolean canFloat){
		/*if(entity.turretYaw > 360F || entity.turretYaw < -360F){
			entity.turretYaw = 0;
		}
		if(entity.turretYawMove > 360F || entity.turretYawMove < -360F){
			entity.turretYawMove = 0;
		}*/
		if (entity.getTargetType() != 1 && entity.getHealth() > 0.0F)
		{
			if(entity.tracktick % (30*sound_speed) == 0 && (entity.getX() != entity.xo || entity.getZ() != entity.zo)){
				if(sound != null){
					//entity.playSound(sound, 4.0F,1F);
					if(!entity.level.isClientSide)entity.level.playSound((PlayerEntity)null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundCategory.WEATHER, 3.0F, 1.0F);
				}else{
					//if(!entity.level.isClientSide)entity.level.playSound((PlayerEntity)null, entity.getX(), entity.getY(), entity.getZ(), WMSoundEvent.sound_tank, SoundCategory.WEATHER, 4.0F, 1.0F);
				}
			}
		}
		double x = 0;
		double y = -0.05D*entity.getBbWidth()*entity.getBbHeight()-1;
		if(canFloat)y=entity.getDeltaMovement().y;
		double z = 0;
		if((entity.throttle > 0.1F || entity.throttle < -0.1F)){
			if (entity.horizontalCollision) {
				x -= MathHelper.sin(f1) * sp * 1.8F * entity.throttle * 2;
				z += MathHelper.cos(f1) * sp * 1.8F * entity.throttle * 2;
			} else {
				x -= MathHelper.sin(f1) * sp * 1.8F * entity.throttle * 1;
				z += MathHelper.cos(f1) * sp * 1.8F * entity.throttle * 1;
			}
		}
		//setentityrote(entity);//
		float enc_sp = 1+entity.enc_power*0.1F;
		entity.setDeltaMovement(x*enc_sp, y*enc_sp, z*enc_sp);
		//entity.motion = new Vector3d(x, y, z);
		float speed = MathHelper.sqrt(x * x + z * z);
		{
			if(entity.throttle > 0){
				if(entity.thpera < 360){
					entity.thpera = entity.thpera + (speed*40);
				}else{
					entity.thpera = 0;
				}
			}else {
				if(entity.thpera < 0){
					entity.thpera = 360;
				}else{
					entity.thpera = entity.thpera - (speed*40);
				}
			}
		}
		if(ModList.get().isLoaded("safx")){
			if(entity.getHealth() <= entity.getMaxHealth()/2){
				if(entity.getHealth() <= entity.getMaxHealth()/4){
					//entity.level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2, entity.getY() + 2D, entity.getZ()+2, 0.0D, 0.0D, 0.0D);
					//entity.level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()+2, entity.getY() + 2D, entity.getZ()-1, 0.0D, 0.0D, 0.0D);
					int rx = entity.level.random.nextInt(5);
					int rz = entity.level.random.nextInt(5);
					//entity.level.addParticle(ParticleTypes.FLAME, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0.0D, 0.0D, 0.0D);
					//entity.level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0.0D, 0.0D, 0.0D);
					if(entity.level.random.nextInt(5)==0)SagerFX.proxy.createFX("BlackSmokeUp", null, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0,0,0,0.3F+entity.getBbWidth()*0.1F);
				}else{
					//entity.level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2, entity.getY() + 2D, entity.getZ()+2, 0.0D, 0.0D, 0.0D);
					//entity.level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()+2, entity.getY() + 2D, entity.getZ()-1, 0.0D, 0.0D, 0.0D);
					int rx = entity.level.random.nextInt(5);
					int rz = entity.level.random.nextInt(5);
					//entity.level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0.0D, 0.0D, 0.0D);
					if(entity.level.random.nextInt(7)==0)SagerFX.proxy.createFX("BlackSmokeUp", null, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0,0,0,0.3F+entity.getBbWidth()*0.1F);
				}
			}
		}
	}
}
