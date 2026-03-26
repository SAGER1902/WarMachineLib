package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
	public void encode(MessageFire message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.id);
	}

	@Override
	public MessageFire decode(FriendlyByteBuf buffer)
	{
		return new MessageFire(buffer.readInt());
	}

	@Override
	public void handle(MessageFire message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
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
