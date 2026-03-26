package advancearmy.entity.mob;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_TurretBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import wmlib.api.IEnemy;
public class ERO_AA extends EntitySA_TurretBase implements Enemy,IEnemy{
	public ERO_AA(EntityType<? extends ERO_AA> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.isturret=true;
		fireproduct=true;
		this.w1recoilp = 1;
		this.w1recoilr = 3;
		this.turretPitchMax = -90;
		this.turretPitchMin = 30;
		this.minyaw = -75F;
		this.maxyaw = 75F;
		this.turretSpeed = 0.2F;
		this.ammo1=2;
		this.fireposX1 = 1.74F;
		this.fireposY1 = 3.22F;
		this.fireposZ1 = 2.92F;
		this.firebaseY = 2.81F;
		this.firebaseZ = 0F;
		
		this.fireposX2 = 1.28F;
		this.fireposY2 = 4.16F;
		this.fireposZ2 = 2.4F;
		
		this.is_aa = true;
		this.attack_range_max = 30;
		this.attack_height_max = 90;
		this.obj = new SAObjModel("advancearmy:textures/mob/ero/ravager_aa.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ero_ravager.png");
		this.magazine = 400;
		this.magazine2 = 2;
		this.reload_time2 = 100;
		this.reload_time1 = 80;
		this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		this.firesound1 = SASoundEvent.fire_kord_3p.get();
		
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.firesound2 = SASoundEvent.fire_stin.get();
		
		this.w1barrelsize = 0.1F;
		this.weaponCount = 2;
		selfAttack = true;
	}

	public ERO_AA(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.E_AA.get(), worldIn);
	}
    @Override
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(par2<10)par2=5;
		if(par2>10)par2=par2-10;
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}else{
				return super.hurt(source, par2);
			}
		}else{
			return super.hurt(source, par2);
		}
	}
	public void tick() {
		super.tick();
		if (this.isAttacking()){//
			if(this.getRemain1()%4==1){
				this.fireposX1 = 1.74F;
				this.fireposY1 = 3.22F;
			}else if(this.getRemain1()%4==2){
				this.fireposX1 = -1.74F;
				this.fireposY1 = 3.22F;
			}else if(this.getRemain1()%4==3){
				this.fireposX1 = 1.74F;
				this.fireposY1 = 2.54F;
			}else{
				this.fireposX1 = -1.74F;
				this.fireposY1 = 2.54F;
			}
			if(this.getRemain2()%2==0){
				this.fireposX2 = 1.28F;
			}else{
				this.fireposX2 = -1.28F;
			}
			
			if(this.getRemain1()>0){
				ammo = true;
			}else{
				ammo = false;
			}
		}
	}
	public int getTargetType(){
		return 2;
	}
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		String fx1 = "SmokeGun";
		LivingEntity shooter = this;
		if(this.getVehicle() != null && this.getVehicle() instanceof LivingEntity)shooter = (LivingEntity)this.getVehicle();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 0, model, tex, fx1, null, firesound1,
		1.57F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		10, 4, 4F, 1, false, 1, 0.01F, 50, 0);
	}
	
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/stinmissile.obj";
		String tex = "advancearmy:textures/gun/fim92.png";
		String fx1 = "SmokeGun";
		LivingEntity shooter = this;
		Entity locktarget = this.getTarget();
		if(this.getVehicle() != null && this.getVehicle() instanceof LivingEntity)shooter = (LivingEntity)this.getVehicle();
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, fx1, "SAMissileTrail", firesound1,
		1F, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		50, 4, 2F, 2, false, 1, 0.01F, 100, 0);
	}
}