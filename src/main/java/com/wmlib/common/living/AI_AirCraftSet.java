package wmlib.common.living;
//import wmlib.common.event.WMSoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3; 

public class AI_AirCraftSet {
	public static void setFighterMode(EntityWMVehicleBase entity, SoundEvent sound, float f1, float MoveSpeed, float brake){
		double dx = Math.abs(entity.getX() - entity.xo);
		double dz = Math.abs(entity.getZ() - entity.zo);
		float dd = (float)Math.sqrt((dx * dx) + (dz * dz)) * 20;
		if((dd > 5||entity.movePower>5) && (entity.horizontalCollision) && entity != null && entity.getTargetType()<2 && entity.enc_armor==0){
			entity.setHealth(0.0F);
			entity.kill();
		}
		if(entity.getTargetType() == 1) 
		{
			if(entity.throttle > 1){
				entity.throttle-=1;
			}
		}
		
		double x = 0;
		double y = 0;
		double z = 0;
		{
			float rote = 1;
			if(entity.getTargetType()<2){
				rote =(180F-Math.abs(entity.flyRoll)*2)/180F;
			}
			x -= Mth.sin(f1) * MoveSpeed * entity.movePower*((90F-Math.abs(entity.flyPitch))/90F);
			z += Mth.cos(f1) * MoveSpeed * entity.movePower*((90F-Math.abs(entity.flyPitch))/90F);
			if(entity.movePower > 1) {
				if(entity.flyPitch<0){
					y += 0.5F * ((90F-entity.flyPitch)/90F)*(entity.movePower/1.2F) * MoveSpeed * rote;
				}else if(entity.flyPitch>0||entity.flyPitch>20) {
					y -= 0.5F * ((90F+entity.flyPitch)/90F)*(entity.movePower/1.5F) * MoveSpeed * rote;
				}else{
					y = 0;
				}
			}
			if(entity.onGround())y=0.8F*y;
			if(entity.getTargetType()==0){
				if(entity.getMovePitch()>0){
					y += 0.5F * -entity.getMovePitch()*0.01F*(entity.movePower/1.5F) * MoveSpeed * rote;
				}else{
					y += 0.5F * -entity.getMovePitch()*0.5F*(entity.movePower/1.5F) * MoveSpeed * rote;
				}
			}else{
				y += 0.5F * 0.5F*(entity.movePower/2) * MoveSpeed * rote;
			}
		}
		
		if(entity.getHealth()==0||entity.getTargetType() == 1&&entity.getMoveMode()!=5 && !entity.onGround()){
			if(entity.flyPitch<90){
				entity.flyPitch+=1F;
				entity.setXRot(entity.flyPitch);
			}
			y=entity.getDeltaMovement().y;
		}
		
		if(entity.movePower > 0){
			float enc_sp = 1+entity.enc_power*0.1F;
			entity.setDeltaMovement(x*enc_sp, y*enc_sp, z*enc_sp);
		}
		
		if(entity.throttle > 1 && sound!=null){
			if(entity.tracktick % 20 == 0)
			{
				//entity.level.playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.WEATHER, 5.0F, 1.0F);
				entity.playSound(sound, 3.0F, 1.0F);
			}
		}
		setentityrote(entity);//
		if(entity.flyRoll > 0)entity.flyRoll =  entity.flyRoll - (float)(entity.turnSpeed * entity.movePower * 0.01F);
		if(entity.flyRoll < 0)entity.flyRoll =  entity.flyRoll + (float)(entity.turnSpeed * entity.movePower * 0.01F);
		if(entity.movePower >= 2){
			if(entity.thpera < 360){
				entity.thpera = entity.thpera + (entity.throttle*2);
			}else{
				entity.thpera = 0;
			}
		}
	}
	
