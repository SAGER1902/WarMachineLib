package wmlib.common.living.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;

import wmlib.common.living.EntityWMSeat;
public class LivingSearchTargetGoalSA<T extends LivingEntity> extends LivingTargetGoalSA {
	protected final Class<T> targetType;
	protected final int randomInterval;
	protected LivingEntity target;
	protected EntityPredicate targetConditions;

	public float attack_height = 8F;
	public float attack_min = 0;
	public float attack_range = 0;
	public LivingSearchTargetGoalSA(MobEntity soldier, Class<T> classt, int count, float height, boolean istrue, boolean istrue2, @Nullable Predicate<LivingEntity> predicate) {
		this(soldier,classt,count,height, istrue, istrue2, predicate,0,0);
	}
	public LivingSearchTargetGoalSA(MobEntity soldier, Class<T> classt, int count, float height, boolean istrue, boolean istrue2, @Nullable Predicate<LivingEntity> predicate, float maxrange, float minrange) {
	  super(soldier, istrue, istrue2);
	  this.targetType = classt;
	  this.randomInterval = count;
	  this.attack_height = height;
	  this.attack_min = minrange;
	  this.attack_range = maxrange;
	  this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	  this.targetConditions = (new EntityPredicate()).range(this.getFollowDistance()).selector(predicate);
	}

	public boolean canUse() {
		if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0 && this.mob.getTarget()!=null) {
			return false;
		} else {
			this.findTarget();
			return this.target != null;
		}
	}

	public AxisAlignedBB getTargetSearchArea(double rand, double height) {
		if(attack_range==0){
			return this.mob.getBoundingBox().inflate(rand, height, rand);//
		}else{
			return this.mob.getBoundingBox().inflate(attack_range, height, attack_range);//
		}
	}

	public void findTarget() {
		if(this.mob.isAggressive())this.mob.setAggressive(false);
		if (this.targetType != PlayerEntity.class && this.targetType != ServerPlayerEntity.class) {
			this.target = this.mob.level.getNearestLoadedEntity(
			this.targetType, this.targetConditions, 
			this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 
			this.getTargetSearchArea(this.getFollowDistance(), attack_height));
		} else {
			this.target = this.mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
		}
	}

	public void start() {
		if(this.mob.getTarget()==null && this.target!=null){
			this.mob.setTarget(this.target);
			this.mob.setAggressive(true);
		}
		super.start();
	}

	public void setTarget(@Nullable LivingEntity attacktarget) {
	  this.target = attacktarget;
	}
}