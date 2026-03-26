package advancearmy.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;          // BlockState 路径更新
import net.minecraft.world.level.block.Blocks;                   // Blocks 包路径更新
import net.minecraft.world.entity.EntityType;                    // 路径更新
import net.minecraft.world.entity.MobSpawnType;                  // SpawnReason → MobSpawnType
import net.minecraft.world.entity.player.Player;                 // Player → Player
import net.minecraft.world.item.Item;                            // 路径更新
import net.minecraft.world.item.ItemStack;                       // 路径更新
import net.minecraft.world.item.context.UseOnContext;            // ItemUseContext → UseOnContext
import net.minecraft.world.InteractionResult;                    // 路径更新
import net.minecraft.world.InteractionHand;                      // Hand → InteractionHand
import net.minecraft.nbt.CompoundTag;                            // CompoundNBT → CompoundTag
import net.minecraft.stats.Stats;                                // 路径更新
import net.minecraft.world.level.block.entity.SpawnerBlockEntity; // MobSpawnerTileEntity → SpawnerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity;        // TileEntity → BlockEntity
import net.minecraft.network.chat.Component;                     // ITextComponent → Component
import net.minecraft.ChatFormatting;                             // ChatFormatting → ChatFormatting
import net.minecraft.network.chat.MutableComponent;              // 替代 TranslationTextComponent（通过 Component.translatable()）
import net.minecraft.world.item.TooltipFlag;                     // ITooltipFlag → TooltipFlag
import net.minecraft.world.entity.EquipmentSlot;                 // EquipmentSlotType → EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment;         // 路径更新
import net.minecraft.world.level.Level;                          // World → Level
import net.minecraft.world.entity.LivingEntity;                  // 路径更新
import net.minecraft.world.item.Items;                           // 路径更新
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.server.level.ServerLevel;

import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Creeper;
//import advancearmy.entity.mob.DragonTurret;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;
import advancearmy.entity.mob.EvilPortalOnce;

import advancearmy.entity.mob.ERO_Spider;
import advancearmy.entity.mob.ERO_Giant;
import advancearmy.entity.mob.ERO_Ravager;
import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.map.VehicleRespawn;
import advancearmy.init.ModEntities;
import advancearmy.AdvanceArmy;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.item.ItemSummon;

import advancearmy.entity.mob.EntityMobSquadBase;
import advancearmy.entity.mob.EntityRaiderSquadBase;

import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.flag.FeatureFlagSet;
import java.util.Optional;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraft.world.entity.PathfinderMob;
public class ItemSpawnMob extends ForgeSpawnEggItem{
	int id=0;
	boolean spawnv = false;
	public ItemSpawnMob(Item.Properties builder, int i,int backgroundColor, int highlightColor, boolean can) {
		super(ModEntities.ENTITY_EZOMBIE,backgroundColor, highlightColor,builder);
		this.id = i;
		if(can)this.infor2="advancearmy.interact.mobvehicle.desc";
		spawnv=can;
		
		if(id == 1){
			this.infor1="advancearmy.infor.maptool.desc";
			this.infor2="advancearmy.infor.maptool_remove.desc";
			this.infor3="advancearmy.infor.spawns1.desc";
			this.infor4="advancearmy.infor.spawns2.desc";
			this.infor5="advancearmy.infor.spawns3.desc";
			this.infor7="advancearmy.infor.maptool_show.desc";
		}else if(id == 2){
			this.infor1="advancearmy.infor.maptool.desc";
			this.infor2="advancearmy.infor.maptool_remove.desc";
			this.infor3="advancearmy.infor.spawnv1.desc";
			this.infor4="advancearmy.infor.spawnv2.desc";
			this.infor5="advancearmy.infor.spawnv3.desc";
			this.infor7="advancearmy.infor.maptool_show.desc";
		}else if(id == 3){
			this.infor1="advancearmy.infor.maptool.desc";
			this.infor2="advancearmy.infor.maptool_remove.desc";
			this.infor3="advancearmy.infor.movep1.desc";
			this.infor4="advancearmy.infor.movep2.desc";
			this.infor5="advancearmy.infor.movep3.desc";
			this.infor6="advancearmy.infor.movep4.desc";
			this.infor7="advancearmy.infor.maptool_show.desc";
		}
	}
	
