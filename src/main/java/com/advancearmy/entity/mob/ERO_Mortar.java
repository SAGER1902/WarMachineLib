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
public class ERO_Mortar extends EntitySA_TurretBase implements Enemy,IEnemy{
	public ERO_Mortar(EntityType<? extends ERO_Mortar> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.isturret=true;
		this.onlythrow=true;
		this.isthrow = true;
		this.throwspeed = 6F;
		this.throwgrav = 0.1F;
		fireproduct=true;
		this.renderHudOverlay = false;
		this.renderHudIcon = false;
		this.renderHudOverlayZoom = false;

		this.w1recoilp = 2;
		this.w1recoilr = 8;

		this.turretPitchMax = -90;
		this.turretPitchMin = -45;
		this.fire1tex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/muzzleflash3.png");
		
		this.turretSpeed = 0.2F;
		this.ammo1=5;
		this.fireposX1 = 0;
		this.fireposY1 = 2.81F;
		this.fireposZ1 = 3.84F;
		this.firebaseY = 0;
		this.firebaseZ = 0F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/ero/ravager_mortar.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ero_ravager.png");
		this.magazine = 1;
		this.reload_time1 = 200;
		this.reloadSound1 = SASoundEvent.reload_81mm_m1.get();
		this.firesound1 = SASoundEvent.fire_81mm_m1.get();
		selfAttack = true;
		this.weaponCount = 1;
	}

	public ERO_Mortar(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.E_MORTAR.get(), worldIn);
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
		/*if (this.isAttacking())*/{//
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
		String model = "advancearmy:textures/mob/ero/mortar_bullet.obj";
		String tex = "advancearmy:textures/mob/ero/ero_ravager.png";
		String fx1 = "AdvTankFire";
		LivingEntity shooter = this;
		if(this.getVehicle() != null && this.getVehicle() instanceof LivingEntity)shooter = (LivingEntity)this.getVehicle();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, null, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		30, this.throwspeed, 3F, 5, false, 1, this.throwgrav, 5000, 3);
	}
}