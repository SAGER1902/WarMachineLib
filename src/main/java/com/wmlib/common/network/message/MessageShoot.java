package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageShoot implements IMessage<MessageShoot>
{
    public MessageShoot() {}

    public MessageShoot(Player player)
    {
    }

	@Override
	public void encode(MessageShoot message, FriendlyByteBuf buffer)
	{
	}

	@Override
	public MessageShoot decode(FriendlyByteBuf buffer)
	{
		return new MessageShoot();
	}
	@Override
	public void handle(MessageShoot message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleShoot(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
