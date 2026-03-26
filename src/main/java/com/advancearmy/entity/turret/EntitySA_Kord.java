package advancearmy.entity.turret;
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

public class EntitySA_Kord extends EntitySA_TurretBase{
	public EntitySA_Kord(EntityType<? extends EntitySA_Kord> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 0;
		seatPosZ[0] = -0.75F;
		this.isturret=true;
		this.renderHudOverlay = false;
		this.renderHudIcon = true;
		this.hudIcon = "wmlib:textures/hud/aim.png";
		this.renderHudOverlayZoom = false;
		this.w1name = "12.7毫米KORD重机枪";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		seatTurret[0] = true;
		this.w1recoilp = 1;
		this.w1recoilr = 3;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
		this.minyaw = -75F;
		this.maxyaw = 75F;
		this.turretSpeed = 0.2F;
		this.ammo1=4;
		this.fireposX1 = 0;
		this.fireposY1 = 0.76F;
		this.fireposZ1 = 1.84F;
		this.firebaseY = 0;
		this.firebaseZ = 0F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/kord_t.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/kord.png");
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/triphead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/tripbody.png");
		this.magazine = 100;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		this.firesound1 = SASoundEvent.fire_kord.get();
		this.w1barrelsize = 0.2F;
		this.weaponCount = 1;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
	}

	public EntitySA_Kord(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_KORD.get(), worldIn);
	}
	
	public void tick() {
		super.tick();
		if (this.getFirstSeat() != null){//
			if(this.getTargetType()==0){
				this.firesound1=SASoundEvent.fire_kord.get();
			}else{
				this.firesound1=SASoundEvent.fire_kord_3p.get();
			}
			EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
			if(this.getRemain1()>0){
				ammo = true;
				if(this.cooltime<1){
					if(count<2){
						++count;
					}else{
						count = 0;
					}
				}
			}else{
				ammo = false;
			}
		}
	}
	
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		String fx1 = "SmokeGun";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 0, model, tex, fx1, null, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		10, 4, 1F, 1, false, 1, 0.01F, 50, 0);
	}
}