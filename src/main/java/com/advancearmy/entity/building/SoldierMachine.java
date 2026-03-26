package advancearmy.entity.building;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import advancearmy.item.ItemSpawn;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import advancearmy.init.ModItems;
import wmlib.api.IHealthBar;
import wmlib.api.IBuilding;
import wmlib.api.IEnemy;

import net.minecraft.tags.ItemTags;
public class SoldierMachine extends TamableAnimal implements IHealthBar, IBuilding{
	public SoldierMachine(EntityType<? extends SoldierMachine> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public SoldierMachine(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_SMAC.get(), worldIn);
	}
	public void checkDespawn() {
	}
	public static AttributeSupplier.Builder createAttributes() {
        return SoldierMachine.createMobAttributes();
    }
	public SoldierMachine getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
	  return null;
	}
   
	private static final EntityDataAccessor<Integer> VehicleC = SynchedEntityData.<Integer>defineId(SoldierMachine.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> Money = SynchedEntityData.<Integer>defineId(SoldierMachine.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> Build = SynchedEntityData.<Integer>defineId(SoldierMachine.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> YawRote = SynchedEntityData.<Integer>defineId(SoldierMachine.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("VehicleC", this.getVehicleC());
		compound.putInt("Money", this.getMoney());
		compound.putInt("Build", this.getBuild());
		compound.putInt("YawRote", this.getYawRoteNBT());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setVehicleC(compound.getInt("VehicleC"));
	   this.setMoney(compound.getInt("Money"));
	   this.setBuild(compound.getInt("Build"));
	   this.setYawRoteNBT(compound.getInt("YawRote"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(VehicleC, Integer.valueOf(0));
		this.entityData.define(Money, Integer.valueOf(0));
		this.entityData.define(Build, Integer.valueOf(0));
		this.entityData.define(YawRote, Integer.valueOf(0));
	}
	public int getYawRoteNBT() {
		return ((this.entityData.get(YawRote)).intValue());
	}
	public void setYawRoteNBT(int stack) {
		this.entityData.set(YawRote, Integer.valueOf(stack));
	}
	
	public int getVehicleC() {
	return ((this.entityData.get(VehicleC)).intValue());
	}
	public void setVehicleC(int stack) {
	this.entityData.set(VehicleC, Integer.valueOf(stack));
	}
	public int getMoney() {
	return ((this.entityData.get(Money)).intValue());
	}
	public void setMoney(int stack) {
	this.entityData.set(Money, Integer.valueOf(stack));
	}
	public int getBuild() {
	return ((this.entityData.get(Build)).intValue());
	}
	public void setBuild(int stack) {
	this.entityData.set(Build, Integer.valueOf(stack));
	}
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(player.isCreative()||this.getOwner()==player){
			ItemStack heldItem = player.getItemInHand(hand);
			ItemStack this_heldItem = this.getMainHandItem();
			Item item = heldItem.getItem();
			{
				if(heldItem.is(ItemTags.PICKAXES)){
					if(player.isCrouching()){
						if(!this.level().isClientSide && this.getHealth()>=this.getMaxHealth()){
							this.dropItemStack(new ItemStack(ModItems.soldier_machine.get()));
							if(!this_heldItem.isEmpty()&&this_heldItem!=null)this.dropItemStack(this_heldItem);
							int amount = this.getMoney() / 100;
							if(amount>0){
								for(int i=0; i<amount; ++i){
									this.dropItemStack(new ItemStack(Items.GOLD_INGOT));
								}
							}
							player.sendSystemMessage(Component.translatable("Recycle"));
							this.discard();
							return InteractionResult.SUCCESS;
						}
					}else{
						if(this.getYawRoteNBT()<360){
							this.setYawRoteNBT(this.getYawRoteNBT()+90);
						}else{
							this.setYawRoteNBT(0);
						}
						return InteractionResult.SUCCESS;
					}
				}
				if(!this_heldItem.isEmpty()&&this_heldItem!=null){
					if(item == Items.GOLD_INGOT){
						this.setMoney(this.getMoney()+100);
						if(!heldItem.isEmpty())heldItem.shrink(1);
						return InteractionResult.SUCCESS;
					}else if(item == Items.DIAMOND){
						this.setMoney(this.getMoney()+300);
						if(!heldItem.isEmpty())heldItem.shrink(1);
						return InteractionResult.SUCCESS;
					}else if(item == Items.EMERALD){
						this.setMoney(this.getMoney()+200);
						if(!heldItem.isEmpty())heldItem.shrink(1);
						return InteractionResult.SUCCESS;
					}else{
						player.sendSystemMessage(Component.translatable("------"));
						player.sendSystemMessage(Component.translatable("Count="+this.getVehicleC()));
						player.sendSystemMessage(Component.translatable("======"));
						if(player.isCrouching()){
							if(this.getVehicleC()>0)this.setVehicleC(this.getVehicleC()-1);
							return InteractionResult.SUCCESS;
						}else{
							this.setVehicleC(this.getVehicleC()+1);
							return InteractionResult.SUCCESS;
						}
					}
				}else{
					if(item instanceof ItemSpawn){
						ItemSpawn vehicleitem = (ItemSpawn)item;
						if(this.getVehicleC()==0){
							if(vehicleitem.type == 1){
								{
									if(!this_heldItem.isEmpty()&&this_heldItem!=null)this.dropItemStack(this_heldItem);
								}
								if(!this.level().isClientSide)this.setItemSlot(EquipmentSlot.MAINHAND, heldItem.copy());
								heldItem.shrink(1);
								return InteractionResult.SUCCESS;
							}else{
								player.sendSystemMessage(Component.translatable("advancearmy.interact.machines.desc"));
								return InteractionResult.PASS;
							}
						}else{
							player.sendSystemMessage(Component.translatable("advancearmy.interact.machineadd.desc"));
							return InteractionResult.PASS;
						}
					}else{
						player.sendSystemMessage(Component.translatable("advancearmy.interact.machines.desc"));
						return InteractionResult.PASS;
					}
				}
			}
		}
		return super.mobInteract(player, hand);
    }
	private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), item);
	  this.level().addFreshEntity(itementity);
	}
	public boolean isCover = false;
	
	/*public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}*/
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				ItemStack heldItem = entity1.getMainHandItem();
				if(heldItem.is(ItemTags.PICKAXES)&&this.getHealth()<this.getMaxHealth()){
					this.setHealth(this.getHealth()+1+par2);
					this.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
					heldItem.hurtAndBreak(1, entity1, (ent) -> {
					ent.broadcastBreakEvent(entity1.getUsedItemHand());});
					//par2=0;
					return false;
				}
				
				if(this.getOwner()==entity||this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof TamableAnimal){
						TamableAnimal soldier = (TamableAnimal)entity;
						if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							return super.hurt(source, par2);
						}
					}else{
						return super.hurt(source, par2);
					}
				}
			}else{
				return super.hurt(source, par2);
			}
		}else {
			return super.hurt(source, par2);
		}
    }
	Player player = null;
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int total_count = 200;
	public int finish_time = 20;
	public float cooltime6 = 0;
	
	int showbartime = 0;
	public boolean isShow(){
		return this.showbartime>0||this.getOwner()!=null;
	}
	public int getBarType(){
		return 0;
	}
	public LivingEntity getBarOwner(){
		return this.getOwner();
	}
	
	public void aiStep() {
		if(this.hurtTime>0){
			if(showbartime<1)showbartime = 70;
		}
		if(showbartime>0)--showbartime;
    	if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
    	{
			BlockPos blockpos = new BlockPos((int)(this.setx + 0.5),(int)(this.sety - 1),(int)(this.setz + 0.5));
			BlockState iblockstate = this.level().getBlockState(blockpos);
			if (this.setx != 0 && !iblockstate.isAir()){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
    	}
		if (this.isAlive()){
			if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
				++cooltime6;
				if(cooltime6 > 60){
					this.setHealth(this.getHealth() + 1);
					cooltime6=0;
				}
			}
			if(this.getOwner()!=null && this.getOwner() instanceof Player)player=(Player)this.getOwner();
			if(this.getMainHandItem()!=null){
				ItemStack this_heldItem = this.getMainHandItem();
				Item item = this_heldItem.getItem();
				if(item instanceof ItemSpawn){
					ItemSpawn vehicleitem = (ItemSpawn)item;
					this.finish_time = vehicleitem.xp;
					double xx11 = 0;
					double zz11 = 0;
					/*xx11 -= Math.sin(this.getYawRoteNBT() * 0.01745329252F) * z;
					zz11 += Math.cos(this.getYawRoteNBT() * 0.01745329252F) * z;*/
					xx11 -= Math.sin(this.getYawRoteNBT() * 0.01745329252F + 1.57F) * 4;
					zz11 += Math.cos(this.getYawRoteNBT() * 0.01745329252F + 1.57F) * -4;
					double i1 = this.getX()+xx11;
					double j1 = this.getY();
					double k1 = this.getZ()+zz11;
					/*int range = 1;
					boolean covered = false;
					AABB axisalignedbb = (new AABB(i1-range, j1-range, k1-range, i1+range, j1+range, k1+range)).inflate(1D);
					List<Entity> list = this.level().getEntities(this, axisalignedbb);
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity ent = list.get(k2);
						{
							if(ent != null && ent instanceof LivingEntity){
								covered= true;
							}
						}
					}
					this.isCover = covered;*/
					{
						if(this.getVehicleC()>0 && !this.isCover){
							/*if(player != null && this.isOwner(player))*/{
								if(this.getMoney()>0){
									if(this.getBuild() < this.finish_time){
										this.setBuild(this.getBuild()+2);
										this.setMoney(this.getMoney()-2);
										if(this.getBuild()%20==0)this.playSound(SoundEvents.PORTAL_AMBIENT, 1.0F, 1.0F);
									}
								}
							}
						}
						if(this.getMoney()>0){
							if(this.getBuild() >= this.finish_time){
								if (!(this.level() instanceof ServerLevel)) {
								} else {
									ServerLevel serverworld = (ServerLevel)this.level();
									vehicleitem.spawnCreature(serverworld, player, 0, true, (double)i1, (double)j1, (double)k1, 1);
								}
								this.setBuild(0);
								this.setVehicleC(this.getVehicleC() - 1);
								this.playSound(SoundEvents.PORTAL_TRAVEL, 2.0F, 1.0F);
							}
						}
					}
				}
			}
		}
		super.aiStep();
    }
}
