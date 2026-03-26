package wmlib.common.living;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;  
//import wmlib.common.event.WMSoundEvent;
import net.minecraft.world.phys.Vec3; 
import safx.SagerFX;

public class AI_TankSet {
	public static void set(EntityWMVehicleBase entity, SoundEvent sound, float soundSpeed, float f1, float sp, float brake, boolean isFloat){
		/*if(entity.turretYaw > 360F || entity.turretYaw < -360F){
			entity.turretYaw = 0;
		}
		if(entity.rotation_turret > 360F || entity.rotation_turret < -360F){
			entity.rotation_turret = 0;
		}*/
		if (entity.getTargetType() != 1 && entity.getHealth() > 0.0F)
		{
			if(entity.tracktick % (30*soundSpeed) == 0 && (entity.getX() != entity.xo || entity.getZ() != entity.zo)){
				if(sound != null){
					//entity.playSound(sound, 4.0F,1F);
					if(!entity.level().isClientSide)entity.level().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.WEATHER, 3.0F, 1.0F);
				}else{
					//if(!entity.level().isClientSide)entity.level().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), WMSoundEvent.sound_tank, SoundSource.WEATHER, 4.0F, 1.0F);
				}
			}
		}
		double x = 0;
		double y = -0.05D*entity.getBbWidth()*entity.getBbHeight();
		if(isFloat)y=entity.getDeltaMovement().y;
		double z = 0;
		if((entity.throttle > 0.1F || entity.throttle < -0.1F)){
			if (entity.horizontalCollision) {
				x -= Mth.sin(f1) * sp * 1.8F * entity.throttle * 2;
				z += Mth.cos(f1) * sp * 1.8F * entity.throttle * 2;
			} else {
				x -= Mth.sin(f1) * sp * 1.8F * entity.throttle * 1;
				z += Mth.cos(f1) * sp * 1.8F * entity.throttle * 1;
			}
		}
		//setentityrote(entity);//
		float enc_sp = 1+entity.enc_power*0.1F;
		entity.setDeltaMovement(x*enc_sp, y*enc_sp, z*enc_sp);
		//entity.motion = new Vector3d(x, y, z);
		float speed = Mth.sqrt((float)(x * x + z * z));
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
					//entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2, entity.getY() + 2D, entity.getZ()+2, 0.0D, 0.0D, 0.0D);
					//entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()+2, entity.getY() + 2D, entity.getZ()-1, 0.0D, 0.0D, 0.0D);
					int rx = entity.level().random.nextInt(5);
					int rz = entity.level().random.nextInt(5);
					//entity.level().addParticle(ParticleTypes.FLAME, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0.0D, 0.0D, 0.0D);
					//entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0.0D, 0.0D, 0.0D);
					if(entity.level().random.nextInt(5)==0)SagerFX.proxy.createFX("BlackSmokeUp", null, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0,0,0,0.3F+entity.getBbWidth()*0.1F);
				}else{
					//entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2, entity.getY() + 2D, entity.getZ()+2, 0.0D, 0.0D, 0.0D);
					//entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()+2, entity.getY() + 2D, entity.getZ()-1, 0.0D, 0.0D, 0.0D);
					int rx = entity.level().random.nextInt(5);
					int rz = entity.level().random.nextInt(5);
					//entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0.0D, 0.0D, 0.0D);
					if(entity.level().random.nextInt(7)==0)SagerFX.proxy.createFX("BlackSmokeUp", null, entity.getX()-2+rx, entity.getY() + 2D, entity.getZ()-2+rz, 0,0,0,0.3F+entity.getBbWidth()*0.1F);
				}
			}
		}
	}
}
