package wmlib.common.living;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;        
import net.minecraft.sounds.SoundEvents;  
import net.minecraft.network.chat.Component;    // TranslationTextComponent  Component     
import net.minecraft.network.protocol.Packet;   // IPacket  Packet
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
// NBT
import net.minecraft.nbt.CompoundTag;// CompoundNBT  CompoundTag
import net.minecraft.nbt.ListTag;    // ListNBT  ListTag
import net.minecraft.nbt.NbtUtils;   // NBTUtil  NbtUtils
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;   // BlockPos
import net.minecraft.util.Mth;        // MathHelper  Mth
import net.minecraft.world.phys.Vec3; // Vector3d  Vec3
import net.minecraft.server.level.ServerLevel;  // ServerWorld  ServerLevel
import net.minecraft.world.level.Level;// World  Level
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.damagesource.DamageSource;    
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;   
import net.minecraft.world.InteractionHand;     // Hand  InteractionHand
import net.minecraft.world.entity.ai.attributes.AttributeSupplier; // AttributeModifierMap  AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;// PlayerEntity  Player
import net.minecraft.world.entity.monster.Enemy;// IMob  Enemy
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.gameevent.GameEvent;

import net.minecraft.world.entity.PathfinderMob;// CreatureEntity  PathfinderMob
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;// TameableEntity  TamableAnimal
import net.minecraft.world.entity.Mob;         // MobEntity  Mob
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;       // EntitySize  EntityDimensions
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.AgeableMob;   // AgeableEntity  AgeableMob
import net.minecraft.world.entity.EquipmentSlot;// EquipmentSlotType  EquipmentSlot
import net.minecraft.world.scores.Team;
// Forge 
import net.minecraftforge.network.NetworkEvent; 
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import wmlib.common.living.ai.EntitySearchTarget;
import wmlib.api.ITool;
public abstract class EntityWMSeat extends Entity implements ITool{
	public EntityWMSeat(EntityType<? extends EntityWMSeat> sodier, Level worldIn) {
		super(sodier, worldIn);
		//this.sensing = new Sensing(this);
	}
	public Team getTeam() {
		if (this.getAnyPassenger()!=null) {
			LivingEntity livingentity = this.getAnyPassenger();
			if (livingentity != null) {
				return livingentity.getTeam();
			}
		}
		return super.getTeam();
	}
	@Nullable
	public LivingEntity getControllingPassenger() {//坐上面的玩家 用于主驾驶位玩家驾驶
	   List<Entity> list = this.getPassengers();
	   if(list.isEmpty()){
		   return null;
	   }else{
		   if(list.get(0) instanceof Player){
			   return (Player)list.get(0);
		   }else{
			   return null;
		   }
	   }
	}
	@Nullable
	public LivingEntity getAnyPassenger() {//坐上面的任意实体 用于获取队伍/伤害来源者
	   List<Entity> list = this.getPassengers();
	   if(list.isEmpty()){
			return null;
	   }else{
		   if(list.get(0) instanceof LivingEntity){
			   return (LivingEntity)list.get(0);
		   }else{
			   return null;
		   }
	   }
	}
	@Nullable
	public Entity getNpcPassenger() {//坐上面的NPC 用于副座位NPC乘客攻击
	   List<Entity> list = this.getPassengers();
	   if(list.isEmpty()){
		   return null;
	   }else{
		   if(list.get(0) instanceof PathfinderMob && !(list.get(0) instanceof Player)){
			   return list.get(0);
		   }else{
			   return null;
		   }
	   }
	}
	
	public boolean rideableUnderWater() {
	  return true;
	}
	public boolean canBeSteered() {
		return true;
	}
	
	public boolean hurt(DamageSource source, float par2)
    {
		if(this.getVehicle()!=null){
			Entity entity;
			entity = source.getEntity();
			Entity vehicle = this.getVehicle();
			if(entity!=null)vehicle.hurt(this.damageSources().thrown(entity, entity), par2);
		}
		return false;
    }
	
	protected void registerGoals() {}
	public void checkDespawn() {}
	
