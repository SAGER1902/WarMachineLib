package wmlib.common.living;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.potion.Effects;
import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;

import wmlib.common.world.WMExplosionBase;
import wmlib.WarMachineLib;
import net.minecraftforge.fml.ModList;
//import wmlib.common.event.WMSoundEvent;
//import wmlib.common.event.ShotManager;
import safx.SagerFX;
public abstract class AI_EntityWeapon_SA{
	public static void Attacktask(LivingEntity living, LivingEntity shooter, Entity target, int id,
		String model, String tex, String fx1, String fx2, SoundEvent sound, 
		float side, double fireOffsetX, double fireOffsetY, double fireOffsetZ, double baseOffsetX, double baseOffsetZ, double posX, double posY, double posZ, float roteY, float roteX,
		int damage, float speed, float recoil, float exlevel, boolean destroy, int count,  float gra, int maxtime, int dameid){
    	int ra = living.level.random.nextInt(10) + 1;
    	float val = ra * 0.02F;
    	if(sound != null)living.playSound(sound, 4.0F+exlevel, 0.9F + val);
		if(id < 10){
			double xx11 = 0;
			double zz11 = 0;
			xx11 -= MathHelper.sin(roteY * 0.01745329252F) * fireOffsetZ;
			zz11 += MathHelper.cos(roteY * 0.01745329252F) * fireOffsetZ;
			xx11 -= MathHelper.sin(roteY * 0.01745329252F + side) * fireOffsetX;
			zz11 += MathHelper.cos(roteY * 0.01745329252F + side) * fireOffsetX;
			double base = 0;
			base = MathHelper.sqrt((fireOffsetZ - baseOffsetZ)* (fireOffsetZ - baseOffsetZ) + (fireOffsetX - baseOffsetX)*(fireOffsetX - baseOffsetX)) * MathHelper.sin(-roteX  * (1 * (float) Math.PI / 180));
			if(fx1!=null && ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fx1, null, posX + xx11, posY + fireOffsetY + base, posZ + zz11, 0, 0, 0, 1F+exlevel*0.1F);
			EntityBulletBase bullet;
			for (int i = 0; i < count; ++i) {
				if (id == 1) {
					bullet = new EntityBullet(living.level, shooter);
				}else if (id == 2) {
					bullet = new EntityShell(living.level, shooter);
				}else if (id == 3) {
					bullet = new EntityShell(living.level, shooter);
				}else if (id == 4) {
					bullet = new EntityMissile(living.level, shooter, target, shooter);
				}else{
					bullet = new EntityBullet(living.level, shooter);
				}
				bullet.power = damage;
				bullet.destroy = destroy;
				bullet.setExLevel(exlevel);
				bullet.timemax = maxtime;
				bullet.setGravity(gra);
				if(fx2!=null)bullet.setFX(fx2);
				bullet.setModel(model);
				bullet.setTex(tex);
				bullet.setBulletType(dameid);
				if(dameid == 5)bullet.flame = true;
				bullet.moveTo(posX + xx11, posY + fireOffsetY + base, posZ + zz11, roteY, roteX);
				if (shooter.hasEffect(Effects.BLINDNESS))
				{
					recoil = recoil * 10F;
				}
				bullet.shootFromRotation(living, roteX, roteY, 0.0F, speed, recoil);
				if (!living.level.isClientSide) {
					living.level.addFreshEntity(bullet);
				}
			}
		}
    }
}