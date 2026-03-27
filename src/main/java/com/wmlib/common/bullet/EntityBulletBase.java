package wmlib.common.bullet;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import wmlib.WarMachineLib;
import wmlib.common.world.WMExplosionBase;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Explosion;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.particles.BlockParticleData;
import wmlib.common.WMSoundEvent;
import wmlib.api.IEnemy;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import wmlib.common.living.EntityWMSeat;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.particles.RedstoneParticleData;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import safx.SagerFX;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.Constants;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.AxisAlignedBB;
public abstract class EntityBulletBase extends ProjectileBase/* implements IEntityAdditionalSpawnData*/{
	private static final DataParameter<Float> Gravity = EntityDataManager.defineId(EntityBulletBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> ExLevel = EntityDataManager.defineId(EntityBulletBase.class, DataSerializers.FLOAT);
    
	private static final DataParameter<Integer> Type = EntityDataManager.<Integer>defineId(EntityBulletBase.class, DataSerializers.INT);
	private static final DataParameter<String> Model = EntityDataManager.<String>defineId(EntityBulletBase.class, DataSerializers.STRING);
    private static final DataParameter<String> Tex = EntityDataManager.<String>defineId(EntityBulletBase.class, DataSerializers.STRING);
    private static final DataParameter<String> FX = EntityDataManager.<String>defineId(EntityBulletBase.class, DataSerializers.STRING);
	
    public static final String MODEL_TEX = "ModelTex";
	
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
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id, soundName));
		//SoundEvent sound = new SoundEvent(new ResourceLocation(id,soundName));
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
	
	protected EntityBulletBase(EntityType<? extends EntityBulletBase> p_i48546_1_, World p_i48546_2_) {
	  super(p_i48546_1_, p_i48546_2_);
	}
	protected EntityBulletBase(EntityType<? extends EntityBulletBase> p_i48547_1_, double p_i48547_2_, double p_i48547_4_, double p_i48547_6_, World p_i48547_8_) {
	  this(p_i48547_1_, p_i48547_8_);
	  this.setPos(p_i48547_2_, p_i48547_4_, p_i48547_6_);
	}
	protected EntityBulletBase(EntityType<? extends EntityBulletBase> bullet, LivingEntity living, World p_i48548_3_) {
	  this(bullet, living.getX(), living.getEyeY() - (double)0.1F, living.getZ(), p_i48548_3_);
	  this.setOwner(living);
	  shooter=living;
	}
   
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
	   return true;
	}
	public float speedfly = 1;
	
	public void shootFromRotation(Entity entity, float pitch, float yaw, float count, float speed, float bure) {
		speedfly=speed;
		float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
		float f1 = -MathHelper.sin((pitch + count) * ((float)Math.PI / 180F));
		float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
		if(speed>4F&(this instanceof EntityBullet))speed = 4F;//fix
		this.shoot((double)f, (double)f1, (double)f2, speed, bure);
	}
	public void shoot(double movex, double movey, double movez, float speed, float spread) {
		speedfly=speed;
		Vector3d vector3d = (new Vector3d(movex, movey, movez)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)spread, this.random.nextGaussian() * (double)0.0075F * (double)spread, this.random.nextGaussian() * (double)0.0075F * (double)spread).scale((double)speed);
		this.setDeltaMovement(vector3d);
		float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
		float x = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
		this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
		this.xRot = x;
		this.yRotO = this.yRot;
		this.xRotO = this.xRot;
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
			System.out.println(this.getModel());
		}else{
			System.out.println("no model nbt");
		}
		if(this.getTex()!=null && this.getTex()!=""){
			tex = new ResourceLocation(this.getTex());
			System.out.println(this.getTex());
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
		this.getEntityData().define(ExLevel, 0F);
		this.getEntityData().define(Type, 0);
	}
	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("Gravity", this.getEntityData().get(Gravity));
		compound.putFloat("ExLevel", this.getEntityData().get(ExLevel));
		compound.putInt("Type", this.getEntityData().get(Type));
        compound.putString(MODEL_TEX, this.getModel());
        compound.putString("Tex", this.getTex());
        compound.putString("FX", this.getFX());
	}
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.getEntityData().set(Gravity, compound.getFloat("Gravity"));
		this.getEntityData().set(ExLevel, compound.getFloat("ExLevel"));
		this.getEntityData().set(Type, compound.getInt("Type"));
        if (compound.contains(MODEL_TEX, Constants.NBT.TAG_STRING)) {
            this.setModel(compound.getString(MODEL_TEX));
        }
		this.getEntityData().set(Model, compound.getString("Model"));
		this.getEntityData().set(Tex, compound.getString("Tex"));
		this.getEntityData().set(FX, compound.getString("FX"));
	}
   
	public float getGravityBullet() {
		return ((this.getEntityData().get(Gravity)).floatValue());
	}
	public void setGravity(float s) {
		this.getEntityData().set(Gravity, Float.valueOf(s));
	}
	
	public float getExLevel() {
		return ((this.getEntityData().get(ExLevel)).floatValue());
	}
	public void setExLevel(float s) {
		this.getEntityData().set(ExLevel, Float.valueOf(s));
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
	public boolean stopRote = false;
	public void tick() {
		super.tick();
		IChunk chunk = this.level.getChunk(new BlockPos(this.getX(), this.getY(), this.getZ()));
		if (chunk.getStatus() != ChunkStatus.FULL){//null
			this.remove();
		}
		++this.time;
		if(fly_sound!=null){
			++this.sound_tick;
			if(this.sound_tick>this.sound_cyc){
				this.playSound(this.getSound(modid,fly_sound),2+this.getExLevel(), 1.0F);
				this.sound_tick=0;
			}
		}
		if(this.time==1){
			this.exlevel=this.getExLevel();
			if(this.getModel()!=null && this.getModel()!=""){
				this.obj = new SAObjModel(this.getModel());
				//System.out.println(this.getModel());
			}
			if(this.getTex()!=null && this.getTex()!=""){
				this.tex = new ResourceLocation(this.getTex());
				//System.out.println(this.getTex());
			}
			this.createTrailFX_SA();
		}
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		if(this.time>=this.timemax){
			BlockPos pos = this.blockPosition();
			BlockState blockstate = this.level.getBlockState(pos);
			this.expblock(blockstate, pos);
			if(this instanceof EntityGrenade){
				//this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
				//this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2F, false, Explosion.Mode.NONE);
			}
			this.remove();
		}
		
		this.checkInsideBlocks();
		Vector3d vector3d = this.getDeltaMovement();
		double d2 = this.getX() + vector3d.x;
		double d0 = this.getY() + vector3d.y;
		double d1 = this.getZ() + vector3d.z;
		if(!this.isOnGround() && !stopRote)this.updateRotation();
		float f;
		if (this.isInWater()) {
		 for(int i = 0; i < 4; ++i) {
			float f1 = 0.25F;
			this.level.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
		 }
		 f = 0.8F;
		} else {
		 f = 0.99F;
		}
		this.setDeltaMovement(vector3d.scale((double)f));
		if (!this.isNoGravity()) {
			Vector3d vector3d1 = this.getDeltaMovement();
			this.setDeltaMovement(vector3d1.x, vector3d1.y - (double)this.getGravity(), vector3d1.z);
		}
		this.setPos(d2, d0, d1);
		
		RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
		boolean flag1 = false;
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getBlockPos();
			BlockState blockstate = this.level.getBlockState(blockpos);
			if (blockstate.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockpos);
				flag1 = true;
			} else if (blockstate.is(Blocks.END_GATEWAY)) {
				TileEntity tileentity = this.level.getBlockEntity(blockpos);
				if (tileentity instanceof EndGatewayTileEntity && EndGatewayTileEntity.canEntityTeleport(this)) {
				   ((EndGatewayTileEntity)tileentity).teleportEntity(this);
				}
				flag1 = true;
			}
		}
		if (raytraceresult.getType() != RayTraceResult.Type.MISS && !flag1 && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onHit(raytraceresult);
		}
	}
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}

	/**
	* Called when the arrow hits an entity
	*/
	protected void onHitEntity(EntityRayTraceResult result) {
	}

	/**
	* Called when this EntityFireball hits a block or entity.
	*/
	protected void onHitBlock(BlockRayTraceResult pos) {
	}
	@Override
	public IPacket<?> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
	
    public void shockPlayer(PlayerEntity player) {
        if(player != null){
			if(player.getVehicle()!=null){
				WarMachineLib.proxy.onFireAnimation(0,this.getExLevel()*0.2F);
				//expPitch = expPitch+this.getExLevel()*5*0.6F;
			}else{
				WarMachineLib.proxy.onFireAnimation(0,this.getExLevel()*4);
				//expPitch = expPitch+this.getExLevel()*5;
			}
        }
    }
}
