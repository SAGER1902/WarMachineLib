package wmlib.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public interface IMessage2
{
    void encode(FriendlyByteBuf buffer);

    void decode(FriendlyByteBuf buffer);

    void handle(Supplier<NetworkEvent.Context> supplier);
}
