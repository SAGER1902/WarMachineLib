package wmlib.common.living;
import java.util.Random;
import java.util.List;
import javax.annotation.Nullable;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import org.lwjgl.glfw.GLFW;
// 路径查找
import net.minecraft.world.entity.ai.navigation.PathNavigation;       // PathNavigator → PathNavigation
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation; // GroundPathNavigator → GroundPathNavigation
// 世界和维度
import net.minecraft.world.level.Level;                               // Level → Level
import net.minecraft.server.level.ServerLevel;                        // ServerWorld → ServerLevel
// 交互和文本
import net.minecraft.world.InteractionHand;                           // Hand → InteractionHand
import net.minecraft.network.chat.Component;                          // TranslationTextComponent → Component
// 声音和粒子
import net.minecraft.sounds.SoundEvent;                               // 包路径更新
import net.minecraft.sounds.SoundEvents;                              // 包路径更新
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;                    // 粒子类路径更新
// 数学和向量
import net.minecraft.world.phys.Vec3;                                 // Vec3 → Vec3
// 物品和方块
import net.minecraft.world.item.Items;                                // 路径不变
import net.minecraft.world.item.Item;                                 // 路径不变
import net.minecraft.world.level.block.Blocks;                        // 包路径更新
import net.minecraft.world.item.ItemStack;                            // 路径不变
// 实体属性和行为
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;    // AttributeModifierMap → AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes;           // 路径更新
import net.minecraft.world.entity.player.Player;                      // Player → Player
import net.minecraft.world.entity.monster.Enemy;                      // Enemy → Enemy（标记接口）
import net.minecraft.world.entity.LivingEntity;                       // 路径更新
import net.minecraft.world.entity.MoverType;                          // 路径更新
import net.minecraft.world.entity.EntityType;                         // 路径更新
import net.minecraft.world.entity.EquipmentSlot;                      // EquipmentSlotType → EquipmentSlot
// 网络和数据同步
import net.minecraft.network.syncher.EntityDataAccessor;              // DataParameter → EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers;           // DataSerializers → EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData;               // EntityDataManager → SynchedEntityData
// NBT 和资源
import net.minecraft.nbt.CompoundTag;                                 // CompoundTag → CompoundTag
import net.minecraft.resources.ResourceLocation;                      // 路径更新
// 伤害和队伍
import net.minecraft.world.damagesource.DamageSource;                 // 路径更新
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.scores.Team;                               // 包路径更新
// 其他
import net.minecraft.world.phys.AABB;                                 // AxisAlignedBB → AABB
import net.minecraft.world.level.Explosion;                           // 路径更新
import net.minecraft.world.entity.item.ItemEntity;                    // 路径更新
import net.minecraftforge.network.NetworkHooks;                       // 包路径更新
import net.minecraftforge.fml.ModList;                                // 路径不变
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import safx.SagerFX;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.item.ItemBless;
import wmlib.util.WMEnchantmentHelper;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.client.obj.SAObjModel;
import wmlib.api.IHealthBar;
import wmlib.api.ITool;
import net.minecraftforge.network.PacketDistributor;
import wmlib.common.network.message.MessageFirstTarget;
import wmlib.common.network.PacketHandler;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec2;
import wmlib.api.IArmy;
import net.minecraft.tags.ItemTags;
//import wmlib.api.IPara;

//import wmlib.api.IAnimPacket;
public abstract class WeaponVehicleBase extends EntityWMVehicleBase implements IHealthBar,ITool{
	protected PathNavigation navigationsa;
	public WeaponVehicleBase(EntityType<? extends WeaponVehicleBase> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.navigationsa = this.createNavigationSA(worldIn);
	}

	public SoundEvent lockAlertSound = SoundEvents.LAVA_EXTINGUISH;
	//public SoundEvent lockTargetSound = null;
	public SoundEvent destroySoundStart = SoundEvents.GENERIC_EXPLODE;
	public SoundEvent destroySoundEnd = SoundEvents.DRAGON_FIREBALL_EXPLODE;
	
