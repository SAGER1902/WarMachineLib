package advancearmy.entity.map;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.scores.Team;
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import advancearmy.event.SASoundEvent;
import advancearmy.item.ItemSpawn;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import wmlib.api.IHealthBar;
import wmlib.api.IBuilding;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.EntitySA_LandBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IEnemy;
import net.minecraft.server.level.ServerPlayer;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.EntitySA_HeliBase;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.item.ItemSpawn;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;
import net.minecraft.tags.ItemTags;
public class VehicleRespawn extends Mob implements ITool{
	public VehicleRespawn(EntityType<? extends VehicleRespawn> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public VehicleRespawn(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_VRES.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return VehicleRespawn.createMobAttributes();
    }
	
	public void checkDespawn() {
	}
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	public boolean canCollideWith(Entity entity) {
		return false;
	}
	public void push(Entity entity) {
		
	}
	private static final EntityDataAccessor<Integer> VehicleID = SynchedEntityData.<Integer>defineId(VehicleRespawn.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("VehicleID", this.getVehicleID());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setVehicleID(compound.getInt("VehicleID"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(VehicleID, Integer.valueOf(0));
	}
	public int getVehicleID() {
	return ((this.entityData.get(VehicleID)).intValue());
	}
	public void setVehicleID(int stack) {
	this.entityData.set(VehicleID, Integer.valueOf(stack));
	}
	
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(player.isCreative()){
			ItemStack heldItem = player.getItemInHand(hand);
			Item item = heldItem.getItem();
			if(!heldItem.isEmpty()){
				if(heldItem.is(ItemTags.PICKAXES)&&player.isCrouching()){
					if(!this.level().isClientSide){
						this.discard();
						player.sendSystemMessage(Component.translatable("Remove"));
						return InteractionResult.SUCCESS;
					}
				}
				if(item instanceof ItemSpawn){
					if(!this.level().isClientSide)this.setItemSlot(EquipmentSlot.MAINHAND, heldItem.copy());
					player.sendSystemMessage(Component.translatable("changed Summon Type"));
					return InteractionResult.SUCCESS;
				}
			}else{
				player.sendSystemMessage(Component.translatable("------"));
				player.sendSystemMessage(Component.translatable("Summon Check ID ="+this.getVehicleID()));
				player.sendSystemMessage(Component.translatable("======"));				
				if(player.isCrouching()){
					if(this.getVehicleID()>0)this.setVehicleID(this.getVehicleID()-1);
					return InteractionResult.SUCCESS;
				}else{
					this.setVehicleID(this.getVehicleID()+1);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.mobInteract(player, hand);
    }

	boolean isEnemyRespawn = false;
	
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int total_count = 200;
	public int max_summon = 20;
	public float cheacktime = 0;
	public float summontime = 0;
	public float cooltime6 = 0;
	public void aiStep() {
    	if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
    	{
			BlockPos blockpos = new BlockPos((int)(this.setx),(int)(this.sety - 1),(int)(this.setz));
			BlockState iblockstate = this.level().getBlockState(blockpos);
			if (this.setx != 0 && !iblockstate.isAir()){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
    	}
		if (this.isAlive()){
			if(cooltime6<50)++cooltime6;
			if (!(this.level() instanceof ServerLevel)) {
				//return false;
			} else {
			ServerLevel serverworld = (ServerLevel)this.level();
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY());
			int k = Mth.floor(this.getZ());
			if(cheacktime<200)++cheacktime;
			if(cheacktime>150){//
				int count = 0;
				int ve = 0;
				int i1 = i;
				int j1 = j + Mth.nextInt(this.random, 1, 2);
				int k1 = k;
				List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(200D, 150D, 200D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					{
						if(ent instanceof WeaponVehicleBase){
							WeaponVehicleBase unit = (WeaponVehicleBase)ent;
							if(unit.getHealth()>0 && unit.getArmyType2()==this.getVehicleID())++count;
						}
					}
				}
				if(count<1){
					++summontime;
					if(summontime>250+count){
						if(this.getMainHandItem()!=null){
							ItemStack this_heldItem = this.getMainHandItem();
							Item item = this_heldItem.getItem();
							if(item instanceof ItemSpawn){
								ItemSpawn vehicleitem = (ItemSpawn)item;
								vehicleitem.spawnCreature(serverworld, null, 0, true, (double)i1, (double)j1, (double)k1, this.getVehicleID());
							}
						}
						this.summontime = 0;
						this.cheacktime = 0;
					}
				}
			}
		}
      }
      super.aiStep();
   }
}
