package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageChoose implements IMessage<MessageChoose>
{
    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;
	public MessageChoose() {}

	public MessageChoose(double x, double y, double z, double ex, double ey, double ez)
	{
        this.x1 = x;
        this.y1 = y;
        this.z1 = z;
        this.x2 = ex;
        this.y2 = ey;
        this.z2 = ez;
	}

	@Override
	public void encode(MessageChoose message, PacketBuffer buf)
	{
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
	}

	@Override
	public MessageChoose decode(PacketBuffer buffer)
	{
		return new MessageChoose(buffer.readDouble(),buffer.readDouble(),buffer.readDouble(),buffer.readDouble(),buffer.readDouble(),buffer.readDouble());
	}

	@Override
	public void handle(MessageChoose message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleChooseMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public double getX1(){
		return this.x1;
	}
	public double getY1(){
		return this.y1;
	}
	public double getZ1(){
		return this.z1;
	}	
	public double getX2(){
		return this.x2;
	}	
	public double getY2(){
		return this.y2;
	}	
	public double getZ2(){
		return this.z2;
	}
}
