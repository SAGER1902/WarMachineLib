package wmlib.common.living;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import wmlib.common.bullet.EntityMissile;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
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
			ix -= Mth.sin(f11) * (vehicle.getBbWidth() / 2);
			iz += Mth.cos(f11) * (vehicle.getBbWidth() / 2);
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
						if(vehicle.level().getRandom().nextInt(3)>1){
							part2 = part2*0.5F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						part2=part2-vehicle.armor_turret_front*0.5F;
					}
				}else {
					if(part2 < vehicle.armor_front) {
						if(vehicle.level().getRandom().nextInt(3)>1){
							part2 = part2*0.5F;
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
						if(vehicle.level().getRandom().nextInt(3)>1){
							part2 = part2*0.5F;
						}else{
							part2 = 0;
						}
					}
				}else {
					if(part2 < vehicle.armor_side) {
						if(vehicle.level().getRandom().nextInt(3)>1){
							part2 = part2*0.5F;
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
						if(vehicle.level().getRandom().nextInt(3)>1){
							part2 = part2*0.5F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						if(noAPBullet(bullet))part2=part2-vehicle.armor_turret_back*0.5F;
					}
				}else {
					if(part2 < vehicle.armor_back) {
						if(vehicle.level().getRandom().nextInt(3)>1){
							part2 = part2*0.5F;
						}else{
							part2 = 0;
						}
					}else{
						break_armor = true;
						if(noAPBullet(bullet))part2=part2-vehicle.armor_back*0.5F;
					}
				}
			}
			if(break_armor && vehicle.getArmorValue()<=20D){
				float b_type = 0;
				float type = 1.2F;
				if(vehicle.VehicleType<3)type = 2F;
				if(bullet instanceof EntityShell){
					b_type=vehicle.level().random.nextFloat()*2;
				}else
				if(bullet instanceof EntityMissile){
					b_type=vehicle.level().random.nextFloat();
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
	public static float antiBullet(EntityWMVehicleBase vehicle, Entity bullet, float part2, DamageSource source, 
	float level, float level1,  float level2, float level3, float level4){
		float ab = 1;
		/*if(antiBulletType(bullet,part2,source)==0)//外部
        {
    		ab = level4;
        }*/
		if(antiBulletType(bullet,part2,source)==1)//枪弹
		{
			ab = level;
        }
    	if(antiBulletType(bullet,part2,source)==2)//机炮弹
        {
    		ab = level1;
        }
    	if(antiBulletType(bullet,part2,source)==3)//穿甲
        {
    		ab = level2;
        }
    	if(antiBulletType(bullet,part2,source)==4)//高爆
        {
    		ab = level3;
        }
    	return ab;
	}
	public static int antiBulletType(Entity bullet,float part2, DamageSource source){
		if(bullet instanceof EntityBullet && part2<20){
			return 1;
		}else if(bullet instanceof EntityBullet && part2>=20||bullet instanceof EntityShell && part2<50){
			return 2;
		}else if(bullet instanceof EntityMissile||bullet instanceof EntityShell){
			return 3;
		}else if(source.is(DamageTypes.EXPLOSION)){
			return 4;
		}else{
			return 0;
		}
	}
}
