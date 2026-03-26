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
public class ERO_ROCKET extends EntitySA_TurretBase implements Enemy,IEnemy{
	public ERO_ROCKET(EntityType<? extends ERO_ROCKET> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.isturret=true;
		//this.onlythrow=true;
		this.isthrow = true;
		this.throwspeed = 4F;
		this.throwgrav = 0.1F;
		fireproduct=true;
		this.renderHudOverlay = false;
		this.renderHudIcon = false;
		this.renderHudOverlayZoom = false;

		this.w1recoilp = 2;
		this.w1recoilr = 8;

		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
		this.fire1tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
		
		this.turretSpeed = 0.2F;
		this.ammo1=40;
		this.fireposX1 = -2.37F;
		this.fireposY1 = 2.98F;
		this.fireposZ1 = 3.26F;
		this.firebaseY = 0;
		this.firebaseZ = 0F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/ero/ravager_rocket.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ero_ravager.png");
		this.magazine = 6;
		this.reload_time1 = 250;
		this.reloadSound1 = SASoundEvent.reload_81mm_m1.get();
		this.firesound1 = SASoundEvent.fire_81mm_m1.get();
		selfAttack = true;
		this.weaponCount = 1;
	}

	public ERO_ROCKET(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.E_ROCKET.get(), worldIn);
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
			if(this.getRemain1()%2==0){
				this.fireposX1 = -2.37F;
			}else{
				this.fireposX1 = 2.37F;
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
		String model = "advancearmy:textures/entity/bullet/bulletrocket.obj";
		String tex = "advancearmy:textures/entity/bullet/bulletrocket.png";
		String fx1 = "AdvTankFire";
		LivingEntity shooter = this;
		if(this.getVehicle() != null && this.getVehicle() instanceof LivingEntity)shooter = (LivingEntity)this.getVehicle();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, "SAMissileSmoke", firesound1,
		1.57F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		40, this.throwspeed, 3F, 4, false, 1, this.throwgrav, 5000, 5);
	}
}