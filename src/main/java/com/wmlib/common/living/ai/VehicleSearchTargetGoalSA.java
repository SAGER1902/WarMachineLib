package wmlib.common.living.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.ai.goal.Goal;
import wmlib.common.living.EntityWMVehicleBase;
public class VehicleSearchTargetGoalSA<T extends LivingEntity> extends VehicleTargetGoalSA {
	protected final Class<T> targetType;
	protected final int randomInterval;
	protected LivingEntity target;
	protected TargetingConditions targetConditions;
	public VehicleSearchTargetGoalSA(EntityWMVehicleBase soldier, Class<T> classt, int count, boolean istrue, @Nullable Predicate<LivingEntity> predicate) {
		this(soldier,classt, count, istrue,predicate,0,0);
	}
	public VehicleSearchTargetGoalSA(EntityWMVehicleBase soldier, Class<T> classt, int count, boolean istrue, @Nullable Predicate<LivingEntity> predicate, float maxrange, float minrange) {
	  super(soldier, istrue);
	  this.targetType = classt;
	  this.randomInterval = count;
	  this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	  this.targetConditions = TargetingConditions.forCombat()
        .range(this.getFollowDistance()) // 设置搜索范围
        //.ignoreLineOfSight() // 可选：忽略视线检查
        //.ignoreInvisibilityTesting() // 可选：忽略隐身效果
        .selector(predicate);
	}

	public boolean canUse() {
		if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0 && this.mob.getTarget()!=null||this.mob.getTargetType()<2) {
			//this.mob.setAttacking(true);
			return false;
		} else {
			this.mob.setAttacking(false);
			this.findTarget();
			return this.target != null;
		}
	}

	public AABB getTargetSearchArea(double rand, double height) {
		return this.mob.getBoundingBox().inflate(rand, height, rand);//
	}

	public void findTarget() {
		if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
			this.target = this.mob.level().getNearestEntity(
			this.targetType, this.targetConditions, 
			this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 
			this.getTargetSearchArea(this.getFollowDistance(), this.getAADistance()));
		} else {
			this.target = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
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