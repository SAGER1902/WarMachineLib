package advancearmy.entity.land;
import javax.annotation.Nullable;
import net.minecraftforge.fml.ModList;

import net.minecraft.world.level.Level;                            // 替换 World
import net.minecraft.world.level.ServerLevelAccessor;              // 替换 IServerWorld
import net.minecraft.world.entity.ai.attributes.AttributeSupplier; // 替换 AttributeModifierMap
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;                   // 替换 PlayerEntity
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;                                     // 替换 MathHelper
import net.minecraft.world.entity.PathfinderMob;                   // 替换 CreatureEntity (通常)
import net.minecraft.world.entity.Mob;                             // 或者使用 Mob
import net.minecraft.nbt.CompoundTag;                             // 替换 CompoundNBT
import net.minecraft.world.entity.SpawnGroupData;                  // 替换 ILivingEntityData
import net.minecraft.world.entity.MobSpawnType;                    // 替换 SpawnReason
import net.minecraft.world.DifficultyInstance;
import net.minecraftforge.network.PlayMessages;
import net.minecraft.world.entity.Entity;
import java.util.List;
import net.minecraft.world.entity.monster.Enemy;
import wmlib.api.ITool;

import safx.SagerFX;
import wmlib.api.IArmy;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.AI_TankSet;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.util.SummonEntity;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.event.SASoundEvent;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import advancearmy.init.ModEntities;
import advancearmy.util.TargetSelect;
public class EntitySA_Bike extends WeaponVehicleBase implements IArmy{
	public EntitySA_Bike(EntityType<? extends EntitySA_Bike> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 0.8F;
		seatPosZ[0] = 0.1F;
		seatCanFire[0]=true;
		seatView3X=0F;
		seatView3Y=-1F;
		seatView3Z=-4F;
		//this.seatNoThird = true;
		this.VehicleType = 2;
		this.seatProtect = 1F;
        this.MoveSpeed = 0.05F;
        this.turnSpeed = 4F;
		this.turretSpeed = 1F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
	}
	public ResourceLocation getIcon1(){
		return ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_bike.png");
	}
	public ResourceLocation getIcon2(){
		return null;
	}
	public void stopUnitPassenger(){
		this.stopPassenger();
	}
	public void setAttack(LivingEntity target){
		this.setTarget(target);
	}
	public void setSelect(boolean stack){
		this.setChoose(stack);
	}
	public void setMove(int id, int x, int y, int z){
		this.setMoveType(id);
		this.setMovePosX(x);
		this.setMovePosY(y);
		this.setMovePosZ(z);
	}
	public boolean getSelect(){
		return this.getChoose() && this.getTargetType()==3;
	}
	public boolean isDrive(){
		return this.getVehicle()!=null||this.getTargetType()<2;
	}
	public boolean isCommander(LivingEntity owner){
		return this.getOwner() == owner;
	}
	public LivingEntity getArmyOwner(){
		return this.getOwner();
	}
	public int getArmyMoveT(){
		return this.getMoveType();
	}

	public int getTeamCount(){
		return getTeamC();
	}
	public void setTeamCount(int id){
		setTeamC(id);
	}
	public int getArmyMoveX(){
		return this.getMovePosX();
	}
	public int getArmyMoveY(){
		return this.getMovePosY();
	}
	public int getArmyMoveZ(){
		return this.getMovePosZ();
	}
	public boolean isShow(){
		return false;
	}
	
