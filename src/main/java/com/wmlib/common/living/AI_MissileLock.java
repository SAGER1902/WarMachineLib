package wmlib.common.living;
import java.util.List;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.util.math.vector.Vector2f;
import wmlib.common.bullet.EntityFlare;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.item.ItemGun;
public class AI_MissileLock {
    public static boolean CanLock(Entity attacker, Entity entity){
		if(entity instanceof EntityFlare){
			return true;
		}else if(entity instanceof WeaponVehicleBase){
			WeaponVehicleBase ve = (WeaponVehicleBase)entity;
			Team team = attacker.getTeam();
			Team team1 = ve.getTeam();
			if(ve.getTargetType()==2||team != null && team1 != team && team1 != null){
				//ve.playSound(SASoundEvent.laser_lock.get(), 1F, 1.0F);
				ve.playSound(ve.lockAlertSound,1, 1.0F);
				if(ve.flaretime>50)ve.flaretime = 0;
				return true;
			}else{
				return false;
			}
		}else
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && entity.getVehicle()==null){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = attacker.getTeam();
			Team team1 = entity1.getTeam();
			if(team != null && team1 != team && team1 != null){
				return true;
			}else if(entity instanceof IMob && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1)){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }

   public static Vector2f getRoteVector(Entity ent, float fix) {
      return new Vector2f(ent.xRot*fix, ent.yRot);
   }


	public static void SeatLock(EntityWMSeat seat, PlayerEntity player, boolean aam, float fix) {
		Vector3d locken = Vector3d.directionFromRotation(getRoteVector(player, fix));//getLookAngle//player.getRotationVector()
		float d = 120;
		boolean islock = false;
		int block_height = seat.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)seat.getX(), (int)seat.getZ());
		Entity lockTarget = null;
		int range = 2;
		if(seat.lockcool>15){
			for(int xxx = 0; xxx < d; ++xxx) {
				if(xxx>10){
					AxisAlignedBB axisalignedbb = (new AxisAlignedBB(seat.getX() + locken.x * xxx-range, seat.getY() + locken.y * xxx-range, seat.getZ() + locken.z * xxx-range, 
							seat.getX() + locken.x * xxx+range, seat.getY() + locken.y * xxx+range, seat.getZ() + locken.z * xxx+range)).inflate(1D);
					List<Entity> llist = seat.level.getEntities(seat,axisalignedbb);
					if (llist != null) {
						for (int lj = 0; lj < llist.size(); lj++) {
							Entity entity1 = (Entity) llist.get(lj);
							if (entity1 != null /*&& entity1.canBeCollidedWith() && entity1 instanceof LivingEntity*/) {
								boolean flag = seat.getSensing().canSee(entity1);
								if (CanLock(player,entity1) && entity1 != player && entity1 != seat && flag && !(entity1 instanceof EntityWMSeat)) {
									lockTarget = entity1;
									if(aam){
										if(lockTarget.getY() > block_height + 5) {
											seat.mitarget = lockTarget;
											seat.locktime = 50;
										}
									}else{
										if(lockTarget.getY() < block_height + 10){
											seat.mitarget = lockTarget;
											seat.locktime = 20;
										}
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		if(seat.mitarget !=null){
			if(!seat.mitarget.isAlive()||/*||seat.mitarget.getHealth()==0||*/seat.locktime<10) {
				seat.mitarget =null;
			}
		}
		if(seat.mitarget !=null){
			if(seat.mitarget instanceof EntityFlare){
				if(seat.lockcool>16)seat.lockcool = 0;
			}
			if (seat.tracktick % 15 == 0){
				//seat.playSound(SASoundEvent.growler_lock.get(), 0.4F, 1.0F);
				seat.playSound(seat.lockTargetSound,1, 1.0F);
			}
		}
	}


	public static void ItemLock(ItemGun item, PlayerEntity player, boolean aam, float fix) {
		Vector3d locken = Vector3d.directionFromRotation(getRoteVector(player, fix));//getLookAngle//player.getRotationVector()
		float d = 120;
		boolean islock = false;
		int block_height = player.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)player.getX(), (int)player.getZ());
		Entity lockTarget = null;
		int range = 2;
		if(item.lockcool>15){
			for(int xxx = 0; xxx < d; ++xxx) {
				if(xxx>10){
					AxisAlignedBB axisalignedbb = (new AxisAlignedBB(player.getX() + locken.x * xxx-range, player.getY() + locken.y * xxx-range, player.getZ() + locken.z * xxx-range, 
							player.getX() + locken.x * xxx+range, player.getY() + locken.y * xxx+range, player.getZ() + locken.z * xxx+range)).inflate(1D);
					List<Entity> llist = player.level.getEntities(player,axisalignedbb);
					if (llist != null) {
						for (int lj = 0; lj < llist.size(); lj++) {
							Entity entity1 = (Entity) llist.get(lj);
							if (entity1 != null /*&& entity1.canBeCollidedWith() && entity1 instanceof LivingEntity*/) {
								if (CanLock(player,entity1) && entity1 != player && !(entity1 instanceof EntityWMSeat)) {
									lockTarget = entity1;
									if(aam){
										if(lockTarget.getY() > block_height + 5) {
											item.mitarget = lockTarget;
											item.locktime = 50;
										}
									}else{
										if(lockTarget.getY() < block_height + 10){
											item.mitarget = lockTarget;
											item.locktime = 20;
										}
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		if(item.mitarget !=null){
			if(!item.mitarget.isAlive()||/*||item.mitarget.getHealth()==0||*/item.locktime<10) {
				item.mitarget =null;
			}
		}
		if(item.mitarget !=null){
			if(item.mitarget instanceof EntityFlare){
				if(item.lockcool>16)item.lockcool = 0;
			}
			/*if (item.tracktick % 5 == 0){
				player.playSound(SASoundEvent.growler_lock.get(), 0.4F, 1.0F);
			}*/
		}
	}
}
