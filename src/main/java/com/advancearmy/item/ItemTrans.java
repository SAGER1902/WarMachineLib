package advancearmy.item;
import java.util.List;
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

import wmlib.common.living.EntityWMVehicleBase;
import advancearmy.event.SASoundEvent;
import wmlib.common.enchantment.EnchantmentTypes;
import advancearmy.AdvanceArmy;
import wmlib.client.obj.SAObjModel;
import advancearmy.init.ModEntities;
import net.minecraft.core.Direction;
import advancearmy.entity.map.ParticlePoint;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import wmlib.api.IArmy;
public class ItemTrans extends Item{
	public ItemTrans(Item.Properties builder, boolean isv) {
		super(builder);
		this.isVehicle = isv;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(this.isVehicle){
			tooltip.add(Component.translatable("advancearmy.itemtrans.vehicle.desc").withStyle(ChatFormatting.AQUA));//name
		}else{
			tooltip.add(Component.translatable("advancearmy.itemtrans.unit1.desc").withStyle(ChatFormatting.AQUA));//name
			tooltip.add(Component.translatable("advancearmy.itemtrans.unit.desc").withStyle(ChatFormatting.GREEN));//name
		}
	}
	
	boolean isVehicle = false;
	public boolean enc = false;
	@Override
  	public boolean isFoil(ItemStack p_77636_1_) {
	  return true;
	}
	
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		Player playerIn=context.getPlayer();
		ItemStack itemstack = context.getItemInHand();
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			trans(world,playerIn);
			return InteractionResult.SUCCESS;
		}
	}
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (worldIn.isClientSide) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else {
			trans(worldIn,playerIn);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
		}
	}
	
	void trans(Level worldIn, Player playerIn){
		ParticlePoint bullet;
		ParticlePoint bullet2;
		int v=0;
		
		BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos();
		int air = 0;
		int rx = (int)playerIn.getX();
		int ry = (int)playerIn.getY();
		int rz = (int)playerIn.getZ();
		
		List<Entity> list = worldIn.getEntities(playerIn, playerIn.getBoundingBox().inflate(100D, 100D, 100D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity1 = list.get(k2);
			if (entity1 != null){
				if (entity1 instanceof IArmy && entity1.getVehicle()==null){
					IArmy unit = (IArmy) entity1;
					if(unit.getArmyOwner()==playerIn && unit.getArmyMoveT()!=3){
						
						bullet = new ParticlePoint(ModEntities.ENTITY_P.get(), worldIn);
						if(this.isVehicle){
							if(entity1 instanceof EntityWMVehicleBase){
								++v;
								bullet.moveTo(entity1.getX(), entity1.getY(), entity1.getZ(), 0, 0);
								worldIn.addFreshEntity(bullet);
								bullet.playSound(SASoundEvent.csk.get(), 6.0f, 1.0f);
								bullet.setPType(1);
								bullet.setSTime(40);
								
								rx = rx-v+worldIn.random.nextInt(v*2+1);
								rz = rz-v+worldIn.random.nextInt(v*2+1);
								blockpos.set(rx, ry, rz);
								for(int i = 0; i < 25; ++i){
									BlockState groundState = worldIn.getBlockState(blockpos);
									blockpos.move(Direction.UP);
									if (groundState.isAir()){
										++air;
										if(air>1)break;
									}
									++ry;
								}
								entity1.moveTo(rx,ry,rz);
							
								bullet2 = new ParticlePoint(ModEntities.ENTITY_P.get(), worldIn);
								bullet2.moveTo(entity1.getX(), entity1.getY(), entity1.getZ(), 0, 0);
								worldIn.addFreshEntity(bullet2);
								bullet2.setPType(1);
								bullet2.setSTime(40);
								unit.setMove(unit.getArmyMoveT(),rx,ry,rz);
							}
						}else if(!(entity1 instanceof EntityWMVehicleBase)){
							++v;
							bullet.moveTo(entity1.getX(), entity1.getY(), entity1.getZ(), 0, 0);
							worldIn.addFreshEntity(bullet);
							bullet.playSound(SoundEvents.ENDERMAN_TELEPORT, 6.0f, 1.0f);
							
							if(!playerIn.isCrouching()){
								rx = rx-v+worldIn.random.nextInt(v*2+1);
								rz = rz-v+worldIn.random.nextInt(v*2+1);
							}
							blockpos.set(rx, ry, rz);
							for(int i = 0; i < 25; ++i){
								BlockState groundState = worldIn.getBlockState(blockpos);
								blockpos.move(Direction.UP);
								if (groundState.isAir()){
									++air;
									if(air>1)break;
								}
								++ry;
							}
							entity1.moveTo(rx,ry,rz);
							bullet.setSTime(50);
							
							bullet2 = new ParticlePoint(ModEntities.ENTITY_P.get(), worldIn);
							bullet2.moveTo(entity1.getX(), entity1.getY(), entity1.getZ(), 0, 0);
							worldIn.addFreshEntity(bullet2);
							unit.setMove(unit.getArmyMoveT(),rx,ry,rz);
							bullet2.setSTime(50);
						}
					}
				}else
				if(entity1 instanceof TamableAnimal ent){
					if(ent.getOwner()==playerIn && !ent.isOrderedToSit()){
						++v;
						bullet = new ParticlePoint(ModEntities.ENTITY_P.get(), worldIn);
						bullet.moveTo(entity1.getX(), entity1.getY(), entity1.getZ(), 0, 0);
						worldIn.addFreshEntity(bullet);
						bullet.playSound(SoundEvents.ENDERMAN_TELEPORT, 6.0f, 1.0f);
						if(!playerIn.isCrouching()){
							rx = rx-v+worldIn.random.nextInt(v*2+1);
							rz = rz-v+worldIn.random.nextInt(v*2+1);
						}
						blockpos.set(rx, ry, rz);
						for(int i = 0; i < 25; ++i){
							BlockState groundState = worldIn.getBlockState(blockpos);
							blockpos.move(Direction.UP);
							if (groundState.isAir()){
								++air;
								if(air>1)break;
							}
							++ry;
						}
						entity1.moveTo(rx,ry,rz);
						bullet.setSTime(50);
						
						bullet2 = new ParticlePoint(ModEntities.ENTITY_P.get(), worldIn);
						bullet2.moveTo(entity1.getX(), entity1.getY(), entity1.getZ(), 0, 0);
						worldIn.addFreshEntity(bullet2);
						bullet2.setSTime(50);
					}
				}
			}
		}
		if(v>0){
			if(this.isVehicle){
				playerIn.getCooldowns().addCooldown(this, v*10);
			}else{
				playerIn.getCooldowns().addCooldown(this, v*5);
			}
		}
	}
	
	
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity entity) {
		return false;
	}
}