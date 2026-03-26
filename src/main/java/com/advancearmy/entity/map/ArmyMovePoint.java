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

import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.EntityWMSeat;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.mob.EntityMobSoldierBase;
import advancearmy.event.SASoundEvent;
import advancearmy.item.ItemSpawn;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import wmlib.api.IHealthBar;
import wmlib.api.ITool;
import wmlib.api.IEnemy;
import net.minecraft.tags.ItemTags;
public class ArmyMovePoint extends Mob implements ITool{
	public ArmyMovePoint(EntityType<? extends ArmyMovePoint> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	}
	public ArmyMovePoint(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_MOVEP.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return ArmyMovePoint.createMobAttributes();
    }
	
	public void checkDespawn() {
	}
	private static final EntityDataAccessor<Integer> MoveId = SynchedEntityData.<Integer>defineId(ArmyMovePoint.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("MoveId", this.getMoveId());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setMoveId(compound.getInt("MoveId"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(MoveId, Integer.valueOf(0));
	}
	public int getMoveId() {
	return ((this.entityData.get(MoveId)).intValue());
	}
	public void setMoveId(int stack) {
	this.entityData.set(MoveId, Integer.valueOf(stack));
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
				}else
				if(!this.level().isClientSide){
					if(item == Items.GOLD_INGOT){
						if(isEnemyPoint){
							this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND));
							player.sendSystemMessage(Component.translatable("set Friend Type"));
							return InteractionResult.SUCCESS;
						}else{
							this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_INGOT));
							player.sendSystemMessage(Component.translatable("set Enemy Type"));
							return InteractionResult.SUCCESS;
						}
					}else if(item == Items.GOLDEN_SWORD){
						if(this.pointType==3){
							this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.IRON_SWORD));
							player.sendSystemMessage(Component.translatable("set Land Vehicle Type"));
							return InteractionResult.SUCCESS;
						}else if(this.pointType==2){
							this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.GOLDEN_SWORD));
							player.sendSystemMessage(Component.translatable("set Heli Type"));
							return InteractionResult.SUCCESS;
						}else{
							this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.DIAMOND_SWORD));
							player.sendSystemMessage(Component.translatable("set Plane Type"));
							return InteractionResult.SUCCESS;
						}
					}
				}else{
					return InteractionResult.PASS;
				}
			}else{
				if(player.isCrouching()){
					if(this.getMoveId()>0)this.setMoveId(this.getMoveId()-1);
					return InteractionResult.SUCCESS;
				}else{
					this.setMoveId(this.getMoveId()+1);
					return InteractionResult.SUCCESS;
				}
			}
			player.sendSystemMessage(Component.translatable("------"));
			if(isEnemyPoint){
				player.sendSystemMessage(Component.translatable("Enemy Type"));
			}else{
				player.sendSystemMessage(Component.translatable("Friend Type"));
			}
			player.sendSystemMessage(Component.translatable("Move ID ="+this.getMoveId()));
			player.sendSystemMessage(Component.translatable("======"));
		}
		return super.mobInteract(player, hand);
    }
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	public boolean canCollideWith(Entity entity) {
		return false;
	}
	public void push(Entity entity) {
		
	}
	
	public int pointType = 0;
	public int connectRange = 50;
	public boolean isEnemyPoint = false;
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
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
		
		if(this.getMainHandItem()!=null){
			ItemStack this_heldItem = this.getMainHandItem();
			Item item = this_heldItem.getItem();
			if(item == Items.DIAMOND)this.isEnemyPoint = false;
			if(item == Items.IRON_INGOT)this.isEnemyPoint = true;
		}
		if(this.getOffhandItem()!=null){
			ItemStack this_heldItem2 = this.getOffhandItem();
			Item item2 = this_heldItem2.getItem();
			if(item2 == Items.DIAMOND_SWORD){
				this.pointType = 3;
			}
			if(item2 == Items.IRON_SWORD){
				this.pointType = 2;
			}
			if(item2 == Items.GOLDEN_SWORD){
				this.pointType = 1;
				connectRange = 30;
			}
		}
		
		if (this.isAlive()){
			//this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
			if(cooltime6<50)++cooltime6;
			/*if (!(this.level() instanceof ServerLevel)) {
				//return false;
			} else */{
			//ServerLevel serverworld = (ServerLevel)this.level();
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY());
			int k = Mth.floor(this.getZ());
			if(summontime<100)++summontime;
			if(summontime>10){//
				int i1 = i + Mth.nextInt(this.random, 2, 4) * Mth.nextInt(this.random, -1, 1);
				int j1 = j + Mth.nextInt(this.random, 2, 3);
				int k1 = k + Mth.nextInt(this.random, 2, 4) * Mth.nextInt(this.random, -1, 1);
				List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(connectRange, connectRange*2F, connectRange));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent!=null && ent.getVehicle()==null && !(ent instanceof EntityWMSeat)){
						if(isEnemyPoint){
							if(this.pointType ==0 && ent instanceof EntityMobSoldierBase){
								EntityMobSoldierBase unit = (EntityMobSoldierBase)ent;
								if(this.distanceTo(unit)<10 && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
									unit.setMoveType(1);
									unit.setMovePosY(unit.getMovePosY()+1);
								}
								if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
									//unit.getNavigation().moveTo(i1, j1, k1, 1.5F);
									unit.setMovePosX(i1);
									unit.setMovePosZ(k1);
									unit.setMoveType(4);
								}
							}
							if(ent instanceof WeaponVehicleBase){
								WeaponVehicleBase unit = (WeaponVehicleBase)ent;
								if(unit.getTargetType()==2 && unit.getTarget()==null && 
								(this.pointType ==1 && unit.VehicleType <3||this.pointType ==2 && unit.VehicleType == 3||this.pointType ==3 && unit.VehicleType == 4)){
									if(this.distanceTo(unit)<8 && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
										unit.setMoveType(1);
										unit.setMoveMode(unit.getMoveMode()+1);
										break;
									}
									if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
										unit.setMoveType(4);
										unit.setMovePosX(i1);
										//unit.setMoveId(j1);
										unit.setMovePosZ(k1);
										break;
									}
								}
							}
						}else{
							if(this.pointType ==0 && ent instanceof EntitySA_SoldierBase){
								EntitySA_SoldierBase unit = (EntitySA_SoldierBase)ent;
								if(this.distanceTo(unit)<10 && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
									unit.setMoveType(1);
									unit.setMovePosY(unit.getMovePosY()+1);
								}
								if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
									//unit.getNavigation().moveTo(i1, j1, k1, 1.5F);
									unit.setMovePosX(i1);
									unit.setMovePosZ(k1);
									unit.setMoveType(4);
								}
							}
							if(ent instanceof WeaponVehicleBase){
								WeaponVehicleBase unit = (WeaponVehicleBase)ent;
								if(unit.getTargetType()==3 && unit.getTarget()==null && 
								(this.pointType ==1 && unit.VehicleType <3||this.pointType ==2 && unit.VehicleType == 3||this.pointType ==3 && unit.VehicleType == 4)){
									if(this.distanceTo(unit)<8 && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
										unit.setMoveType(1);
										unit.setMoveMode(unit.getMoveMode()+1);
										break;
									}
									if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
										unit.setMoveType(4);
										unit.setMovePosX(i1);
										//unit.setMoveId(j1);
										unit.setMovePosZ(k1);
										break;
									}
								}
							}
						}
					}
				}
				this.summontime = 0;
			}
		}
      }
      super.aiStep();
   }
}
