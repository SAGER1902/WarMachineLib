package wmlib.common.network.play;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IArmy;
import wmlib.common.item.ItemGun;
import wmlib.common.network.message.MessageShoot;
import wmlib.common.network.message.MessageCommander;
import wmlib.common.network.message.MessageCame;
import wmlib.common.network.message.MessageRote;
import wmlib.common.network.message.MessageThrottle;
import wmlib.common.network.message.MessageFire;
import wmlib.common.network.message.MessageVehicleAnim;
import wmlib.common.network.message.MessageTrail;
import wmlib.common.network.message.MessageChoose;
import wmlib.common.network.message.MessageAddOne;
import wmlib.common.network.message.MessageRtsClick;
import wmlib.common.network.message.MessageRtsCameraMove;
import wmlib.rts.XiangJiEntity;
import wmlib.rts.RtsMoShiMenu;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;               // Hand  InteractionHand
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.level.Level; // World -> Level
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks; // ?????
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.MenuProvider;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos; // ?????
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy; // Enemy -> Enemy (??)
public class ServerPlayHandler
{
    public static void handleFireMessage(ServerPlayer player, MessageFire message)
    {
        Entity riding = player.getVehicle();
		if(riding instanceof EntityWMSeat)
        {
            ((EntityWMSeat) riding).setFire(message.getID());
        }
    }

    public static void handleCommanderMessage(ServerPlayer player, MessageCommander message)
    {
		int posXm = 0;
		int posYm = 0;
		int posZm = 0;
		LivingEntity target = null;
		Class<?> targetClass = null;
		if(message.getID()==1||message.getID()==6||message.getID()==7){
			Vec3 lock = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
			double range = 0.5;
			int ix = 0;
			int iy = 0;
			int iz = 0;
			for(int x2 = 0; x2 < 120; ++x2) {
				ix = (int) (player.getX() + lock.x * x2);
				iy = (int) (player.getY() + 1.5 + lock.y * x2);
				iz = (int) (player.getZ() + lock.z * x2);
				if(ix != 0 && iz != 0 && iy != 0){
					BlockPos blockpos = BlockPos.containing(
						player.getX() + lock.x * x2,
						player.getY() + 1.5 + lock.y * x2,
						player.getZ() + lock.z * x2
					);
					BlockState iblockstate = player.level().getBlockState(blockpos);
					if (!iblockstate.isAir()){
						posXm = ix;
						posYm = iy;
						posZm = iz;
						break;
					}else{
						AABB axisalignedbb = (new AABB(ix-range, iy-range, iz-range, ix+range, iy+range, iz+range)).inflate(1D);
						List<Entity> llist = player.level().getEntities(player,axisalignedbb);
						if (llist != null) {
							boolean close = false;
							for (int lj = 0; lj < llist.size(); lj++) {
								Entity entity1 = (Entity) llist.get(lj);
								if (entity1 != null && entity1 instanceof LivingEntity && entity1.getVehicle()==null) {
									if(message.getID()==6){
										target = (LivingEntity)entity1;
										close=true;
										break;
									}else{
										if(entity1 instanceof Enemy){
											target = (LivingEntity)entity1;
											close=true;
											break;
										}
										if (entity1 instanceof IArmy){
											IArmy unit = (IArmy) entity1;
											if (unit.getArmyOwner()==player){
												targetClass=entity1.getClass();
												if(!unit.getSelect()){
													unit.setSelect(true);
													close=true;
													break;
												}
											}else{
												target = (LivingEntity)entity1;
												close=true;
												break;
											}
										}
									}
								}
							}
							if(close)break;
						}
					}
				}
			}
		}
		int v=0;
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(100D, 100D, 100D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity1 = list.get(k2);
			if (entity1 != null/* && entity1 instanceof LivingEntity*/){
				/*if(message.getID()==31){
					if(entity1 instanceof XiangJiEntity){
						XiangJiEntity xj = (XiangJiEntity)entity1;
						if(xj.getOwner()==player){
							xj.setChoose(true);
							xj.setOpen(true);
							player.connection.send(new ClientboundSetCameraPacket(xj));
							BlockPos pos = BlockPos.containing(xj.getX(), xj.getY(), xj.getZ());
							NetworkHooks.openScreen(player, new MenuProvider() {
								@Override
								public Component getDisplayName() {
									return Component.literal("RtsMode");
								}
								@Override
								public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
									return new RtsMoShiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
								}
							}, pos);
							xj.setOpen(true);
							break;
						}
					}
				}else */if(message.getID()==32){
					if(entity1 instanceof XiangJiEntity){
						XiangJiEntity xj = (XiangJiEntity)entity1;
						if(xj.getOwner()==player && xj.getChoose()){
							xj.setChoose(false);
							xj.setOpen(false);
							break;
						}
					}
				}else if (entity1 instanceof IArmy){
					IArmy unit = (IArmy) entity1;
					
					if(unit.getArmyOwner()==player && message.getID()==3){
						if(!unit.getSelect())unit.setSelect(true);
					}
					if(message.getID()>10 && message.getID()<20){//add team count
						if(!unit.getSelect() && unit.getTeamCount()==message.getID())unit.setTeamCount(0);
					}
					if(message.getID()>20 && message.getID()<30){//add team count
						if(unit.getTeamCount()==(message.getID()-20)){
							if(!unit.getSelect())unit.setSelect(true);
						}
					}
					
					if(targetClass!=null && targetClass == unit.getClass() && unit.getArmyOwner()==player && message.getID()==7){
						if(!unit.getSelect())unit.setSelect(true);
					}else
					if (unit.getArmyOwner()==player && unit.getSelect()){
						if(message.getID()==1||message.getID()==6||message.getID()==7){
							if(target!=null){
								++v;
								/*if(target.distanceToSqr(entity1)>unit.getShootRange()*unit.getShootRange())*/{
									unit.setMove(4,(int)target.getX()-v+entity1.level().random.nextInt(v*2+1),(int)target.getY()+1,(int)target.getZ()-v+entity1.level().random.nextInt(v*2)+1);
								}
								unit.setAttack(target);
							}else
							if((posXm!=0 || posYm!=0 || posZm!=0) && entity1.getVehicle()==null){
								++v;
								/*if(entity1 instanceof EntityWMVehicleBase){
									EntityWMVehicleBase vehicle = (EntityWMVehicleBase)entity1;
									vehicle.setMoveType(2);
									vehicle.setMovePosX(posXm-v+entity1.level().random.nextInt(v*2+1));
									vehicle.setMovePosY(posYm);
									vehicle.setMovePosZ(posZm-v+entity1.level().random.nextInt(v*2+1));
								}else*/{
									//unit.setMove(2,posXm,posYm,posZm);
									unit.setMove(2,posXm-v+entity1.level().random.nextInt(v*2+1),posYm,posZm-v+entity1.level().random.nextInt(v*2)+1);//Vehicle use this will be bad
								}
							}
						}
						if(message.getID()==2){
							unit.setSelect(false);
						}
						
						if(message.getID()==4){
							/*if(entity1 instanceof EntityWMVehicleBase){
								EntityWMVehicleBase vehicle = (EntityWMVehicleBase)entity1;
								vehicle.setMoveType(3);
								vehicle.setAttacking(false);
								vehicle.setTarget(null);
							}else*/{
								unit.setMove(3,0,0,0);
								unit.setAttack(null);
							}
						}
						if(message.getID()==5){
							/*if(entity1 instanceof EntityWMVehicleBase){
								EntityWMVehicleBase vehicle = (EntityWMVehicleBase)entity1;
								vehicle.setMoveType(1);
							}else*/{
								unit.setMove(1,0,0,0);
							}
						}
						if(message.getID()==8){
							unit.setMove(0,0,0,0);
							unit.setAttack(null);
						}
						if(message.getID()==9){
							if(entity1.isVehicle()){
								unit.stopUnitPassenger();
							}else{
								if(entity1.getVehicle()!=null){
									entity1.stopRiding();
									unit.setMove(3,0,0,0);
								}
							}
							unit.setAttack(null);
						}
						if(message.getID()>10 && message.getID()<20){//add team count
							unit.setTeamCount(message.getID()-10);
						}
					}
				}
			}
		}
    }
	
