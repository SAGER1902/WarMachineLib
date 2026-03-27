package wmlib.common.living;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.MoverType;
public class PL_LandMove {
	public static void rotecrawler(EntityWMVehicleBase entity, int id, boolean on, float r) {
		if (id == 0) {
			if (!on) {
				if (entity.throttleRight <= 1) {
					entity.throttleRight = entity.throttleRight + 0.01F;
				} else {
					entity.throttleRight = 0;
				}
			} else {
				if (entity.throttleRight > 0) {
					entity.throttleRight = entity.throttleRight - 0.01F;
				} else {
					entity.throttleRight = 1;
				}
			}
		}
		if (id == 1) {
			if (!on) {
				if (entity.throttleLeft <= 1) {
					entity.throttleLeft = entity.throttleLeft + 0.01F;
				} else {
					entity.throttleLeft = 0;
				}
			} else {
				if (entity.throttleLeft > 0) {
					entity.throttleLeft = entity.throttleLeft - 0.01F;
				} else {
					entity.throttleLeft = 1;
				}
			}
		}
	}
	
	public static void moveCarMode(PlayerEntity player, EntityWMVehicleBase entity, float sp, float turnSpeed){
		if(player.yHeadRot > 360F || player.yHeadRot < -360F){
			player.yHeadRot = 0;
			player.yRot = 0;
			player.yRotO = 0;
			player.yHeadRotO = 0;
			////player.renderYawOffset = 0;
		}
		if(player.yHeadRot > 180F){
			player.yHeadRot = -179F;
			player.yRot = -179F;
			player.yRotO = -179F;
			player.yHeadRotO = -179F;
			////player.renderYawOffset = -179F;
		}
		if(player.yHeadRot < -180F){
			player.yHeadRot = 179F;
			player.yRot = 179F;
			player.yRotO = 179F;
			player.yHeadRotO = 179F;
			////player.renderYawOffset = 179F;
		}
		boolean handle = false;
		boolean handle_front = true;
		if (entity.getForwardMove() > 0.1F) {
			if(entity.throttle < entity.throttleMax){
				entity.throttle = entity.throttle + entity.thFrontSpeed;
			}
			rotecrawler(entity, 0, true,0.01F);
			rotecrawler(entity, 1, true,0.01F);
			handle = true;
			handle_front = true;
		}
		if (entity.getForwardMove() < -0.1F) {
			if(entity.throttle > entity.throttleMin){
				entity.throttle = entity.throttle + entity.thBackSpeed;
			}
			rotecrawler(entity, 0, false,0.01F);
			rotecrawler(entity, 1, false,0.01F);
			handle = true;
			handle_front = false;
		}
		if(entity.throttle > 0) {
			handle = true;
			handle_front = true;
		}else if(entity.throttle < 0) {
			handle = true;
			handle_front = false;
		}else {
			handle = false;
		}
		if(handle) {
			if(handle_front) {
				if (entity.getStrafingMove() < 0.0F) {
					entity.yHeadRot = entity.yHeadRot + turnSpeed;
					entity.yRot = entity.yRot + turnSpeed;
					player.yHeadRot += turnSpeed;
					player.yRot += turnSpeed;
				}
				if (entity.getStrafingMove() > 0.0F) {
					entity.yHeadRot = entity.yHeadRot - turnSpeed;
					entity.yRot = entity.yRot - turnSpeed;
					player.yHeadRot -= turnSpeed;
					player.yRot -= turnSpeed;
				}
			}else {
				if (entity.getStrafingMove() < 0.0F) {
					entity.yHeadRot = entity.yHeadRot - turnSpeed;
					entity.yRot = entity.yRot - turnSpeed;
					player.yHeadRot -= turnSpeed;
					player.yRot -= turnSpeed;
				}
				if (entity.getStrafingMove() > 0.0F) {
					entity.yHeadRot = entity.yHeadRot + turnSpeed;
					entity.yRot = entity.yRot + turnSpeed;
					player.yHeadRot += turnSpeed;
					player.yRot += turnSpeed;
				}
			}
		}
	}
}
