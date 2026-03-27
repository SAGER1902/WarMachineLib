package wmlib.common.living;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.CreatureEntity;
import wmlib.api.ITool;
public abstract class EntityWMSeat extends MobEntity implements ITool{
	public EntityWMSeat(EntityType<? extends EntityWMSeat> sodier, World worldIn) {
		super(sodier, worldIn);
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
		   if(list.get(0) instanceof PlayerEntity){
			   return (PlayerEntity)list.get(0);
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
		   if(list.get(0) instanceof CreatureEntity && !(list.get(0) instanceof PlayerEntity)){
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
			if(entity!=null)vehicle.hurt(DamageSource.thrown(entity, entity), par2);
		}
		return false;
    }
	protected void registerGoals() {}
	public void checkDespawn() {}

    private static final DataParameter<Integer> remain2 = 
    		EntityDataManager.<Integer>defineId(EntityWMSeat.class, DataSerializers.INT);
    private static final DataParameter<Integer> remain1 = 
    		EntityDataManager.<Integer>defineId(EntityWMSeat.class, DataSerializers.INT);
    private static final DataParameter<Integer> drivertype = 
    		EntityDataManager.<Integer>defineId(EntityWMSeat.class, DataSerializers.INT);
	
	public boolean isZoom = false;
	public SoundEvent lockTargetSound = SoundEvents.STONE_BUTTON_CLICK_OFF;
	public float seatView1X = 0F;
	public float seatView1Y = 0F;
	public float seatView1Z = 0F;
	public float seatView3X = 0F;//thirdview
	public float seatView3Y = -1F;//thirdview
	public float seatView3Z = -2F;//thirdview
	public boolean stand = false;
	public boolean gunner_aim = false;
	public boolean canNightV = false;
	public boolean openNightV = false;
	public boolean seatCanZoom = true;
	public float seatZoomFov = 2.0F;
	
	public int weaponCount=0;
	public boolean is_aa = false;
	public boolean canlock = false;
	public boolean render_hud_box = false;
	public String hud_box_obj = "wmlib:textures/hud/box.obj";
	public String hud_box_tex = "wmlib:textures/hud/box.png";
	public boolean renderHudIcon = false;
	public String hudIcon = "wmlib:textures/hud/cross.png";
	
	public boolean renderHudOverlay = false;
	public String hudOverlay = "wmlib:textures/misc/scope.png";
	public boolean renderHudOverlayZoom = false;
	public String hudOverlayZoom = "wmlib:textures/misc/scope.png";
	
	public boolean renderHud = false;
	public boolean renderRader = true;
	public String w1name = "WEAPON1";
	public String w2name = "WEAPON2";
	public float seatProtect = 0.25F;
	
	public float turretYaw;
	public float turretPitch;
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
	public LivingEntity targetentity = null;

	public void setFire(int power){}
	public float boxheight = 0;
	public float boxwidth = 0;
	/*public EntitySize getDimensions(Pose pos) {
		EntitySize entitysize = super.getDimensions(pos);
		if(this.boxwidth!=0&&this.boxheight!=0){
			return entitysize.scale(boxwidth,boxheight);
		}else{
			return entitysize;
		}
	}
	private void updateSizeInfo() {
	  this.refreshDimensions();
	}
	public void onSyncedDataUpdated(DataParameter<?> nbt) {
		if(this.boxwidth!=0&&this.boxheight!=0){
			this.updateSizeInfo();
		}
		super.onSyncedDataUpdated(nbt);
	}*/
	public int lockcool = 0;
	public int locktime = 0;
	public Entity mitarget = null;
	public float turretSpeed = 1F;
	public float deltaRotation = 0;
	public int tracktick = 0;
	public int find_time = 0;	 
	int clear_time = 0;
	public void tick() {
		super.tick();
		if(this.lockcool<20)++lockcool;
		if(this.locktime>0)--locktime;
		if(this.locktime==1){
			if(!this.level.isClientSide)this.mitarget=null;
		}
		if(cooltime < 200)++cooltime;
		if(cooltime2 < 200)++cooltime2;

		if(gun_count1 < 100)++gun_count1;
		if(gun_count2 < 100)++gun_count2;
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
	
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		{
			if(weaponCount>1)compound.putInt("remain2", getRemain2());
			if(weaponCount>0)compound.putInt("remain1", getRemain1());
			compound.putInt("drivertype", getTargetType());
		}
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		super.readAdditionalSaveData(compound);
		{
			if(weaponCount>1)this.setRemain2(compound.getInt("remain2"));
			if(weaponCount>0)this.setRemain1(compound.getInt("remain1"));
			this.setTargetType(compound.getInt("drivertype"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
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