package wmlib.common.living;
import java.util.Random;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.SoundEvent;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ActionResultType;

import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Blocks;

import net.minecraft.item.ItemStack;

import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.EntityType;
import net.minecraft.world.Explosion;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.FMLPlayMessages;
import wmlib.common.WMSoundEvent;
import net.minecraft.inventory.EquipmentSlotType;
import wmlib.util.WMEnchantmentHelper;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.ResourceLocation;
import wmlib.common.item.ItemBless;
import safx.SagerFX;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.nbt.CompoundNBT;
import wmlib.common.bullet.EntityShell;
import wmlib.common.bullet.EntityMissile;
import wmlib.api.IHealthBar;
import wmlib.api.IPara;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.client.Minecraft;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.item.PickaxeItem;
import wmlib.WarMachineLib;
import wmlib.api.ITool;
import wmlib.api.IArmy;
public abstract class WeaponVehicleBase extends EntityWMVehicleBase implements IHealthBar,ITool{
	protected PathNavigator navigationsa;
	public WeaponVehicleBase(EntityType<? extends WeaponVehicleBase> sodier, World worldIn) {
		super(sodier, worldIn);
		this.navigationsa = this.createNavigationSA(worldIn);
	}

	protected PathNavigator createNavigationSA(World p_175447_1_) {
	  return new GroundPathNavigator(this, p_175447_1_);
	}
	public PathNavigator getNavigation() {//
		return this.navigationsa;
	}
	public boolean rideableUnderWater() {
	  return true;
	}
	
	public boolean fireproduct=false;
	public boolean fireImmune() {
		return fireproduct;
	}
	
	public SoundEvent lockAlertSound = SoundEvents.LAVA_EXTINGUISH;
	//public SoundEvent lockTargetSound = null;
	public SoundEvent destroySoundStart = SoundEvents.GENERIC_EXPLODE;
	public SoundEvent destroySoundEnd = SoundEvents.DRAGON_FIREBALL_EXPLODE;
	
	public Vector4f transformPosition(Matrix4f transform, float x, float y, float z) {
		Vector4f result = new Vector4f(x, y, z, 1.0F);
		result.transform(transform);
		return result;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Matrix4f getClientVehicleTransform(float ticks, PlayerEntity player) {
		Matrix4f transform = Matrix4f.createTranslateMatrix(
			(float) MathHelper.lerp(ticks, xo, getX()),
			(float) MathHelper.lerp(ticks, yo + 2.375f, getY() + 2.375f),
			(float) MathHelper.lerp(ticks, zo, getZ())
		);
		if (this.VehicleType > 2 && this.getArmyType1() == 0) {
			transform.multiply(Vector3f.YP.rotationDegrees((float) (-MathHelper.lerp(ticks, this.turretYawO, this.turretYaw))));
			transform.multiply(Vector3f.XP.rotationDegrees((float) (MathHelper.lerp(ticks, this.turretPitchO, this.turretPitch))));
		} else {
			transform.multiply(Vector3f.YP.rotationDegrees((float) (-MathHelper.lerp(ticks, player.yRotO, player.yRot))));
			transform.multiply(Vector3f.XP.rotationDegrees((float) (MathHelper.lerp(ticks, player.xRotO, player.xRot))));
		}
		transform.multiply(Vector3f.ZP.rotationDegrees(this.flyRoll));
		return transform;
	}
	
    @OnlyIn(Dist.CLIENT)
    public @Nullable Vector2f getCameraRotation(float partialTicks, PlayerEntity player, boolean turret, boolean isThird, boolean isFirst) {
		if(isThird && !turret||turret && isFirst){
			return new Vector2f(player.yRot, player.xRot);
		}else{
			return null;
		}
		
    }
    @OnlyIn(Dist.CLIENT)
    public Vector3d getCameraPosition(float partialTicks, PlayerEntity player, boolean isZoom, boolean turret, boolean isThird, boolean isFirst) {
		Matrix4f transform = getClientVehicleTransform(partialTicks, player);
		Minecraft mc = Minecraft.getInstance();
		Vector4f maxCameraPosition = transformPosition(transform, 
		this. seatView3X, -this. seatView3Y*1.5F, this. seatView3Z*2);
		
		if(this.VehicleType>2 && isZoom && isThird)maxCameraPosition = transformPosition(transform, 
		this.airzoom_x, -this.airzoom_y, this.airzoom_z);
		
		if(turret && isFirst)maxCameraPosition = transformPosition(transform, 
		this.heligun_x, this.heligun_y, this.heligun_z);
		
		Vector3d finalPos = this.getMaxZoom(transform, maxCameraPosition);
		if(isThird && !turret||turret && isFirst){
			return finalPos;
		}else{
			return null;
		}
    }
	@OnlyIn(Dist.CLIENT)
    public Vector3d getMaxZoom(Matrix4f transform, Vector4f maxCameraPos) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        Vector4f vehiclePos = transformPosition(transform, 0, 0, 0);
        Vector3d maxCameraPosVec3 = new Vector3d(maxCameraPos.x(), maxCameraPos.y(), maxCameraPos.z());
        Vector3d vehiclePosVec3 = new Vector3d(vehiclePos.x(), vehiclePos.y(), vehiclePos.z());
        Vector3d toVec = vehiclePosVec3.vectorTo(maxCameraPosVec3);
        if (player != null) {
			RayTraceResult hitresult = player.level.clip(new RayTraceContext(vehiclePosVec3, vehiclePosVec3.add(toVec).add(toVec.normalize().scale(1)), RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, player));
			if (hitresult.getType() == RayTraceResult.Type.BLOCK) {
				return hitresult.getLocation().add(toVec.normalize().scale(-1));
			}
		}
        return maxCameraPosVec3;
    }
	
