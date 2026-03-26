package advancearmy.registry;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.Structure;
import advancearmy.AdvanceArmy;
import advancearmy.world.structure.SmallDefenseStructure;
import advancearmy.world.structure.FriendBaseStructure;
import advancearmy.world.structure.EnemyBaseStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import advancearmy.AAConfig;
@Mod.EventBusSubscriber(modid = AdvanceArmy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, AdvanceArmy.MODID);
	public static final RegistryObject<StructureType<SmallDefenseStructure>> DEFENSE = registerSTRUCTURE("defense_work",SmallDefenseStructure.CODEC);
	public static final RegistryObject<StructureType<FriendBaseStructure>> FRIEND = registerSTRUCTURE("friend",FriendBaseStructure.CODEC);
	public static final RegistryObject<StructureType<EnemyBaseStructure>> ENEMY = registerSTRUCTURE("enemy",EnemyBaseStructure.CODEC);
	
	private static <T extends Structure> RegistryObject<StructureType<T>> registerSTRUCTURE(String name, Codec code){
		/*if(AAConfig.addStructure)*/{
			return STRUCTURES.register(name, () -> () -> code);
		}/*else{
			return null;
		}*/
	}
}
