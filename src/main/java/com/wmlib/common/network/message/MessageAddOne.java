package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.Entity;
import java.util.function.Supplier;
import wmlib.api.IArmy;
public class MessageAddOne implements IMessage<MessageAddOne>
{
    private int id;
	public MessageAddOne() {}

	public MessageAddOne(int i)
	{
        this.id = i;
	}

	@Override
	public void encode(MessageAddOne message, FriendlyByteBuf buf)
	{
        buf.writeVarInt(message.id);
	}

	@Override
	public MessageAddOne decode(FriendlyByteBuf buffer)
	{
		return new MessageAddOne(buffer.readVarInt());
	}

	@Override
	public void handle(MessageAddOne message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleAddMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}
	public int getEntID(){
		return this.id;
	}
}
