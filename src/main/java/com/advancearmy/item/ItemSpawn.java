package advancearmy.item;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.world.entity.ai.attributes.Attributes;

import advancearmy.entity.land.EntitySA_Bike;
import advancearmy.entity.land.EntitySA_BMP2;
import advancearmy.entity.land.EntitySA_BMPT;
import advancearmy.entity.land.EntitySA_M109;
import advancearmy.entity.land.EntitySA_M2A2;
import advancearmy.entity.land.EntitySA_M2A2AA;
import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_FTK;
import advancearmy.entity.land.EntitySA_Hmmwv;
import advancearmy.entity.land.EntitySA_99G;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.entity.land.EntitySA_LaserAA;
import advancearmy.entity.land.EntitySA_MASTDOM;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_LAV;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.land.EntitySA_Sickle;
import advancearmy.entity.land.EntitySA_Tank;
import advancearmy.entity.land.EntitySA_Prism;
import advancearmy.entity.land.EntitySA_Reaper;
import advancearmy.entity.land.EntitySA_Tesla;
import advancearmy.entity.land.EntitySA_Mirage;
import advancearmy.entity.land.EntitySA_APAGAT;
import advancearmy.entity.land.EntitySA_MMTank;

import advancearmy.entity.land.EntitySA_STAPC;
import advancearmy.entity.land.EntitySA_RockTank;

import advancearmy.entity.air.EntitySA_AH1Z;
import advancearmy.entity.air.EntitySA_Plane;
import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_Helicopter;
import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.air.EntitySA_A10a;
import advancearmy.entity.air.EntitySA_A10c;
import advancearmy.entity.air.EntitySA_SU33;
import advancearmy.entity.air.EntitySA_AH6;
import advancearmy.entity.air.EntitySA_MI24;

import advancearmy.entity.turret.EntitySA_Mortar;
import advancearmy.entity.turret.EntitySA_M2hb;
import advancearmy.entity.turret.EntitySA_Kord;
import advancearmy.entity.turret.EntitySA_TOW;
import advancearmy.entity.turret.EntitySA_STIN;

import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_GI;
import advancearmy.entity.soldier.EntitySA_OFG;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
import advancearmy.entity.soldier.EntitySA_RADS;
import advancearmy.entity.soldier.EntitySA_GAT;
import advancearmy.entity.soldier.EntitySA_MWDrone;
import advancearmy.entity.building.SoldierMachine;
import advancearmy.entity.building.VehicleMachine;
import advancearmy.entity.building.SandBag;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.init.ModEntities;
import advancearmy.AdvanceArmy;
import wmlib.client.obj.SAObjModel;
import wmlib.common.block.BlockRegister;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.enchantment.EnchantmentTypes;
import wmlib.common.item.ItemSummon;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import advancearmy.render.CustomItemStackRenderer;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
public class ItemSpawn extends ItemSummon{
	public SAObjModel obj_model = null;
	public String obj_tex = "wmlib:textures/misc/vehicle_glint.png";
	public int id = 0;
	public int xp = 10;
	public int cool = 20;
	public int type = 0;//0 vehicle 1 ent 3 building
	public EntitySA_SoldierBase ent = null;
	public WeaponVehicleBase vehicle = null;
	public TamableAnimal building = null;
	public ItemSpawn(Item.Properties builder, int i, int t, int x, int c,
	String if1,String if2,String if3,String if4, SAObjModel obj) {
		super(builder);
		this.id = i;
		this.type = t;
		this.xp=x;
		this.cool=c;
		this.inforx="建造价格:"+x;
		this.inforc="建造时间:"+c/20F+"s";
		if(obj!=null)obj_model=obj;
		if(if1!=null&&if1!="")infor1=if1;
		if(if2!=null&&if2!="")infor2=if2;
		if(if3!=null&&if3!="")infor3=if3;
		if(if4!=null&&if4!="")infor4=if4;
	}


    /*@Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(this.getClientExtensions());
    }
    @OnlyIn(Dist.CLIENT)
    public IClientItemExtensions getClientExtensions() {
        return new IClientItemExtensions() {
            //private final BlockEntityWithoutLevelRenderer renderer = BocekItem.this.getRenderer().get();
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new CustomItemStackRenderer();
            }
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        };
    }
    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            // 我们将在这里返回自定义的 ItemStackRenderer
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				// 返回你的自定义渲染器
				return CustomItemStackRenderer.INSTANCE;
			}
        });
    }*/

