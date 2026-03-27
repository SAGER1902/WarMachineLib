package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCame implements IMessage<MessageCame>
{
	private int power;
	public MessageCame() {}

	public MessageCame(int power)
	{
		this.power = power;
	}

	@Override
	public void encode(MessageCame message, PacketBuffer buffer)
	{
		buffer.writeInt(message.power);
	}

	@Override
	public MessageCame decode(PacketBuffer buffer)
	{
		return new MessageCame(buffer.readInt());
	}

	@Override
	public void handle(MessageCame message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleCameMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public int getPower()
	{
		return this.power;
	}
}
