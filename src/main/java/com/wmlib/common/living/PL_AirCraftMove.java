package wmlib.common.living;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3; 
public class PL_AirCraftMove {
	public static void moveHeliCopterMode(Player player, EntityWMVehicleBase entity,float MoveSpeed, float turnSpeed){
		/*if(player!=null){
			if(player.yHeadRot > 360F || player.yHeadRot < -360F){
				player.yHeadRot = 0;
				player.setYRot(0);
				player.yRotO = 0;
				player.yHeadRotO = 0;
				player.yBodyRot = 0;
			}
			if(player.yHeadRot > 180F){
				player.yHeadRot = -179F;
				player.setYRot(-179F);
				player.yRotO = -179F;
				player.yHeadRotO = -179F;
				player.yBodyRot = -179F;
			}
			if(player.yHeadRot < -180F){
				player.yHeadRot = 179F;
				player.setYRot(179F);
				player.yRotO = 179F;
				player.yHeadRotO = 179F;
				player.yBodyRot = 179F;
			}
		}*/
	}
	
	public static void moveFighterMode(Player player, EntityWMVehicleBase entity,float MoveSpeed, float turnSpeed){
		/*if(player!=null){
			if(player.yHeadRot > 360F || player.yHeadRot < -360F){
				player.yHeadRot = 0;
				player.setYRot(0);
				player.yRotO = 0;
				player.yHeadRotO = 0;
				player.yBodyRot = 0;
			}
			if(player.yHeadRot > 180F){
				player.yHeadRot = -179F;
				player.setYRot(-179F);
				player.yRotO = -179F;
				player.yHeadRotO = -179F;
				player.yBodyRot = -179F;
			}
			if(player.yHeadRot < -180F){
				player.yHeadRot = 179F;
				player.setYRot(179F);
				player.yRotO = 179F;
				player.yHeadRotO = 179F;
				player.yBodyRot = 179F;
			}
		}*/
		//entity.xRot = entity.flyPitch;
		if(entity.getArmyType1()!=0 && entity.getTargetType()==0 && !entity.can_follow && !entity.isSpaceShip){
			float speedy = 1+entity.MoveSpeed*5;
			if (entity.flyPitch > speedy) {
				entity.flyPitch-=speedy;
			} else if (entity.flyPitch < -speedy) {
				entity.flyPitch+=speedy;
			}else{
				entity.flyPitch = 0;
			}
			/*if(entity.can_follow){
				entity.setXRot(entity.flyPitch);
			}else*/{
				entity.setXRot(entity.flyPitch);
				entity.turretPitch = entity.getXRot();
			}
		}
	}
}
