package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCommander implements IMessage<MessageCommander>
{
	private int id;

	public MessageCommander() {}

	public MessageCommander(int id)
	{
		this.id = id;
	}

	@Override
	public void encode(MessageCommander message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.id);
	}

	@Override
	public MessageCommander decode(FriendlyByteBuf buffer)
	{
		return new MessageCommander(buffer.readInt());
	}

	@Override
	public void handle(MessageCommander message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleCommanderMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public int getID()
	{
		return this.id;
	}
}