	public float rote_wheel = 0;
	public EntitySA_Bike(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_BIKE.get(), worldIn);
	}
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(this.canAddPassenger(null)){
				EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(), this.level());
				seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
				this.level().addFreshEntity(seat);
				seat.startRiding(this);
			}
			float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);//
			AI_TankSet.set(this, SASoundEvent.move_vodnik.get(),0.15F, f1, this.MoveSpeed, 0.1F, false);//
			if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
				if(this.getTargetType()>0)this.setTargetType(0);//
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				Player player = (Player)seat.getControllingPassenger();
				PL_LandMove.moveCarMode(player, this, this.MoveSpeed, turnSpeed);
			}else{
				if(this.getTargetType()==0)this.setTargetType(1);//
			}
			if(this.getTargetType()>0){
				if (this.getFirstSeat() != null){
					EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
					if(seat.getTargetType()==0&&this.enc_soul==0){
						if(this.getTargetType()!=1)this.setTargetType(1);
					}
					if(seat.getTargetType()==2){
						if(this.getTargetType()!=2)this.setTargetType(2);
					}
					if(seat.getTargetType()==3||this.enc_soul>0)if(this.getTargetType()!=3){
						this.setTargetType(3);
					}
					if(seat.getNpcPassenger()!=null){
						PathfinderMob gunner = (PathfinderMob)seat.getNpcPassenger();
						this.ai_move(gunner.getTarget(), MoveSpeed, 30, 30);
					}
				}
			}
			if(this.throttle != 0){
				if(this.getForwardMove()==0 && this.throttle < 0.09 && this.throttle > -0.09) {
					this.throttle = 0;
				}
				if(this.throttle > 0){
					this.throttle = this.throttle - 0.1F;
				}
				if(this.throttle < 0){
					this.throttle = this.throttle + 0.1F;
				}
			}
			
			if (this.getStrafingMove() < 0){
				if(rote_wheel>-40)rote_wheel-=5;
			}
			if (this.getStrafingMove() > 0){
				if(rote_wheel<40)rote_wheel+=5;
			}
			if(this.getStrafingMove()==0F){
				if(rote_wheel>0){
					rote_wheel-=5;
				}else if(rote_wheel<0){
					rote_wheel+=5;
				}
			}
			if(this.getTargetType()!=1){
				if(this.tracktick % 5 == 0 && this.throttle>0){//
					List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(1D));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity attackentity = list.get(k2);
						if(attackentity instanceof LivingEntity && ((LivingEntity)attackentity).getHealth()>0){
							if(this.CanAttack(attackentity))attackentity.hurt(this.damageSources().thrown(this, this), this.getBbHeight()*this.getBbWidth()+this.throttle);
						}
					}
				}
			}
		}
	}
	
    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getTargetType()!=1){
			if(this.getTargetType()==2){
				return TargetSelect.mobCanAttack(this,entity,this.getTarget());
			}else{
				return entity instanceof Enemy;
			}
    	}else{
			return false;
		}
    }	
	float body_yaw;
	public void ai_move(LivingEntity target, float MoveSpeed, double max, double range1) {
		boolean crash = false;
		float angle = 0;
		{
			if(this.getMoveType()==0 && this.getOwner()!=null && followTime>30){
				if(this.distanceTo(this.getOwner())>12){
					this.setMovePosX((int)this.getOwner().getX());
					this.setMovePosY((int)this.getOwner().getY());
					this.setMovePosZ((int)this.getOwner().getZ());
					followTime=0;
				}
			}
			if((this.getMovePosX()!=0||this.getMovePosZ()!=0)&&(this.getMoveType()==0||this.getMoveType()==2||!this.isAttacking()&&this.getMoveType()==4)){
				double dx = this.getMovePosX() - this.getX();
				double dz = this.getMovePosZ() - this.getZ();
				double ddx = Math.abs(dx);
				double ddz = Math.abs(dz);
				if(ddx>5||ddz>5){
					this.targetYaw = (float) Mth.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
					this.targetYaw = this.clampYaw(this.targetYaw);
					this.setForwardMove(2);
					float f3 = (float) (this.targetYaw-this.getYRot());// -180 ~ 0 ~ 180
					f3 = this.clampYaw(f3);
					if(f3>2){// +1
						this.body_yaw+=2;
					}else if(f3<-2){// -1
						this.body_yaw-=2;
					}
					if(f3>-5F&&f3<5F){
						this.body_yaw = this.targetYaw;
					}
					this.setYHeadRot(this.body_yaw);//位移
					this.setYRot(this.body_yaw);
					this.yRotO = this.yBodyRot = this.body_yaw;//
				}else{
					if(this.getMoveType()==2){
						this.setMoveType(3);
						this.setMovePosX(0);
						//this.setMovePosY(0);
						this.setMovePosZ(0);
					}
				}
			}
			if (target != null) {
				if (!target.isInvisible()) {//target
					if (target.getHealth() > 0.0F) {
						double dx = target.getX() - this.getX();
						double dz = target.getZ() - this.getZ();
						double dis = Math.sqrt(dx * dx + dz * dz);

						if(this.getMoveType() != 3/*&&!this.getSelect()*/){//
							this.targetYaw = (float) Mth.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
							this.targetYaw = this.clampYaw(this.targetYaw);
							this.setForwardMove(2);
							if (dis>10) {
								float f3 = (float) (this.targetYaw-this.getYRot());// -180 ~ 0 ~ 180
								f3 = this.clampYaw(f3);
								if(f3>2){// +1
									this.body_yaw+=2;
								}else if(f3<-2){// -1
									this.body_yaw-=2;
								}
								if(f3>-5F&&f3<5F){
									this.body_yaw = this.targetYaw;
								}
							}
							if(dis < 25){//
								this.setForwardMove(2);
							}
							this.setYHeadRot(this.body_yaw);//位移
							this.setYRot(this.body_yaw);
							this.yRotO = this.yBodyRot = this.body_yaw;//
						}
					}
				}
			}
			if (this.getForwardMove()>1F)
			{
				this.setForwardMove(this.getForwardMove()-0.2F);
				if(this.throttle < this.throttleMax){
				 this.throttle = this.throttle + this.thFrontSpeed;
				}
			}
			if (this.getForwardMove()<-1F){
				this.setForwardMove(this.getForwardMove()+0.2F);
				if(this.throttle > this.throttleMin){
					this.throttle = this.throttle + this.thBackSpeed;
				}
			}
			/*{
				if (this.getOwner() != null) {
					if (!this.getOwner().isInvisible()){
						if (this.getOwner().getHealth() > 0.0F) {
							if (this.distanceTo(this.getOwner())>20F) {//
								this.setMovePosX((int)this.getOwner().getX()+4-this.random.nextInt(8));
								this.setMovePosY((int)this.getOwner().getY());
								this.setMovePosZ((int)this.getOwner().getZ()+4-this.random.nextInt(8));
							}
						}
					}
				}
			}*/
		}
	}
	//@Nullable
	//public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance diff, SpawnReason reason, @Nullable ILivingEntityData data ,@Nullable CompoundNBT nbt) {
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelIn, DifficultyInstance diff, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		data = super.finalizeSpawn(levelIn, diff, reason, data, nbt);
		if(levelIn.getRandom().nextInt(2)==1){
			SummonEntity.wildSummon(this.level(), this.getX(), this.getY()+1, this.getZ(), 34, true, null,1);
		}else{
			SummonEntity.wildSummon(this.level(), this.getX(), this.getY()+1, this.getZ(), 33, true, null,1);
		}
		return data;
	}
}