package advancearmy.entity.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.TamableAnimal;
import advancearmy.entity.mob.EntityMobSoldierBase;
import wmlib.common.living.EntityWMSeat;
public class MobSearchTargetGoalSA<T extends LivingEntity> extends MobTargetGoalSA {
	protected final Class<T> targetType;
	protected final int randomInterval;
	protected LivingEntity target;
	protected TargetingConditions targetConditions;

	public float attack_height = 8F;
	public float attack_min = 0;
	public float attack_range = 0;
	public MobSearchTargetGoalSA(EntityMobSoldierBase soldier, Class<T> classt, int count, float height, boolean istrue, boolean istrue2, @Nullable Predicate<LivingEntity> predicate) {
		this(soldier,classt,count,height, istrue, istrue2,predicate,0,0);
	}
	public MobSearchTargetGoalSA(EntityMobSoldierBase soldier, Class<T> classt, int count, float height, boolean istrue, boolean istrue2, @Nullable Predicate<LivingEntity> predicate, float maxrange, float minrange) {
	  super(soldier, istrue, istrue2);
	  this.targetType = classt;
	  this.randomInterval = count;
	  this.attack_height = height;
	  this.attack_min = minrange;
	  this.attack_range = maxrange;
	  this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	  this.targetConditions = TargetingConditions.forCombat()
        //.range(this.getFollowDistance()) // 设置搜索范围
        //.ignoreLineOfSight() // 可选：忽略视线检查
        //.ignoreInvisibilityTesting() // 可选：忽略隐身效果
        .selector(predicate);
	}

	public boolean canUse() {
		if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0 && this.mob.getTarget()!=null) {
			return false;
		} else {
			if(this.mob.getVehicle()!=null && this.mob.getVehicle() instanceof EntityWMSeat seat){
				if(!seat.seatCanFire)return false;
			}
			this.findTarget();
			return this.target != null;
		}
	}

	public AABB getTargetSearchArea(double rand, double height) {
		if(attack_range==0){
			return this.mob.getBoundingBox().inflate(rand, height, rand);//
		}else{
			return this.mob.getBoundingBox().inflate(attack_range, height, attack_range);//
		}
	}

	public void findTarget() {
		if(this.mob.isAttacking())this.mob.setAttacking(false);
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
			this.mob.setAttacking(true);
		}
		super.start();
	}

	public void setTarget(@Nullable LivingEntity attacktarget) {
	  this.target = attacktarget;
	}
}