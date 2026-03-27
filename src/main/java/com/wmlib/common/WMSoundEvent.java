package wmlib.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wmlib.WarMachineLib;
public class WMSoundEvent {
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WarMachineLib.MOD_ID);
	
	public static final RegistryObject<SoundEvent> reload_mag = registerSound("wmlib.reload_mag");
	public static SoundEvent getSound(String soundbase) {
		return null;
	}

    private static RegistryObject<SoundEvent> registerSound(String id)
    {
        return WMSoundEvent.REGISTER.register(id, () -> new SoundEvent(new ResourceLocation(WarMachineLib.MOD_ID, id)));
    }
}