	public String infor1 = null;
	public String infor2 = "advancearmy.infor.spawn1.desc";
	public String infor3 = null;
	public String infor4 = null;
	public String infor5 = null;
	public String infor6 = null;
	public String inforx = null;
	public String inforc = null;

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(type==0){
			if(infor1!=null)tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));//create
			if(infor3!=null)tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(Component.translatable(infor4).withStyle(ChatFormatting.YELLOW));//describe2
			if(inforx!=null)tooltip.add(Component.translatable(inforx).withStyle(ChatFormatting.AQUA));//describe1
			if(inforc!=null)tooltip.add(Component.translatable(inforc).withStyle(ChatFormatting.AQUA));//describe2
			/*if(infor5!=null)tooltip.add(Component.translatable(infor5).withStyle(ChatFormatting.RED));//weapon
			if(infor6!=null)tooltip.add(Component.translatable(infor6).withStyle(ChatFormatting.RED));//health/seat
			if(infor7!=null)tooltip.add(Component.translatable(infor7).withStyle(ChatFormatting.GREEN));//armor
			if(infor8!=null)tooltip.add(Component.translatable(infor8).withStyle(ChatFormatting.AQUA));//turret_armor*/
			tooltip.add(Component.translatable("advancearmy.infor.pickaxe_fix.desc").withStyle(ChatFormatting.GREEN));
		}else{
			if(infor1!=null)tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));//create
			if(infor3!=null)tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(Component.translatable(infor4).withStyle(ChatFormatting.YELLOW));//describe2
			/*if(infor5!=null)tooltip.add(Component.translatable(infor5).withStyle(ChatFormatting.RED));//weapon
			if(infor6!=null)tooltip.add(Component.translatable(infor6).withStyle(ChatFormatting.RED));//health*/
		}
		if(type==3){
			tooltip.add(Component.translatable("advancearmy.infor.building_recycle.desc").withStyle(ChatFormatting.BLUE));
		}
	}
	
	public void spawnAddSeat(Level worldIn, Player playerIn,double x, double y, double z, Entity seat){
		chooseEntity(worldIn, playerIn, 0, x, y, z);
		if(ent!=null)spawnsoldier(ent, worldIn, playerIn, x, y, z, 0,seat);
		if(vehicle!=null)spawnvehicle(vehicle, worldIn, playerIn, x, y, z, 0);
	}
	
	
	public void spawnCreature(Level worldIn, Player playerIn, int weaponid, boolean summon, double x, double y, double z, int summonid)
	{
		if (worldIn.isClientSide) return;
		chooseEntity(worldIn, playerIn, weaponid, x, y, z);
		if(summon){
			if(ent!=null)spawnsoldier(ent, worldIn, playerIn, x, y, z, summonid,null);
			if(vehicle!=null)spawnvehicle(vehicle, worldIn, playerIn, x, y, z, summonid);
		}else{
			if(playerIn!=null){
				if(playerIn.getScore()>=xp||playerIn.isCreative()){
					if(!playerIn.isCreative()){
						playerIn.giveExperiencePoints(-xp);
						playerIn.getCooldowns().addCooldown(this, cool);
					}
					if(ent!=null)spawnsoldier(ent, worldIn, playerIn, x, y, z, summonid,null);
					if(vehicle!=null)spawnvehicle(vehicle, worldIn, playerIn, x, y, z, summonid);
					if(building!=null)spawnbuilding(building, worldIn, playerIn, x, y, z);
				}/*else{
					//if(playerIn.level().isClientSide)playerIn.sendSystemMessage(Component.translatable("ExperiencePoints not enough!", new Object[0]), playerIn.getUUID());
				}*/
			} 
		}
	}
	
	void chooseEntity(Level worldIn, Player playerIn, int weaponid, double x, double y, double z){
		if(id == 1){
			ent = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(), worldIn);
			if(weaponid==1){
				if(ent.level().random.nextInt(3)==1){
					ent.setWeapon(1);
				}else if(ent.level().random.nextInt(3)==2){
					ent.setWeapon(11);
				}else{
					ent.setWeapon(12);
				}
			}else if(weaponid==2){
				if(ent.level().random.nextInt(4)==1){
					ent.setWeapon(4);
				}else if(ent.level().random.nextInt(4)==2){
					ent.setWeapon(13);
				}else if(ent.level().random.nextInt(4)==3){
					ent.setWeapon(3);
				}else{
					ent.setWeapon(6);
				}
			}else if(weaponid==3){
				if(ent.level().random.nextInt(2)==1){
					ent.setWeapon(14);
				}else{
					ent.setWeapon(2);
				}
			}else if(weaponid==4){
				ent.setWeapon(8);
			}else if(weaponid==5){
				ent.setWeapon(5);
			}
		}else if(id == 2){
			vehicle = new EntitySA_Tank(ModEntities.ENTITY_TANK.get(), worldIn);
		}else if(id == 3){
			ent = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(), worldIn);
			if(weaponid==1){
				if(ent.level().random.nextInt(3)==1){
					ent.setWeapon(8);
				}else{
					ent.setWeapon(1);
				}
			}else if(weaponid==2){
				if(ent.level().random.nextInt(4)==1){
					ent.setWeapon(6);
				}else{
					ent.setWeapon(3);
				}
			}else if(weaponid==3){
				if(ent.level().random.nextInt(2)==1){
					ent.setWeapon(7);
				}else{
					ent.setWeapon(2);
				}
			}else if(weaponid==4){
				ent.setWeapon(5);
			}else if(weaponid==5){
				ent.setWeapon(4);
			}
		}else if(id == 4){
			vehicle = new EntitySA_FTK(ModEntities.ENTITY_FTK.get(), worldIn);
		}else if(id == 5){
			vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), worldIn);
		}else if(id == 6){
			vehicle = new EntitySA_Prism(ModEntities.ENTITY_PRISM.get(), worldIn);
		}else if(id == 7){
			vehicle = new EntitySA_Helicopter(ModEntities.ENTITY_HELI.get(), worldIn);
		}else if(id == 8){
			vehicle = new EntitySA_Plane(ModEntities.ENTITY_PLANE.get(), worldIn);
		}else if(id == 9){
			vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), worldIn);
		}else if(id == 10){
			vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), worldIn);
		}else if(id == 11){
			vehicle = new EntitySA_LAVAA(ModEntities.ENTITY_LAVAA.get(), worldIn);
		}else if(id == 12){
			vehicle = new EntitySA_LaserAA(ModEntities.ENTITY_LAA.get(), worldIn);
		}else if(id == 13){
			vehicle = new EntitySA_Sickle(ModEntities.ENTITY_SICKLE.get(), worldIn);
		}else if(id == 14){
			vehicle = new EntitySA_MASTDOM(ModEntities.ENTITY_MAST.get(), worldIn);
		}else if(id == 15){
			vehicle = new EntitySA_M2A2(ModEntities.ENTITY_M2A2.get(), worldIn);
		}else if(id == 16){
			vehicle = new EntitySA_99G(ModEntities.ENTITY_99G.get(), worldIn);
		}else if(id == 17){
			vehicle = new EntitySA_M109(ModEntities.ENTITY_M109.get(), worldIn);
		}else if(id == 18){
			vehicle = new EntitySA_FTK_H(ModEntities.ENTITY_FTK_H.get(), worldIn);
		}else if(id == 19){
			vehicle = new EntitySA_AH1Z(ModEntities.ENTITY_AH1Z.get(), worldIn);
		}else if(id == 20){
			vehicle = new EntitySA_Car(ModEntities.ENTITY_CAR.get(), worldIn);
		}else if(id == 21){
			vehicle = new EntitySA_Hmmwv(ModEntities.ENTITY_HMMWV.get(), worldIn);
		}else if(id == 22){
			vehicle = new EntitySA_F35(ModEntities.ENTITY_F35.get(), worldIn);
		}else if(id == 23){
			vehicle = new EntitySA_BattleShip(ModEntities.ENTITY_BSHIP.get(), worldIn);
		}else if(id == 24){
			vehicle = new EntitySA_M2A2AA(ModEntities.ENTITY_M2A2AA.get(), worldIn);
		}else if(id == 25){
			vehicle = new EntitySA_LAV(ModEntities.ENTITY_LAV.get(), worldIn);
		}else if(id == 26){
			vehicle = new EntitySA_APAGAT(ModEntities.ENTITY_APAGAT.get(), worldIn);
		}else if(id == 27){
			vehicle = new EntitySA_BMP2(ModEntities.ENTITY_BMP2.get(), worldIn);
		}else if(id == 28){
			vehicle = new EntitySA_BMPT(ModEntities.ENTITY_BMPT.get(), worldIn);
		}else if(id == 29){
			vehicle = new EntitySA_A10a(ModEntities.ENTITY_A10A.get(), worldIn);
		}else if(id == 30){
			vehicle = new EntitySA_A10c(ModEntities.ENTITY_A10C.get(), worldIn);
		}else if(id == 31){
			vehicle = new EntitySA_AH6(ModEntities.ENTITY_AH6.get(), worldIn);
		}else if(id == 32){
			vehicle = new EntitySA_MI24(ModEntities.ENTITY_MI24.get(), worldIn);
		}else if(id == 33){
			vehicle = new EntitySA_SU33(ModEntities.ENTITY_SU33.get(), worldIn);
		}else if(id == 34){
			ent = new EntitySA_OFG(ModEntities.ENTITY_OFG.get(), worldIn);
		}else if(id == 35){
			vehicle = new EntitySA_Mortar(ModEntities.ENTITY_MORTAR.get(), worldIn);
		}else if(id == 36){
			vehicle = new EntitySA_M2hb(ModEntities.ENTITY_M2HB.get(), worldIn);
		}else if(id == 37){
			vehicle = new EntitySA_Kord(ModEntities.ENTITY_KORD.get(), worldIn);
		}else if(id == 38){
			ent = new EntitySA_GI(ModEntities.ENTITY_GI.get(), worldIn);
		}else if(id == 39){
			ent = new EntitySA_ConscriptX(ModEntities.ENTITY_CONSX.get(), worldIn);
		}else if(id == 40){
			vehicle = new EntitySA_TOW(ModEntities.ENTITY_TOW.get(), worldIn);
		}else if(id == 41){
			vehicle = new EntitySA_STIN(ModEntities.ENTITY_STIN.get(), worldIn);
		}else if(id == 42){
			building = new SoldierMachine(ModEntities.ENTITY_SMAC.get(), worldIn);
		}else if(id == 43){
			building = new VehicleMachine(ModEntities.ENTITY_VMAC.get(), worldIn);
		}else if(id == 44){
			ent = new EntitySA_RADS(ModEntities.ENTITY_RADS.get(), worldIn);
		}else if(id == 45){
			ent = new EntitySA_GAT(ModEntities.ENTITY_GAT.get(), worldIn);
		}else if(id == 46){
			vehicle = new EntitySA_Reaper(ModEntities.ENTITY_REAPER.get(), worldIn);
		}else if(id == 47){
			vehicle = new EntitySA_MMTank(ModEntities.ENTITY_MMTANK.get(), worldIn);
		}else if(id == 48){
			building = new SandBag(ModEntities.ENTITY_SANDBAG.get(), worldIn);
		}else if(id == 49){
			building = new SandBag(ModEntities.ENTITY_SANDBAG.get(), worldIn);
			building.getAttribute(Attributes.MAX_HEALTH).setBaseValue(60);
			building.setHealth(60);
		}else if(id == 50){
			building = new SandBag(ModEntities.ENTITY_SANDBAG.get(), worldIn);
			building.getAttribute(Attributes.MAX_HEALTH).setBaseValue(90);
			building.setHealth(90);
		}else if(id == 51){
			vehicle = new EntitySA_Mirage(ModEntities.ENTITY_MIRAGE.get(), worldIn);
		}else if(id == 52){
			vehicle = new EntitySA_Tesla(ModEntities.ENTITY_TESLA.get(), worldIn);
		}else if(id == 53){
			vehicle = new EntitySA_Bike(ModEntities.ENTITY_BIKE.get(), worldIn);
		}else if(id == 54){
			ent = new EntitySA_MWDrone(ModEntities.ENTITY_MWD.get(), worldIn);
		}else if(id == 55){
			vehicle = new EntitySA_RockTank(ModEntities.ENTITY_RCTANK.get(), worldIn);
		}else if(id == 56){
			vehicle = new EntitySA_STAPC(ModEntities.ENTITY_STAPC.get(), worldIn);
		}
	}
	
	public void spawnsoldier(EntitySA_SoldierBase ent, Level worldIn, Player playerIn, double x, double y, double z, int summonid, Entity seat)
	{
		if(ent!=null){
			if(playerIn!=null){
				if(playerIn.isCrouching() && playerIn.isCreative()){
				}else{
					ent.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof PlayerTeam){
						playerIn.level().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), (PlayerTeam)playerIn.getTeam());
					}
				}
			}
			++y;
			if(summonid==1){
				ent.setMoveType(2);
				ent.setMovePosX((int)x);
				ent.setMovePosZ((int)z+6);
			}else{
				ent.setMoveType(1);
			}
			ent.moveTo(x + 0.5, y, z + 0.5, 0, 0);
			worldIn.addFreshEntity(ent);
			if(seat!=null)ent.startRiding(seat);
		} 
	}
	public void spawnbuilding(TamableAnimal ent, Level worldIn, Player playerIn, double x, double y, double z)
	{
		if(ent!=null){
			if(playerIn!=null){
				if(ent instanceof SandBag){
					((SandBag)ent).setYawRoteNBT(90-(int)playerIn.yHeadRot);
				}
				if(playerIn.isCrouching() && playerIn.isCreative()){
				}else{
					ent.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof PlayerTeam){
						playerIn.level().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), (PlayerTeam)playerIn.getTeam());
					}
				}
			}
			++y;
			//ent.setMoveType(1);
			ent.moveTo(x + 0.5, y, z + 0.5, 0, 0);
			worldIn.addFreshEntity(ent);
		} 
	}
	public void spawnvehicle(WeaponVehicleBase entity, Level worldIn, Player playerIn, double x, double y, double z, int summonid)
	{
		if(entity!=null){
			if(playerIn!=null){
				if(playerIn.isCrouching() && playerIn.isCreative()){
				}else{
					entity.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof PlayerTeam){
						playerIn.level().getScoreboard().addPlayerToTeam(entity.getUUID().toString(), (PlayerTeam)playerIn.getTeam());
					}
				}
				//entity.turretYaw = entity.yBodyRot= entity.yRot = entity.yHeadRot = -((float) Math.atan2(x - playerIn.getX(), z - playerIn.getZ())) * 180.0F/ (float) Math.PI;
			}

			entity.setRemain1(entity.magazine);
			entity.setRemain2(entity.magazine2);
			entity.setRemain3(entity.magazine3);
			entity.setRemain4(entity.magazine4);
			++y;
			entity.setArmyType2(summonid);
			entity.moveTo(x + 0.5, y, z + 0.5, 0, 0);
			entity.setMoveType(1);
			/*if (!(worldIn instanceof ServerLevel)) {
				//return false;
			}else{
				ServerLevel serverworld = (ServerLevel)worldIn;
				DifficultyInstance difficulty = worldIn.getCurrentDifficultyAt(entity.blockPosition());
				entity.finalizeSpawn(serverworld, difficulty, 
									MobSpawnType.REINFORCEMENT, 
									(SpawnGroupData) null, 
									(CompoundTag) null);
			}*/
			worldIn.addFreshEntity(entity);
		} 
	}
   
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		ItemStack offitem = context.getPlayer().getOffhandItem();
		Item item2 = offitem.getItem();
		if (world.isClientSide && (item2==null||!(item2 instanceof ItemSupport))) {
			if(!context.getPlayer().isCreative() && type!=3){
				if(type==0){
					context.getPlayer().sendSystemMessage(Component.translatable("advancearmy.infor.spawn2.desc"));
				}else{
					context.getPlayer().sendSystemMessage(Component.translatable("advancearmy.infor.spawn3.desc"));
				}	 
			}
			return InteractionResult.SUCCESS;
		} else {
			if(context.getPlayer().isCreative()||type == 3||item2!=null && item2 instanceof ItemSupport && ((ItemSupport)item2).isSummon){
				BlockPos pos = context.getClickedPos();
				BlockState blockstate = world.getBlockState(pos);
				int weaponid = 0;
				if(blockstate.is(BlockRegister.ASSULT.get()))weaponid=1;
				if(blockstate.is(BlockRegister.RECON.get()))weaponid=2;
				if(blockstate.is(BlockRegister.ENGINEER.get()))weaponid=3;
				if(blockstate.is(BlockRegister.MEDIC.get()))weaponid=4;
				if(blockstate.is(BlockRegister.SUPPORT.get()))weaponid=5;
				
				if(context.getPlayer().isCreative()){
					if(item2 instanceof ItemSpawn){
						ItemSpawn vehicleitem = (ItemSpawn)item2;
						if(vehicleitem.type == 0){
							vehicleitem.spawnCreature(world, context.getPlayer(), 0, true, (double)pos.getX(), (double)pos.getY()+1, (double)pos.getZ(), 3);
							if(vehicleitem.vehicle!=null && vehicleitem.vehicle.seatMaxCount>1){
								for(int k2 = 0; k2 < vehicleitem.vehicle.seatMaxCount; ++k2){
									spawnCreature(world, context.getPlayer(), weaponid, false, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),0);
								}
							}
						}
						
					}
				}
				ItemStack itemstack = context.getItemInHand();
				if(itemstack == context.getPlayer().getMainHandItem()){
					spawnCreature(world, context.getPlayer(), weaponid, false, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),0);
				}
				if(type == 3)itemstack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
	}

	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity entity) {
	  return false;
	}
}