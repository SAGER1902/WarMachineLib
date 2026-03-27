package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageFire implements IMessage<MessageFire>
{
	private int id;

	public MessageFire() {}

	public MessageFire(int id)
	{
		this.id = id;
	}

	@Override
	public void encode(MessageFire message, PacketBuffer buffer)
	{
		buffer.writeInt(message.id);
	}

	@Override
	public MessageFire decode(PacketBuffer buffer)
	{
		return new MessageFire(buffer.readInt());
	}

	@Override
	public void handle(MessageFire message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleFireMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public int getID()
	{
		return this.id;
	}
}
