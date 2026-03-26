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
import wmlib.api.IEnemy;

import wmlib.api.IEnemy;
import net.minecraft.server.level.ServerPlayer;
import wmlib.common.living.WeaponVehicleBase;

import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;

import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_GI;

import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Creeper;

import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;
import net.minecraft.tags.ItemTags;
public class CreatureRespawn extends Mob implements ITool{
	public CreatureRespawn(EntityType<? extends CreatureRespawn> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	/*
      PINK("pink", TextFormatting.RED),
      BLUE("blue", TextFormatting.BLUE),
      RED("red", TextFormatting.DARK_RED),
      GREEN("green", TextFormatting.GREEN),
      YELLOW("yellow", TextFormatting.YELLOW),
      PURPLE("purple", TextFormatting.DARK_BLUE),
      WHITE("white", TextFormatting.WHITE);
	*/
	//private final ServerBossEvent FriendCount = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS))/*.setDarkenScreen(true)*/;
	//private final ServerBossEvent EnemyCount = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS))/*.setDarkenScreen(true)*/;
	public CreatureRespawn(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_CRES.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return CreatureRespawn.createMobAttributes();
    }
	
	public void checkDespawn() {
	}
	private static final EntityDataAccessor<Integer> RespawnCount = SynchedEntityData.<Integer>defineId(CreatureRespawn.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("RespawnCount", this.getRespawnCount());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setRespawnCount(compound.getInt("RespawnCount"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(RespawnCount, Integer.valueOf(0));
	}
	public int getRespawnCount() {
	return ((this.entityData.get(RespawnCount)).intValue());
	}
	public void setRespawnCount(int stack) {
	this.entityData.set(RespawnCount, Integer.valueOf(stack));
	}
	public boolean canCollideWith(Entity entity) {
		return false;
	}
	public void push(Entity entity) {
		
	}
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
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
				if(item == Items.GOLD_INGOT){
					if(isEnemyRespawn){
						if(!this.level().isClientSide)this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND));
						return InteractionResult.SUCCESS;
					}else{
						if(!this.level().isClientSide)this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_INGOT));
						return InteractionResult.SUCCESS;
					}
				}else{
					player.sendSystemMessage(Component.translatable("use GOLD_INGOT click to change[Friend/Enemy]"));
				}
			}else{
				if(player.isCrouching()){
					this.setRespawnCount(0);
					return InteractionResult.SUCCESS;
				}else{
					this.setRespawnCount(this.total_count);
					return InteractionResult.SUCCESS;
				}
			}
			player.sendSystemMessage(Component.translatable("------"));
			if(isEnemyRespawn){
				player.sendSystemMessage(Component.translatable("Enemy Spawner"));
			}else{
				player.sendSystemMessage(Component.translatable("Friend Spawner"));
			}
			player.sendSystemMessage(Component.translatable("Max Count ="+this.total_count));
			player.sendSystemMessage(Component.translatable("Now Count ="+this.getRespawnCount()));
			player.sendSystemMessage(Component.translatable("======"));
		}
		//return super.mobInteract(player, hand);
		return InteractionResult.PASS;
    }

	public boolean isEnemyRespawn = false;
	
	/*public void startSeenByPlayer(ServerPlayer p_184178_1_) {
	  super.startSeenByPlayer(p_184178_1_);
	  if(isEnemyRespawn){
		  this.EnemyCount.addPlayer(p_184178_1_);
	  }else{
		  this.FriendCount.addPlayer(p_184178_1_);
	  }
	}
	public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
	  super.stopSeenByPlayer(p_184203_1_);
	  if(isEnemyRespawn){
			this.EnemyCount.removePlayer(p_184203_1_);
	  }else{
			this.FriendCount.removePlayer(p_184203_1_);
	  }
	}
	public void setCustomName(@Nullable Component p_200203_1_) {
		super.setCustomName(p_200203_1_);
		//this.EnemyCount.setName(this.getDisplayName());
		if(isEnemyRespawn){
			this.EnemyCount.setName(this.getDisplayName());
		}else{
			this.FriendCount.setName(this.getDisplayName());
		}
	}*/
	
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
	/*protected void tickDeath() {

	}*/
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int total_count = 800;
	public int max_summon = 20;
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
			if(item == Items.DIAMOND){
				this.isEnemyRespawn = false;
				//this.setDisplayName("敌方兵力");
			}
			if(item == Items.IRON_INGOT){
				this.isEnemyRespawn = true;
				//this.setDisplayName("我方兵力");
			}
		}
		/*if(this.isEnemyRespawn){
			this.EnemyCount.setProgress(this.getRespawnCount() / 200);//this.getMoveY()
		}else{
			this.FriendCount.setProgress(this.getRespawnCount() / 200);
		}*/
		if (this.isAlive()){
			if(cooltime6<50)++cooltime6;
			/*if (!(this.level() instanceof ServerLevel)) {
				//return false;
			}*/ else {
				//ServerLevel serverworld = (ServerLevel)this.level();
				int i = Mth.floor(this.getX());
				int j = Mth.floor(this.getY());
				int k = Mth.floor(this.getZ());
				if(summontime<100)++summontime;
				if(summontime>20 && this.getRespawnCount()>0){
					int count = 0;
					int ve = 0;
					int i1 = i + Mth.nextInt(this.random, 2, 10) * Mth.nextInt(this.random, -1, 1);
					int j1 = j + Mth.nextInt(this.random,1, 2);
					int k1 = k + Mth.nextInt(this.random, 2, 10) * Mth.nextInt(this.random, -1, 1);
					List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(200D, 100D, 200D));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity ent = list.get(k2);
						if(ent instanceof LivingEntity){
							LivingEntity living = (LivingEntity)ent;
							if(isEnemyRespawn){
								if(ent instanceof ERO_REB){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof ERO_Pillager){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
							}else{
								if(ent instanceof EntitySA_Soldier){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof EntitySA_GI){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof EntitySA_Conscript){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof Player){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
							}
						}
					}
					if (!(this.level() instanceof ServerLevel)) {
						//return false;
					}else{
						ServerLevel serverworld = (ServerLevel)this.level();
						if(count<max_summon){
							if(summontime>50+count){
								   BlockPos blockpos = new BlockPos(i1, j1, k1);
								   if(isEnemyRespawn){
										if(this.level().random.nextInt(6)==2){
											ERO_Zombie army = new ERO_Zombie(ModEntities.ENTITY_EZOMBIE.get(), serverworld);
											army.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(army);
										}else if(this.level().random.nextInt(6)==4){
											ERO_Skeleton ent = new ERO_Skeleton(ModEntities.ENTITY_SKELETON.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}else if(this.level().random.nextInt(5)==2){
											ERO_Creeper ent = new ERO_Creeper(ModEntities.ENTITY_CREEPER.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}
										if(this.level().random.nextInt(3)==1){
											ERO_Pillager ent = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}else{
											ERO_REB ent = new ERO_REB(ModEntities.ENTITY_REB.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}
								   }else{
										if(this.level().random.nextInt(8)==1){
											EntitySA_Conscript ent = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
											if(this.level().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
												this.level().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), this.level().getScoreboard().getPlayerTeam("AdvanceArmy"));
											}
										}else if(this.level().random.nextInt(8)==2){
											EntitySA_GI ent = new EntitySA_GI(ModEntities.ENTITY_GI.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
											if(this.level().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
												this.level().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), this.level().getScoreboard().getPlayerTeam("AdvanceArmy"));
											}
										}else{
											EntitySA_Soldier ent = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(), serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
											if(this.level().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
												this.level().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), this.level().getScoreboard().getPlayerTeam("AdvanceArmy"));
											}
										}
								   }
								this.summontime = 0;
							}
						}
					}
				}
			}
		}
		super.aiStep();
    }
}