	public static void setHeliCopterMode(EntityWMVehicleBase entity, SoundEvent sound, float f1, float MoveSpeed, float brake){
		double dx = Math.abs(entity.getX() - entity.xo);
		double dz = Math.abs(entity.getZ() - entity.zo);
		float dd = (float)Math.sqrt((dx * dx) + (dz * dz)) * 20;
		if((dd > 5||entity.movePower>5) && (entity.horizontalCollision) && entity != null && entity.getTargetType()<2 && entity.enc_armor==0){
			entity.setHealth(0.0F);
			entity.kill();
		}
		if(entity.getTargetType() == 1) 
		{
			if(entity.movePower > 1){
				entity.movePower = entity.movePower - brake;
			}
			if(entity.throttle > 1){
				--entity.throttle;
			}
		}
		
		double x = 0;
		double y = 0;
		double z = 0;
		{
			float rote =(180F-Math.abs(entity.flyRoll)*2)/180F;
			x -= Mth.sin(f1) * MoveSpeed * entity.movePower* 3 * entity.flyPitch/90F;
			z += Mth.cos(f1) * MoveSpeed * entity.movePower* 3 * entity.flyPitch/90F;
			if(entity.movePower>1){
				double speed_rotez = entity.flyRoll * 0.15 * entity.movePower/5;
				x += Mth.sin(entity.yHeadRot * 0.01745329252F - 1.57F) * MoveSpeed * speed_rotez;
				z -= Mth.cos(entity.yHeadRot * 0.01745329252F - 1.57F) * MoveSpeed * speed_rotez;
			}
			if(entity.onGround()) {
				if(entity.movePower >= entity.throttleMax){
					y += 0.1F * (entity.movePower/2) * MoveSpeed;
				}
			}else {
				if(entity.movePower > 1) {
					if(entity.throttle >= entity.throttleMax*0.35F){
						if(entity.flyPitch>20||entity.flyPitch<-25){
							y = -1F*(Math.abs(entity.flyPitch)/90F)*(entity.throttleMax-entity.movePower)/entity.throttleMax;
						}else{
							if((entity.flyPitch>5||entity.flyPitch<-10)&&entity.throttle < entity.throttleMax*0.75F){
								y = 0;
							}else{
								y += (entity.movePower/2) * MoveSpeed * rote;
							}
						}
					}else{
						if(entity.flyPitch>5||entity.flyPitch<-10||entity.throttle < entity.throttleMax*0.3F){
							y = -0.1F;
						}else{
							y = 0;
						}
					}
				}
			}
		}
		if(entity.getHealth()==0){
			entity.setYRot(entity.getYRot()+1);
			++entity.yHeadRot;
			y=entity.getDeltaMovement().y;
		}
		if(entity.movePower>2){
			float enc_sp = 1+entity.enc_power*0.1F;
			entity.setDeltaMovement(x*enc_sp, y*enc_sp, z*enc_sp);
		}

		if(entity.throttle > 1 && sound!=null){
			if(entity.tracktick % 25 == 0){
				entity.playSound(sound, 3.0F, 1.0F);
			}
		}
		setentityrote(entity);//
		if(entity.flyRoll > 0)entity.flyRoll =  entity.flyRoll - (float)(entity.turnSpeed * entity.movePower * 0.01F);
		if(entity.flyRoll < 0)entity.flyRoll =  entity.flyRoll + (float)(entity.turnSpeed * entity.movePower * 0.01F);
		if(entity.movePower > 1){
			if(entity.thpera < 360){
				entity.thpera = entity.thpera + (entity.throttle*2);
			}else{
				entity.thpera = 0;
			}
		}
	}
	
	public static void setentityrote(EntityWMVehicleBase entity) {
		if(entity.yHeadRot > 360F || entity.yHeadRot < -360F){
        	entity.turretYaw = 0;
			entity.yHeadRot = 0;
			entity.setYRot(0);
			entity.yRotO = 0;
			entity.yHeadRotO = 0;
			//entity.yBodyRot = 0;
		}
		if(entity.yHeadRot > 180F){
			entity.turretYaw = -179.9F;
			entity.yHeadRot = -179.9F;
			entity.setYRot(-179.9F);
			entity.yRotO = -179.9F;
			entity.yHeadRotO = -179.9F;
			//entity.yBodyRot = -179.9F;
		}
		if(entity.yHeadRot < -180F){
			entity.turretYaw = 179.9F;
			entity.yHeadRot = 179.9F;
			entity.setYRot(179.9F);
			entity.yRotO = 179.9F;
			entity.yHeadRotO = 179.9F;
			//entity.yBodyRot = 179.9F;
		}
	}
}
