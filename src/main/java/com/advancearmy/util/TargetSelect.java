package advancearmy.util;
import java.util.List;
import advancearmy.AdvanceArmy;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.scores.Team;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.function.Predicate;
import advancearmy.entity.EntitySA_Seat;
import wmlib.api.ITool;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.MobSearchTargetGoalSA;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import wmlib.api.IEnemy;
import wmlib.api.ITool;
import wmlib.api.IPara;
import wmlib.api.IHealthBar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import advancearmy.AAConfig;
import net.minecraftforge.fml.ModList;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import wmlib.util.TurretAngle;
import net.minecraft.world.entity.MobCategory;
public class TargetSelect{
    public static boolean mobSoldierCanAttack(PathfinderMob attacker, Entity target, float range_min, float height_min, float height_max){
		double disY = target.getY() - attacker.getY();
		if(attacker.distanceTo(target)>range_min && disY >height_min && disY <height_max){
			if(mobCanAttack(attacker,target,attacker.getTarget())){
				/*if(attacker.getVehicle()!=null && attacker.getVehicle() instanceof EntityWMSeat){
					EntityWMSeat seat = (EntityWMSeat)attacker.getVehicle();
					return TurretAngle.targetRange(attacker, target, seat.attack_height_max, seat.attack_height_min, seat.getYRot(), seat.minyaw, seat.maxyaw, seat.attack_range_min);
				}else*/{
					return true;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
    public static boolean soldierCanAttack(PathfinderMob attacker, Entity target, float range_min, float height_min, float height_max){
		double disY = target.getY() - attacker.getY();
		if(attacker.distanceTo(target)>range_min && disY >height_min && disY <height_max){
			if(friendCanAttack(attacker,target,attacker.getTarget())){
				/*if(attacker.getVehicle()!=null && attacker.getVehicle() instanceof EntityWMSeat){
					EntityWMSeat seat = (EntityWMSeat)attacker.getVehicle();
					return TurretAngle.targetRange(attacker, target, seat.attack_height_max, seat.attack_height_min, seat.getYRot(), seat.minyaw, seat.maxyaw, seat.attack_range_min);
				}else*/{
					return true;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
	
    public static boolean mobCanAttack(Entity attacker, Entity target, Entity forceTarget){
		Team team = attacker.getTeam();
		Team team1 = target.getTeam();
		if(team != null && team1 != team && team1 != null)return true;
		if(team != null && team1 == team && team1 != null)return false;
		
		if (targetList(AAConfig.mobsNotTargeted,target)) {
            return false;
        }
		return !(target instanceof Enemy||target instanceof ITool||target instanceof WaterAnimal)||target==forceTarget
		||targetList(AAConfig.mobsTargeted,target);
    }
	public static boolean friendCanAttack(Entity attacker, Entity target, Entity forceTarget){
		Team team = attacker.getTeam();
		Team team1 = target.getTeam();
		if(team != null && team1 != team && team1 != null)return true;
		if(team != null && team1 == team && team1 != null)return false;
		
		if (targetList(AAConfig.friendNotTargeted,target)) {
            return false;
        }
		return target instanceof Enemy||target==forceTarget||target.getType().getCategory() == MobCategory.MONSTER
		||targetList(AAConfig.friendTargeted,target);
    }
	
    public static boolean landVehicleCanAttack(Mob attacker, Entity target, int targetType, float range_min, float range_max, 
	float height_min, float height_max, boolean is_aa){
		if(targetType!=1){
			boolean can = false;
			if(is_aa){
				double ddy = Math.abs(target.getY()-attacker.getY());
				if(ddy>15){
					can = true;
				}else{
					if(attacker.distanceTo(target)<=range_max){
						can = true;
					}
				}
			}else{
				can = true;
			}
			if(can){
				if(targetType==2){
					return mobCanAttack(attacker,target,attacker.getTarget());
				}else{
					return friendCanAttack(attacker,target,attacker.getTarget());
				}
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }
	
    public static boolean seatCanAttack(EntityWMSeat seat, Entity target, int targetType, float range_max, boolean is_aa){
		if(targetType!=0){
			boolean can = false;
			if(is_aa){
				double ddy = Math.abs(target.getY()-seat.getY());
				if(ddy>15){
					can = true;
				}else{
					if(seat.distanceTo(target)<=range_max){
						can = true;
					}
				}
			}else{
				can = true;
			}
			if(can){
				if(TurretAngle.targetRange(seat, target, seat.attack_height_max, seat.attack_height_min, seat.getYRot(), seat.minyaw, seat.maxyaw, seat.attack_range_min, seat.turretPitchMax, seat.turretPitchMin)){
					if(targetType==2){
						return mobCanAttack(seat,target,seat.getTarget());
					}else{
						return friendCanAttack(seat,target,seat.getTarget());
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }
	
	public static boolean canDriveEntity(Entity target, boolean isEnemy){
		boolean can = false;
		if(target instanceof EntityWMSeat){
			EntityWMSeat seat = (EntityWMSeat)target;
			if(seat.getAnyPassenger()==null){
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof WeaponVehicleBase ve){
					if(isEnemy){
						return (ve.getHealth()>1 && ve.ridcool <= 0 && ve.getOwner()==null && (ve.getTargetType()==1||ve.getTargetType()==2));
					}else{
						return (ve.getHealth()>1 && ve.ridcool <= 0 && ve.getTargetType()!=2);
					}
				}
			}
		}
		/*if(ModList.get().isLoaded("superbwarfare")){
			if(target instanceof VehicleEntity){
				VehicleEntity seat = (VehicleEntity)target;
				if (seat.getPassengers().size() < seat.getMaxPassengers()){
					return seat.getHealth()>0;
				}
			}
		}*/
		return can;
	}
	
    /**
     * 判断目标实体是否在配置的黑名单中。
     * 黑名单支持：
     *   - 精确实体ID，如 "minecraft:zombie"
     *   - 模组前缀（以冒号结尾），如 "creeperoverhaul:"，匹配该模组的所有实体
     */
    private static boolean targetList(List<String> list, Entity target) {
        // 获取目标的注册表名，例如 "minecraft:zombie"
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        if (key == null) return false; // 理论上不会发生
        String targetId = key.toString();
        for (String blacklisted : list) {
            if (blacklisted.endsWith(":")) {
                // 前缀匹配（模组前缀）
                if (targetId.startsWith(blacklisted)) {
                    return true;
                }
            } else {
                // 精确匹配实体ID
                if (targetId.equals(blacklisted)) {
                    return true;
                }
            }
        }
        return false;
    }
}