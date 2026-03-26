package advancearmy.entity.air;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import advancearmy.init.ModEntities;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_AirBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.world.entity.player.Player;
import safx.util.EntityCondition;
import safx.SagerFX;
public class EntitySA_A10a extends EntitySA_AirBase{
	public EntitySA_A10a(EntityType<? extends EntitySA_A10a> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 2F;
		seatPosZ[0] = 0F;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		seatMaxCount = 1;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.canNightV=true;
		VehicleType = 4;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/a10icon.png");
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-12F;
		this.noCulling = true;
		
        this.MoveSpeed = 0.038F;
        this.turnSpeed = 2F;
		this.setMaxUpStep(1.5F);
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.fly_height = 75;
		
		this.w1aa = false;
		this.w1max = 50;
		this.w1min = 5;
		this.w1aim = 5;
		this.w2aa = false;
		this.w2max = 100;
		this.w2min = 5;
		this.w2aim = 5;
		this.w4aa = false;
		this.w4max = 10;
		this.w4min = -10;
		this.w4aim = 5;
		this.stayrange = 50;
		this.min_height = 15;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/a10a.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/a10us.png");
		
		this.w1tex = ResourceLocation.tryParse("advancearmy:textures/entity/bullet/hy70.png");
		this.w4tex = ResourceLocation.tryParse("advancearmy:textures/entity/bullet/mk82a.png");
		
		this.reloadSound1 = SASoundEvent.bomb_reload.get();
		this.reloadSound2 = SASoundEvent.reload_chaingun.get();
		this.reloadSound4 = SASoundEvent.reload_missile.get();
		this.magazine = 20;
		this.reload_time1 = 400;
		this.ammo1=5;
		this.ammo2=1;
		this.magazine2 = 750;
		this.reload_time2 = 400;
		this.magazine4 = 8;
		this.reload_time4 = 300;
		this.ammo4 = 8;
		this.fireposX2 = -0.18F;
		this.fireposY2 = 1.49F;
		this.fireposZ2 = 3.52F;
		this.movesound = SASoundEvent.a10_move.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_a10_3p.get();
		this.firesound4 = SASoundEvent.bomb_release.get();
		
		this.magazine3 = 12;
		this.reload_time3 = 200;
		
		this.w1showammo = true;
		this.w4showammo = true;
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/hy70.png";
		this.w2icon="advancearmy:textures/hud/gau8.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="advancearmy:textures/hud/mk82a.png";
		this.w1name = "70毫米九头蛇火箭弹";
		this.w2name = "25毫米GAU8机炮";
		this.w4name = "MK82航空炸弹";
	}

	public EntitySA_A10a(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_A10A.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_A10a.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 8D);
    }
	int attack_start = 0;
	boolean trail = false;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(!this.onGround() && this.getMovePitch()<-1F && this.movePower>10){
				if(!trail){
					if(ModList.get().isLoaded("safx")){
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, 9f, -2.2f, -5.0f, true, EntityCondition.ENTITY_PLANE);
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, -9f, -2.2f, -5.0f, true, EntityCondition.ENTITY_PLANE);
					}
					trail = true;
				}
			}else{
				if(trail)trail = false;
			}
			
			if(this.getMoveMode()==5){
				this.movePower = throttleMax;
				this.throttle = throttleMax;
				this.drop = 200;
				++attack_start;
				if(attack_start==1)this.setMovePitch(50);
				if(attack_start>30 && attack_start<100){
					this.attack1=true;
					this.attack2=true;
				}else{
					this.attack1=false;
					this.attack2=false;
				}
				if(attack_start>90 && attack_start<120){
					this.attack4=true;
				}else{
					this.attack4=false;
				}
				if(attack_start==120)this.setMovePitch(-30);
				if(attack_start==200)this.setMovePitch(0);
				if(this.getArmyType2()>0){
					this.setArmyType2(this.getArmyType2()-1);
				}else{
					this.removePassenger();
					this.discard();
				}
			}
		}
	}

	public void weaponActive1(){
		float fireX = 0;
		if(this.getRemain1()%2==0){
			fireX = 2.83F;
		}else{
			fireX = -2.83F;
		}
		String model = "advancearmy:textures/entity/bullet/bulletrocket.obj";
		String tex = "advancearmy:textures/entity/bullet/bulletrocket.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, "RocketTrail", firesound1,
		1F, fireX,0,0,1.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		50, 3F, 1.1F, 5, false, 1, 0.001F, 500, 3);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet1.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound2,
		1F, this.fireposX2,this.seatPosY[0],this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		35, 8F, 1.5F, 1, false, 1, 0.01F, 50, 0);
	}
	public void weaponActive4(){
		float fireX = 0;
		if(this.getRemain4()%2==0){
			fireX = 3;
		}else{
			fireX = -3;
		}
		String model = "advancearmy:textures/entity/bullet/mk82a.obj";
		String tex = "advancearmy:textures/entity/bullet/mk82a.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound4,
		1F, fireX,0,0,0.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		120, 1.5F, 1.1F, 7, false, 1, 0.02F, 500, 3);
	}
}