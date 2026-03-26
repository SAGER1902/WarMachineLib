package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageThrottle implements IMessage<MessageThrottle>
{
	private float power;
	private float power2;
	public MessageThrottle() {}

	public MessageThrottle(float power, float power2)
	{
		this.power = power;
		this.power2 = power2;
	}

	@Override
	public void encode(MessageThrottle message, FriendlyByteBuf buffer)
	{
		buffer.writeFloat(message.power);
		buffer.writeFloat(message.power2);
	}

	@Override
	public MessageThrottle decode(FriendlyByteBuf buffer)
	{
		return new MessageThrottle(buffer.readFloat(), buffer.readFloat());
	}

	@Override
	public void handle(MessageThrottle message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleThrottleMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public float getPower()
	{
		return this.power;
	}
	public float getPower2()
	{
		return this.power2;
	}
}
