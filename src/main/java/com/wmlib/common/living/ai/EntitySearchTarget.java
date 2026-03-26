package wmlib.common.living.ai;
import java.util.List;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.TamableAnimal;

import wmlib.common.living.EntityWMSeat;
public class EntitySearchTarget{
	public static void getlockTarget(EntityWMSeat seat, double range, double maxy, double miny){
		if (seat.level().isClientSide) return;
		LivingEntity livingentity = seat.lockTarget;
		if (livingentity != null) {
			if (!livingentity.isAlive() || seat.distanceToSqr(livingentity) > range * range 
			||!seat.hasLineOfSightTo(livingentity) || !seat.CanAttack(livingentity)||
			(livingentity instanceof Player && ((Player)livingentity).isCreative()||livingentity.isSpectator())) {
				seat.lockTarget = null;
			}
			return;
		}
		double x = seat.getX();
		double y = seat.getY();
		double z = seat.getZ();
		AABB aabb = new AABB(x - range, y - miny, z - range, x + range, y + maxy, z + range).inflate(1.0);
		if(seat.is_aa)aabb = new AABB(x - maxy*0.75F, y - miny, z - maxy*0.75F, x + maxy*0.75F, y + maxy, z + maxy*0.75F).inflate(1.0);
		List<Entity> entities = seat.level().getEntities(seat, aabb);
		if (entities.isEmpty()) return;
		for (Entity entity : entities) {
			if (!entity.isAlive()) continue;
			if (!seat.CanAttack(entity)) continue;
			if (!seat.hasLineOfSightTo(entity)) continue;
			//if (seat.level().random.nextInt(2)==0) continue;
			if(entity instanceof Player && ((Player)entity).isCreative()||entity.isSpectator()) continue;
			if (entity instanceof LivingEntity) {
				seat.lockTarget = (LivingEntity) entity;
				break;
			}
			return;
		}
	}
}