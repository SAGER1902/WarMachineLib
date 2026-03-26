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
import advancearmy.entity.EntitySA_SoldierBase;
import wmlib.common.living.EntityWMSeat;
public class SoldierSearchTargetGoalSA<T extends LivingEntity> extends SoldierTargetGoalSA {
	protected final Class<T> targetType;
	protected final int randomInterval;
	protected LivingEntity target;
	protected TargetingConditions targetConditions;

	public SoldierSearchTargetGoalSA(EntitySA_SoldierBase soldier, Class<T> classt, int useslow, boolean mustsee, @Nullable Predicate<LivingEntity> predicate) {
		super(soldier, mustsee);
		this.targetType = classt;
		this.randomInterval = useslow;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		this.targetConditions = TargetingConditions.forCombat().selector(predicate);
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
	
	public AABB getTargetSearchArea() {
		float range = this.mob.attack_range_max;
		if(this.mob.is_aa)range = this.mob.attack_height_max;
		return this.mob.getBoundingBox().inflate(range, this.mob.attack_height_max, range);//
	}

	public void findTarget() {
		if(this.mob.isAttacking())this.mob.setAttacking(false);
		if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
			this.target = this.mob.level().getNearestEntity(
			this.targetType, this.targetConditions, 
			this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 
			this.getTargetSearchArea());
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