    public static void handleChooseMessage(ServerPlayer player, MessageChoose message)
    {
		AABB range = (new AABB(message.getX1(), message.getY1(), message.getZ1(), message.getX2(), message.getY2(), message.getZ2())).inflate(1);
		List llist2 = player.level().getEntities(player, range);
		if(llist2!=null){//
			for (int lj2 = 0; lj2 < llist2.size(); lj2++) {
				Entity entity1 = (Entity)llist2.get(lj2);
				if (entity1 != null && entity1 instanceof LivingEntity){
					if (entity1 instanceof IArmy){
						IArmy unit = (IArmy) entity1;
						if (unit.getArmyOwner()==player && !unit.getSelect()){
							unit.setSelect(true);
						}
					}
				}
			}
		}
    }
	
    public static void handleRtsCameraMessage(ServerPlayer player, MessageRtsCameraMove message)
    {
		Level level = player.level();
		if (message.getId() != -1) {
			Entity ent = level.getEntity(message.getId());
			if (ent != null && ent.isAlive()) {
				if(ent instanceof XiangJiEntity){
					XiangJiEntity xj = (XiangJiEntity) ent;
					xj.setRote(message.getRT());
					xj.setMoveX((float)message.getMX());
					xj.setMoveY(message.getMY());
					xj.setMoveZ((float)message.getMZ());
				}
			}
		}
    }
	public static void handleRtsClickMessage(ServerPlayer player, MessageRtsClick message)
    {
		Level level = player.level();
		boolean attack = true;
		LivingEntity target = null;
		Entity ent = null;
		if (message.getId() != -1) {
			// 处理实体交互 - 选择或取消选中士兵
			ent = level.getEntity(message.getId());
			if(ent.getTeam()==player.getTeam()&&player.getTeam()!=null){
				attack=false;
			}else if(!(ent instanceof Enemy)){
				attack=false;
			}
			if(message.forceAttack()){
				target = (LivingEntity)ent;
			}else{
				if (ent instanceof IArmy){
					IArmy unit = (IArmy) ent;
					if (unit.getArmyOwner()==player){
						if(!unit.getSelect()){
							unit.setSelect(true);
						}else{
							unit.setSelect(false);
						}
					}else{
						if(attack)target = (LivingEntity)ent;
					}
				}else if(attack){
					target = (LivingEntity)ent;
				}
			}
		}
		int posXm = message.getX();
		int posYm = message.getY();
		int posZm = message.getZ();
		int v=0;
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(100D, 100D, 100D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity1 = list.get(k2);
			if (entity1 != null && entity1 instanceof LivingEntity) {
				if (entity1 instanceof IArmy){
					IArmy unit = (IArmy) entity1;
					if(message.isLink()){
						if(ent!=null && ent.getClass() == unit.getClass() && unit.getArmyOwner()==player){
							if(!unit.getSelect())unit.setSelect(true);
						}
					}
					if (unit.getArmyOwner()==player && unit.getSelect()){
						if(target!=null){
							++v;
							unit.setMove(4,(int)target.getX()-v+entity1.level().random.nextInt(v*2+1),(int)target.getY()+1,(int)target.getZ()-v+entity1.level().random.nextInt(v*2)+1);
							unit.setAttack(target);
						}else if((posXm!=0 || posYm!=0 || posZm!=0) && entity1.getVehicle()==null){
							++v;
							/*if(entity1 instanceof EntityWMVehicleBase){
								EntityWMVehicleBase vehicle = (EntityWMVehicleBase)entity1;
								vehicle.setMoveType(2);
								vehicle.setMovePosX(posXm-v+entity1.level().random.nextInt(v*2+1));
								vehicle.setMovePosY(posYm+1);
								vehicle.setMovePosZ(posZm-v+entity1.level().random.nextInt(v*2+1));
							}else*/{
								//unit.setMove(2,posXm,posYm,posZm);
								unit.setMove(2,posXm-v+entity1.level().random.nextInt(v*2+1),posYm+1,posZm-v+entity1.level().random.nextInt(v*2)+1);//Vehicle use this will be bad
							}
						}
					}
				}
			}
		}
    }
	
	
	public static void handleAddMessage(ServerPlayer player, MessageAddOne message)
    {
		if (message.getEntID() != -1) {
			Entity ent = player.level().getEntity(message.getEntID());
			if (ent instanceof IArmy){
				IArmy unit = (IArmy) ent;
				if (unit.getArmyOwner()==player){
					if(!unit.getSelect())unit.setSelect(true);
				}
			}
		}
    }
	
