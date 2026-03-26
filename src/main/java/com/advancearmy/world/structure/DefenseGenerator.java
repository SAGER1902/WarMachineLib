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
import advancearmy.AAConfig;
import advancearmy.entity.mob.EntityMobSquadBase;
import advancearmy.entity.mob.EntityRaiderSquadBase;
import advancearmy.init.ModEntities;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

import java.util.List;

public class DefenseGenerator {

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, 
                                 StructurePieceAccessor holder) {
        if (AAConfig.addStructure) {
            // 根据权重随机选择一个防御建筑（包含原始 pieceId 和模板路径）
            WeightedDefense selected = selectWeightedDefense();
            // 添加建筑方块，使用选中的 pieceId
            holder.addPiece(new DefenseGenerator.Piece(manager, 
                    selected.templateId(), pos, rotation, selected.pieceId()));
        } else {
            // 未开启结构生成时，添加占位建筑（void），pieceId = -1
            holder.addPiece(new BaseGenerator.Piece(manager,
                    ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "void"),
                    pos, rotation, -1));
        }
    }

    // 带权重、原始 pieceId 和模板ID的防御建筑定义
    private static record WeightedDefense(int pieceId, ResourceLocation templateId, int weight) {}

    // 所有可生成的防御建筑列表（按原 id 顺序，权重可自定义调整）
    private static final List<WeightedDefense> DEFENSES = List.of(
        new WeightedDefense(0,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_bunker1"), 14),
        new WeightedDefense(1,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_bunker1"), 12),
        new WeightedDefense(2,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_bunker2"), 8),
        new WeightedDefense(3,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_bunker2"), 10),
        new WeightedDefense(4,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_fox_hole1"), 15),
        new WeightedDefense(5,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_fox_hole1"), 16),
        new WeightedDefense(6,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_fox_hole2"), 17),
        new WeightedDefense(7,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_fox_hole2"), 18),
        new WeightedDefense(8,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_mg"), 11),
        new WeightedDefense(9,  ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_mg"), 12),
        new WeightedDefense(10, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_trench_mg"), 7),
        new WeightedDefense(11, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_trench_mg"), 6),
        new WeightedDefense(12, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_trench_mortar"), 5),
        new WeightedDefense(13, ResourceLocation.tryParse(AdvanceArmy.MODID + ":" + "d_trench_mortar"), 4)
    );

    // 权重随机选择算法，返回完整的 WeightedDefense 对象
    private static WeightedDefense selectWeightedDefense() {
        RandomSource random = RandomSource.create(); // 可优化为复用随机源
        int totalWeight = DEFENSES.stream().mapToInt(WeightedDefense::weight).sum();
        int r = random.nextInt(totalWeight);
        
        for (WeightedDefense def : DEFENSES) {
            r -= def.weight();
            if (r < 0) {
                return def;
            }
        }
        // fallback（正常情况下不会执行到这里）
        return DEFENSES.get(0);
    }

    public static class Piece extends TemplateStructurePiece {
        public static final String ROTATION_KEY = "Rotation";
		int id;
        public Piece(StructureTemplateManager manager, ResourceLocation res, BlockPos pos, Rotation rotation, int i) {
            super(ModStructurePieceTypes.DEFENSE.get(), 64, manager, res, res.toString(), createPlacementData(rotation), pos);
			id=i;
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
			/*if (isBlockedAbove(world, boundingBox) || isNotSupportedBelow(world, boundingBox)) {
				// 上方有遮挡物，放弃生成，直接返回false
				return;
			}*/
			//if(!AAConfig.addMobBase)return;
            super.postProcess(world, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, blockPos);
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource random, BoundingBox boundingBox) {
			boolean isEnemy = id%2==0;
            /*if ("chest".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/tank_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.WEST)));
            }else if ("chest1".equals(metadata)) {
                Rotation rotation = this.placeSettings.getRotation();
                this.createChest(serverLevelAccessor, boundingBox, random, blockPos, 
				ResourceLocation.tryParse("advancearmy:chests/tank_chest"),
				Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST)));
            }else*/{
					if("man".equals(metadata)||"man_d".equals(metadata)){
						if (isEnemy){
							if(random.nextInt(2)==0){
								EntityMobSquadBase ent = new ERO_REB(ModEntities.ENTITY_REB.get(), serverLevelAccessor.getLevel());
								if("man_d".equals(metadata))ent.setMoveType(3);
								ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
								serverLevelAccessor.addFreshEntity(ent);
							}else{
								EntityRaiderSquadBase ent = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverLevelAccessor.getLevel());
								if("man_d".equals(metadata))ent.setMoveType(3);
								ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
								serverLevelAccessor.addFreshEntity(ent);
							}
						}else{
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
								ent.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
								if("man_d".equals(metadata))ent.setMoveType(3);
								serverLevelAccessor.addFreshEntity(ent);
							}
						}
					}
				{
					WeaponVehicleBase vehicle = null;
					if("hmg".equals(metadata)){
						if(isEnemy){
							vehicle = new EntitySA_Kord(ModEntities.ENTITY_KORD.get(), serverLevelAccessor.getLevel());
						}else{
							vehicle = new EntitySA_M2hb(ModEntities.ENTITY_M2HB.get(), serverLevelAccessor.getLevel());
						}
					}
					if ("mortar".equals(metadata)){
						vehicle = new EntitySA_Mortar(ModEntities.ENTITY_MORTAR.get(), serverLevelAccessor.getLevel());
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
			Direction baseDirection = Direction.SOUTH;
			Direction rotatedDirection = rotation.rotate(baseDirection);
			return rotatedDirection.toYRot();
		}
		private boolean isBlockedAbove(WorldGenLevel world, BoundingBox boundingBox) {
			// 可配置的检查参数
			int checkHeight = 11; // 从结构顶部（maxY）向上检查多少格
			BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
			// 获取结构顶部Y坐标，并向上检查
			int topY = boundingBox.maxY();
			// 遍历结构在XZ平面上的投影区域，并逐层向上检查
			for (int y = 1; y <= checkHeight; y++) {
				int currentWorldY = topY + y;
				for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
					for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
						checkPos.set(x, currentWorldY, z);
						// 核心判断：如果当前位置不是空气，则视为被遮挡
						if (!world.getBlockState(checkPos).isAir()) {
							// 发现遮挡，立即返回true，中断所有后续检查
							return true;
						}
					}
				}
			}
			// 所有检查位置都是空气，安全
			return false;
		}
		private boolean isNotSupportedBelow(WorldGenLevel world, BoundingBox boundingBox) {
			// 可配置的检查参数
			int checkDepth = 6; // 从结构底部（minY）向下检查的深度（格数）
			BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
			// 获取结构底部Y坐标
			int bottomY = boundingBox.minY();
			// 遍历结构在XZ平面上的投影区域，并逐层向下检查
			for (int depth = 1; depth <= checkDepth; depth++) {
				int currentWorldY = bottomY - depth;
				// 简单优化：如果已低于世界底部，停止检查（通常不会发生）
				if (currentWorldY < world.getMinBuildHeight()) {
					break;
				}
				for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
					for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
						checkPos.set(x, currentWorldY, z);
						BlockState state = world.getBlockState(checkPos);
						// 核心判断：如果当前位置是“非支撑性”方块，则判定为悬空风险
						if (!isSolidSupportBlock(state, world, checkPos)) {
							// 发现一个位置缺少支撑，立即返回true（判定为悬空）
							return true;
						}
					}
				}
			}
			// 所有检查位置都有稳固支撑
			return false;
		}
		private boolean isSolidSupportBlock(BlockState state, WorldGenLevel world, BlockPos pos) {
			if (state.isSolid() && state.getBlock() != Blocks.GRAVEL) { // 排除沙砾，因为会下落
				// 2. 可选：排除一些特殊的“不可靠”固体，如玻璃、菌丝等（根据你的模组设定）
				if (state.getBlock() == Blocks.GLASS || state.getBlock() == Blocks.ICE) {
					return false;
				}
				return true;
			}
			return false;
		}
    }
}