    private static final EntityDataAccessor<Integer> remain2 = 
    		SynchedEntityData.<Integer>defineId(EntityWMSeat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> remain1 = 
    		SynchedEntityData.<Integer>defineId(EntityWMSeat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> drivertype = 
    		SynchedEntityData.<Integer>defineId(EntityWMSeat.class, EntityDataSerializers.INT);
	
	public boolean isZoom = false;
	public boolean stand = false;
	public boolean gunner_aim = false;
	public boolean canNightV = false;
	public boolean openNightV = false;
	public SoundEvent lockTargetSound = SoundEvents.STONE_BUTTON_CLICK_OFF;
	public float seatView1X = 0F;
	public float seatView1Y = 0F;
	public float seatView1Z = 0F;
	public float seatView3X = 0F;//thirdview
	public float seatView3Y = -1F;//thirdview
	public float seatView3Z = -2F;//thirdview
	
	
	public boolean seatCanZoom = true;
	public float seatZoomFov = 2.0F;
	public float ridding_zoom_first = 2.0F;
	
	public int weaponCount=0;
	public boolean is_aa = false;
	public boolean canlock = false;
	public boolean render_hud_box = false;
	public String hud_box_obj = "wmlib:textures/hud/box.obj";
	public String hud_box_tex = "wmlib:textures/hud/box.png";
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
	public String w1name = "WEAPON1";
	public String w2name = "WEAPON2";
	public float seatProtect = 0.25F;
	
	public float turretYaw;
	public float turretYaw1;
	public float turretPitch;
	public float turretPitch1;
	public float turretYawO;
	public float turretPitchO;
	public float turretYawMove;
	public float turretPitchMove;
	
	public float minyaw = -360F;
	public float maxyaw = 360F;
	public float turretPitchMax = -90;
	public float turretPitchMin = 90;
	
	public float attack_range_max = 40;
	public float attack_range_min = 0;
	public float attack_height_max = 20;
	public float attack_height_min = -20;

	public boolean seatCanFire = false;
	public boolean seatHide = false;
	public boolean seatTurret = false;
	
	public String w1icon="wmlib:textures/hud/weapon1.png";
	public String w2icon="wmlib:textures/hud/weapon2.png";
	public boolean showhelp = false;
	public boolean keyc = false;
	public boolean keyf = false;
	public boolean keyr = false;
	public boolean keyg = false;
	public boolean keyx = false;
	public boolean keyv = false;
	public boolean keylook = false;
	public boolean fire1 = false;
	public boolean fire2 = false;
	public boolean fire3 = false;
	public boolean keyj = false;
	public boolean keyk = false;
	
	public boolean keyrun = false;
	
	public int ammo1;
	public int ammo2;
	public int ammo3;
	public int ammo4;
	public int ammo5;
	public int cooltime;
	public int cooltime2;
	public int cooltime3;
	public int cooltime4;
	public int cooltime5;
	public int cooltime6;
	public int magazine;
	public int magazine2;
	public int magazine3;
	public int magazine4;
	public boolean counter1 = false;
	public boolean counter2 = false;
	public boolean counter3 = false;
	public boolean counter4 = false;

	public int gun_count1 = 0;
	public int gun_count2 = 0;
	public int gun_count3 = 0;
	public int gun_count4 = 0;
	public int gun_count5 = 0;
	public int gun_count6 = 0;
	public int reload1 = 0;
	public int reload2 = 0;
	public int reload3 = 0;
	public int reload4 = 0;
	public int reload5 = 0;
	public int reload6 = 0;
	public int reload_time1;
	public int reload_time2;
	public int reload_time3;
	public int reload_time4;

	public int w1cycle;
	public int w2cycle;
	public int w3cycle;
	public int w4cycle;
	public int w5cycle;
	
	public int w1barst;
	public int w2barst;
	public int w3barst;
	public int w4barst;
	public int w5barst;

	public boolean firetrue = false;
	
	public void setFire(int power){}

	public float boxheight = 0;
	public float boxwidth = 0;

	public int lockcool = 0;
	public int locktime = 0;
	public Entity mitarget = null;
	public float turretSpeed = 1F;
	public float deltaRotation = 0;
	public int tracktick = 0;
	public int find_time = 0;	 
	int clear_time = 0;

    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        return this.mobInteract(player, hand);
        //return InteractionResult.PASS;
    }
	@Override
    public boolean isPickable() {
        return !this.isRemoved();
    }
	public LivingEntity lockTarget = null;
    public boolean isAttackable() {
        return false;
    }
    @Nullable
    public LivingEntity getTarget() {
        return this.lockTarget;
    }
    public void setTarget(@Nullable LivingEntity target) {
        this.lockTarget = target;
    }
    public float yHeadRot;
    public float yHeadRotO;
    @Override
    public float getYHeadRot() {
        return this.yHeadRot;
    }
    @Override
    public void setYHeadRot(float yHeadRot) {
        this.yHeadRot = yHeadRot;
    }
	
	protected double lerpXRot;
    protected double lyHeadRot;
	protected int lerpSteps;
    protected int lerpHeadSteps;
    @Override
    public void lerpHeadTo(float yaw, int pitch) {
        this.lyHeadRot = yaw;
        this.lerpHeadSteps = pitch;
    }
    /*@Override
    public float getViewYRot(float partialTick) {
        if (partialTick == 1.0f) {
            return this.yHeadRot;
        }
        return Mth.lerp(partialTick, this.yHeadRotO, this.yHeadRot);
    }
    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        this.yHeadRot = packet.getYHeadRot();
        this.yHeadRotO = this.yHeadRot;
    }*/
	protected boolean dead = false;
    public float getHealth() {
        return 1/*this.entityData.get(DATA_HEALTH_ID).floatValue()*/;
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
	
	private int serchCool = 0;
    private final IntSet seen = new IntOpenHashSet();
    private final IntSet unseen = new IntOpenHashSet();
	public void targetSerch(){
		if (--serchCool <= 0) {
			serchCool = 4;
			seen.clear();
			unseen.clear();
			EntitySearchTarget.getlockTarget(this, this.attack_range_max, this.attack_height_max, -this.attack_height_min);
		}
	}
	
	public boolean hasLineOfSightTo(Entity target) {
		// 服务端执行，客户端直接返回 true（或根据你的需求处理）
		if (this.level().isClientSide) {
			return true;
		}
		if (target.level() != this.level()) {
			return false;
		}
		int id = target.getId();
		if (seen.contains(id)) {
			return true;
		}
		if (unseen.contains(id)) {
			return false;
		}
		boolean result = performLineOfSightCheck(target);// 缓存未命中，执行实际检测
		if (result) {// 存储结果到缓存
			seen.add(id);
		} else {
			unseen.add(id);
		}
		return result;
	}
	private boolean performLineOfSightCheck(Entity target) {
		// 获取源实体眼睛位置（Entity 默认眼睛高度为 1.62，但子类可覆写 getEyeHeight）
		double eyeY = this.getY() + this.getEyeHeight();
		Vec3 eyePos = new Vec3(this.getX(), eyeY, this.getZ());
		// 获取目标眼睛位置
		double targetEyeY = target.getY() + target.getEyeHeight();
		Vec3 targetPos = new Vec3(target.getX(), targetEyeY, target.getZ());
		// 可选：距离预检，避免超长距离射线（可自定义阈值，如 128 格）
		double maxDistSq = 128.0 * 128.0; // 与原版一致，也可作为参数传入
		if (eyePos.distanceToSqr(targetPos) > maxDistSq) {
			return false;
		}
		ClipContext context = new ClipContext(
				eyePos,
				targetPos,
				ClipContext.Block.COLLIDER,   // 所有碰撞箱方块都阻挡
				ClipContext.Fluid.NONE,        // 流体不阻挡（可根据需要修改）
				this                           // 忽略源实体自身
		);
		BlockHitResult hitResult = this.level().clip(context);
		// 如果射线没有击中任何方块，或者击中点几乎等于目标位置，认为有视线
		return hitResult.getType() == HitResult.Type.MISS ||
			   hitResult.getLocation().distanceToSqr(targetPos) < 1.0;
	}
	
	public void tick() {
		super.tick();
		if(this.dead)this.discard();
		
		
		
		
		if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            //this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }
		if (this.lerpSteps > 0) {
			this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
			--this.lerpSteps;
		}
        if (this.lerpHeadSteps > 0) {
            this.yHeadRot += (float)Mth.wrapDegrees(this.lyHeadRot - (double)this.yHeadRot) / (float)this.lerpHeadSteps;
            --this.lerpHeadSteps;
        }
		
        while (this.yHeadRot - this.yHeadRotO < -180.0f) {
            this.yHeadRotO -= 360.0f;
        }
        while (this.yHeadRot - this.yHeadRotO >= 180.0f) {
            this.yHeadRotO += 360.0f;
        }
		this.yHeadRotO = this.yHeadRot;
		
		if(this.lockcool<20)++lockcool;
		if(this.locktime>0)--locktime;
		if(this.locktime==1){
			if(!this.level().isClientSide)this.mitarget=null;
		}
		if(cooltime < 200)++cooltime;
		if(cooltime2 < 200)++cooltime2;
		
		if(gun_count1 < 100)++gun_count1;
		if(gun_count2 < 100)++gun_count2;
		while(this.turretYaw - this.turretYawO < -180.0F) {
			this.turretYawO -= 360.0F;
		}
		while(this.turretPitch - this.turretPitchO >= 180.0F) {
			this.turretPitchO += 360.0F;
		}
		
		if(this.turretYaw>360F||this.turretYaw<-360F){
			this.turretYaw = 0;
			this.turretYawMove = 0;
			this.setRemain2(0);
		}
		if(this.getYHeadRot()>360F||this.getYHeadRot()<-360F){
			this.setYHeadRot(0);
		}
		
		this.turretYawO = this.turretYaw;
		this.turretPitchO = this.turretPitch;
	}
	
    public boolean CanAttack(Entity entity){
    	return false;
    }
	public boolean canDrive() {
		return this.getPassengers().size() < 1;
	}
	
	public void addAdditionalSaveData(CompoundTag compound)
	{
		//super.addAdditionalSaveData(compound);
		{
			if(weaponCount>1)compound.putInt("remain2", getRemain2());
			if(weaponCount>0)compound.putInt("remain1", getRemain1());
			compound.putInt("drivertype", getTargetType());
		}
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   //super.readAdditionalSaveData(compound);
		{
			if(weaponCount>1)this.setRemain2(compound.getInt("remain2"));
			if(weaponCount>0)this.setRemain1(compound.getInt("remain1"));
			this.setTargetType(compound.getInt("drivertype"));
		}
	}
	protected void defineSynchedData()
	{
		//super.defineSynchedData();
		{
			this.entityData.define(remain2, Integer.valueOf(0));
			this.entityData.define(remain1, Integer.valueOf(0));
			this.entityData.define(drivertype, Integer.valueOf(0));
		}
	}
	
    public int getTargetType() {
    	return ((this.entityData.get(drivertype)).intValue());
    }
    public void setTargetType(int stack) {
    	this.entityData.set(drivertype, Integer.valueOf(stack));
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
}