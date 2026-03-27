package wmlib.common.living.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.ai.goal.Goal;
import wmlib.common.living.EntityWMVehicleBase;
public class VehicleSearchTargetGoalSA<T extends LivingEntity> extends VehicleTargetGoalSA {
	protected final Class<T> targetType;
	protected final int randomInterval;
	protected LivingEntity target;
	protected EntityPredicate targetConditions;
	public VehicleSearchTargetGoalSA(EntityWMVehicleBase soldier, Class<T> classt, int count, float height, boolean mustSee, @Nullable Predicate<LivingEntity> predicate) {
		this(soldier,classt,count,height, mustSee, predicate,0,0);
	}
	public VehicleSearchTargetGoalSA(EntityWMVehicleBase soldier, Class<T> classt, int count, float height, boolean mustSee, @Nullable Predicate<LivingEntity> predicate, float maxrange, float minrange) {
	  super(soldier, mustSee);
	  this.targetType = classt;
	  this.randomInterval = count;
	  this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	  this.targetConditions = (new EntityPredicate()).range(this.getFollowDistance()).selector(predicate);
	}

	public boolean canUse() {
		if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0 && this.mob.getTarget()!=null||this.mob.getTargetType()<2) {
			return false;
		} else {
			this.mob.setAttacking(false);
			this.findTarget();
			return this.target != null;
		}
	}

	public AxisAlignedBB getTargetSearchArea(double rand, double height) {
		return this.mob.getBoundingBox().inflate(rand, height, rand);//
	}

	public void findTarget() {
		if (this.targetType != PlayerEntity.class && this.targetType != ServerPlayerEntity.class) {
			this.target = this.mob.level.getNearestLoadedEntity(
			this.targetType, this.targetConditions, 
			this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 
			this.getTargetSearchArea(this.getFollowDistance(), this.getAADistance()));
		} else {
			this.target = this.mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
		}
	}

	public void start() {
		if(this.mob.getTarget()==null && this.target!=null){
			this.mob.setTarget(this.target);
		}
		super.start();
	}

	public void setTarget(@Nullable LivingEntity attacktarget) {
	  this.target = attacktarget;
	}
}