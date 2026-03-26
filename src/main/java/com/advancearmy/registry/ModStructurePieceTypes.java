package advancearmy.registry;

import advancearmy.AdvanceArmy;
import advancearmy.world.structure.DefenseGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import advancearmy.AAConfig;
@Mod.EventBusSubscriber(modid = AdvanceArmy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModStructurePieceTypes {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, AdvanceArmy.MODID);
    public static final RegistryObject<StructurePieceType> DEFENSE = register("defense_work", DefenseGenerator.Piece::new);
	public static final RegistryObject<StructurePieceType> FRIEND = register("friend", DefenseGenerator.Piece::new);
	public static final RegistryObject<StructurePieceType> ENEMY = register("enemy", DefenseGenerator.Piece::new);
    private static RegistryObject<StructurePieceType> register(String id, StructurePieceType type) {
        return STRUCTURE_PIECE_TYPES.register(id, () -> type);
    }

    private static RegistryObject<StructurePieceType> register(String id, StructurePieceType.StructureTemplateType type) {
		/*if(AAConfig.addStructure)*/{
			return register(id, (StructurePieceType) type);
		}/*else{
			return null;
		}*/
    }
}
