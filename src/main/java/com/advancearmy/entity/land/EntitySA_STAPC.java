package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
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
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.soldier.EntitySA_OFG;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import wmlib.common.living.EntityWMSeat;

import wmlib.api.IArmy;
public class EntitySA_STAPC extends EntitySA_LandBase{
	public EntitySA_STAPC(EntityType<? extends EntitySA_STAPC> sodier, Level worldIn) {
		super(sodier, worldIn);
		startShield=true;
		seatPosX[0] = 0.72F;
		seatPosY[0] = 0.8F;
		seatPosZ[0] = 2.8F;
		seatMaxCount = 8;
		seatPosX[1] = -0.86F;
		seatPosY[1] = 1.4F;
		seatPosZ[1] = 2.21F;
		
		seatPosX[2] = 1.33F;
		seatPosY[2] = 0.8F;
		seatPosZ[2] = -1.36F;
		seatPosX[3] = 1.33F;
		seatPosY[3] = 0.35F;
		seatPosZ[3] = -0.31F;
		seatPosX[4] = 1.33F;
		seatPosY[4] = 0.25F;
		seatPosZ[4] = 0.79F;
		
		seatPosX[5] = -1.33F;
		seatPosY[5] = 0.8F;
		seatPosZ[5] = -1.36F;
		seatPosX[6] = -1.33F;
		seatPosY[6] = 0.35F;
		seatPosZ[6] = -0.31F;
		seatPosX[7] = -1.33F;
		seatPosY[7] = 0.25F;
		seatPosZ[7] = 0.79F;
		
		seatHide[0] = true;
		seatHide[1] = true;
		canFloat=true;
		canBreakLog=false;
		this.armor_front = 18;
		this.armor_side = 18;
		this.armor_back = 18;
		this.armor_top = 18;
		this.armor_bottom = 18;
		this.seatNoThird = true;
		this.noturret=true;
		this.seatProtect = 0.1F;
        this.MoveSpeed = 0.07F;
        this.turnSpeed = 5F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 1F;
		this.thBackSpeed = -1F;
		this.setMaxUpStep(1.5F);

		this.startsound = SASoundEvent.start_hmmwv.get();
		this.movesound = SASoundEvent.mirage_move.get();
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/stbody.png");
	}

	public EntitySA_STAPC(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_STAPC.get(), worldIn);
	}
	
	public void stopPassenger(){
		this.setMoveMode(5);
	}
	public void dropPassenger(){
		int v = 0;
		int rx = (int)this.getX();
		int ry = (int)this.getY();
		int rz = (int)this.getZ();
		for(int i = 1; i < this.seatMaxCount; ++i) {
			if(this.getAnySeat(i)!=null && !((EntityWMSeat)this.getAnySeat(i)).canDrive()){
				ridcool = 20;
				EntityWMSeat seat = (EntityWMSeat)this.getAnySeat(i);
				if(seat.weaponCount==0 && seat.getNpcPassenger()!=null){
					this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
					/*if(seat.getNpcPassenger() instanceof IPara){
						IPara para = (IPara)seat.getNpcPassenger();
						para.setDrop();
					}*/
					if(seat.getNpcPassenger() instanceof IArmy unit){
						++v;
						rx = rx-v+this.level().random.nextInt(v*2+1);
						rz = rz-v+this.level().random.nextInt(v*2+1);
						unit.setMove(3,rx,ry,rz);
					}
					seat.getNpcPassenger().stopRiding();
				}
			}
		}
	}
	
	float door_speed=0.5F;
	
	public float roteDoor;
	int rtime;
	int block_height;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(this.getMoveMode()==5){
				++rtime;
				if(rtime>50*door_speed && rtime<100*door_speed){
					--roteDoor;
				}
				if(rtime>150*door_speed){
					this.setMoveMode(0);
					this.dropPassenger();
					rtime=0;
				}
			}else{
				rtime=0;
				if(roteDoor<0)++roteDoor;
			}
			
			
			block_height = 2+this.level().getHeight(Heightmap.Types.WORLD_SURFACE,(int)this.getX(),(int)this.getZ());
			if(this.getY()<block_height){
				Vec3 vector3d = this.getDeltaMovement();
				this.setDeltaMovement(vector3d.x, 0.05D, vector3d.z);
			}
			if (this.getAnySeat(1) != null){//
				EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
				if(this.setSeat){
					seat.turretSpeed = 0.6F;
					seat.render_hud_box = true;
					seat.hud_box_obj = "wmlib:textures/hud/laseraa.obj";
					seat.hud_box_tex = "wmlib:textures/hud/box.png";
					seat.renderHudOverlay = false;
					seat.seatPosZ[0] = 0.7F;
					seat.laserweapon[0] = true;
					seat.laserwidth[0] = 1;
					seat.laser_model_tex1 = "advancearmy:textures/entity/flash/aa_beam";
					seat.laserfxfire1 = "LaserFlashGun";
					seat.laserfxhit1 = "LaserHit";
					seat.turretPitchMax = -40;
					seat.turretPitchMin = 30;
					seat.maxyaw=100F;
					seat.minyaw=-100F;
					seat.w1name="轻型激光炮";
					seat.magazine = 10;
					seat.seatHide = true;
					seat.weaponCount = 1;
					seat.ammo1 = 15;
					seat.reload_time1 = 100;
					seat.attack_range_max = 65;
					seat.turret_speed = true;
					seat.attack_height_max = 20;
					seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
					seat.setWeapon(0, 2, null, null, null, null, SASoundEvent.stapcf.get(), 0F,0.5F,1F,0,0.6F,
					45, 6F, 0, 2, false, 0, 0, 15, 0);
				}
				//render
				this.turretYaw1=seat.getYHeadRot();
				if(seat.turretPitch<15)this.turretPitch1=seat.turretPitch;
				while(this.turretYaw1 - this.turretYawO1 < -180.0F) {
					this.turretYawO1 -= 360.0F;
				}
				while(this.turretPitch1 - this.turretPitchO1 >= 180.0F) {
					this.turretPitchO1 += 360.0F;
				}
				this.turretYawO1 = this.turretYaw1;
				this.turretPitchO1 = this.turretPitch1;
				
				if(seat.getRemain1()>0){
					ammo = true;
					if(seat.fire1){
						if(count<2){
							++count;
						}else{
							count = 0;
						}
					}
				}else{
					ammo = false;
				}
				if(seat.getRemain2()>0){
					if(count<2){
						++count;
					}else{
						count = 0;
					}
				}
			}
		}
	}
}