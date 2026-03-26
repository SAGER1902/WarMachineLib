package advancearmy.entity.ai;

import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import wmlib.WarMachineLib;
import advancearmy.event.SASoundEvent;
import net.minecraftforge.fml.ModList;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import wmlib.common.block.MuzzleFlashBlock;
import wmlib.common.block.BlockRegister;
import net.minecraft.world.level.block.state.BlockState;

import advancearmy.AAConfig;
import safx.SagerFX;
public abstract class AI_EntityWeapon{
	public static void Attacktask(Entity living, LivingEntity shooter, Entity target, int id,
		String model, String tex, String fx1, String fx2, SoundEvent sound, 
		float side, double fireOffsetX, double fireOffsetY, double fireOffsetZ, double baseOffsetX, double baseOffsetZ, double posX, double posY, double posZ, float roteY, float roteX,
		int damage, float speed, float recoil, float exlevel, boolean destroy, int count,  float gra, int maxtime, int dameid){
    	int ra = living.level().getRandom().nextInt(10) + 1;
    	float val = ra * 0.02F;
		float soundSize = 2F;
		if(exlevel>0)soundSize=4F+exlevel;
    	if(sound != null)living.playSound(sound, soundSize, 0.9F + val);
		if(id < 10){
			double xx11 = 0;
			double zz11 = 0;
			xx11 -= Math.sin(roteY * 0.01745329252F) * fireOffsetZ;
			zz11 += Math.cos(roteY * 0.01745329252F) * fireOffsetZ;
			xx11 -= Math.sin(roteY * 0.01745329252F + side) * fireOffsetX;
			zz11 += Math.cos(roteY * 0.01745329252F + side) * fireOffsetX;
			//double base = 0;
			//base = Math.sqrt((float)((fireOffsetZ - baseOffsetZ)* (fireOffsetZ - baseOffsetZ) + (fireOffsetX - baseOffsetX)*(fireOffsetX - baseOffsetX)) * Math.sin(-roteX  * (1 * (float) Math.PI / 180)));//神秘崩溃
			// 计算平方和并确保非负
			double term = (fireOffsetZ - baseOffsetZ) * (fireOffsetZ - baseOffsetZ) + (fireOffsetX - baseOffsetX) * (fireOffsetX - baseOffsetX);
			if (term < 0) {
				term = 0;
			}
			// 规范化角度并计算正弦值
			//roteX = roteX % 360;
			double radians = -roteX * Math.PI / 180;
			double sinVal = Math.sin(radians);
			double base = Math.sqrt(term) * sinVal;
			if(AAConfig.useFireLight){
				BlockPos pos = new BlockPos((int)(posX + xx11), (int)(posY + fireOffsetY + base), (int)(posZ + zz11));
				BlockState current = living.level().getBlockState(pos);
				if (current.isAir()) {
					if(exlevel>1){
						living.level().setBlock(pos, BlockRegister.CANNONB.get().defaultBlockState(),Block.UPDATE_CLIENTS);
					}else{
						living.level().setBlock(pos, BlockRegister.MUZZB.get().defaultBlockState(),Block.UPDATE_CLIENTS);
					}
				}/*else if (current.getBlock() instanceof MuzzleFlashBlock) {
					MuzzleFlashBlock.activateBlock(living.level(), pos);
				}*/
			}
			if(fx1!=null && ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fx1, null, posX + xx11, posY + fireOffsetY + base, posZ + zz11, 0, 0, 0, 1F+exlevel*0.1F);
			EntityBulletBase bullet;
			for (int i = 0; i < count; ++i) {
				if (id == 1) {
					bullet = new EntityBullet(living.level(), shooter);
				}else if (id == 2) {
					bullet = new EntityShell(living.level(), shooter);
				}else if (id == 3) {
					bullet = new EntityShell(living.level(), shooter);
				}else if (id == 4) {
					bullet = new EntityMissile(living.level(), shooter, target, shooter);
				}else{
					bullet = new EntityBullet(living.level(), shooter);
				}
				bullet.power = damage;
				bullet.destroy = destroy;
				bullet.setExLevel(exlevel);
				
				if (exlevel > 5.0F){
					if (exlevel > 30.0F){
						//this.playSound(SASoundEvent.nuclear_exp.get(), 2.0F + exlevel, 1.0F);
						bullet.hitEntitySound=SASoundEvent.nuclear_exp.get();
						bullet.hitBlockSound=SASoundEvent.nuclear_exp.get();
					}else{
						if (exlevel > 15.0F) {
							//this.playSound(SASoundEvent.missile_hit1.get(), 2.0F + exlevel, 1.0F);
							bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
							bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
						} else {
							//this.playSound(SASoundEvent.artillery_impact.get(), 2.0F + exlevel, 1.0F);
							bullet.hitEntitySound=SASoundEvent.artillery_impact.get();
							bullet.hitBlockSound=SASoundEvent.artillery_impact.get();
						}
					}
				}else{
					if (exlevel == 1) {
						//this.playSound(SASoundEvent.missile_hit1.get(), 2.0F + exlevel, 1.0F);
						bullet.hitEntitySound=SASoundEvent.ag_metal.get();
						bullet.hitBlockSound=SASoundEvent.ag_impact.get();
					} else if(exlevel>1){
						//this.playSound(SASoundEvent.tank_shell_metal.get(), 2+this.getExLevel(), 1.0F);
						bullet.hitEntitySound=SASoundEvent.tank_shell_metal.get();
						bullet.hitBlockSound=SASoundEvent.tank_shell.get();
					}
				}
				
				bullet.timemax = maxtime;
				bullet.setGravity(gra);
				if(fx2!=null)bullet.setFX(fx2);
				bullet.setModel(model);
				bullet.setTex(tex);
				bullet.setBulletType(dameid);
				if(dameid == 5)bullet.flame = true;
				if (shooter.hasEffect(MobEffects.BLINDNESS))
				{
					recoil = recoil * 10F;
				}
				{
					bullet.moveTo(posX + xx11, posY + fireOffsetY + base, posZ + zz11, roteY, roteX);
					//bullet.moveTo(posX, posY , posZ , 1, 1);
					bullet.shootFromRote(roteX, roteY, 0.0F, speed, recoil);
					if (!living.level().isClientSide) {
						living.level().addFreshEntity(bullet);
					}
				}
			}
		}
    }
}