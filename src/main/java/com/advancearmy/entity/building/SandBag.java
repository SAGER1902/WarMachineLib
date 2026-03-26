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
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import wmlib.api.IBuilding;
import wmlib.api.ITool;
import net.minecraft.world.entity.item.ItemEntity;
import advancearmy.init.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
public class SandBag extends TamableAnimal implements ITool,IBuilding{
	public SandBag(EntityType<? extends SandBag> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  //this.noCulling = true;
	}
	public SandBag(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_SANDBAG.get(), worldIn);
	}
	public void checkDespawn() {
	}
	public boolean canBeCollidedWith() {//
		return true;
	}
	public static AttributeSupplier.Builder createAttributes() {
        return SandBag.createMobAttributes();
    }
	public float boxheight = 0.9F;
	public float boxwidth = 1F;
	public EntityDimensions getDimensions(Pose pos) {
		EntityDimensions entitysize = super.getDimensions(pos);
		if(this.getHealth()>60){
			this.boxheight=1F;
		}else if(this.getHealth()>30&&this.getHealth()<=60){
			this.boxheight=0.7F;
		}else if(this.getHealth()<=30){
			this.boxheight=0.4F;
		}
		if(this.boxwidth!=0&&this.boxheight!=0){
			return entitysize.scale(boxwidth,boxheight);
		}else{
			return entitysize;
		}
	}
	private void updateSizeInfo() {
	  this.refreshDimensions();
	}
	public void onSyncedDataUpdated(EntityDataAccessor<?> nbt) {
		if(this.boxwidth!=0&&this.boxheight!=0){
			this.updateSizeInfo();
		}
		super.onSyncedDataUpdated(nbt);
	}
	
	private static final EntityDataAccessor<Integer> YawRote = SynchedEntityData.<Integer>defineId(SandBag.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("YawRote", this.getYawRoteNBT());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setYawRoteNBT(compound.getInt("YawRote"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(YawRote, Integer.valueOf(0));
	}
	public int getYawRoteNBT() {
		return ((this.entityData.get(YawRote)).intValue());
	}
	public void setYawRoteNBT(int stack) {
		this.entityData.set(YawRote, Integer.valueOf(stack));
	}
	
	public float getYawRote() {
		//return ((this.entityData.get(YawRote)).intValue());
		return this.getYRot();
	}
	public void setYawRote(float stack) {
		//this.entityData.set(YawRote, Integer.valueOf(stack));
		this.setYRot(stack);
	}
	public VehicleMachine getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
		return null;
	}
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(par2>25){
		}else{
			par2 = par2*0.1F;
		}
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				ItemStack heldItem = entity1.getMainHandItem();
				if(heldItem.is(ItemTags.PICKAXES)&&this.getHealth()<this.getMaxHealth()){
					this.setHealth(this.getHealth()+1+par2);
					this.playSound(SoundEvents.ANVIL_USE, 2.0F, 1.0F);
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
	int ridcool = 0;
	LivingEntity deployer = null;
	public boolean can_hand_deploy = true;
	public boolean isDeploying = false;
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack heldItem = player.getMainHandItem();
		if(player.isCrouching() && this.getOwner()==player && heldItem.is(ItemTags.PICKAXES)&&this.getHealth()>=this.getMaxHealth()){
			if(!this.level().isClientSide){
				if(this.getHealth()>60){
					this.dropItemStack(new ItemStack(ModItems.item_spawn_sandbag3.get()));
				}else if(this.getHealth()>30&&this.getHealth()<=60){
					this.dropItemStack(new ItemStack(ModItems.item_spawn_sandbag2.get()));
				}else if(this.getHealth()<=30){
					this.dropItemStack(new ItemStack(ModItems.item_spawn_sandbag.get()));
				}
				this.discard();
				player.sendSystemMessage(Component.translatable("Recycle"));
				return InteractionResult.SUCCESS;
			}
		}
		if(can_hand_deploy){
			if(player.isCrouching() && this.getOwner()==player && player.getVehicle()==null){
				if(!isDeploying/* && !this.level().isClientSide*/){
					isDeploying=true;
					deployer=player;
					ridcool = 20;
				}
				return InteractionResult.SUCCESS;
			}else{
				if(isDeploying||deployer!=null){
					return InteractionResult.PASS;
				}else{
					if(this.getOwner()==player && player.getVehicle()==null){
						this.setYRot(this.getYRot()+45F);
					}
					return super.mobInteract(player, hand);
				}
			}
		}else{
			return super.mobInteract(player, hand);
		}
    }
	
	private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), item);
	  this.level().addFreshEntity(itementity);
	}
	
	//int flarecd;
	/*public int setx = 0;
	public int sety = 0;
	public int setz = 0;*/
	int roteyaw = 0;
	public void aiStep() {
		if(ridcool>0)--ridcool;
		
		if(this.getYawRoteNBT()!=0){
			this.setYRot(getYawRoteNBT());
			this.setYawRoteNBT(0);
		}
		
		if(can_hand_deploy){
			if(deployer!=null && isDeploying){
				deployer.setDeltaMovement(deployer.getDeltaMovement().x * 0.5F, deployer.getDeltaMovement().y, deployer.getDeltaMovement().z * 0.5F);
				float f1 = deployer.getYRot() * (2 * (float) Math.PI / 360);
				double ix = 0;
				double iz = 0;
				ix -= Math.sin(f1) * 1.5F;
				iz += Math.cos(f1) * 1.5F;
				this.setPos(deployer.getX() + ix, deployer.getY()+0.5F, deployer.getZ() + iz);
				roteyaw=90-(int)deployer.getYRot();
				/*if(roteyaw >= 360 || roteyaw <= -360){
					roteyaw = 0;
				}*/
				this.setYawRote(roteyaw);
				if(ridcool==0 && deployer.isCrouching()||deployer.getHealth()==0){
					isDeploying=false;
					deployer=null;
				}
			}
		}
		
		/*if(flarecd<20)++flarecd;
		if(flarecd>2){
			System.out.println("==================");
			System.out.println("this.getYRot()"+this.getYRot());
			System.out.println("this.getYawRote()"+this.getYawRote());
			System.out.println("roteyaw"+roteyaw);
			System.out.println("------------------");
			flarecd = 0;
		}*/
		
		if(deployer!=null && isDeploying){
			
		}else{
			this.setDeltaMovement(this.getDeltaMovement().add(-this.getDeltaMovement().x*0.5F, -0.05D, -this.getDeltaMovement().z*0.5F));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
   }
}
