package wmlib.rts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import wmlib.rts.RtsMoShiMenu;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.syncher.EntityDataAccessor;              // DataParameter → EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers;           // DataSerializers → EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData;               // EntityDataManager → SynchedEntityData
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.scores.Team;
import wmlib.init.WMModEntities;
import wmlib.api.ITool;
import java.util.List;

import net.minecraft.world.level.levelgen.Heightmap;
public class XiangJiEntity extends TamableAnimal implements ITool{
	public XiangJiEntity(EntityType<XiangJiEntity> type, Level world) {
		super(type, world);
		this.noCulling = true;
		//setNoAi(true);
		//setPersistenceRequired();
		//this.moveControl = new FlyingMoveControl(this, 10, true);
		//this.setNoGravity(true);
	}
	public XiangJiEntity(PlayMessages.SpawnEntity packet, Level worldIn) { 
		super(WMModEntities.XIANG_JI.get(), worldIn); 
	}
	/*public XiangJiEntity(Level level) {
		super(WMModEntities.XIANG_JI.get(), level);
		this.setNoGravity(true);
	}*/

	public Team getTeam() {
		if (this.getOwner()!=null) {
			LivingEntity livingentity = this.getOwner();
			if (livingentity != null) {
				return livingentity.getTeam();
			}
		}
		return super.getTeam();
	}
	public XiangJiEntity getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
	  return null;
	}
	/*@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}*/

	@Override
	public boolean causeFallDamage(float l, float d, DamageSource source) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	/*@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}*/

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.FLYING_SPEED, 0.3);
		return builder;
	}
	
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
    private static final EntityDataAccessor<Integer> moy = SynchedEntityData.<Integer>defineId(XiangJiEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> mox = SynchedEntityData.<Float>defineId(XiangJiEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> moz = SynchedEntityData.<Float>defineId(XiangJiEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> rote = SynchedEntityData.<Integer>defineId(XiangJiEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> choose = SynchedEntityData.<Boolean>defineId(XiangJiEntity.class, EntityDataSerializers.BOOLEAN);
	//private static final EntityDataAccessor<Boolean> open = SynchedEntityData.<Boolean>defineId(XiangJiEntity.class, EntityDataSerializers.BOOLEAN);
	public void addAdditionalSaveData(CompoundTag compound){
		super.addAdditionalSaveData(compound);
		compound.putInt("moy", getMoveY());
		compound.putFloat("mox", getMoveX());
		compound.putFloat("moz", getMoveZ());
		compound.putInt("rote", getRote());
		compound.putBoolean("choose", this.getChoose());
		//compound.putBoolean("open", this.isOpen());
	}
	public void readAdditionalSaveData(CompoundTag compound){
	   super.readAdditionalSaveData(compound);
		this.setMoveY(compound.getInt("moy"));
		this.setMoveX(compound.getFloat("mox"));
		this.setMoveZ(compound.getFloat("moz"));
		this.setRote(compound.getInt("rote"));
		this.setChoose(compound.getBoolean("choose"));
		//this.setChoose(compound.getBoolean("open"));
	}
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(moy, Integer.valueOf(0));
		this.entityData.define(mox, Float.valueOf(0));
		this.entityData.define(moz, Float.valueOf(0));
		this.entityData.define(rote, Integer.valueOf(0));
		this.entityData.define(choose, Boolean.valueOf(false));
		//this.entityData.define(open, Boolean.valueOf(false));
	}
	public int getMoveY() {
		return ((this.entityData.get(moy)).intValue());
	}
	public void setMoveY(int stack) {
		this.entityData.set(moy, Integer.valueOf(stack));
	}
	public float getMoveX() {
		return ((this.entityData.get(mox)).floatValue());
	}
	public void setMoveX(float stack) {
		this.entityData.set(mox, Float.valueOf(stack));
	}
	public float getMoveZ() {
		return ((this.entityData.get(moz)).floatValue());
	}
	public void setMoveZ(float stack) {
		this.entityData.set(moz, Float.valueOf(stack));
	}
	public int getRote() {
		return ((this.entityData.get(rote)).intValue());
	}
	public void setRote(int stack) {
		this.entityData.set(rote, Integer.valueOf(stack));
	}
	public boolean getChoose() {
		return ((this.entityData.get(choose)).booleanValue());
	}
	public void setChoose(boolean stack) {
		this.entityData.set(choose, Boolean.valueOf(stack));
	}
	boolean open = false;
	public boolean isOpen() {
		//return ((this.entityData.get(open)).booleanValue());
		return open;
	}
	public void setOpen(boolean stack) {
		//this.entityData.set(open, Boolean.valueOf(stack));
		open = stack;
	}
	
	int block_height;
	int opentime;
	int rtime;
	float roteyaw = 315F;
	public void tick() {
		super.tick();
		if(!this.getChoose()){
			if(this.getOwner()!=null){
				this.block_height = (int)this.getOwner().getY();
			}else{
				this.block_height = this.level().getHeight(
					Heightmap.Types.WORLD_SURFACE, 
					(int)Math.floor(this.getX()), 
					(int)Math.floor(this.getZ())
				);
			}
			if(this.getY()<this.block_height+5){
				this.setDeltaMovement(this.getDeltaMovement().x, 0.2F, this.getDeltaMovement().z);
			}
		}else{
			if (this.level() instanceof ServerLevel) {
				ServerLevel serverLevel = (ServerLevel) this.level();
				BlockPos entityPos = this.blockPosition();
				int chunkX = entityPos.getX() >> 4;
				int chunkZ = entityPos.getZ() >> 4;
				int range = 2;
				for (int x = chunkX - range; x <= chunkX + range; x++) {
					for (int z = chunkZ - range; z <= chunkZ + range; z++) {
						serverLevel.setChunkForced(x, z, true);
					}
				}
			}
			double mx=0;
			double my=0;
			double mz=0;
			
			if(this.getRote()==1)roteyaw-=5;
			if(this.getRote()==2)roteyaw+=5;
			if(roteyaw >= 360F || roteyaw <= -360F){
				roteyaw = 0;
			}
			this.setYRot(roteyaw);
			this.yHeadRot = this.getYRot();
			this.yBodyRot = this.getYRot();
			float f1 = this.getYRot() * (2 * (float) Math.PI / 360);//
			mx -= Math.sin(f1) * this.getMoveZ();
			mz += Math.cos(f1) * this.getMoveZ();
			mx += Math.sin(this.yHeadRot * 0.0175F - 1.57F)*-this.getMoveX();
			mz -= Math.cos(this.yHeadRot * 0.0175F - 1.57F)*-this.getMoveX();
			my=this.getMoveY();
			this.setDeltaMovement(mx*1.5F, my, mz*1.5F);
			this.setXRot(45.0F);
		}
		
		boolean have = false;
		List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(100D, 100D, 100D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity ent = list.get(k2);
			if(ent instanceof XiangJiEntity){
				XiangJiEntity xj = (XiangJiEntity)ent;
				if(this.getOwner()!=null && xj.getOwner()==this.getOwner()){
					this.discard();
					break;
				}
			}
			if(this.getOwner()!=null && ent == this.getOwner()){
				have = true;
				double dx = ent.getX() - this.getX();
				double dz = ent.getZ() - this.getZ();
				double dy = ent.getY() - this.getY();
				float yawoffset = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
				float yaw = yawoffset * (2 * (float) Math.PI / 360);
				double mox = 0;
				double moz = 0;
				if(!this.getChoose()){
					roteyaw=yaw;
					if(roteyaw >= 360F || roteyaw <= -360F){
						roteyaw = 0;
					}
					this.setYRot(roteyaw);
					this.yHeadRot = this.getYRot();
					this.yBodyRot = this.getYRot();
					double dis = Math.sqrt(dx * dx + dz * dz);
					if(dis>5){
						mox -= Math.sin(yaw);
						moz += Math.cos(yaw);
						this.setDeltaMovement(mox*0.5F, this.getDeltaMovement().y, moz*0.5F);
					}
				}else{
					/*double dis = Math.sqrt(dx*dx + dz*dz + dy*dy);
					if(dis>100){
						mox -= Math.sin(yaw);
						moz += Math.cos(yaw);
						this.setDeltaMovement(mox, this.getDeltaMovement().y, moz);
					}*/
				}
			}
		}
		if(!have){
			++rtime;
			if(rtime>100)this.discard();
		}
		//if(opentime<100)++opentime;
		if(this.getOwner()!=null && this.getOwner() instanceof Player){
			Entity ent = this.getOwner();
			if(this.isOpen()){
				if (ent instanceof ServerPlayer) {
					ServerPlayer sp = (ServerPlayer)ent;
					sp.connection.send(new ClientboundSetCameraPacket(this));
					BlockPos pos = BlockPos.containing(ent.getX(), ent.getY(), ent.getZ());
					NetworkHooks.openScreen(sp, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("RtsMode");
						}
						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new RtsMoShiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
						}
					}, pos);
					this.setOpen(false);
				}
			}
		}
	}
	/*@Override
	public void setYRot(float yRot) {
		// 覆盖设置偏航角的方法，确保始终保持在315度
		super.setYRot(315.0F);
	}

	/*@Override
	public void setXRot(float xRot) {
		// 覆盖设置俯仰角的方法，确保始终保持在45度
		super.setXRot(45.0F);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return false;  // 防止被其他实体推动
	}

	@Override
	protected void pushEntities() {
		// 不执行任何操作，防止推动其他实体
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;  // 防止与其他实体碰撞
	}

	@Override
	public boolean isPushedByFluid() {
		return false;  // 防止被流体推动
	}

	@Override
	public boolean isPickable() {
		return false;  // 防止被玩家点击选中
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return false;  // 防止与任何实体碰撞
	}

	/*@Override
	protected AABB makeBoundingBox() {
		// 创建一个很小的碰撞箱
		return new AABB(
			this.getX() - 0.1D, 
			this.getY() - 0.1D, 
			this.getZ() - 0.1D,
			this.getX() + 0.1D, 
			this.getY() + 0.1D, 
			this.getZ() + 0.1D
		);
	}*/
}
