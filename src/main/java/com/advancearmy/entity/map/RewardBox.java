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
import net.minecraft.world.entity.Entity.RemovalReason;

import net.minecraft.server.level.ServerPlayer;
import advancearmy.event.SASoundEvent;
import advancearmy.item.ItemSpawn;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import advancearmy.init.ModItems;
import wmlib.api.IHealthBar;
import wmlib.api.IBuilding;
import wmlib.api.IEnemy;
import wmlib.api.ITool;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import advancearmy.entity.soldier.EntitySA_Swun;

import advancearmy.AdvanceArmy;
import wmlib.common.block.BlockRegister;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import advancearmy.event.SASoundEvent;
public class RewardBox extends Mob implements ITool{
	public RewardBox(EntityType<? extends RewardBox> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public RewardBox(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_RBOX.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return RewardBox.createMobAttributes();
    }
	
	public void checkDespawn() {
	}
	private static final EntityDataAccessor<Integer> BoxID = SynchedEntityData.<Integer>defineId(RewardBox.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("BoxID", this.getBoxID());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setBoxID(compound.getInt("BoxID"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(BoxID, Integer.valueOf(0));
	}
	public int getBoxID() {
	return ((this.entityData.get(BoxID)).intValue());
	}
	public void setBoxID(int stack) {
	this.entityData.set(BoxID, Integer.valueOf(stack));
	}
	
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	
	int iron = 1;
	int gold = 1;
	int emerald = 1;
	int diamond = 1;
	int goldmelon = 1;
	int ironmelon = 1;
	
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		player.playSound(SASoundEvent.open_box.get(),1F,1F);
		if(!this.level().isClientSide){
			this.discard();
			if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).connection.send(new ClientboundRemoveEntitiesPacket(this.getId()));
            }
			//this.die(DamageSource.GENERIC);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
    }
	
   public void remove(RemovalReason r) {
      super.remove(r);
		if(this.getBoxID()==1){//mob
			this.dropItemStack(new ItemStack(ModItems.item_spawn_m2hb.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_tow.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_stin.get()));
			iron = 10;
			gold = 10;
			emerald = 3;
			goldmelon = 1;
			ironmelon = 2;
			this.dropItemStack(new ItemStack(ModItems.challenge_reb.get()));
		}else if(this.getBoxID()==2){//reb
			this.dropItemStack(new ItemStack(ModItems.item_spawn_tank.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_m2a2.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_egal.get()));
			this.dropItemStack(new ItemStack(ModItems.support_155.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_mortar.get()));
			iron = 15;
			gold = 20;
			emerald = 8;
			diamond = 3;
			goldmelon = 3;
			ironmelon = 4;
			this.dropItemStack(new ItemStack(ModItems.challenge_pillager.get()));
		}else if(this.getBoxID()==3){//pill
			this.dropItemStack(new ItemStack(ModItems.item_spawn_t90.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_bmpt.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_heli.get()));
			this.dropItemStack(new ItemStack(ModItems.support_kh29l.get()));
			this.dropItemStack(new ItemStack(ModItems.support_a10a.get()));
			iron = 20;
			gold = 30;
			emerald = 12;
			diamond = 8;
			goldmelon = 4;
			ironmelon = 5;
			this.dropItemStack(new ItemStack(ModItems.challenge_tank.get()));
		}else if(this.getBoxID()==4){//tank
			this.dropItemStack(new ItemStack(ModItems.item_spawn_a10a.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_m6aa.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_gltk.get()));
			iron = 30;
			gold = 40;
			emerald = 16;
			diamond = 12;
			goldmelon = 5;
			ironmelon = 6;
			this.dropItemStack(new ItemStack(ModItems.challenge_mobair.get()));
		}else if(this.getBoxID()==5){//mobair
			this.dropItemStack(new ItemStack(ModItems.item_spawn_su33.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_mi24.get()));
			iron = 35;
			gold = 45;
			emerald = 18;
			diamond = 15;
			goldmelon = 6;
			ironmelon = 7;
			this.dropItemStack(new ItemStack(ModItems.challenge_air.get()));
		}else if(this.getBoxID()==6){//air
			this.dropItemStack(new ItemStack(ModItems.item_spawn_f35.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_skyfire.get()));
			this.dropItemStack(new ItemStack(ModItems.support_f35bomb.get()));
			iron = 40;
			gold = 50;
			emerald = 20;
			diamond = 18;
			goldmelon = 7;
			ironmelon = 8;
			this.dropItemStack(new ItemStack(ModItems.challenge_aohuan.get()));//challenge_sea
		}else if(this.getBoxID()==7){//
			this.dropItemStack(new ItemStack(ModItems.support_trident.get()));
			iron = 45;
			gold = 55;
			emerald = 25;
			diamond = 20;
			goldmelon = 8;
			ironmelon = 9;
			this.dropItemStack(new ItemStack(ModItems.challenge_aohuan.get()));
		}else if(this.getBoxID()==8){
			this.dropItemStack(new ItemStack(ModItems.support_3m22.get()));
			this.dropItemStack(new ItemStack(ModItems.support_ember.get()));
			this.dropItemStack(new ItemStack(ModItems.item_spawn_mast.get()));
			if(this.level().random.nextInt(2)==1)this.dropItemStack(new ItemStack(ModItems.support_swun.get()));
			if(this.level().random.nextInt(3)==1)this.dropItemStack(new ItemStack(ModItems.support_youhun.get()));
			iron = 50;
			gold = 60;
			emerald = 30;
			diamond = 25;
			goldmelon = 9;
			ironmelon = 10;
			this.dropItemStack(new ItemStack(ModItems.challenge_portal.get()));
		}else if(this.getBoxID()==9){
			this.dropItemStack(new ItemStack(ModItems.support_ftkh.get()));
			this.dropItemStack(new ItemStack(ModItems.support_nuke.get()));
			this.dropItemStack(new ItemStack(ModItems.support_fw020.get()));
			iron = 64;
			gold = 64;
			emerald = 32;
			diamond = 32;
			goldmelon = 10;
			ironmelon = 12;
		}else{
			this.dropItemStack(new ItemStack(BlockRegister.GOLD_MELON.get().asItem()));
		}
		
		for(int k2 = 0; k2 < iron+this.level().random.nextInt(iron); ++k2){
			this.dropItemStack(new ItemStack(Items.IRON_INGOT));
		}
		for(int k2 = 0; k2 < gold+this.level().random.nextInt(gold); ++k2){
			this.dropItemStack(new ItemStack(Items.GOLD_INGOT));
		}
		for(int k2 = 0; k2 < emerald+this.level().random.nextInt(emerald); ++k2){
			this.dropItemStack(new ItemStack(Items.EMERALD));
		}
		for(int k2 = 0; k2 < diamond+this.level().random.nextInt(diamond); ++k2){
			this.dropItemStack(new ItemStack(Items.DIAMOND));
		}
		for(int k2 = 0; k2 < goldmelon+this.level().random.nextInt(goldmelon); ++k2){
			this.dropItemStack(new ItemStack(BlockRegister.GOLD_MELON.get().asItem()));
		}
		for(int k2 = 0; k2 < ironmelon+this.level().random.nextInt(ironmelon); ++k2){
			this.dropItemStack(new ItemStack(BlockRegister.IRON_MELON.get().asItem()));
		}
   }
	

	private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), item);
	  this.level().addFreshEntity(itementity);
	}
	
	/*public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}*/
	/*protected void tickDeath() {
	}*/
}