    private static final DataParameter<Integer> enc = EntityDataManager.<Integer>defineId(WeaponVehicleBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> tc = EntityDataManager.<Integer>defineId(WeaponVehicleBase.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound){
		super.addAdditionalSaveData(compound);
		compound.putInt("enc", getEnc());
		compound.putInt("tc", getTeamC());
	}
	public void readAdditionalSaveData(CompoundNBT compound){
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
	public double[] seatPosX = new double[12];
	public double[] seatPosY = new double[12];
	public double[] seatPosZ = new double[12];
	public double[] seatRoteX = new double[12];
	public double[] seatRoteY = new double[12];
	public double[] seatRoteZ = new double[12];
	
	public ResourceLocation fire1tex = null;
	public ResourceLocation fire2tex = null;
	public ResourceLocation fire3tex = null;
	public ResourceLocation fire4tex = null;
	
	public boolean isSpaceShip = false;
	public boolean startShield = false;
    public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity = source.getDirectEntity();
    	Entity living = source.getEntity();
		if(this.enc_armor>0){
			if(source.isExplosion()){
				par2 = par2*(6F-this.enc_armor)/5F;
			}else{
				par2 = par2*(11F-this.enc_armor)/10F;
			}
		}
		if(startShield)par2=par2*0.5F;
		if(entity != null){
			if (entity instanceof LivingEntity) {
				LivingEntity attacker = (LivingEntity)entity;
				ItemStack heldItem = attacker.getMainHandItem();
				if(heldItem.getItem() instanceof PickaxeItem &&this.getHealth()<this.getMaxHealth()){
					this.setHealth(this.getHealth()+1+par2);
					this.playSound(SoundEvents.ANVIL_USE, 2.0F, 1.0F);
					heldItem.hurtAndBreak(1, attacker, (ent) -> {
					ent.broadcastBreakEvent(attacker.getUsedItemHand());});
					//par2=0;
					return false;
				}
			}
			if(this.getControllingPassenger()!=null && entity == this.getControllingPassenger()||entity.getVehicle()==this||
				this.getOwner()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
				return false;
			}else{
				if(haveTurretArmor) {
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
				return super.hurt(source, par2);
			}
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
			if(par2<1)par2=0;
			return super.hurt(source, par2);
		}else{
			return super.hurt(source, par2);
		}
    }
	
	
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && !(entity instanceof EntityWMSeat)){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			boolean canattack = true;
			if(entity instanceof TameableEntity){
				TameableEntity soldier = (TameableEntity)entity;
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
				if(entity instanceof IMob && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1))canattack= false;
			}
			return canattack;
    	}else{
			return false;
		}
	}
	
	protected void tickDeath() {
		++this.deathTime;
		if(this.deathTime == 1){
			//this.playSound(SASoundEvent.tank_explode.get(), 3.0F+this.getBbWidth()*0.1F, 1.0F);
			this.playSound(this.destroySoundStart, 3.0F+this.getBbWidth()*0.1F, 1.0F);
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 3+this.getBbWidth()*0.1F, false, Explosion.Mode.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp1", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
		}
		if(this.deathTime >= 120) {
			this.remove();
			//this.playSound(SASoundEvent.wreck_explosion.get(), 2.0F+this.getBbWidth()*0.1F, 1.0F);
			this.playSound(this.destroySoundEnd, 3.0F+this.getBbWidth()*0.1F, 1.0F);
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2+this.getBbWidth()*0.1F, false, Explosion.Mode.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp2", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
		}
	}
	
	private void dropItemStack(ItemStack item) {
		ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), item);
		this.level.addFreshEntity(itementity);
	}
	
	public int ridcool = 0;
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if(player.getVehicle()==null && ridcool == 0){
			ItemStack heldItem = player.getItemInHand(hand);
			ItemStack this_heldItem = this.getWeaponItem(EquipmentSlotType.MAINHAND);
			Item item = heldItem.getItem();
			if(heldItem!=null && !heldItem.isEmpty()){
				/*if(this.getOwner()==null&&item == Items.REDSTONE_BLOCK && heldItem!=null && !heldItem.isEmpty() && this.getTargetType()!=2){
					this.tame(player);
					player.sendMessage(new TranslationTextComponent("Vehicle occupation successful!", new Object[0]), player.getUUID());
					heldItem.shrink(1);
					return ActionResultType.PASS;
				}*/
				if(item instanceof ItemBless && this_heldItem.isEmpty()&&this_heldItem!=null && heldItem.isEnchanted() && this.getEnc()==0){
					if(!this_heldItem.isEmpty()&&this_heldItem!=null)this.dropItemStack(this_heldItem);
					if(!this.level.isClientSide){
						this.setWeaponItem(EquipmentSlotType.MAINHAND, heldItem.copy());
						player.sendMessage(new TranslationTextComponent("Oh, That's good~", new Object[0]), player.getUUID());
					}
					heldItem.shrink(1);
					//heldItem.removeTagKey("Enchantments");
					ridcool = 20;
					for(int i = 0; i < 20; ++i){
						int ry = this.level.random.nextInt(3);
						this.level.addParticle(ParticleTypes.ENCHANT, this.getX()-2, this.getY() + 3D +ry, this.getZ()+2, 0.0D, 0.0D, 0.0D);
						this.level.addParticle(ParticleTypes.ENCHANT, this.getX()+2, this.getY() + 3D +ry, this.getZ()-1, 0.0D, 0.0D, 0.0D);
						int rx = this.level.random.nextInt(9);
						int rz = this.level.random.nextInt(9);
						this.level.addParticle(ParticleTypes.ENCHANT, this.getX()-4+rx, this.getY() + 3D +ry, this.getZ()-4+rz, 0.0D, 0.0D, 0.0D);
					}
					this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 3.0F, 1.0F);
					return ActionResultType.PASS;
				}
			}else{
				if(player.isCreative()||player == this.getOwner()||this.getOwner()==null){
					if(!player.isSecondaryUseActive()){
						this.catchPassenger(player);
						/*player.yRot=this.turretYaw;
						player.yHeadRot=this.turretYaw;
						player.xRot=this.turretPitch;*/
						return ActionResultType.sidedSuccess(this.level.isClientSide);
					}
					if(player.isCrouching()){
						this.stopPassenger();
						player.sendMessage(new TranslationTextComponent("下车！", new Object[0]), player.getUUID());
					}
					if(this.getMoveType() == 1) {
						this.setMoveType(0);
						player.sendMessage(new TranslationTextComponent("Follow me!", new Object[0]), player.getUUID());
						return ActionResultType.PASS;
					}
					else if(this.getMoveType() == 0) {
						this.setMoveType(3);
						player.sendMessage(new TranslationTextComponent("Stay!", new Object[0]), player.getUUID());
						return ActionResultType.PASS;
					}
					else if(this.getMoveType() == 3) {
						this.setMoveType(1);
						player.sendMessage(new TranslationTextComponent("Free Attack!", new Object[0]), player.getUUID());
						return ActionResultType.PASS;
					}
				}
			}
		}
		return super.mobInteract(player, hand);
    }
	public boolean canBeCollidedWith() {//
		return !this.removed;
	}
	
	public void catchPassenger(Entity ent){
		for(int i = 0; i < this.seatMaxCount; ++i) {
			if(this.getAnySeat(i)!=null && ((EntityWMSeat)this.getAnySeat(i)).canDrive()){
				this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
				if (!this.level.isClientSide)ent.startRiding(this.getAnySeat(i));
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
					if(seat.getNpcPassenger() instanceof IArmy){
						((IArmy)seat.getNpcPassenger()).setMove(3,0,0,0);
					}
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
					seat.getNpcPassenger().remove();
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
		float enc_reload = 0F;
		
		ItemStack this_heldItem = this.getWeaponItem(EquipmentSlotType.MAINHAND);
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
        if(weaponCount>0&&this.getRemain1() <= 0){
			reload1+=(1+enc_reload);
			if(reload1 == reload_time1 - reloadSoundStart1)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), reloadSound1, SoundCategory.WEATHER, 1.0F, 1.0F);
			if(reload1 >= reload_time1){
				this.setRemain1(this.magazine);
				reload1 = 0;
			}
		}
		if(weaponCount>1&&this.getRemain2() <= 0){
			reload2+=(1+enc_reload);
			if(reload2 == reload_time2 - reloadSoundStart2)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), reloadSound2, SoundCategory.WEATHER, 1.0F, 1.0F);
			if(reload2 >= reload_time2){
				this.setRemain2(this.magazine2);
				reload2 = 0;
			}
		}
		if(weaponCount>2&&this.getRemain3() <= 0){
			reload3+=(1+enc_reload);
			if(reload3 == reload_time3 - reloadSoundStart3)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), reloadSound3, SoundCategory.WEATHER, 1.0F, 1.0F);
			if(reload3 >= reload_time3){
				this.setRemain3(this.magazine3);
				reload3 = 0;
			}
		}
		if (weaponCount>3&&this.getRemain4() <= 0) {
			reload4+=(1+enc_reload);
			if (reload4 == reload_time4 - reloadSoundStart4)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), reloadSound4, SoundCategory.WEATHER, 1.0F, 1.0F);
			if (reload4 >= reload_time4) {
				this.setRemain4(this.magazine4);
				reload4 = 0;
			}
		}
	}
	
	public void positionRider(Entity passenger){
        if (this.hasPassenger(passenger))
        {
			int i = this.getPassengers().indexOf(passenger);
			double bx = 0;
			double by = 0;
			double bz = 0;
			float rpitch = this.flyPitch;
			bx = seatPosX[i];
			by = seatPosY[i];
			bz = seatPosZ[i];
            {
				double ix = 0;
				double iy = 0;
				double iz = 0;
				float roteyaw = 0;
				if (this.seatTurret[i]) {
					if(this.getTargetType()==0){
						if(this.getChange()>0){
							roteyaw = this.turretYaw;
						}else{
							if(this.getControllingPassenger()!=null)roteyaw = this.getControllingPassenger().getYHeadRot();
						}
					}else{
						roteyaw = this.turretYaw;
					}
				}else{
					roteyaw = this.yRot;
				}
				float f1 = roteyaw * (2 * (float) Math.PI / 360);
				ix -= MathHelper.sin(f1) * bz;
				iz += MathHelper.cos(f1) * bz;
				ix -= MathHelper.sin(f1 - 1.57F) * bx;
				iz += MathHelper.cos(f1 - 1.57F) * bx;
				double ix2 = 0;
				double iz2 = 0;
				//float f12 = passenger.yRot * (2 * (float) Math.PI / 360);
				//float f12 = this.turretYaw * (2 * (float) Math.PI / 360);
				float f12 = this.yRot * 0.01745329252F;
				ix2 -= MathHelper.sin(f12) * seatRoteZ[i];
				iz2 += MathHelper.cos(f12) * seatRoteZ[i];
				ix2 -= MathHelper.sin(f12 - 1.57F) * seatRoteX[i];
				iz2 += MathHelper.cos(f12 - 1.57F) * seatRoteX[i];
				//ix2 -= MathHelper.sin(f12 + 1F) * seatRoteX[i];
				//iz2 += MathHelper.cos(f12 + 1F) * seatRoteX[i];
				double b = 0;
				double b2 = 0;
				double a = 0;
				double ax = 0;
				double az = 0;
				{
					b =  bz * MathHelper.sin(rpitch  * (1 * (float) Math.PI / 180)) *  1.25D;
					a =  bz * Math.abs(Math.cos(rpitch  * (1 * (float) Math.PI / 180))) *  1.0D;
					ax -= MathHelper.sin(roteyaw * (2 * (float) Math.PI / 360)) * a;
					az += MathHelper.cos(roteyaw * (2 * (float) Math.PI / 360)) * a;
					ax -= MathHelper.sin(roteyaw * (2 * (float) Math.PI / 360) - 1.57F) * bx;
					az += MathHelper.cos(roteyaw * (2 * (float) Math.PI / 360) - 1.57F) * bx;
				}
				Vector3d vec3d = new Vector3d(ax + ix2, by + seatRoteY[i] + passenger.getMyRidingOffset() - b, az + iz2);
				passenger.setPos(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z);
            }
        }
    }
	
    public void onFireAnimation(float count, float roll) {
		WarMachineLib.proxy.onFireAnimation(count,roll);
    }
}