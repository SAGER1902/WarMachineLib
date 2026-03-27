package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageShoot implements IMessage<MessageShoot>
{
    public MessageShoot() {}

    public MessageShoot(PlayerEntity player)
    {
    }

	@Override
	public void encode(MessageShoot message, PacketBuffer buffer)
	{
	}

	@Override
	public MessageShoot decode(PacketBuffer buffer)
	{
		return new MessageShoot();
	}
	@Override
	public void handle(MessageShoot message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleShoot(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
