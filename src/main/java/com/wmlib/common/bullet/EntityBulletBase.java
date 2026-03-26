package wmlib.common.bullet;
// Forge 和 Mod 相关
import net.minecraftforge.fml.ModList;                          // 路径不变
import net.minecraftforge.api.distmarker.Dist;                 // 路径不变
import net.minecraftforge.api.distmarker.OnlyIn;               // 路径不变
// 实体相关
import net.minecraft.world.entity.Entity;                      // Entity（新路径）
import net.minecraft.world.entity.EntityType;                  // EntityType（新路径）
import net.minecraft.world.entity.LivingEntity;                // LivingEntity（新路径）
import net.minecraft.world.entity.monster.Blaze;               // BlazeEntity → Blaze
import net.minecraft.world.entity.projectile.ProjectileUtil;   // ProjectileUtil → ProjectileUtil
import net.minecraft.world.entity.TamableAnimal;               // TamableAnimal → TamableAnimal
import net.minecraft.world.entity.monster.Enemy;               // IMob → Enemy（标记接口）
// 物品和方块
import net.minecraft.world.item.Item;                          // Item（新路径）
import net.minecraft.world.item.ItemStack;                     // ItemStack（新路径）
import net.minecraft.world.item.Items;                         // Items（新路径）
import net.minecraft.world.level.block.state.BlockState;       // BlockState（新路径）
import net.minecraft.core.BlockPos;                            // BlockPos（新路径）
// NBT 和数据同步
import net.minecraft.nbt.CompoundTag;                          // CompoundTag → CompoundTag
import net.minecraft.network.FriendlyByteBuf;                  // PacketBuffer → FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataAccessor;       // EntityDataAccessor → EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers;    // EntityDataSerializers → EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData;               // SynchedEntityData → SynchedEntityData
// 网络和包
import net.minecraft.network.protocol.Packet;                  // IPacket → Packet（泛型需补全）
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket; // SSpawnObjectPacket 替代
import net.minecraftforge.network.NetworkHooks;                // 路径更新
import net.minecraftforge.network.PlayMessages;                // FMLPlayMessages → PlayMessages
// 粒子效果
import net.minecraft.core.particles.ParticleOptions;           // IParticleData → ParticleOptions
import net.minecraft.core.particles.ItemParticleOption;        // ItemParticleData → ItemParticleOption
import net.minecraft.core.particles.ParticleTypes;             // ParticleTypes（新路径）
import net.minecraft.core.particles.BlockParticleOption;       // BlockParticleData → BlockParticleOption
import net.minecraft.core.particles.DustParticleOptions;       // RedstoneParticleData → DustParticleOptions
// 数学和工具
import net.minecraft.util.Mth;                                 // MathHelper → Mth
import net.minecraft.world.phys.Vec3;                          // Vec3（新路径）
import net.minecraft.world.phys.HitResult;                     // RayTraceResult → HitResult
import net.minecraft.world.phys.EntityHitResult;               // EntityHitResult（新路径）
import net.minecraft.world.phys.BlockHitResult;                // BlockHitResult（新路径）
// 世界和伤害
import net.minecraft.world.level.Level;                        // Level → Level
import net.minecraft.world.damagesource.DamageSource;          // DamageSource（新路径）
import net.minecraft.world.level.Explosion;                    // Explosion（新路径）
// 玩家和队伍
import net.minecraft.world.entity.player.Player;               // PlayerEntity → Player
import net.minecraft.world.scores.Team;                        // Team（新路径）
// 属性和 AI
import net.minecraft.world.entity.ai.attributes.AttributeSupplier; // AttributeModifierMap → AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes;    // Attributes（新路径）
// 方块实体和结构
import net.minecraft.world.level.block.entity.BlockEntity;      // TileEntity → BlockEntity
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity; // EndGatewayTileEntity → TheEndGatewayBlockEntity
import net.minecraft.world.level.chunk.LevelChunk;             // IChunk → LevelChunk
// 资源和声音
import net.minecraft.resources.ResourceLocation;               // ResourceLocation（新路径）
import net.minecraft.sounds.SoundEvent;                        // SoundEvent（新路径）
import net.minecraft.sounds.SoundEvents;
// 其他
import net.minecraft.world.level.block.Blocks;                 // Blocks（新路径）

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraftforge.event.ForgeEventFactory; 

