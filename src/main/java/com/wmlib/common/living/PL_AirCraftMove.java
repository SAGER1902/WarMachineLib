package wmlib.common.living;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
public class PL_AirCraftMove {
	public static void moveHeliCopterMode(PlayerEntity player, EntityWMVehicleBase entity,float sp, float turnSpeed){
		/*if(player!=null){
			if(player.yHeadRot > 360F || player.yHeadRot < -360F){
				player.yHeadRot = 0;
				player.yRot = 0;
				player.yRotO = 0;
				player.yHeadRotO = 0;
				player.yBodyRot = 0;
			}
			if(player.yHeadRot > 180F){
				player.yHeadRot = -179F;
				player.yRot = -179F;
				player.yRotO = -179F;
				player.yHeadRotO = -179F;
				player.yBodyRot = -179F;
			}
			if(player.yHeadRot < -180F){
				player.yHeadRot = 179F;
				player.yRot = 179F;
				player.yRotO = 179F;
				player.yHeadRotO = 179F;
				player.yBodyRot = 179F;
			}
		}*/

		if( entity.throttle >= 0){
			if(entity.movePower < entity.throttle){
				if(entity.flyPitch<75&&entity.flyPitch>-75){
					if( entity.throttle > entity.throttleMax-5){
						entity.movePower = entity.movePower + entity.thFrontSpeed;
					}else{
						entity.movePower = entity.movePower + entity.thFrontSpeed*0.8F;
					}
				}else{
					if(entity.movePower > entity.throttleMax*0.5F){
						entity.movePower-=entity.thBackSpeed;
					}
				}
			}else{
				entity.movePower = entity.movePower + entity.thBackSpeed;
			}
			if(entity.throttle <= 0 && entity.movePower > 0){
				entity.movePower = entity.movePower + entity.thBackSpeed * 2;
			}
		}
		if(entity.getMoveMode()==1){
			if(entity.throttle > entity.throttleMax*0.5F){
				entity.throttle-=0.25F;
			}
			if(entity.throttle < entity.throttleMax*0.5F){
				entity.throttle+=0.25F;
			}
		}else{
			if(entity.throttle > entity.throttleMax*0.3F){//down
				entity.throttle-=0.25F;
			}
		}
	}
	
	public static void moveFighterMode(PlayerEntity player, EntityWMVehicleBase entity,float sp, float turnSpeed){
		/*if(player!=null){
			if(player.yHeadRot > 360F || player.yHeadRot < -360F){
				player.yHeadRot = 0;
				player.yRot = 0;
				player.yRotO = 0;
				player.yHeadRotO = 0;
				player.yBodyRot = 0;
			}
			if(player.yHeadRot > 180F){
				player.yHeadRot = -179F;
				player.yRot = -179F;
				player.yRotO = -179F;
				player.yHeadRotO = -179F;
				player.yBodyRot = -179F;
			}
			if(player.yHeadRot < -180F){
				player.yHeadRot = 179F;
				player.yRot = 179F;
				player.yRotO = 179F;
				player.yHeadRotO = 179F;
				player.yBodyRot = 179F;
			}
		}*/
		//entity.xRot = entity.flyPitch;
	}
}
