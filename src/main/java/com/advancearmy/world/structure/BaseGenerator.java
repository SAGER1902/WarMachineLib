package advancearmy.world.structure;

import advancearmy.AdvanceArmy;
import advancearmy.registry.ModStructurePieceTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;

import net.minecraft.world.entity.TamableAnimal;
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
import advancearmy.entity.building.SoldierMachine;
import advancearmy.entity.building.VehicleMachine;
import advancearmy.entity.building.SandBag;
import advancearmy.entity.EntitySA_SquadBase;

import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.ERO_Spider;
import advancearmy.entity.mob.ERO_Giant;
import advancearmy.entity.mob.ERO_Ravager;
import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Creeper;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_ROCKET;
import advancearmy.entity.mob.ERO_AA;
import advancearmy.entity.mob.ERO_Mortar;
import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;
import advancearmy.entity.mob.EntityMobSquadBase;
import advancearmy.entity.mob.EntityRaiderSquadBase;
import advancearmy.AAConfig;
import advancearmy.init.ModEntities;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

import net.minecraft.util.RandomSource;
import java.util.List;
import java.util.Random; // 或使用 RandomSource

public class BaseGenerator {
	public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, 
                             StructurePieceAccessor holder, boolean isFriend) {
		if (AAConfig.addStructure) {
			// 1. 根据权重随机选择一个建筑及其原始 pieceId
			WeightedBuilding selected = selectWeightedBuilding(isFriend);
			// 2. 添加建筑方块，使用选中的 pieceId 替换原传入的 id
			holder.addPiece(new BaseGenerator.Piece(manager, 
					selected.templateId(), pos, rotation, selected.pieceId()));
		} else {
			// 未开启结构生成时，添加占位建筑（void），pieceId = -1
			holder.addPiece(new BaseGenerator.Piece(manager,
					ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "void"),
					pos, rotation, -1));
		}
	}
	
	public static void addPiecesOne(StructureTemplateManager manager, BlockPos pos, Rotation rotation, StructurePieceAccessor holder, int id) {
		if (AAConfig.addStructure) {
			if(id==1){
				holder.addPiece(new BaseGenerator.Piece(manager,ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_camp"),pos, rotation, 11));
			}
		} else {
			holder.addPiece(new BaseGenerator.Piece(manager,ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "void"),pos, rotation, -1));
		}
	}
	
	// 带权重、原始 pieceId 和模板ID的建筑定义
	private static record WeightedBuilding(int pieceId, ResourceLocation templateId, int weight) {}

	// 所有可生成的建筑列表（按原 id 顺序，并赋予权重）
	private static final List<WeightedBuilding> BUILDINGS = List.of(
		new WeightedBuilding(0,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_aa"),         15),
		new WeightedBuilding(1,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_base_air"),   6),
		new WeightedBuilding(2,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_camp"),       20),
		new WeightedBuilding(3,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_camp_giant"), 2),
		new WeightedBuilding(4,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_fact"),       4),
		new WeightedBuilding(5,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_hide_at"),    16),
		new WeightedBuilding(6,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_hide_mg"),    16),
		new WeightedBuilding(7,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_hide_tank"),  15),
		new WeightedBuilding(8,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_tank_dirt"),  17),
		new WeightedBuilding(9,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "m_tower"),      18)
	);

	private static final List<WeightedBuilding> BUILDINGS2 = List.of(
		new WeightedBuilding(10, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_at"),         14),
		new WeightedBuilding(11, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_camp"),       19),
		new WeightedBuilding(12, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_camp_m"),     4),
		new WeightedBuilding(13, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_cannon"),     15),
		new WeightedBuilding(14, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_aa"),         15),
		new WeightedBuilding(15, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "v_tank"),       13),
		new WeightedBuilding(16, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_gi"),         14),
		new WeightedBuilding(17, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_ofg"),        14),
		new WeightedBuilding(18, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_bunker1"),    16),
		new WeightedBuilding(19, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "s_bunker2"),    16),
		new WeightedBuilding(20, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "v_heli"),       13)
	);

	// 权重随机选择算法，返回完整的 WeightedBuilding 对象（包含 pieceId）
	private static WeightedBuilding selectWeightedBuilding(boolean isFriend) {
		RandomSource random = RandomSource.create(); // 也可使用类静态变量复用
		if(isFriend){
			int totalWeight = BUILDINGS2.stream().mapToInt(WeightedBuilding::weight).sum();
			int r = random.nextInt(totalWeight);
			for (WeightedBuilding b : BUILDINGS2) {
				r -= b.weight();
				if (r < 0) {
					return b;
				}
			}
			// fallback（不会执行到这里）
			return BUILDINGS2.get(0);
		}else{
			int totalWeight = BUILDINGS.stream().mapToInt(WeightedBuilding::weight).sum();
			int r = random.nextInt(totalWeight);
			for (WeightedBuilding b : BUILDINGS) {
				r -= b.weight();
				if (r < 0) {
					return b;
				}
			}
			// fallback（不会执行到这里）
			return BUILDINGS.get(0);
		}

	}

    public static class Piece extends TemplateStructurePiece {
        public static final String ROTATION_KEY = "Rotation";
		int id;
        public Piece(StructureTemplateManager manager, ResourceLocation res, BlockPos pos, Rotation rotation, int i) {
            super(ModStructurePieceTypes.DEFENSE.get(), 64, manager, res, res.toString(), createPlacementData(rotation), pos);
			this.id = i;
        }

        public Piece(StructureTemplateManager manager, CompoundTag nbt) {
            super(ModStructurePieceTypes.DEFENSE.get(), nbt, manager, res -> createPlacementData(Rotation.valueOf(nbt.getString(ROTATION_KEY))));
        }

        private static StructurePlaceSettings createPlacementData(Rotation rotation) {
            return new StructurePlaceSettings().setRotation(rotation).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
            super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
            compoundTag.putString(ROTATION_KEY, this.placeSettings.getRotation().name());
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
			/*if (isBlockedAbove(world, boundingBox)) {
				// 上方有遮挡物，放弃生成，直接返回false
				return;
			}*/
            super.postProcess(world, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, blockPos);
        }
        @Override
        protected void handleDataMarker(String metadata, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource random, BoundingBox boundingBox) {
            if ("chest".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/tank_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.WEST)));
            }else if ("chest1".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/soldier_camp_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
            }else if ("chestgiant".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/giant_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
            }else if ("chestair".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/mob_air_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
            }else if ("chest_reb".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/reb_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
            }else if ("chest_soldier".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/soldier_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
            }else{
				if(id<10){
					if ("mob".equals(metadata)||"mob_d".equals(metadata)){
						if(random.nextInt(2)==0){
							EntityMobSquadBase ent = new ERO_REB(ModEntities.ENTITY_REB.get(), serverLevelAccessor.getLevel());
							if("mob_d".equals(metadata))ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							serverLevelAccessor.addFreshEntity(ent);
						}else{
							EntityRaiderSquadBase ent = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverLevelAccessor.getLevel());
							if("mob_d".equals(metadata))ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							serverLevelAccessor.addFreshEntity(ent);
						}
					}else if ("mob2".equals(metadata)){
						ERO_Pillager mob = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						mob.setMoveType(4);
						mob.setMoveType(2);
						mob.setMovePosX((int)blockPos.getX());
						mob.setMovePosY((int)blockPos.getY());
						mob.setMovePosZ((int)blockPos.getZ());
						serverLevelAccessor.addFreshEntity(mob);
					}else if ("mob1".equals(metadata)){
						ERO_REB mob = new ERO_REB(ModEntities.ENTITY_REB.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor.addFreshEntity(mob);
					}else if ("giant".equals(metadata)){
						ERO_Giant mob = new ERO_Giant(ModEntities.ENTITY_GIANT.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor/*.getLevel()*/.addFreshEntity(mob);
					}else if ("eroaa".equals(metadata)){
						ERO_AA mob = new ERO_AA(ModEntities.E_AA.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor.addFreshEntity(mob);
					}else if ("erospg".equals(metadata)){
						ERO_ROCKET mob = new ERO_ROCKET(ModEntities.E_ROCKET.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor.addFreshEntity(mob);
					}else if ("chestgiant".equals(metadata)) {
						Rotation rotation = this.placeSettings.getRotation();
						this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
						ResourceLocation.tryParse("advancearmy:chests/tank_chest"),
						Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
					}
				}else{
					if ("soldier".equals(metadata)||"soldier_d".equals(metadata)){
						EntitySA_SquadBase ent = null;
						if(random.nextInt(2)==0){
							ent = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(), serverLevelAccessor.getLevel());
						}else{
							ent = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(), serverLevelAccessor.getLevel());
						}
						if(ent!=null){
							if(AAConfig.structureFriendTeam){
								if(serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
									serverLevelAccessor.getLevel().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy"));
								}
							}
							if("soldier_d".equals(metadata))ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							serverLevelAccessor.addFreshEntity(ent);
						}
					}else{
						if ("ranger".equals(metadata)||"ranger_d".equals(metadata)){
							EntitySA_Soldier ent = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(), serverLevelAccessor.getLevel());
							if("ranger_d".equals(metadata))ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							if(AAConfig.structureFriendTeam){
								if(serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
									serverLevelAccessor.getLevel().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy"));
								}
							}
							serverLevelAccessor.addFreshEntity(ent);
						}
						if ("gi".equals(metadata)){
							EntitySA_GI ent = new EntitySA_GI(ModEntities.ENTITY_GI.get(), serverLevelAccessor.getLevel());
							ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							if(AAConfig.structureFriendTeam){
								if(serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
									serverLevelAccessor.getLevel().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy"));
								}
							}
							serverLevelAccessor.addFreshEntity(ent);
						}
						if ("ofg".equals(metadata)){
							EntitySA_OFG ent = new EntitySA_OFG(ModEntities.ENTITY_OFG.get(), serverLevelAccessor.getLevel());
							if(random.nextInt(2)==0)ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							if(AAConfig.structureFriendTeam){
								if(serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
									serverLevelAccessor.getLevel().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy"));
								}
							}
							serverLevelAccessor.addFreshEntity(ent);
						}
						if ("cons".equals(metadata)){
							EntitySA_Conscript ent = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(), serverLevelAccessor.getLevel());
							if(random.nextInt(2)==0)ent.setMoveType(3);
							ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
							if(AAConfig.structureFriendTeam){
								if(serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
									serverLevelAccessor.getLevel().getScoreboard().addPlayerToTeam(ent.getUUID().toString(), serverLevelAccessor.getLevel().getScoreboard().getPlayerTeam("AdvanceArmy"));
								}
							}
							serverLevelAccessor.addFreshEntity(ent);
						}
					}
				}
				
				{
					WeaponVehicleBase vehicle = null;
					if ("tank".equals(metadata)){
						if(random.nextInt(5)==0){
							vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==1){
							vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==2){
							vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), serverLevelAccessor.getLevel());
						}else{
							vehicle = new EntitySA_Tank(ModEntities.ENTITY_TANK.get(), serverLevelAccessor.getLevel());
						}
					}
					if ("apc".equals(metadata)){
						if(random.nextInt(5)==0){
							vehicle = new EntitySA_BMP2(ModEntities.ENTITY_BMP2.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==1){
							vehicle = new EntitySA_M2A2(ModEntities.ENTITY_M2A2.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==2){
							vehicle = new EntitySA_M2A2AA(ModEntities.ENTITY_M2A2AA.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==3){
							vehicle = new EntitySA_LAVAA(ModEntities.ENTITY_LAVAA.get(), serverLevelAccessor.getLevel());
						}else{
							vehicle = new EntitySA_LAV(ModEntities.ENTITY_LAV.get(), serverLevelAccessor.getLevel());
						}
					}
					if ("heli".equals(metadata)){
						if(random.nextInt(5)==0){
							vehicle = new EntitySA_MI24(ModEntities.ENTITY_MI24.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==1){
							vehicle = new EntitySA_AH1Z(ModEntities.ENTITY_AH1Z.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==2){
							vehicle = new EntitySA_Helicopter(ModEntities.ENTITY_HELI.get(), serverLevelAccessor.getLevel());
						}else{
							vehicle = new EntitySA_AH6(ModEntities.ENTITY_AH6.get(), serverLevelAccessor.getLevel());
						}
					}
					if ("mob_tank".equals(metadata)){
						if(random.nextInt(5)==0){
							vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==1){
							vehicle = new EntitySA_BMP2(ModEntities.ENTITY_BMP2.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==2){
							vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), serverLevelAccessor.getLevel());
						}else{
							vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), serverLevelAccessor.getLevel());
						}
						ERO_REB mob = new ERO_REB(ModEntities.ENTITY_REB.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor.addFreshEntity(mob);
					}
					if ("mob_tank1".equals(metadata)){
						if(random.nextInt(5)==0){
							vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), serverLevelAccessor.getLevel());
						}else if(random.nextInt(5)==1){
							vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), serverLevelAccessor.getLevel());
						}else{
							vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), serverLevelAccessor.getLevel());
						}
						ERO_REB mob = new ERO_REB(ModEntities.ENTITY_REB.get(), serverLevelAccessor.getLevel());
						mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor.addFreshEntity(mob);
					}
					if ("ah6".equals(metadata)){
						vehicle = new EntitySA_AH6(ModEntities.ENTITY_AH6.get(), serverLevelAccessor.getLevel());
					}
					if ("plane1".equals(metadata)){
						vehicle = new EntitySA_Plane1(ModEntities.ENTITY_PLANE1.get(), serverLevelAccessor.getLevel());
					}
					if ("plane2".equals(metadata)){
						vehicle = new EntitySA_Plane2(ModEntities.ENTITY_PLANE2.get(), serverLevelAccessor.getLevel());
					}
					if ("t72".equals(metadata)){
						vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), serverLevelAccessor.getLevel());
					}
					if ("t55".equals(metadata)){
						vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), serverLevelAccessor.getLevel());
					}
					if ("m109".equals(metadata)){
						vehicle = new EntitySA_M109(ModEntities.ENTITY_M109.get(), serverLevelAccessor.getLevel());
						vehicle.setMoveType(3);
					}
					if ("kord".equals(metadata)){
						vehicle = new EntitySA_Kord(ModEntities.ENTITY_KORD.get(), serverLevelAccessor.getLevel());
					}
					if ("m2hb".equals(metadata)){
						vehicle = new EntitySA_M2hb(ModEntities.ENTITY_M2HB.get(), serverLevelAccessor.getLevel());
					}
					if ("mortar".equals(metadata)){
						vehicle = new EntitySA_Mortar(ModEntities.ENTITY_MORTAR.get(), serverLevelAccessor.getLevel());
					}
					if ("fim92".equals(metadata)){
						vehicle = new EntitySA_STIN(ModEntities.ENTITY_STIN.get(), serverLevelAccessor.getLevel());
					}
					if ("tow".equals(metadata)){
						vehicle = new EntitySA_TOW(ModEntities.ENTITY_TOW.get(), serverLevelAccessor.getLevel());
					}
					if(vehicle!=null){
						float entityYaw = getEntityYawForRotation(this.placeSettings.getRotation());
						vehicle.setMoveYaw(entityYaw);
						vehicle.setYRot(entityYaw);
						vehicle.yBodyRot=vehicle.yHeadRot=entityYaw;
						vehicle.turretYaw=entityYaw;
						vehicle.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
						serverLevelAccessor.addFreshEntity(vehicle);
					}
				}
            }
        }
		private float getEntityYawForRotation(Rotation rotation) {
			// 计算旋转后的方向
			// 假设实体默认朝向南（SOUTH），你可以根据需求调整基准方向
			Direction baseDirection = Direction.SOUTH;
			Direction rotatedDirection = rotation.rotate(baseDirection);
			// 将方向转换为yaw角度
			// Minecraft中：南=0°, 西=90°, 北=180°, 东=270°
			return rotatedDirection.toYRot();
		}
		private boolean isBlockedAbove(WorldGenLevel world, BoundingBox boundingBox) {
			int checkHeight = 12;
			BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
			int topY = boundingBox.maxY();
			for (int y = 1; y <= checkHeight; y++) {
				int currentWorldY = topY + y;
				for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
					for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
						checkPos.set(x, currentWorldY, z);
						if (!world.getBlockState(checkPos).isAir()) {
							return true;
						}
					}
				}
			}
			return false;
		}
		private boolean isNotSupportedBelow(WorldGenLevel world, BoundingBox boundingBox) {
			int checkDepth = 7;
			BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
			int bottomY = boundingBox.minY();
			for (int depth = 1; depth <= checkDepth; depth++) {
				int currentWorldY = bottomY - depth;
				if (currentWorldY < world.getMinBuildHeight()) {
					break;
				}
				for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
					for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
						checkPos.set(x, currentWorldY, z);
						BlockState state = world.getBlockState(checkPos);
						if (!isSolidSupportBlock(state, world, checkPos)) {
							return true;
						}
					}
				}
			}
			return false;
		}
		private boolean isSolidSupportBlock(BlockState state, WorldGenLevel world, BlockPos pos) {
			if (state.isSolid() && state.getBlock() != Blocks.GRAVEL) {
				if (state.getBlock() == Blocks.GLASS || state.getBlock() == Blocks.ICE) {
					return false;
				}
				return true;
			}
			return false;
		}
    }
}
