package wmlib.common.living;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;

import net.minecraft.item.Items;
import net.minecraft.block.Blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.block.material.Material;

import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;

import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.AgeableEntity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.AgeableEntity;
import java.util.UUID;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.NonNullList;
import net.minecraft.nbt.ListNBT;
public abstract class EntityWMVehicleBase extends TameableEntity{
	public EntityWMVehicleBase(EntityType<? extends EntityWMVehicleBase> sodier, World worldIn) {
		super(sodier, worldIn);
	}

	protected void registerGoals() {
	}

	public EntityWMVehicleBase getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
	  return null;
	}
	
	private final NonNullList<ItemStack> weaponItems = NonNullList.withSize(2, ItemStack.EMPTY);
    private static final DataParameter<Integer> remain_2 = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> remain_1 = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> remain_3 = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> remain_4 = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
	//private static final DataParameter<Boolean> isattack = EntityDataManager.<Boolean>defineId(EntityWMVehicleBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> choose = EntityDataManager.<Boolean>defineId(EntityWMVehicleBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> armytype2 = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> armytype1 = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Float> moveyaw = EntityDataManager.<Float>defineId(EntityWMVehicleBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> movepitch = EntityDataManager.<Float>defineId(EntityWMVehicleBase.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> moveforward = EntityDataManager.<Float>defineId(EntityWMVehicleBase.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> movestrafing = EntityDataManager.<Float>defineId(EntityWMVehicleBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> movemode = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> aitype = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> targettype = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> change = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> movetype = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> moveposx = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> moveposy = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> moveposz = EntityDataManager.<Integer>defineId(EntityWMVehicleBase.class, DataSerializers.INT);
	
	public float w1recoilp = 1;
	public float w1recoilr = 1;
	public float w1barrelsize = 1F;
	
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
	
	public int enc_soul = 0;
	public int enc_armor = 0;
	public int enc_heal = 0;
	public int enc_power = 0;
	public int enc_protect = 0;
	public int enc_control = 0;
	public boolean can_follow = false;
	public float seatProtect = 0.25F;
	
	public boolean isZoom = false;
	public boolean seatNoThird = false;
	public float seatView1X = 0F;
	public float seatView1Y = 0F;
	public float seatView1Z = 0F;
	public float seatView3X = 0F;
	public float seatView3Y = -1F;
	public float seatView3Z = -2F;
	
	public boolean seatCanZoom = true;
	public float seatZoomFov = 2.0F;
	public boolean render_hud_box = false;
	public String hud_box_obj = "wmlib:textures/hud/box.obj";
	public String hud_box_tex = "wmlib:textures/hud/box.png";
	
	public String w1icon="wmlib:textures/hud/weapon1.png";
	public String w2icon="wmlib:textures/hud/weapon2.png";
	public String w3icon="wmlib:textures/hud/weapon3.png";
	public String w4icon="wmlib:textures/hud/weapon4.png";
	
	public int weaponCount=1;
	public boolean renderHudIcon = false;
	public String hudIcon = "wmlib:textures/hud/cross.png";

	public boolean renderHudOverlay = false;
	public String hudOverlay = "wmlib:textures/misc/scope.png";
	
	public boolean renderHudOverlayZoom = false;
	public String hudOverlayZoom = "wmlib:textures/misc/scope.png";
	
	
	public boolean renderHud = false;
	public boolean renderRader = true;
	
	public float targetYaw = 0;
	
	public float turretYaw;
	
	public float turretYawO;
	public float turretPitchO;
	
	public float turretYaw_1;
	public float turretYaw_2;
	public float turretPitch;
	public float turretPitch_1;
	public float turretPitch_2;

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
	public double movePower;
	
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
	
	
	public float flyPitchMax = 30;
	public float flyPitchMin = -20;
	
	public int VehicleType = 0;
	
	public boolean[] seatCanFire = new boolean[16];
	public boolean[] seatHide = new boolean[16];
	public boolean[] seatTurret = new boolean[16];
	
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

	public LivingEntity targetentity = null;
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
	public void checkDespawn() {}
	
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
				if(this.getControllingPassenger()!=null && entity == this.getControllingPassenger()||entity.getVehicle()==this||
				this.getOwner()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof TameableEntity){
						TameableEntity soldier = (TameableEntity)entity;
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
	public Entity getControllingPassenger() {//主驾驶座位上面的玩家
		List<Entity> list = this.getPassengers();
		if(list.isEmpty()){
		   return null;
		}else{
			if(list.get(0) instanceof EntityWMSeat){
				EntityWMSeat seat = (EntityWMSeat)list.get(0);
				return seat.getControllingPassenger();
			}else{
				return null;
			}
		}
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
	
	public boolean canNightV = false;
	public float airzoom_x = 2F;
	public float airzoom_y = -3F;
	public float airzoom_z = -3F;
	
	public float heligun_x = 0F;
	public float heligun_y = -2.5F;
	public float heligun_z = 3.5F;
	
	
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
	public float deltaRotation = 0;
	public int tracktick = 0;
	public int find_time = 0;	 
	int seat_time = 0;
	
	public int attack_time = 0;
	public void tick() {
		super.tick();
		speedKPH = getSpeedKPH(this);
		if(seat_time<20)++seat_time;
		if(seat_time<18){
			this.setSeat = true;
		}else{
			if(this.setSeat)this.setSeat = false;
		}
		
		if(this.getHealth()>0){
			if(tracktick<360){
				++tracktick;
			}else{
				tracktick = 0;
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
			if(this.isOnGround()){
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
	public ItemStack getWeaponItem(EquipmentSlotType eq) {
		switch(eq.getType()) {
		case HAND:
			return this.weaponItems.get(eq.getIndex());
		default:
			return ItemStack.EMPTY;
		}
	}

	public void setWeaponItem(EquipmentSlotType eq, ItemStack stack) {
		switch(eq.getType()) {
		case HAND:
			this.weaponItems.set(eq.getIndex(), stack);
		break;
		}
	}
	
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		{
			ListNBT listnbt1 = new ListNBT();
			for(ItemStack itemstack1 : this.weaponItems) {
				CompoundNBT compoundnbt1 = new CompoundNBT();
				if (!itemstack1.isEmpty()) {
					itemstack1.save(compoundnbt1);
				}
				listnbt1.add(compoundnbt1);
			}
			compound.put("weaponitems", listnbt1);
			
			compound.putInt("movetype", this.getMoveType());
			compound.putInt("moveposx", this.getMovePosX());
			compound.putInt("moveposy", this.getMovePosY());
			compound.putInt("moveposz", this.getMovePosZ());
			
			if(weaponCount>0)compound.putInt("remain_2", this.getRemain2());
			if(weaponCount>1)compound.putInt("remain_1", this.getRemain1());
			if(weaponCount>2)compound.putInt("remain_3", this.getRemain3());
			if(weaponCount>3)compound.putInt("remain_4", this.getRemain4());
			
			compound.putInt("armytype2", this.getArmyType2());//
			compound.putInt("armytype1", this.getArmyType1());//

			compound.putInt("movemode", this.getMoveMode());//
			compound.putInt("aitype", this.getAIType());
			compound.putInt("targettype", this.getTargetType());
			compound.putInt("change", this.getChange());
			
			//compound.putBoolean("isattack", this.isAttacking());
			compound.putBoolean("choose", this.getChoose());
			compound.putFloat("moveforward", this.getForwardMove());
			compound.putFloat("movestrafing", this.getStrafingMove());
			compound.putFloat("moveyaw", this.getMoveYaw());//
			compound.putFloat("movepitch", this.getMovePitch());//
		}
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			if (compound.contains("weaponitems", 9)) {
				ListNBT listnbt1 = compound.getList("weaponitems", 10);
				for(int j = 0; j < this.weaponItems.size(); ++j) {
					this.weaponItems.set(j, ItemStack.of(listnbt1.getCompound(j)));
				}
			}
			this.setMoveType(compound.getInt("movetype"));
			this.setMovePosX(compound.getInt("moveposx"));
			this.setMovePosY(compound.getInt("moveposy"));
			this.setMovePosZ(compound.getInt("moveposz"));
			
			if(weaponCount>0)this.setRemain2(compound.getInt("remain_2"));
			if(weaponCount>1)this.setRemain1(compound.getInt("remain_1"));
			if(weaponCount>2)this.setRemain3(compound.getInt("remain_3"));
			if(weaponCount>3)this.setRemain4(compound.getInt("remain_4"));
			
			this.setArmyType2(compound.getInt("armytype2"));
			this.setArmyType1(compound.getInt("armytype1"));

			//this.setAttacking(compound.getBoolean("isattack"));
			this.setChoose(compound.getBoolean("choose"));
			this.setMoveMode(compound.getInt("movemode"));
			this.setAIType(compound.getInt("aitype"));
			this.setTargetType(compound.getInt("targettype"));
			this.setChange(compound.getInt("change"));
			this.setForwardMove(compound.getFloat("moveforward"));
			this.setStrafingMove(compound.getFloat("movestrafing"));
			this.setMoveYaw(compound.getFloat("moveyaw"));
			this.setMovePitch(compound.getFloat("movepitch"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(movetype, Integer.valueOf(1));
		this.entityData.define(moveposx, Integer.valueOf(0));
		this.entityData.define(moveposy, Integer.valueOf(0));
		this.entityData.define(moveposz, Integer.valueOf(0));
		
		this.entityData.define(remain_2, Integer.valueOf(0));
		this.entityData.define(remain_1, Integer.valueOf(0));
		this.entityData.define(remain_3, Integer.valueOf(0));
		this.entityData.define(remain_4, Integer.valueOf(0));
		//this.entityData.define(isattack, Boolean.valueOf(false));
		this.entityData.define(choose, Boolean.valueOf(false));
		this.entityData.define(armytype2, Integer.valueOf(0));
        this.entityData.define(armytype1, Integer.valueOf(0));

		this.entityData.define(movemode, Integer.valueOf(0));
        this.entityData.define(aitype, Integer.valueOf(0));
        this.entityData.define(targettype, Integer.valueOf(0));
        this.entityData.define(change, Integer.valueOf(0));
		this.entityData.define(moveforward, Float.valueOf(0F));
        this.entityData.define(movestrafing, Float.valueOf(0F));
        this.entityData.define(moveyaw, Float.valueOf(0));
        this.entityData.define(movepitch, Float.valueOf(0));
	}
	
	public int getMoveType() {
			return ((this.entityData.get(movetype)).intValue());
	}
	public void setMoveType(int stack) {
		this.entityData.set(movetype, Integer.valueOf(stack));
	}
	public int getMovePosX() {
		return ((this.entityData.get(moveposx)).intValue());
	}
	public void setMovePosX(int stack) {
	this.entityData.set(moveposx, Integer.valueOf(stack));
	}
	public int getMovePosY() {
	return ((this.entityData.get(moveposy)).intValue());
	}
	public void setMovePosY(int stack) {
	this.entityData.set(moveposy, Integer.valueOf(stack));
	}
	public int getMovePosZ() {
	return ((this.entityData.get(moveposz)).intValue());
	}
	public void setMovePosZ(int stack) {
	this.entityData.set(moveposz, Integer.valueOf(stack));
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
    	return ((this.entityData.get(remain_2)).intValue());
    }
    public void setRemain2(int stack) {
    	this.entityData.set(remain_2, Integer.valueOf(stack));
    }
    public int getRemain1() {
    	return ((this.entityData.get(remain_1)).intValue());
    }
    public void setRemain1(int stack) {
    	this.entityData.set(remain_1, Integer.valueOf(stack));
    }

    public int getRemain3() {
    	return ((this.entityData.get(remain_3)).intValue());
    }
    public void setRemain3(int stack) {
    	this.entityData.set(remain_3, Integer.valueOf(stack));
    }
    public int getRemain4() {
    	return ((this.entityData.get(remain_4)).intValue());
    }
    public void setRemain4(int stack) {
    	this.entityData.set(remain_4, Integer.valueOf(stack));
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
    	return ((this.entityData.get(moveforward)).floatValue());
    }
    public void setForwardMove(float stack) {
    	this.entityData.set(moveforward, Float.valueOf(stack));
    }
    public float getStrafingMove() {
    	return ((this.entityData.get(movestrafing)).floatValue());
    }
    public void setStrafingMove(float stack) {
    	this.entityData.set(movestrafing, Float.valueOf(stack));
    }
}