	protected PathNavigation createNavigationSA(Level p_175447_1_) {
	  return new GroundPathNavigation(this, p_175447_1_);
	}
	public PathNavigation getNavigation() {//
		return this.navigationsa;
		//return null;
	}
	public boolean rideableUnderWater() {
	  return true;
	}
	
    public Vector4f transformPosition(Matrix4f transform, float x, float y, float z) {
        return transform.transform(new Vector4f(x, y, z, 1));
    }
	@OnlyIn(Dist.CLIENT)
    public Matrix4f getClientVehicleTransform(float ticks, Player player) {
        Matrix4f transform = new Matrix4f();
        transform.translate((float) Mth.lerp(ticks, xo, getX()), (float) Mth.lerp(ticks, yo + 2.375f, getY() + 2.375f), (float) Mth.lerp(ticks, zo, getZ()));
        if(this.VehicleType>2&&this.getArmyType1()==0){
			transform.rotate(Axis.YP.rotationDegrees((float) (-Mth.lerp(ticks, this.turretYawO, this.turretYaw))));
			transform.rotate(Axis.XP.rotationDegrees((float) (Mth.lerp(ticks, this.turretPitchO, this.turretPitch))));
		}else{
			transform.rotate(Axis.YP.rotationDegrees((float) (-Mth.lerp(ticks, player.yRotO, player.getYRot()))));
			transform.rotate(Axis.XP.rotationDegrees((float) (Mth.lerp(ticks, player.xRotO, player.getXRot()))));
		}
        transform.rotate(Axis.ZP.rotationDegrees(this.flyRoll));
        return transform;
    }
    @OnlyIn(Dist.CLIENT)
    public @Nullable Vec2 getCameraRotation(float partialTicks, Player player, boolean gunner_aim, boolean isThird, boolean isFirst) {
		if(isThird && !gunner_aim||gunner_aim && isFirst){
			return new Vec2(player.getYRot(), player.getXRot());
		}else{
			return null;
		}
    }
    @OnlyIn(Dist.CLIENT)
    public Vec3 getCameraPosition(float partialTicks, Player passenger, boolean isZoom, boolean gunner_aim, boolean isThird, boolean isFirst) {
		Matrix4f transform = getClientVehicleTransform(partialTicks, passenger);
		Minecraft mc = Minecraft.getInstance();
		Vector4f maxCameraPosition = transformPosition(transform, this.seatView3X, -this.seatView3Y*1.5F, this.seatView3Z*2);
		
		if(this.VehicleType>2 && isZoom && isThird)maxCameraPosition = transformPosition(transform, this.airzoom_x, -this.airzoom_y, this.airzoom_z);
		
		if(gunner_aim && isFirst){
			maxCameraPosition = transformPosition(transform, this.heligun_x, this.heligun_y, this.heligun_z);
			if(this.hasPassenger(passenger)){
				int i = this.getPassengers().indexOf(passenger);
				double relX = this.heligun_x;
				double relY = this.heligun_y;
				double relZ = this.heligun_z;
				double centerX = 0;
				double centerY = 0;
				double centerZ = 0;
				if(this.VehicleType>2){
					centerY=seatPosY[0];
					centerZ=seatPosZ[0];
				}
				float seatYaw;
				if (seatTurret[i]) {
					if (this.getTargetType() == 0 && this.getChange() <= 0 && this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger() != null) {
						seatYaw = this.getFirstSeat().getControllingPassenger().getYHeadRot();
					} else {
						seatYaw = this.turretYaw;
					}
				} else {
					seatYaw = this.getYHeadRot();
				}
				float pitch = this.flyPitch;
				float roll = this.flyRoll;
				double px = relX - centerX;
				double py = relY - centerY;
				double pz = relZ - centerZ;
				Vec3 finalLocal = applyVehicleRotation(new Vec3(px, py, pz), this.getYRot(), pitch, 0);
				double finalX = finalLocal.x + centerX;
				double finalY = finalLocal.y + centerY;
				double finalZ = finalLocal.z + centerZ;
				maxCameraPosition = transformPosition(transform, (float)finalX, (float)finalY, (float)finalZ);
			}
		}
		
		Vec3 finalPos = this.getMaxZoom(transform, maxCameraPosition);
		if(isThird && !gunner_aim||gunner_aim && isFirst){
			return finalPos;
		}else{
			return null;
		}
    }
	@OnlyIn(Dist.CLIENT)
    public Vec3 getMaxZoom(Matrix4f transform, Vector4f maxCameraPos) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Vector4f vehiclePos = transformPosition(transform, 0, 0, 0);
        Vec3 maxCameraPosVec3 = new Vec3(maxCameraPos.x, maxCameraPos.y, maxCameraPos.z);
        Vec3 vehiclePosVec3 = new Vec3(vehiclePos.x, vehiclePos.y, vehiclePos.z);
        Vec3 toVec = vehiclePosVec3.vectorTo(maxCameraPosVec3);
        if (player != null) {
            HitResult hitresult = player.level().clip(new ClipContext(vehiclePosVec3, vehiclePosVec3.add(toVec).add(toVec.normalize().scale(1)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                return hitresult.getLocation().add(toVec.normalize().scale(-1));
            }
        }
        return maxCameraPosVec3;
    }
	
    @Override
    public void makeStuckInBlock(BlockState state, net.minecraft.world.phys.Vec3 motionMultiplier) {
        /*if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }*/
    }
	public boolean fireproduct=false;
	public boolean fireImmune() {
		return fireproduct;
	}
	
    private static final EntityDataAccessor<Integer> enc = SynchedEntityData.<Integer>defineId(WeaponVehicleBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> tc = SynchedEntityData.<Integer>defineId(WeaponVehicleBase.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound){
		super.addAdditionalSaveData(compound);
		compound.putInt("enc", getEnc());
		compound.putInt("tc", getTeamC());
	}
	public void readAdditionalSaveData(CompoundTag compound){
	   super.readAdditionalSaveData(compound);
		this.setEnc(compound.getInt("enc"));
		this.setTeamC(compound.getInt("tc"));
	}
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(enc, Integer.valueOf(0));
		this.entityData.define(tc, Integer.valueOf(0));
	}
	public int getEnc() {
		return ((this.entityData.get(enc)).intValue());
	}
	public void setEnc(int stack) {
		this.entityData.set(enc, Integer.valueOf(stack));
	}

	public int getTeamC() {
		return ((this.entityData.get(tc)).intValue());
	}
	public void setTeamC(int stack) {
		this.entityData.set(tc, Integer.valueOf(stack));
	}
	public boolean seatRotePitch = false;

	public double[] seatRoteX = new double[12];
	public double[] seatRoteY = new double[12];
	public double[] seatRoteZ = new double[12];
	
	public ResourceLocation fire1tex = null;
	public ResourceLocation fire2tex = null;
	public ResourceLocation fire3tex = null;
	public ResourceLocation fire4tex = null;
	
	public boolean startShield = false;
    public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getDirectEntity();
    	Entity living = source.getEntity();
		
		if (source.is(DamageTypes.DROWN))par2=par2+100F;
		if(this.enc_armor>0){
			if (source.is(DamageTypes.EXPLOSION)){
				par2 = par2*(6F-this.enc_armor)/5F;
			}else{
				par2 = par2*(11F-this.enc_armor)/10F;
			}
		}
		if(startShield)par2=par2*0.5F;
		if(entity != null){
			if (entity instanceof LivingEntity attacker) {
				ItemStack heldItem = attacker.getMainHandItem();
				if(heldItem.is(ItemTags.PICKAXES)&&this.getHealth()<this.getMaxHealth()){
					this.setHealth(this.getHealth()+1+par2);
					this.playSound(SoundEvents.ANVIL_USE, 2.0F, 1.0F);
					heldItem.hurtAndBreak(1, attacker, (ent) -> {
					ent.broadcastBreakEvent(attacker.getUsedItemHand());});
					//par2=0;
					return false;
				}
			}
			if(this.getFirstSeat()!=null && entity == this.getFirstSeat().getAnyPassenger()||entity.getVehicle()==this||
				this.getOwner()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
				return false;
			}else if(haveTurretArmor) {
				double ey = entity.getY();
				if(ey >= this.getBoundingBox().maxY) {
					if(par2 < armor_top) {
						par2 = 0;
					}else{
						if(!(entity instanceof EntityShell) || !(entity instanceof EntityMissile))par2=par2-armor_top;
					}
				}else if(ey >= this.getBoundingBox().minY + armor_turret_height) {//命中炮塔
					par2 = AI_Damage.damageArmor(this, entity, par2, true, this.turretYaw);
				}else if(ey >= this.getBoundingBox().minY){//命中车身
					par2 = AI_Damage.damageArmor(this, entity, par2, false, this.yHeadRot);
				}else {//命中顶部
					if(par2 < armor_bottom) {
						par2 = 0;
					}else{
						if(!(entity instanceof EntityShell) || !(entity instanceof EntityMissile))par2=par2-armor_bottom;
					}
				}
			}else {
				par2 = AI_Damage.damageArmor(this, entity, par2, false, this.yHeadRot);
			}
			par2 = par2 * AI_Damage.antiBullet(this, entity, par2, source, 
			this.antibullet_0, this.antibullet_1, this.antibullet_2, this.antibullet_3, this.antibullet_4);
			return super.hurt(source, par2);
		}else if(living!= null){
			if(haveTurretArmor) {
				double ey = living.getY();
				if(ey >= this.getBoundingBox().maxY) {
					if(par2 < armor_top) {
						par2 = 0;
					}else{
						par2=par2-armor_top;
					}
				}else if(ey >= this.getBoundingBox().minY + armor_turret_height) {
					par2 = AI_Damage.damageArmor(this, living, par2, true, this.turretYaw);
				}else if(ey >= this.getBoundingBox().minY){
					par2 = AI_Damage.damageArmor(this, living, par2, false, this.yHeadRot);
				}else {
					if(par2 < armor_bottom) {
						par2 = 0;
					}else{
						par2=par2-armor_bottom;
					}
				}
			}else {
				par2 = AI_Damage.damageArmor(this, living, par2, false, this.yHeadRot);
			}
			if(par2 <= 1) {
				this.playSound(SoundEvents.ANVIL_LAND, 0.1F, 1F);
				par2=0;
			}
			return super.hurt(source, par2);
		}else{
			par2=par2*0.5F;
			if(par2 <= 1) {
				this.playSound(SoundEvents.ANVIL_LAND, 0.1F, 1F);
				par2=0;
			}
			return super.hurt(source, par2);
		}
    }
	
	
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && entity!=this && !(entity instanceof EntityWMSeat)){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			boolean canattack = true;
			if(entity instanceof TamableAnimal){
				TamableAnimal soldier = (TamableAnimal)entity;
				if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
					canattack=false;
				}
			}
			if(this.getOwner()!=null && this.getOwner()==entity1){
				canattack=false;
			}
			if(this.getFirstSeat()!=null && this.getFirstSeat().getAnyPassenger()==entity1){
				canattack=false;
			}
			if(team != null && team1 == team)canattack= false;
			if(this.getTargetType()==2){
				if(entity instanceof Enemy && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1))canattack= false;
			}
			return canattack;
    	}else{
			return false;
		}
	}
	
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 1){
			this.playSound(this.destroySoundStart, 3.0F+this.getBbWidth()*0.1F, 1.0F);
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3+this.getBbWidth()*0.1F, false, Level.ExplosionInteraction.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp1", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
		}
		if (this.deathTime >= 120) {
			this.discard();
			this.playSound(this.destroySoundEnd, 2.0F+this.getBbWidth()*0.1F, 1.0F);
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2+this.getBbWidth()*0.1F, false, Level.ExplosionInteraction.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp2", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
		}
	}
	
	private void dropItemStack(ItemStack item) {
		ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), item);
		this.level().addFreshEntity(itementity);
	}
	
	public boolean nodrop=false;
	public int ridcool = 0;
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(player.getVehicle()==null && ridcool == 0){
			ItemStack heldItem = player.getItemInHand(hand);
			ItemStack this_heldItem = this.getWeaponItem(EquipmentSlot.MAINHAND);
			Item item = heldItem.getItem();
			if(heldItem!=null && !heldItem.isEmpty()){
				if(item instanceof ItemBless && this_heldItem.isEmpty()&&this_heldItem!=null && heldItem.isEnchanted() && this.getEnc()==0){
					if(!this_heldItem.isEmpty()&&this_heldItem!=null)this.dropItemStack(this_heldItem);
					if(!this.level().isClientSide){
						this.setWeaponItem(EquipmentSlot.MAINHAND, heldItem.copy());
						player.sendSystemMessage(Component.literal("Oh, That's good~"));
					}
					heldItem.shrink(1);
					//heldItem.removeTagKey("Enchantments");
					ridcool = 20;
					for(int i = 0; i < 20; ++i){
						int ry = this.level().getRandom().nextInt(3);
						this.level().addParticle(ParticleTypes.ENCHANT, this.getX()-2, this.getY() + 3D +ry, this.getZ()+2, 0.0D, 0.0D, 0.0D);
						this.level().addParticle(ParticleTypes.ENCHANT, this.getX()+2, this.getY() + 3D +ry, this.getZ()-1, 0.0D, 0.0D, 0.0D);
						int rx = this.level().getRandom().nextInt(9);
						int rz = this.level().getRandom().nextInt(9);
						this.level().addParticle(ParticleTypes.ENCHANT, this.getX()-4+rx, this.getY() + 3D +ry, this.getZ()-4+rz, 0.0D, 0.0D, 0.0D);
					}
					this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 3.0F, 1.0F);
					return InteractionResult.SUCCESS;
				}
			}else{
				if(player.isCreative()||player == this.getOwner()||this.getOwner()==null){
					if(!player.isSecondaryUseActive()){
						this.catchPassenger(player);
						return InteractionResult.sidedSuccess(this.level().isClientSide);
					}
					if(player.isCrouching()){
						this.stopPassenger();
						player.sendSystemMessage(Component.literal("下车！"));
						return InteractionResult.SUCCESS;
					}
					if(this.getMoveType() == 1) {
						this.setMoveType(0);
						player.sendSystemMessage(Component.literal("Follow me!"));
						return InteractionResult.SUCCESS;
					}
					else if(this.getMoveType() == 0) {
						this.setMoveType(3);
						player.sendSystemMessage(Component.literal("Stay!"));
						return InteractionResult.SUCCESS;
					}
					else if(this.getMoveType() == 3) {
						this.setMoveType(1);
						player.sendSystemMessage(Component.literal("Free Attack!"));
						return InteractionResult.SUCCESS;
					}
				}
			}
		}
		return super.mobInteract(player, hand);
    }
	public boolean canBeCollidedWith() {//
		return !this.isRemoved();
	}
	
	public void catchPassenger(Entity ent){
		for(int i = 0; i < this.seatMaxCount; ++i) {
			if(this.getAnySeat(i)!=null && ((EntityWMSeat)this.getAnySeat(i)).canDrive()){
				this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
				if (!this.level().isClientSide)ent.startRiding(this.getAnySeat(i));
				ridcool = 20;
				this.cheackseat=0;
				this.playSound(SoundEvents.IRON_DOOR_CLOSE, 3.0F, 1.0F);
				break;
			}
		}
	}
	public void stopPassenger(){
		for(int i = 0; i < this.seatMaxCount; ++i) {
			if(this.getAnySeat(i)!=null && !((EntityWMSeat)this.getAnySeat(i)).canDrive()){
				ridcool = 20;
				EntityWMSeat seat = (EntityWMSeat)this.getAnySeat(i);
				if(seat.getNpcPassenger()!=null){
					this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
					if(seat.getNpcPassenger() instanceof IArmy ent)ent.setMove(3,0,0,0);
					seat.getNpcPassenger().stopRiding();
					break;
				}
			}
		}
	}
	public void dropPassenger(){
		for(int i = 1; i < this.seatMaxCount; ++i) {
			if(this.getAnySeat(i)!=null && !((EntityWMSeat)this.getAnySeat(i)).canDrive()){
				ridcool = 20;
				EntityWMSeat seat = (EntityWMSeat)this.getAnySeat(i);
				if(seat.weaponCount==0 && seat.getNpcPassenger()!=null){
					this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
					/*if(seat.getNpcPassenger() instanceof IPara){
						IPara para = (IPara)seat.getNpcPassenger();
						para.setDrop();
					}*/
					seat.getNpcPassenger().stopRiding();
				}
			}
		}
	}
	public void removePassenger(){
		for(int i = 0; i < this.seatMaxCount; ++i) {
			if(this.getAnySeat(i)!=null && !((EntityWMSeat)this.getAnySeat(i)).canDrive()){
				ridcool = 20;
				EntityWMSeat seat = (EntityWMSeat)this.getAnySeat(i);
				if(seat.getNpcPassenger()!=null){
					seat.getNpcPassenger().discard();
				}
			}
		}
	}

	protected boolean canAddPassenger(Entity passenger) {//
		return this.getPassengers().size() < this.seatMaxCount;
	}
	
	public boolean attack1 = false;
	public boolean attack2 = false;
	public boolean attack3 = false;
	public boolean attack4 = false;
	
	public SoundEvent reloadSound1;
	public SoundEvent reloadSound2;
	public SoundEvent reloadSound3;
	public SoundEvent reloadSound4;
	public SoundEvent reloadSound5;
	public int reloadSoundStart1=20;
	public int reloadSoundStart2=20;
	public int reloadSoundStart3=20;
	public int reloadSoundStart4=20;
	public int reloadSoundStart5=20;
	
	public boolean w1power = false;
	public boolean powerfire = false;
	
	public boolean selfheal = false;
	public void weaponActive1() {}
	public void weaponActive2() {}
	public void weaponActive3() {}
	public void weaponActive4() {}
	public void weaponActive5() {}
	
	public float rider_height = 0;
	public SAObjModel obj = null;
	public ResourceLocation tex = null;
	public boolean isglint = true;
	public int cheackseat = 0;
	
	public boolean fastFly = true;
	int showbartime = 0;
	public boolean isShow(){
		return (this.showbartime>0||this.getOwner()!=null)&&this.getTargetType()>1;
	}
	public int getBarType(){
		if(this.getTargetType()==2){
			return 1;
		}else{
			return 0;
		}
	}
	public LivingEntity getBarOwner(){
		return this.getOwner();
	}
	
	public int followTime = 0;
	public int flystart = 0;
	public void tick() {
		super.tick();
		if(this.hurtTime>0){
			if(showbartime<1)showbartime = 70;
		}
		if(showbartime>0)--showbartime;
		if(followTime<50)++followTime;
		if(this.cheackseat<100)++this.cheackseat;
		if(ridcool>0)--ridcool;
		if(selfheal||this.enc_heal>0){
			if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
				++cooltime6;
				if(cooltime6 > 60){
					this.setHealth(this.getHealth() + 1+this.enc_heal);
					cooltime6=0;
				}
			}
		}
		
		if(this.getTargetType()>1 && this.getChoose() && this.firstTarget!=null){
			if(this.firstTarget!=null){
				if(this.clientTarget==null){
					if(this != null && !this.level().isClientSide)PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageFirstTarget(this.getId(), this.firstTarget.getId()));
				}
			}else{
				if(this.clientTarget!=null){
					if(this != null && !this.level().isClientSide)PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageFirstTarget(this.getId(), -1));
				}
			}
		}
		
		float enc_reload = 0F;
		ItemStack this_heldItem = this.getWeaponItem(EquipmentSlot.MAINHAND);
		if(!this_heldItem.isEmpty() && this_heldItem != null){
			if(this_heldItem.isEnchanted()){
				this.setEnc(1);
			}else{
				this.setEnc(0);
			}
			enc_reload = WMEnchantmentHelper.getReloadEnchantment(this_heldItem);
			enc_soul = WMEnchantmentHelper.getSoulEnchantment(this_heldItem);
			enc_armor = WMEnchantmentHelper.getArmorEnchantment(this_heldItem);
			enc_heal = WMEnchantmentHelper.getHealEnchantment(this_heldItem);
			enc_power = WMEnchantmentHelper.getPowerEnchantment(this_heldItem);
			enc_protect = WMEnchantmentHelper.getProtectEnchantment(this_heldItem);
			enc_control = WMEnchantmentHelper.getConnectEnchantment(this_heldItem);
		}

        if(this.getRemain1() <= 0 && this.weaponCount>0 && this.magazine>0){
			reload1+=(1+enc_reload);
			if(reload1 == reload_time1 - reloadSoundStart1 && reloadSound1!=null)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), reloadSound1, SoundSource.WEATHER, 1.0F, 1.0F);
			if(reload1 >= reload_time1){
				this.setRemain1(this.magazine);
				reload1 = 0;
			}
		}
		if(this.getRemain2() <= 0 && this.weaponCount>1 && this.magazine2>0){
			reload2+=(1+enc_reload);
			if(reload2 == reload_time2 - reloadSoundStart2 && reloadSound2!=null)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), reloadSound2, SoundSource.WEATHER, 1.0F, 1.0F);
			if(reload2 >= reload_time2){
				this.setRemain2(this.magazine2);
				reload2 = 0;
			}
		}
		if(this.getRemain3() <= 0 && this.weaponCount>2 && this.magazine3>0){
			reload3+=(1+enc_reload);
			if(reload3 == reload_time3 - reloadSoundStart3 && reloadSound3!=null)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), reloadSound3, SoundSource.WEATHER, 1.0F, 1.0F);
			if(reload3 >= reload_time3){
				this.setRemain3(this.magazine3);
				reload3 = 0;
			}
		}
		if (this.getRemain4() <= 0 && this.weaponCount>3 && this.magazine4>0) {
			reload4+=(1+enc_reload);
			if (reload4 == reload_time4 - reloadSoundStart4 && reloadSound4!=null)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), reloadSound4, SoundSource.WEATHER, 1.0F, 1.0F);
			if (reload4 >= reload_time4) {
				this.setRemain4(this.magazine4);
				reload4 = 0;
			}
		}
	}

	public double[] seatTransX = new double[12];
	public double[] seatTransY = new double[12];
	public double[] seatTransZ = new double[12];
	
	@Override
	protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
		if (!this.hasPassenger(passenger)) return;
		int i = this.getPassengers().indexOf(passenger);
		// 座位相对于载具几何中心的偏移
		double relX = seatPosX[i];
		double relY = seatPosY[i];
		double relZ = seatPosZ[i];
		// 中心偏移（旋转基轴）
		double centerX = 0;
		double centerY = 0;
		double centerZ = 0;
		if(this.VehicleType>2){
			centerY=seatPosY[0];
			centerZ=seatPosZ[0];
		}
		// 额外炮塔偏移
		double extraX = seatRoteX[i];
		double extraY = seatRoteY[i];
		double extraZ = seatRoteZ[i];
		// 确定座位的旋转角度
		float seatYaw;
		if (seatTurret[i]) {
			if (this.getTargetType() == 0 && this.getChange() <= 0 && this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger() != null) {
				seatYaw = this.getFirstSeat().getControllingPassenger().getYHeadRot();
			} else {
				seatYaw = this.turretYaw;
			}
		} else {
			seatYaw = this.getYHeadRot();
		}
		// 载具的俯仰和滚转
		float pitch = this.flyPitch;
		float roll = this.flyRoll;
		// ---------- 核心算法：绕中心偏移点旋转 ----------
		// 1. 将局部坐标平移到以旋转中心为原点
		double px = relX - centerX;
		double py = relY - centerY;
		double pz = relZ - centerZ;
		// 3. 添加额外炮塔偏移（此偏移通常已经设计为与旋转后的方向对齐，因此直接相加）
		px += extraX;
		py += extraY;
		pz += extraZ;
		// 4. 应用载具整体旋转（Yaw, Pitch, Roll）
		Vec3 finalLocal = applyVehicleRotation(new Vec3(px, py, pz), seatYaw, pitch, 0);
		// 5. 平移回原坐标系（加回中心偏移）
		double finalX = finalLocal.x + centerX;
		double finalY = finalLocal.y + centerY;
		double finalZ = finalLocal.z + centerZ;

		// 6. 最终世界坐标
		Vec3 worldPos = new Vec3(this.getX(), this.getY(), this.getZ()).add(finalX, finalY + passenger.getMyRidingOffset(), finalZ);
		callback.accept(passenger, worldPos.x, worldPos.y, worldPos.z);
	}

	/**
	 * 绕Y轴旋转向量（角度制）
	 */
	private static Vec3 applyYawRotation(Vec3 vec, float yawDeg) {
		double rad = Math.toRadians(yawDeg);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		double x = vec.x * cos - vec.z * sin;
		double z = vec.x * sin + vec.z * cos;
		return new Vec3(x, vec.y, z);
	}
	/**
	 * 应用载具的完整旋转（Pitch → Yaw → Roll）
	 * 旋转顺序：先俯仰（绕X轴），再偏航（绕Y轴），最后滚转（绕Z轴）
	 * 这样偏航不会影响Y坐标，符合原逻辑预期。
	 */
	private static Vec3 applyVehicleRotation(Vec3 vec, float yawDeg, float pitchDeg, float rollDeg) {
		// 1. 俯仰（绕X轴）
		double pitchRad = Math.toRadians(pitchDeg);
		double cosPitch = Math.cos(pitchRad);
		double sinPitch = Math.sin(pitchRad);
		double y1 = vec.y * cosPitch - vec.z * sinPitch;
		double z1 = vec.y * sinPitch + vec.z * cosPitch;
		Vec3 pitched = new Vec3(vec.x, y1, z1);
		
		// 2. 偏航（绕Y轴）
		double yawRad = Math.toRadians(yawDeg);
		double cosYaw = Math.cos(yawRad);
		double sinYaw = Math.sin(yawRad);
		double x2 = pitched.x * cosYaw - pitched.z * sinYaw;
		double z2 = pitched.x * sinYaw + pitched.z * cosYaw;
		Vec3 yawed = new Vec3(x2, pitched.y, z2);
		
		// 3. 滚转（绕Z轴）—— 若不需要滚转，可省略或传0
		double rollRad = Math.toRadians(rollDeg);
		double cosRoll = Math.cos(rollRad);
		double sinRoll = Math.sin(rollRad);
		double x3 = yawed.x * cosRoll - yawed.y * sinRoll;
		double y3 = yawed.x * sinRoll + yawed.y * cosRoll;
		return new Vec3(x3, y3, yawed.z);
	}
	
    public void onFireAnimation(float count, float roll) {
		if(roll!=0)RenderParameters.expPitch += roll;
		if(count!=0){
			RenderParameters.rate = Math.min(RenderParameters.rate + 0.07f, 1f);
			float recoilPitchGripFactor = 1.0f;
			float recoilYawGripFactor = 1.0f;
			float recoilPitchBarrelFactor = 1.0f;
			float recoilYawBarrelFactor = 1.0f;
			float recoilPitchStockFactor = 1.0f;
			float recoilYawStockFactor = 1.0f;
			float recoil = count;
			float recoilPitch1 = recoil;//
			float recoilPitch2 = recoil/4;//
			float recoilYaw1 = recoil/10;//
			float recoilYaw2 = recoil/20;//
			float offsetYaw = 0;
			float offsetPitch = 0;
			offsetPitch = recoilPitch1;
			offsetPitch += ((recoilPitch2 * 2) - recoilPitch2);
			offsetPitch *= (recoilPitchGripFactor * recoilPitchBarrelFactor * recoilPitchStockFactor);
			offsetYaw = recoilYaw1;
			offsetYaw *= new Random().nextFloat() * (recoilYaw2 * 2) - recoilYaw2;
			offsetYaw *= recoilYawGripFactor * recoilYawBarrelFactor * recoilYawStockFactor;
			offsetYaw *= RenderParameters.rate;
			offsetYaw *= RenderParameters.phase ? 1 : -1;
			RenderParameters.playerRecoilPitch += offsetPitch;
			if (Math.random() > 0.5f) {
				RenderParameters.playerRecoilYaw += offsetYaw;
			} else {
				RenderParameters.playerRecoilYaw -= offsetYaw;
			}
			RenderParameters.phase = !RenderParameters.phase;
		}
    }
}