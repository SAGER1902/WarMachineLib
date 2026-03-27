package wmlib.common.living;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import wmlib.common.bullet.EntityMissile;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
public class AI_Damage {
	public static float damageArmor(WeaponVehicleBase vehicle, Entity bullet, float part2, boolean turret, float rotehead){
		double ix = 0;
		double iy = 0;
		double iz = 0;
		float f11 = rotehead * (2 * (float) Math.PI / 360);
		float rote = rotehead;
		boolean break_armor = false;
		{
			double ex = bullet.getX();
			double ey = bullet.getY();
			double ez = bullet.getZ();
			ix -= MathHelper.sin(f11) * (vehicle.getBbWidth() / 2);
			iz += MathHelper.cos(f11) * (vehicle.getBbWidth() / 2);
			double px = vehicle.getX() + ix;
			double pz = vehicle.getZ() + iz;
			double px1 = vehicle.getX() - ix;
			double pz1 = vehicle.getZ() - iz;
			
			double d5 = bullet.getX() - vehicle.getX();
			double d7 = bullet.getZ() - vehicle.getZ();
			
			float rote_base  = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			
			if(rote_base > rote - 45 && rote_base < rote + 45) {
				if(turret) {
					if(part2 < vehicle.armor_turret_front) {
						if(vehicle.level.random.nextInt(3)>1){
							part2 = part2*0.1F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						part2=part2-vehicle.armor_turret_front*0.5F;
					}
				}else {
					if(part2 < vehicle.armor_front) {
						if(vehicle.level.random.nextInt(3)>1){
							part2 = part2*0.1F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						part2=part2-vehicle.armor_front*0.5F;
					}
				}
			}else if(rote_base > rote - 135 && rote_base < rote + 135) {
				if(turret) {
					if(part2 < vehicle.armor_turret_side) {
						if(vehicle.level.random.nextInt(3)>1){
							part2 = part2*0.1F;
						}else{
							part2 = 0;
						}
					}
				}else {
					if(part2 < vehicle.armor_side) {
						if(vehicle.level.random.nextInt(3)>1){
							part2 = part2*0.1F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						if(noAPBullet(bullet))part2=part2-vehicle.armor_side*0.5F;
					}
				}
			}else {
				if(turret) {
					if(part2 < vehicle.armor_turret_back) {
						if(vehicle.level.random.nextInt(3)>1){
							part2 = part2*0.1F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						if(noAPBullet(bullet))part2=part2-vehicle.armor_turret_back*0.5F;
					}
				}else {
					if(part2 < vehicle.armor_back) {
						if(vehicle.level.random.nextInt(3)>1){
							part2 = part2*0.1F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						if(noAPBullet(bullet))part2=part2-vehicle.armor_back*0.5F;
					}
				}
			}
			if(break_armor){
				float b_type = 0;
				float type = 1.2F;
				if(vehicle.VehicleType<3)type = 2F;
				if(bullet instanceof EntityShell){
					b_type=vehicle.level.random.nextFloat()*2;
				}else
				if(bullet instanceof EntityMissile){
					b_type=vehicle.level.random.nextFloat();
				}
				part2=part2*(b_type+type);
			}
			if(part2<1)part2=0F;
		}
		return part2;
	}
	public static boolean noAPBullet(Entity bullet){
		if(!(bullet instanceof EntityShell) || !(bullet instanceof EntityMissile)){
			return true;
		}else{
			return false;
		}
	}
}