    public static void handleThrottleMessage(ServerPlayer player, MessageThrottle message)
    {
        Entity riding = player.getVehicle();
        if(riding instanceof EntityWMSeat)
        {
			if(riding.getVehicle()!=null && riding.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase)riding.getVehicle();
				ve.setForwardMove(message.getPower());
				ve.setStrafingMove(message.getPower2());
			}
        }
    }
	
	public static void handleCameMessage(ServerPlayer player, MessageCame message)
    {
        Entity riding = player.getVehicle();
        if(riding instanceof EntityWMSeat)
        {
			if(riding.getVehicle()!=null && riding.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase) riding.getVehicle();
				ve.setChange(message.getPower());
			}
        }
    }
	
	public static void handleRoteMessage(ServerPlayer player, MessageRote message)
    {
        Entity riding = player.getVehicle();
        if(riding instanceof EntityWMSeat)
        {
			if(riding.getVehicle()!=null && riding.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase) riding.getVehicle();
				ve.setMovePitch(message.getPower());
				ve.setMoveYaw(message.getPower2());
			}
        }
    }
	
    public static void handleShoot(ServerPlayer player, MessageShoot message)
    {
        if(!player.isSpectator())
        {
            Level world = player.level();
            ItemStack heldItem = player.getMainHandItem();
            if(heldItem.getItem() instanceof ItemGun/* && (gun.hasAmmo(heldItem) || player.isCreative())*/)
            {
                ItemGun gun = (ItemGun) heldItem.getItem();
                if(gun != null)
                {
					if (!player.getCooldowns().isOnCooldown(heldItem.getItem())) 
					{
						player.getCooldowns().addCooldown(heldItem.getItem(), gun.cycle);
						gun.Fire_AR_Left(heldItem, world, player, true, true);
					}
				}
			}
        }
    }
}