    public boolean spawnsEntity(@Nullable CompoundTag nbt, EntityType<?> type) {
        return false;
    }
    public EntityType<?> getType(@Nullable CompoundTag nbt) {
        return ModEntities.ENTITY_EZOMBIE.get();
    }
	@Override
    public FeatureFlagSet requiredFeatures() {
        return ModEntities.ENTITY_EZOMBIE.get().requiredFeatures();
    }
	public Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob mob, EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 pos, ItemStack stack) {
		return null;
	}

	public String infor1 = null;
	public String infor2 = null;
	public String infor3 = null;
	public String infor4 = null;
	public String infor5 = null;
	public String infor6 = null;
	public String infor7 = null;
	public String infor8 = null;
	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
			if(infor1!=null)tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));//create
			if(infor3!=null)tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(Component.translatable(infor4).withStyle(ChatFormatting.YELLOW));//describe2
			if(infor5!=null)tooltip.add(Component.translatable(infor5).withStyle(ChatFormatting.YELLOW));//weapon
			if(infor6!=null)tooltip.add(Component.translatable(infor6).withStyle(ChatFormatting.YELLOW));//health/seat
			if(infor7!=null)tooltip.add(Component.translatable(infor7).withStyle(ChatFormatting.GREEN));//armor
			if(infor8!=null)tooltip.add(Component.translatable(infor8).withStyle(ChatFormatting.AQUA));//turret_armor
	}
	public void spawnMob(Level worldIn, double par4, double par5, double par6)
	{
		if (worldIn.isClientSide) return;
		LivingEntity entity = null;
		boolean custom = false;
		if(id == 1){
			entity = new CreatureRespawn(ModEntities.ENTITY_CRES.get(), worldIn);
		}else if(id == 2){
			entity = new VehicleRespawn(ModEntities.ENTITY_VRES.get(), worldIn);
		}else if(id == 3){
			entity = new ArmyMovePoint(ModEntities.ENTITY_MOVEP.get(), worldIn);
		}else if(id == 4){
			entity = new ERO_Skeleton(ModEntities.ENTITY_SKELETON.get(), worldIn);
			custom=true;
		}else if(id == 5){
			entity = new ERO_Pillager(ModEntities.ENTITY_PI.get(), worldIn);
		}else if(id == 6){
			entity = new ERO_Phantom(ModEntities.ENTITY_PHA.get(), worldIn);
			custom=true;
		}else if(id == 7){
			entity = new ERO_Ghast(ModEntities.ENTITY_GST.get(), worldIn);
			custom=true;
		}else if(id == 8){
			entity = new ERO_Creeper(ModEntities.ENTITY_CREEPER.get(), worldIn);
		}else if(id == 9){
			//entity = new DragonTurret(ModEntities.ENTITY_DT.get(), worldIn);
		}else if(id == 10){
			entity = new ERO_REB(ModEntities.ENTITY_REB.get(), worldIn);
		}else if(id == 11){
			entity = new EntityAohuan(ModEntities.ENTITY_AOHUAN.get(), worldIn);
		}else if(id == 12){
			entity = new EvilPortal(ModEntities.ENTITY_POR.get(), worldIn);
		}else if(id == 13){
			entity = new ERO_Zombie(ModEntities.ENTITY_EZOMBIE.get(), worldIn);
		}else if(id == 14){
			entity = new ERO_Husk(ModEntities.ENTITY_EHUSK.get(), worldIn);
		}else if(id == 15){
			entity = new EvilPortalOnce(ModEntities.ENTITY_POR1.get(), worldIn);
			custom = true;
		}else if(id == 16){
			entity = new ERO_Spider(ModEntities.ERO_SPIDER.get(), worldIn);
			custom = true;
		}else if(id == 17){
			//entity = new ERO_Husk(ModEntities.ENTITY_EHUSK.get(), worldIn);
		}else if(id == 18){
			entity = new ERO_Giant(ModEntities.ENTITY_GIANT.get(), worldIn);
			custom = true;
		}else if(id == 19){
			entity = new ERO_Ravager(ModEntities.ERO_RAV.get(), worldIn);
		}
		if(entity!=null)spawn(entity, worldIn, par4, par5, par6, custom);
	}
	
	public void spawn(LivingEntity entity, Level worldIn, double par4, double par5, double par6, boolean custom)
	{
		if(entity!=null){
			entity.moveTo(par4 + 0.5, par5, par6 + 0.5, 0, 0.0F);
			if(custom && entity instanceof Mob mob){
				if (!(worldIn instanceof ServerLevel)) {
				}else{
					ServerLevel serverworld = (ServerLevel)worldIn;
					DifficultyInstance difficulty = worldIn.getCurrentDifficultyAt(entity.blockPosition());
					mob.finalizeSpawn(serverworld, difficulty, 
										MobSpawnType.REINFORCEMENT, 
										(SpawnGroupData) null, 
										(CompoundTag) null);
				}
			}
			worldIn.addFreshEntity(entity);
		} 
	}
   
   /**
    * Called when this item is used when targetting a Block
    */
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = context.getItemInHand();
			if (!context.getPlayer().isCreative())
			{
				itemstack.shrink(1);
			}
			BlockPos pos = context.getClickedPos();
			spawnMob(world, (double)pos.getX(), (double)pos.getY()+1, (double)pos.getZ());
			if(spawnv){
				Item item = context.getPlayer().getOffhandItem().getItem();
				if(item instanceof ItemSpawn){
					ItemSpawn vehicleitem = (ItemSpawn)item;
					if(vehicleitem.type == 0){
						vehicleitem.spawnCreature(world, null, 0, true, (double)pos.getX(), (double)pos.getY()+1, (double)pos.getZ(), 3);
						if(vehicleitem.vehicle!=null && vehicleitem.vehicle.seatMaxCount>1){
							for(int k2 = 0; k2 < vehicleitem.vehicle.seatMaxCount; ++k2){
								spawnMob(world, (double)pos.getX(), (double)pos.getY()+1, (double)pos.getZ());
							}
						}
					}
					
				}
			}
			return InteractionResult.SUCCESS;
		}
	}
}