package wmlib.common.living;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;                    // World  Level
import net.minecraft.server.level.ServerLevel;            // ServerWorld  ServerLevel
import net.minecraft.core.BlockPos;                       // BlockPos
import net.minecraft.world.phys.Vec3;                     // Vector3d  Vec3
import net.minecraft.world.InteractionHand;               // Hand  InteractionHand
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;              // TranslationTextComponent  Component
import net.minecraft.world.damagesource.DamageSource;     
import net.minecraft.sounds.SoundEvent;                  
import net.minecraft.sounds.SoundEvents;                 
import net.minecraft.world.item.Items;                    
import net.minecraft.world.level.block.Blocks;            
import net.minecraft.world.item.ItemStack;                
import net.minecraft.network.protocol.Packet;             // IPacket  Packet
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
// NBT
import net.minecraft.nbt.CompoundTag;                    // CompoundNBT  CompoundTag
import net.minecraft.nbt.ListTag;                        // ListNBT  ListTag
import net.minecraft.nbt.NbtUtils;                       // NBTUtil  NbtUtils
import net.minecraft.world.entity.ai.attributes.AttributeSupplier; // AttributeModifierMap  AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;          // PlayerEntity  Player
import net.minecraft.world.entity.monster.Enemy;          // IMob  Enemy
import net.minecraft.world.entity.Entity;                 
import net.minecraft.world.entity.PathfinderMob;          // CreatureEntity  PathfinderMob
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;          // TameableEntity  TamableAnimal
import net.minecraft.world.entity.Mob;                   // MobEntity  Mob
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;       // EntitySize  EntityDimensions
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.AgeableMob;             // AgeableEntity  AgeableMob
import net.minecraft.world.entity.EquipmentSlot;          // EquipmentSlotType  EquipmentSlot
import net.minecraft.core.NonNullList;                    
// Forge 
import net.minecraftforge.network.NetworkEvent;           
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.scores.Team;
import java.util.UUID;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
public abstract class EntityWMVehicleBase extends TamableAnimal{
	public EntityWMVehicleBase(EntityType<? extends EntityWMVehicleBase> sodier, Level worldIn) {
		super(sodier, worldIn);
	}
	protected void registerGoals() {
	}
	public EntityWMVehicleBase getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
	  return null;
	}
	private final NonNullList<ItemStack> weaponItems = NonNullList.withSize(2, ItemStack.EMPTY);
    private static final EntityDataAccessor<Integer> remain2 = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> remain1 = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> remain3 = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> remain4 = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
	/*private static final EntityDataAccessor<Boolean> isattack = 
    		SynchedEntityData.<Boolean>defineId(EntityWMVehicleBase.class, EntityDataSerializers.BOOLEAN);*/
	private static final EntityDataAccessor<Boolean> choose = 
    		SynchedEntityData.<Boolean>defineId(EntityWMVehicleBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> armytype2 = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> armytype1 = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> moveyaw = 
    		SynchedEntityData.<Float>defineId(EntityWMVehicleBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> movepitch = 
    		SynchedEntityData.<Float>defineId(EntityWMVehicleBase.class, EntityDataSerializers.FLOAT);
	 private static final EntityDataAccessor<Float> forwardmove = 
	    		SynchedEntityData.<Float>defineId(EntityWMVehicleBase.class, EntityDataSerializers.FLOAT);
	 private static final EntityDataAccessor<Float> strafingmove = 
	    		SynchedEntityData.<Float>defineId(EntityWMVehicleBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> movemode = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> aitype = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> targettype = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> change = 
    		SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movetype = SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movex = SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movey = SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movez = SynchedEntityData.<Integer>defineId(EntityWMVehicleBase.class, EntityDataSerializers.INT);
	
    public float armor_front = 6;
    public float armor_side = 5;
    public float armor_back = 4;
    public float armor_top = 3;
    public float armor_bottom = 3;
    public boolean haveTurretArmor = false;
    public float armor_turret_height = 0;
    public float armor_turret_front = 5;
    public float armor_turret_side = 4;
    public float armor_turret_back = 3;
	
	public float antibullet_0 = 0.8F;
	public float antibullet_1 = 1.2F;
	public float antibullet_2 = 1.5F;
	public float antibullet_3 = 0.8F;
	public float antibullet_4 = 0.8F;
	
	public int enc_soul = 0;
	public int enc_armor = 0;
	public int enc_heal = 0;
	public int enc_power = 0;
	public int enc_protect = 0;
	public int enc_control = 0;
	public boolean can_follow = false;
	public float seatProtect = 0.25F;
	
	public boolean canNightV = false;
	public float airzoom_x = 2F;
	public float airzoom_y = -3F;
	public float airzoom_z = -3F;
	
	public float heligun_x = 0F;
	public float heligun_y = -2.5F;
	public float heligun_z = 3.5F;
	public boolean isSpaceShip = false;
	public boolean isZoom = false;
	public boolean seatNoThird = false;
	public float rider_height = 0;
	public float seatView1X = 0F;
	public float seatView1Y = 0F;
	public float seatView1Z = 0F;
	public float seatView3X = 0F;
	public float seatView3Y = -2F;
	public float seatView3Z = -4F;
	
	public boolean seatCanZoom = true;
	public float seatZoomFov = 2.0F;
	public float ridding_zoom_first = 2.0F;
	public boolean render_hud_box = false;
	public String hud_box_obj = "wmlib:textures/hud/box.obj";
	public String hud_box_tex = "wmlib:textures/hud/box.png";
	
	public String w1icon="wmlib:textures/hud/weapon1.png";
	public String w2icon="wmlib:textures/hud/weapon2.png";
	public String w3icon="wmlib:textures/hud/weapon3.png";
	public String w4icon="wmlib:textures/hud/weapon4.png";
	
	public int weaponCount=0;
	public boolean renderHudIcon = false;
	public String hudIcon = "wmlib:textures/hud/cross.png";
	public boolean render_hud_icon_hori = false;
	public String hud_icon_hori = "wmlib:textures/hud/cross.png";
	public boolean renderHudOverlay = false;
	public String hudOverlay = "wmlib:textures/misc/scope.png";
	public boolean renderHudOverlayZoom = false;
	public String hudOverlayZoom = "wmlib:textures/misc/scope.png";
	public boolean render_hud_icon_bomber = false;
	public String hud_icon_bomber = "wmlib:textures/hud/bomber.png";
	public boolean renderHud = false;
	public boolean renderRader = true;
	
	public float targetYaw = 0;
	public float targetPitch = 0;
	
	public float turretYaw;
	
	public float turretYawO;
	public float turretPitchO;
	
	public float turretYaw1;
	public float turretYaw2;
	public float turretPitch;
	public float turretPitch1;
	public float turretPitch2;

	public float turretYawMove;
	public float turretPitchMove;
	
	public float minyaw = -360F;
	public float maxyaw = 360F;
	public float turretPitchMax = -90;
	public float turretPitchMin = 90;
	
	public int startTime = 0;

	public float throttle;
	public float throttleRight;
	public float throttleLeft;
	public float trackl;
	public float trackr;
	public double movePower;//?á|
	public double th;
	public float throttleMax;
	public float throttleMin;
	public float thFrontSpeed;
	public float thBackSpeed;
	public float MoveSpeed = 0.02F;
	public float turnSpeed = 1F;
	public float turretSpeed = 1F;
	public float thpera = 0;//
	public float flyRoll = 0;//
	public float flyPitch;
	
	public double[] seatPosX = new double[12];
	public double[] seatPosY = new double[12];
	public double[] seatPosZ = new double[12];
	
	public int moveangle = 0;
	public float throttle_up = 0;
	public float flyPitchMax = 30;
	public float flyPitchMin = -20;
	
	public int VehicleType = 0;
	
	public boolean[] seatCanFire = new boolean[16];
	public boolean[] seatHide = new boolean[16];
	public boolean[] seatTurret = new boolean[16];
	public String render_hud_information_1 = "";
	public String render_hud_information_2 = "";
	public String render_hud_information_3 = "";
	public String render_hud_information_4 = "";
	public String render_hud_information_5 = "";
	public String render_hud_information_6 = "";
	
	public String w1name = "WEAPON1";
	public String w2name = "WEAPON2";
	public String w3name = "WEAPON3";
	public String w4name = "WEAPON4";
	
	public int block_height = 0;
	public double target_height = 0;

	public float attack_range_max = 0;
	public float attack_range_min = 0;
	public float attack_height_max = 20;
	public float attack_height_min = -20;

	public int ammo1 = 2;
	public int ammo2 = 2;
	public int ammo3 = 2;
	public int ammo4 = 2;
	public int ammo5 = 2;
	public int cooltime;
	public int cooltime2;
	public int cooltime3;
	public int cooltime4;
	
	public int flaretime = 41;
	
	public int cooltime5;
	public int cooltime6;
	public int magazine=1;
	public int magazine2=1;
	public int magazine3=1;
	public int magazine4=1;
	public boolean counter1 = false;
	public boolean counter2 = false;
	public boolean counter3 = false;
	public boolean counter4 = false;

	public boolean w1cross = false;
	public boolean w2cross = false;
	public boolean w3cross = false;
	public boolean w4cross = false;

	public int gun_count1 = 0;
	public int gun_count2 = 0;
	public int gun_count3 = 0;
	public int gun_count4 = 0;
	public int gun_count5 = 0;
	public int gun_count6 = 0;
	public float reload1 = 0;
	public float reload2 = 0;
	public float reload3 = 0;
	public float reload4 = 0;
	public float reload5 = 0;
	public float reload6 = 0;
	public int reload_time1=100;
	public int reload_time2=100;
	public int reload_time3=100;
	public int reload_time4=100;

	public int w1cycle = 1;
	public int w2cycle = 1;
	public int w3cycle = 1;
	public int w4cycle = 1;
	public int w5cycle = 1;
	
	public ResourceLocation icon1tex = null;
	public ResourceLocation icon2tex = null;

	public boolean firetrue = false;
	public LivingEntity missileTarget = null;
	public LivingEntity firstTarget=null;
	public LivingEntity clientTarget=null;
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.getOwner()==null && this.getTargetType()==2) {
            this.discard();
            return;
        }
	}
	
	public int anim1 = 0;
	public int anim2 = 0;
	public int anim3 = 0;
	public int anim4 = 0;
	public int anim5 = 0;
	public int anim6 = 0;
	public int anim7 = 0;
	public int anim8 = 0;
	public int anim9 = 0;
    public void setAnimFire(int id){}

	public void setVehicleAnim(int x){
		if(x==1)this.anim1=0;
		if(x==2)this.anim2=0;
		if(x==3)this.anim3=0;
		if(x==4)this.anim4=0;
		if(x==5)this.anim5=0;
		if(x==6)this.anim6=0;
		if(x==7)this.anim7=0;
		if(x==8)this.anim8=0;
		if(x==9)this.anim9=0;
	}
	
	public Team getTeam() {
		if (this.getOwner()!=null) {
			LivingEntity livingentity = this.getOwner();
			if (livingentity != null) {
				return livingentity.getTeam();
			}
		}else if(this.getFirstSeat()!=null && this.getFirstSeat().getAnyPassenger()!=null){
			return this.getFirstSeat().getAnyPassenger().getTeam();
		}
		return super.getTeam();
	}
	
	public boolean hurt(DamageSource source, float par2)
    {
		float damage = par2;
		if(this.getArmorValue()>9D){
			if(par2<20F){
				if(par2<10F){
					damage = 0;
				}else{
					damage = par2*0.2F;
				}
			}else{
				damage = par2 - 20F;
			}
		}
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				if(this.getFirstSeat()!=null && entity == this.getFirstSeat().getAnyPassenger()||entity.getVehicle()==this||
				this.getOwner()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof TamableAnimal){
						TamableAnimal soldier = (TamableAnimal)entity;
						if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							return super.hurt(source, damage);
						}
					}else{
						return super.hurt(source, damage);
					}
				}
			}else{
				return super.hurt(source, damage);
			}
		}else {
			return super.hurt(source, damage);
		}
    }
	
	public int seatMaxCount = 1;
	@Nullable
	public LivingEntity getControllingPassenger() {//不要使用
	   /*List<Entity> list = this.getPassengers();
	   if(list.isEmpty()){
		   return null;
	   }else{
		   if(list.get(0) instanceof EntityWMSeat){
				EntityWMSeat seat = (EntityWMSeat)list.get(0);
				if(seat.getControllingPassenger()!=null){
					return seat.getControllingPassenger();//神秘问题
				}else{
					return null;
				}
		   }else{
			   return null;
		   }
	   }*/
	   return null;
	}
	@Nullable
	public Entity getAnySeat(int id) {//任意座位
	   List<Entity> list = this.getPassengers();
	   if(list.isEmpty()){
		   return null;
	   }else{
		   if(list.size()>id && list.get(id) instanceof EntityWMSeat){
			   return list.get(id);
		   }else{
			   return null;
		   }
	   }
	}
	@Nullable
	public EntityWMSeat getFirstSeat() {//主驾驶座位
		List<Entity> list = this.getPassengers();
		if(list.isEmpty()){
		   return null;
		}else{
			if(list.get(0) instanceof EntityWMSeat){
				return (EntityWMSeat)list.get(0);
			}else{
				return null;
			}
		}
	}
	
    public static float clampYaw(float yaw) {
        while (yaw < -180.0f) {
            yaw += 360.0f;
        }
        while (yaw >= 180.0f) {
            yaw -= 360.0f;
        }
        return yaw;
    }
	
	public boolean havemissile = false;
	public float fireposX1 = 0;
	public float fireposY1 = 0;
	public float fireposZ1 = 0;
	public float fireposX2 = 0;
	public float fireposY2 = 0;
	public float fireposZ2 = 0;
	public float firebaseY = 0;
	public float firebaseZ = 0;
	public static double getSpeedKPH(LivingEntity entity) {
		double dx = Math.abs(entity.getX() - entity.xo);
		double dy = Math.abs(entity.getY() - entity.yo);
		double dz = Math.abs(entity.getZ() - entity.zo);
		float distance = (float)Math.sqrt((dx * dx) + (dy * dy) + (dz * dz))*20;
        double speedKPH = distance* (3600.0 / 1000.0);
        return speedKPH;
    }
	public boolean isturret = false;
	public boolean onlythrow = false;
	public boolean isthrow = false;
	public float throwspeed = 8F;
	public float throwgrav = 0.1F;
	public double speedKPH = 0;

	public boolean setSeat;
	
	public float sensitivityAdjust = 0;
	public float deltaRotation = 0;
	public int tracktick = 0;
	public int find_time = 0;	 
	int seat_time = 0;
	
	public int attack_time = 0;
	public void tick() {
		super.tick();
		
		if(this.firstTarget!=null){
			if(this.firstTarget.getHealth()<=0){
				this.firstTarget=null;
			}else{
				this.setTarget(this.firstTarget);
			}
		}
		
		speedKPH = getSpeedKPH(this);
		//if(this.VehicleType<3)this.angle_rote(this);
		if(seat_time<20)++seat_time;
		if(seat_time<18){
			this.setSeat = true;
		}else{
			if(this.setSeat)this.setSeat = false;
		}
		if(this.anim1<25)++this.anim1;
		if(this.anim2<25)++this.anim2;
		if(this.anim3<25)++this.anim3;
		if(this.anim4<25)++this.anim4;
		if(this.anim5<25)++this.anim5;
		if(this.anim6<25)++this.anim6;
		if(this.anim7<25)++this.anim7;
		if(this.anim8<25)++this.anim8;
		if(this.anim9<25)++this.anim9;
		if(this.getHealth()>0){
			if(tracktick<360){
				++tracktick;
			}else{
				tracktick = 0;
			}
    		if(cooltime < 200)++cooltime;
    		if(cooltime2 < 200)++cooltime2;
    		if(cooltime3 < 200)++cooltime3;
    		if(cooltime4 < 200)++cooltime4;
    		if(cooltime5 < 200)++cooltime5;
    		if(cooltime6 < 200)++cooltime6;
    		if(startTime < 200 && throttle<=5)++startTime;
			
			if(gun_count1 < 100)++gun_count1;
			if(gun_count2 < 100)++gun_count2;
			if(gun_count3 < 100)++gun_count3;
			if(gun_count4 < 100)++gun_count4;
			if(gun_count5 < 100)++gun_count5;
			if(gun_count6 < 100)++gun_count6;
			
			if (this.getTargetType()!=0) {
				if(this.getChange()>0)this.setChange(0);
			}
		}

		firetrue = false;
		while(this.turretYaw - this.turretYawO < -180.0F) {
			this.turretYawO -= 360.0F;
		}
		while(this.turretYaw - this.turretYawO >= 180.0F) {
			this.turretYawO += 360.0F;
		}
		while(this.turretPitch - this.turretPitchO < -180.0F) {
			this.turretPitchO -= 360.0F;
		}
		while(this.turretPitch - this.turretPitchO >= 180.0F) {
			this.turretPitchO += 360.0F;
		}
		
		this.turretYawO = this.turretYaw;
		this.turretPitchO = this.turretPitch;
		
		if(this.flyPitch > 180F){
			this.flyPitch =-179F;
		}
		if(this.flyPitch < -180F){
			this.flyPitch =179F;
		}
		if(this.flyRoll > 180F){
			this.flyRoll =-179F;
		}
		if(this.flyRoll < -180F){
			this.flyRoll =179F;
		}
		
		if(this.getHealth()==0){
			if(this.onGround()){
			}else{
				if(flyPitch<75)++flyPitch;
			}
		}
		if(this.locktime>0)--locktime;
	}
	
    public boolean CanAttack(Entity entity){
    	return false;
    }
	public boolean canlock = false;
	public boolean is_aa = false;
	public int mitargettime;
	public int locktime = 0;;

	public ItemStack getWeaponItem(EquipmentSlot eq) {
		switch(eq.getType()) {
		case HAND:
			return this.weaponItems.get(eq.getIndex());
		default:
			return ItemStack.EMPTY;
		}
	}

	public void setWeaponItem(EquipmentSlot eq, ItemStack stack) {
		switch(eq.getType()) {
		case HAND:
			this.weaponItems.set(eq.getIndex(), stack);
		break;
		}
	}
	
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		{
			ListTag listnbt1 = new ListTag();
			for(ItemStack itemstack1 : this.weaponItems) {
				CompoundTag compoundnbt1 = new CompoundTag();
				if (!itemstack1.isEmpty()) {
					itemstack1.save(compoundnbt1);
				}
				listnbt1.add(compoundnbt1);
			}
			compound.put("WeaponItems", listnbt1);
			
			compound.putInt("movetype", this.getMoveType());
			compound.putInt("movex", this.getMovePosX());
			compound.putInt("movey", this.getMovePosY());
			compound.putInt("movez", this.getMovePosZ());
			
			if(weaponCount>0)compound.putInt("remain1", this.getRemain1());
			if(weaponCount>1)compound.putInt("remain2", this.getRemain2());
			if(weaponCount>2)compound.putInt("remain3", this.getRemain3());
			if(weaponCount>3)compound.putInt("remain4", this.getRemain4());
			
			
			compound.putInt("armytype2", this.getArmyType2());//
			compound.putInt("armytype1", this.getArmyType1());//
			compound.putFloat("moveyaw", this.getMoveYaw());//
			compound.putFloat("movepitch", this.getMovePitch());//

			compound.putInt("movemode", this.getMoveMode());//
			compound.putInt("aitype", this.getAIType());
			compound.putInt("targettype", this.getTargetType());
			compound.putInt("change", this.getChange());
			
			//compound.putBoolean("isattack", this.isAttacking());
			compound.putBoolean("choose", this.getChoose());
			compound.putFloat("forwardmove", this.getForwardMove());
			compound.putFloat("strafingmove", this.getStrafingMove());
		}
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			if (compound.contains("WeaponItems", 9)) {
				ListTag listnbt1 = compound.getList("WeaponItems", 10);
				for(int j = 0; j < this.weaponItems.size(); ++j) {
					this.weaponItems.set(j, ItemStack.of(listnbt1.getCompound(j)));
				}
			}
			this.setMoveType(compound.getInt("movetype"));
			this.setMovePosX(compound.getInt("movex"));
			this.setMovePosY(compound.getInt("movey"));
			this.setMovePosZ(compound.getInt("movez"));
			if(weaponCount>0)this.setRemain1(compound.getInt("remain1"));
			if(weaponCount>1)this.setRemain2(compound.getInt("remain2"));
			if(weaponCount>2)this.setRemain3(compound.getInt("remain3"));
			if(weaponCount>3)this.setRemain4(compound.getInt("remain4"));
			this.setArmyType2(compound.getInt("armytype2"));
			this.setArmyType1(compound.getInt("armytype1"));
			this.setMoveYaw(compound.getFloat("moveyaw"));
			this.setMovePitch(compound.getFloat("movepitch"));
			//this.setAttacking(compound.getBoolean("isattack"));
			this.setChoose(compound.getBoolean("choose"));
			this.setMoveMode(compound.getInt("movemode"));
			this.setAIType(compound.getInt("aitype"));
			this.setTargetType(compound.getInt("targettype"));
			this.setChange(compound.getInt("change"));
			this.setForwardMove(compound.getFloat("forwardmove"));
			this.setStrafingMove(compound.getFloat("strafingmove"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(movetype, Integer.valueOf(1));
		this.entityData.define(movex, Integer.valueOf(0));
		this.entityData.define(movey, Integer.valueOf(0));
		this.entityData.define(movez, Integer.valueOf(0));
		this.entityData.define(remain1, Integer.valueOf(0));
		this.entityData.define(remain2, Integer.valueOf(0));
		this.entityData.define(remain3, Integer.valueOf(0));
		this.entityData.define(remain4, Integer.valueOf(0));
		//this.entityData.define(isattack, Boolean.valueOf(false));
		this.entityData.define(choose, Boolean.valueOf(false));
		this.entityData.define(armytype2, Integer.valueOf(0));
        this.entityData.define(armytype1, Integer.valueOf(0));
        this.entityData.define(moveyaw, Float.valueOf(0));
        this.entityData.define(movepitch, Float.valueOf(0));
		this.entityData.define(movemode, Integer.valueOf(0));
        this.entityData.define(aitype, Integer.valueOf(0));
        this.entityData.define(targettype, Integer.valueOf(0));
        this.entityData.define(change, Integer.valueOf(0));
		this.entityData.define(forwardmove, Float.valueOf(0F));
        this.entityData.define(strafingmove, Float.valueOf(0F));
	}
	
	public int getMoveType() {
			return ((this.entityData.get(movetype)).intValue());
	}
	public void setMoveType(int stack) {
		this.entityData.set(movetype, Integer.valueOf(stack));
	}
	public int getMovePosX() {
		return ((this.entityData.get(movex)).intValue());
	}
	public void setMovePosX(int stack) {
	this.entityData.set(movex, Integer.valueOf(stack));
	}
	public int getMovePosY() {
	return ((this.entityData.get(movey)).intValue());
	}
	public void setMovePosY(int stack) {
	this.entityData.set(movey, Integer.valueOf(stack));
	}
	public int getMovePosZ() {
	return ((this.entityData.get(movez)).intValue());
	}
	public void setMovePosZ(int stack) {
	this.entityData.set(movez, Integer.valueOf(stack));
	}
	
	public int getArmyType2() {
    	return ((this.entityData.get(armytype2)).intValue());
    }
    public void setArmyType2(int stack) {
    	this.entityData.set(armytype2, Integer.valueOf(stack));
    }
    public int getArmyType1() {
    	return ((this.entityData.get(armytype1)).intValue());
    }
    public void setArmyType1(int stack) {
    	this.entityData.set(armytype1, Integer.valueOf(stack));
    }

    public float getMoveYaw() {
    	return ((this.entityData.get(moveyaw)).floatValue());
    }
    public void setMoveYaw(float stack) {
    	this.entityData.set(moveyaw, Float.valueOf(stack));
    }
    public float getMovePitch() {
    	return ((this.entityData.get(movepitch)).floatValue());
    }
    public void setMovePitch(float stack) {
    	this.entityData.set(movepitch, Float.valueOf(stack));
    }
	
	public boolean isAttacking() {
		//return ((this.entityData.get(isattack)).booleanValue());
		return this.isAggressive();
	}
	public void setAttacking(boolean stack) {
		//this.entityData.set(isattack, Boolean.valueOf(stack));
        this.setAggressive(stack);
	}
	
	public boolean getChoose() {
		return ((this.entityData.get(choose)).booleanValue());
	}
	public void setChoose(boolean stack) {
		this.entityData.set(choose, Boolean.valueOf(stack));
	}
	
    public int getRemain2() {
    	return ((this.entityData.get(remain2)).intValue());
    }
    public void setRemain2(int stack) {
    	this.entityData.set(remain2, Integer.valueOf(stack));
    }
    public int getRemain1() {
    	return ((this.entityData.get(remain1)).intValue());
    }
    public void setRemain1(int stack) {
    	this.entityData.set(remain1, Integer.valueOf(stack));
    }

    public int getRemain3() {
    	return ((this.entityData.get(remain3)).intValue());
    }
    public void setRemain3(int stack) {
    	this.entityData.set(remain3, Integer.valueOf(stack));
    }
    public int getRemain4() {
    	return ((this.entityData.get(remain4)).intValue());
    }
    public void setRemain4(int stack) {
    	this.entityData.set(remain4, Integer.valueOf(stack));
    }
    public int getMoveMode() {
    	return ((this.entityData.get(movemode)).intValue());
    }
    public void setMoveMode(int stack) {
    	this.entityData.set(movemode, Integer.valueOf(stack));
    }
    
    public int getAIType() {
    	return ((this.entityData.get(aitype)).intValue());
    }
    public void setAIType(int stack) {
    	this.entityData.set(aitype, Integer.valueOf(stack));
    }
    public int getTargetType() {
    	return ((this.entityData.get(targettype)).intValue());
    }
    public void setTargetType(int stack) {
    	this.entityData.set(targettype, Integer.valueOf(stack));
    }
    public int getChange() {
    	return ((this.entityData.get(change)).intValue());
    }
    public void setChange(int stack) {
    	this.entityData.set(change, Integer.valueOf(stack));
    }
	
    public float getForwardMove() {
    	return ((this.entityData.get(forwardmove)).floatValue());
    }
    public void setForwardMove(float stack) {
    	this.entityData.set(forwardmove, Float.valueOf(stack));
    }
    public float getStrafingMove() {
    	return ((this.entityData.get(strafingmove)).floatValue());
    }
    public void setStrafingMove(float stack) {
    	this.entityData.set(strafingmove, Float.valueOf(stack));
    }
}