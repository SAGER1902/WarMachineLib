package advancearmy.util;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.scores.Team;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;

import advancearmy.event.SASoundEvent;
import advancearmy.item.ItemSpawn;
import advancearmy.item.ItemCapture;
import advancearmy.item.ItemRemove;

import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;

import wmlib.api.IBuilding;
import wmlib.api.IEnemy;
import wmlib.api.ITool;
import wmlib.api.IHealthBar;
import wmlib.api.IArmy;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.WeaponVehicleBase;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import advancearmy.entity.EntitySA_SoldierBase;
public class InteractSoldier{
	public static InteractionResult interactSoldier(EntitySA_SoldierBase soldier, Player player, InteractionHand hand) {
		ItemStack heldItem = player.getItemInHand(hand);
		Item item = heldItem.getItem();
		boolean have = true;
		if(heldItem!=null && !heldItem.isEmpty()){
			if(soldier.getOwner()==null&&item == Items.GOLD_INGOT){
				have=false;
				soldier.tame(player);
				player.sendSystemMessage(Component.translatable("Ok, I'll follow you"));
				heldItem.shrink(1);
				return InteractionResult.SUCCESS;
			}
			if(item instanceof ItemCapture){
				have=false;
				return InteractionResult.PASS;
			}
			if(item instanceof ItemRemove && soldier.getOwner()==player){
				have=false;
				player.playSound(SoundEvents.ANVIL_DESTROY,1F,1F);
				if(!soldier.level().isClientSide){
					soldier.discard();
					if (player instanceof ServerPlayer) {
						((ServerPlayer) player).connection.send(new ClientboundRemoveEntitiesPacket(soldier.getId()));
					}
					return InteractionResult.SUCCESS;
				}
				if(soldier.level().isClientSide)player.sendSystemMessage(Component.translatable("Remvoed"));
				return InteractionResult.SUCCESS;
			}
		}
		if((player.isCreative()||player == soldier.getOwner()) && have){
			if(soldier.getMoveType() == 1) {
				if(soldier.level().isClientSide)player.sendSystemMessage(Component.translatable("Follow me!"));
				soldier.setMoveType(0);
				soldier.setRemain2(0);
				return InteractionResult.SUCCESS;
			}else if(soldier.getMoveType() == 0) {
				if(soldier.level().isClientSide)player.sendSystemMessage(Component.translatable("Stay!"));
				soldier.setMoveType(3);
				soldier.setRemain2(2);
				return InteractionResult.SUCCESS;
			}else if(soldier.getMoveType() == 3) {
				if(soldier.level().isClientSide)player.sendSystemMessage(Component.translatable("Free Attack!"));
				soldier.setMoveType(1);
				soldier.setRemain2(0);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
}