import wmlib.common.living.EntityWMSeat;
//////import wmlib.common.WMSoundEvent;
import wmlib.api.IEnemy;
import safx.SagerFX;
import wmlib.client.obj.SAObjModel;
import wmlib.WarMachineLib;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.common.living.EntityWMVehicleBase;
public abstract class EntityBulletBase extends ProjectileBase/* implements IEntityAdditionalSpawnData*/{
	private static final EntityDataAccessor<Float> Gravity = SynchedEntityData.defineId(EntityBulletBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> Type = SynchedEntityData.<Integer>defineId(EntityBulletBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> Ex = SynchedEntityData.<Float>defineId(EntityBulletBase.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<String> Model = SynchedEntityData.<String>defineId(EntityBulletBase.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> Tex = SynchedEntityData.<String>defineId(EntityBulletBase.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> FX = SynchedEntityData.<String>defineId(EntityBulletBase.class, EntityDataSerializers.STRING);
	
	public SAObjModel obj = null;
	public ResourceLocation tex = null;
	public String fxname = null;
	
	public SoundEvent hitEntitySound = SoundEvents.GENERIC_EXPLODE;
	public SoundEvent hitBlockSound = SoundEvents.GENERIC_EXPLODE;
	public SoundEvent selfExpSound = SoundEvents.GENERIC_EXPLODE;
	
	public String fly_sound = null;
	int sound_tick = 0;
	public int sound_cyc = 8;
	public String modid = "wmlib";
	public static SoundEvent getSound(String id,String soundName) {
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.tryParse(id+":"+soundName));
		//SoundEvent sound = new SoundEvent(ResourceLocation.tryParse(id,soundName));
		return sound;
	}

	public boolean usesafx = true;
	public int power = 0;
	public int timemax = 150;//150
	public int time = 0;
	public float gra = 0.0299F;//0.0299F
	public float exlevel = 0;
	public boolean flame = false;
	public LivingEntity shooter = null;
	public boolean isRad = false;

	protected EntityBulletBase(EntityType<? extends EntityBulletBase> p_i48546_1_, Level p_i48546_2_) {
	  super(p_i48546_1_, p_i48546_2_);
	}
	protected EntityBulletBase(EntityType<? extends EntityBulletBase> p_i48547_1_, double p_i48547_2_, double p_i48547_4_, double p_i48547_6_, Level p_i48547_8_) {
	  this(p_i48547_1_, p_i48547_8_);
	  this.setPos(p_i48547_2_, p_i48547_4_, p_i48547_6_);
	}
	protected EntityBulletBase(EntityType<? extends EntityBulletBase> bullet, LivingEntity living, Level p_i48548_3_) {
	  this(bullet, living.getX(), living.getEyeY() - (double)0.1F, living.getZ(), p_i48548_3_);
	  this.setOwner(living);
	  shooter=living;
	}
   
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
	   return true;
	}
	public float speedfly = 1;
	
	public void shootFromRote(float pitch, float yaw, float count, float speed, float bure) {
		speedfly=speed;
		float f = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
		float f1 = -Mth.sin((pitch + count) * ((float)Math.PI / 180F));
		float f2 = Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
		//if(speed>4F&(this instanceof EntityBullet))speed = 4F;//fix
		this.shootF((double)f, (double)f1, (double)f2, speed, bure);
	}
	public void shootFromRotation(Entity shooter, float pitch, float yaw, float count, float speed, float bure) {
		speedfly=speed;
		float f = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
		float f1 = -Mth.sin((pitch + count) * ((float)Math.PI / 180F));
		float f2 = Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
		//if(speed>4F&(this instanceof EntityBullet))speed = 4F;//fix
		this.shootF((double)f, (double)f1, (double)f2, speed, bure);
	}
	public void shootF(double movex, double movey, double movez, float speed, float spread) {
		speedfly=speed;
		Vec3 direction = new Vec3(movex, movey, movez).normalize();
		direction = direction.add(
			this.random.nextGaussian() * 0.0075 * spread,
			this.random.nextGaussian() * 0.0075 * spread,
			this.random.nextGaussian() * 0.0075 * spread
		).scale(speed);
		this.setDeltaMovement(direction);
		double horizontalDistance = direction.horizontalDistance();
		float pitch = (float) Math.toDegrees(Mth.atan2(direction.y, horizontalDistance));
		float yaw = (float) Math.toDegrees(Mth.atan2(direction.x, direction.z));
		this.setYRot(yaw);
		this.setXRot(pitch);
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}
	
	public boolean destroy = false;
	//public boolean usesafx = WarMachineLib.wmlibsa_safx;
 	/*@Override
 	public void writeSpawnData(PacketBuffer data) {
 	}
	
 	@Override
 	public void readSpawnData(PacketBuffer data) {
		if(usesafx)this.createTrailFX_SA();
		if(this.getModel()!=null && this.getModel()!=""){
			obj = new SAObjModel(this.getModel());
			//System.out.println(this.getModel());
		}else{
			//System.out.println("no model nbt");
		}
		if(this.getTex()!=null && this.getTex()!=""){
			tex = ResourceLocation.tryParse(this.getTex());
			//System.out.println(this.getTex());
		}
 	}*/
	
	public void createTrailFX_SA() {
		if(ModList.get().isLoaded("safx")){
			if(this.getFX()!=null&&this.getFX()!=""){
				SagerFX.proxy.createFXOnEntity(this.getFX(), this, 1);
			}else{
				/*if(this instanceof EntityBullet){
					SagerFX.proxy.createFXOnEntity("SABulletTrail", this, 1);
				}else if(this instanceof EntityShell||this instanceof EntityGrenade){
					SagerFX.proxy.createFXOnEntity("SAAPTrail", this, 1);
				}else if(this instanceof EntityMissile){
					SagerFX.proxy.createFXOnEntity("SAMissileTrail", this, 1);
				}*/
			}
		}
	}
   
	@Override
	protected void defineSynchedData() {
		//super.defineSynchedData();
		this.getEntityData().define(Model, "");
		this.getEntityData().define(Tex, "");
		this.getEntityData().define(FX, "");
		this.getEntityData().define(Gravity, 0.0299F);
		this.getEntityData().define(Type, 0);
		this.getEntityData().define(Ex, 0F);
	}
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("Gravity", this.getEntityData().get(Gravity));
		compound.putInt("Type", this.getEntityData().get(Type));
		compound.putFloat("Ex", this.getEntityData().get(Ex));
		compound.putString("Model", this.getModel());
        compound.putString("Tex", this.getTex());
        compound.putString("FX", this.getFX());
	}
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.getEntityData().set(Gravity, compound.getFloat("Gravity"));
		this.getEntityData().set(Type, compound.getInt("Type"));
		this.getEntityData().set(Ex, compound.getFloat("Ex"));
		this.getEntityData().set(Model, compound.getString("Model"));
		this.getEntityData().set(Tex, compound.getString("Tex"));
		this.getEntityData().set(FX, compound.getString("FX"));
	}
	public void setExLevel(float ex){
		//this.exlevel = ex;
		this.getEntityData().set(Ex, Float.valueOf(ex));
	}
	public float getExLevel(){
		return ((this.getEntityData().get(Ex)).floatValue());
	}
	public float getGravityBullet() {
		return ((this.getEntityData().get(Gravity)).floatValue());
	}
	public void setGravity(float s) {
		this.getEntityData().set(Gravity, Float.valueOf(s));
	}
	public int getBulletType() {
		return ((this.getEntityData().get(Type)).intValue());
	}
	public void setBulletType(int y){
		this.getEntityData().set(Type, Integer.valueOf(y));
	}
	protected float getGravity() {
		return getGravityBullet();
	}
    public String getModel() {
		//return ((this.getEntityData().get(Model)));
		return this.getEntityData().get(Model);
	}
	public void setModel(String modelId) {
		//this.getEntityData().set(Model, String.valueOf(new String(s)));
		this.getEntityData().set(Model, modelId);
	}
	public String getTex() {
		return ((this.getEntityData().get(Tex)));
	}
	public void setTex(String s) {
		this.getEntityData().set(Tex, String.valueOf(new String(s)));
	}
	public String getFX() {
		return ((this.getEntityData().get(FX)));
	}
	public void setFX(String s) {
		this.getEntityData().set(FX, String.valueOf(new String(s)));
	}

	/*public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getOwner()!=null){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getOwner().getTeam();
			Team team1 = entity1.getTeam();
			if(team != null && team1 != team && team1 != null){
				return true;
			}else if(entity instanceof IMob && !(this.getOwner() instanceof IMob) && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1)){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
	}*/
	public void expblock(BlockState blockstate, BlockPos pos){}
	
	public static boolean CanAttack(Entity living, Entity target){
		boolean hurt = true;
		if(living.getTeam()==target.getTeam() && living.getTeam()!=null||target == living){
			hurt = false;
		}else if(living instanceof TamableAnimal){
			TamableAnimal soldier = (TamableAnimal) living;
			if(soldier.getOwner() == target){
				hurt = false;
			}
		}else if(living instanceof IEnemy && (target instanceof IEnemy||target instanceof EntityWMVehicleBase && ((EntityWMVehicleBase)target).getTargetType()==2)){
			hurt = false;
		}
		return hurt;
	}
	
	public boolean stopRote = false;
	
	/*boolean lightfire = true;
	int fire=0;*/
	public void tick() {
		super.tick();
		/*if(lightfire){
			++fire;
			this.setSecondsOnFire(1);
		}
		if(lightfire && fire>20){
			this.setSecondsOnFire(0);
			lightfire=false;
		}*/
		
		/*// 获取实体所在区块坐标（更高效）
		BlockPos pos = this.blockPosition();
		int chunkX = pos.getX() >> 4; // 区块坐标计算
		int chunkZ = pos.getZ() >> 4;

		// 获取区块并检查状态
		LevelChunk chunk = this.level().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
		if (chunk == null || !chunk.getStatus().isOrAfter(ChunkStatus.FULL)) {
			this.discard();
		}*/
		++this.time;
		if(fly_sound!=null){
			++this.sound_tick;
			if(this.sound_tick>this.sound_cyc){
				this.playSound(this.getSound(modid,fly_sound),2+this.exlevel, 1.0F);
				this.sound_tick=0;
			}
		}
		if(this.time==1){
			if(this.getModel()!=null && this.getModel()!=""){
				this.obj = new SAObjModel(this.getModel());
				////System.out.println(this.getModel());
			}
			if(this.getTex()!=null && this.getTex()!=""){
				this.tex = ResourceLocation.tryParse(this.getTex());
				////System.out.println(this.getTex());
			}
			this.createTrailFX_SA();
		}
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		//if(this.time>=this.timemax)this.discard();
		if(this.time>=this.timemax){
			BlockPos pos = this.blockPosition();
			BlockState blockstate = this.level().getBlockState(pos);
			this.expblock(blockstate, pos);
			if(this instanceof EntityGrenade){
				//this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
				//this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				this.level().explode(null, this.getX(), this.getY(), this.getZ(), 2F, false, Level.ExplosionInteraction.NONE);
			}
			this.discard();
		}
		
		HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		boolean handled = false;
		/*if (hitResult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
			BlockState blockState = this.level().getBlockState(blockPos);
			if (blockState.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockPos);
				handled = true;
			} 
			else if (blockState.is(Blocks.END_GATEWAY)) {
				BlockEntity blockEntity = this.level().getBlockEntity(blockPos);
				if (blockEntity instanceof TheEndGatewayBlockEntity endGateway) {
					if (TheEndGatewayBlockEntity.canEntityTeleport(this)) {
						endGateway.teleportEntity(this);
						handled = true;
					}
				}
			}
		}*/
		if (hitResult.getType() != HitResult.Type.MISS && !handled) {
			/*if (!ForgeEventFactory.onProjectileImpact(this, hitResult))*/ {
				this.onHit(hitResult);
			}
		}
	
		this.checkInsideBlocks();
		Vec3 vector3d = this.getDeltaMovement();
		double d2 = this.getX() + vector3d.x;
		double d0 = this.getY() + vector3d.y;
		double d1 = this.getZ() + vector3d.z;
		if(!this.onGround() && !stopRote)this.updateRotation();
		float f;
		if (this.isInWater()) {
		 for(int i = 0; i < 4; ++i) {
			float f1 = 0.25F;
			this.level().addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
		 }
		 f = 0.8F;
		} else {
		 f = 0.99F;
		}
		this.setDeltaMovement(vector3d.scale((double)f));
		if (!this.isNoGravity()) {
		 Vec3 vector3d1 = this.getDeltaMovement();
		 this.setDeltaMovement(vector3d1.x, vector3d1.y - (double)this.getGravity(), vector3d1.z);
		}
		this.setPos(d2, d0, d1);
	}
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}

	/**
	* Called when the arrow hits an entity
	*/
	protected void onHitEntity(EntityHitResult result) {
	}

	/**
	* Called when this EntityFireball hits a block or entity.
	*/
	protected void onHitBlock(BlockHitResult pos) {
	}
	
	/*@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}*/
	
    public void shockPlayer(Player player) {
        if(player != null){
			if(player.getVehicle()!=null){
				expPitch = expPitch+this.exlevel*5*0.6F;
			}else{
				expPitch = expPitch+this.exlevel*5;
			}
        }
    }
}
