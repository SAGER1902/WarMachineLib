package wmlib.common.network.play;

import wmlib.common.network.message.MessageCommander;
import wmlib.common.network.message.MessageCame;
import wmlib.common.network.message.MessageRote;
import wmlib.common.network.message.MessageThrottle;
import wmlib.common.network.message.MessageFire;
import wmlib.common.network.message.MessageVehicleAnim;
import wmlib.common.network.message.MessageTrail;
import wmlib.common.network.message.MessageChoose;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IArmy;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.CooldownTracker;
import wmlib.common.item.ItemGun;
import wmlib.common.network.message.MessageShoot;
/**
 * Author: MrCrayfish
 */
public class ServerPlayHandler
{
    public static void handleFireMessage(ServerPlayerEntity player, MessageFire message)
    {
        Entity riding = player.getVehicle();
        /*if(riding instanceof EntityWMVehicleBase){
            ((EntityWMVehicleBase) riding).setFire(message.getID());
        }*/
		if(riding instanceof EntityWMSeat)
        {
            ((EntityWMSeat) riding).setFire(message.getID());
        }
    }

    public static void handleCommanderMessage(ServerPlayerEntity player, MessageCommander message)
    {
		{
			Vector3d lock = Vector3d.directionFromRotation(player.xRot, player.yRot);
			int range = 2;
			int ix = 0;
			int iy = 0;
			int iz = 0;
			
			int posXm = 0;
			int posYm = 0;
			int posZm = 0;
			
			LivingEntity target = null;
			Class<?> targetClass = null;
			if(message.getID()==1||message.getID()==6||message.getID()==7){
				for(int x2 = 0; x2 < 120; ++x2) {
					ix = (int) (player.getX() + lock.x * x2);
					iy = (int) (player.getY() + 1.5 + lock.y * x2);
					iz = (int) (player.getZ() + lock.z * x2);
					if(ix != 0 && iz != 0 && iy != 0){
						BlockPos blockpos = new BlockPos(player.getX() + lock.x * x2,player.getY() + 1.5 + lock.y * x2,player.getZ() + lock.z * x2);
						BlockState iblockstate = player.level.getBlockState(blockpos);
						if (!iblockstate.isAir(player.level, blockpos)){
							posXm = ix;
							posYm = iy+1;
							posZm = iz;
							break;
						}else{
							AxisAlignedBB axisalignedbb = (new AxisAlignedBB(ix-range, iy-range, iz-range, ix+range, iy+range, iz+range)).inflate(1D);
							List<Entity> llist = player.level.getEntities(player,axisalignedbb);
							if (llist != null) {
								for (int lj = 0; lj < llist.size(); lj++) {
									Entity entity1 = (Entity) llist.get(lj);
									if (entity1 != null && entity1 instanceof LivingEntity && entity1.getVehicle()==null) {
										if(message.getID()==6){
											target = (LivingEntity)entity1;
											break;
										}else{
											if(entity1 instanceof IMob){
												target = (LivingEntity)entity1;
												break;
											}
											if (entity1 instanceof IArmy){
												IArmy unit = (IArmy) entity1;
												if (unit.getArmyOwner()==player){
													targetClass=entity1.getClass();
													if(!unit.getSelect())unit.setSelect(true);
													break;
												}else if(player.getTeam()!=entity1.getTeam()&&entity1.getTeam()!=null){
													target = (LivingEntity)entity1;
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			int v=0;
			List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(100D, 100D, 100D));
			for(int k2 = 0; k2 < list.size(); ++k2) {
				Entity entity1 = list.get(k2);
				if (entity1 != null && entity1 instanceof IArmy) {
					/*if (entity1 instanceof IArmy)*/{
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
								unit.setAttack(null);
								if(target!=null){
									unit.setAttack(target);
								}else
								if((posXm!=0 || posYm!=0 || posZm!=0) && entity1.getVehicle()==null){
									++v;
									/*if(entity1 instanceof EntityWMVehicleBase){
										EntityWMVehicleBase vehicle = (EntityWMVehicleBase)entity1;
										vehicle.setMoveType(2);
										vehicle.setMovePosX(posXm-v+entity1.level.random.nextInt(v*2+1));
										vehicle.setMovePosY(posYm);
										vehicle.setMovePosZ(posZm-v+entity1.level.random.nextInt(v*2+1));
									}else*/{
										//unit.setMove(2,posXm,posYm,posZm);
										unit.setMove(2,posXm-v+entity1.level.random.nextInt(v*2+1),posYm,posZm-v+entity1.level.random.nextInt(v*2)+1);//Vehicle use this will be bad
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
    }

    public static void handleChooseMessage(ServerPlayerEntity player, MessageChoose message)
    {
		AxisAlignedBB range = (new AxisAlignedBB(message.getX1(), message.getY1(), message.getZ1(), message.getX2(), message.getY2(), message.getZ2())).inflate(1);
		List llist2 = player.level.getEntities(player, range);
		if(llist2!=null){//
			for (int lj2 = 0; lj2 < llist2.size(); lj2++) {
				Entity entity1 = (Entity)llist2.get(lj2);
				if (entity1 != null && entity1 instanceof LivingEntity){
					if (entity1 instanceof IArmy){
						IArmy unit = (IArmy) entity1;
						if (unit.getArmyOwner()==player && !unit.getSelect()){
							unit.setSelect(true);//бЁжа");
						}
					}
				}
			}
		}
    }
	
    public static void handleThrottleMessage(ServerPlayerEntity player, MessageThrottle message)
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
	
	public static void handleCameMessage(ServerPlayerEntity player, MessageCame message)
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
	
	public static void handleRoteMessage(ServerPlayerEntity player, MessageRote message)
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
	
    public static void handleShoot(ServerPlayerEntity player, MessageShoot message)
    {
        if(!player.isSpectator())
        {
            World world = player.level;
            ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
            if(heldItem.getItem() instanceof ItemGun/* && (Gun.hasAmmo(heldItem) || player.isCreative())*/)
            {
                ItemGun gun = (ItemGun) heldItem.getItem();
                if(gun != null)
                {
					/*if(li>0)*/{
						CooldownTracker tracker = player.getCooldowns();
						if(!tracker.isOnCooldown(heldItem.getItem()))
						{
							tracker.addCooldown(heldItem.getItem(), gun.cycle);
							//ItemGunBase.fire(world, player, heldItem);
							gun.Fire_AR_Left(heldItem, world, player, true, true);
						}
					}
				}
			}
        }
    }
}
