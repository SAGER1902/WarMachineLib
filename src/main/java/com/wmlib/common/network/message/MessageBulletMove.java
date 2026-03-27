package wmlib.common.network.message;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.client.Minecraft;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.LogicalSide;
import wmlib.common.network.play.ClientPlayHandler;
public class MessageBulletMove implements IMessage2
{
	int entityId;
	double motionX=0.0D;
	double motionY=0.0D;
	double motionZ=0.0D;
	
	public MessageBulletMove() {
	}	
	public MessageBulletMove(int entityId, double motionX, double motionY, double motionZ) {
		this.entityId = entityId;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	@Override
	public void encode(PacketBuffer buf)
	{
		buf.writeVarInt(entityId);
        buf.writeDouble(motionX);
        buf.writeDouble(motionZ);
        buf.writeDouble(motionY);
	}

	@Override
	public void decode(PacketBuffer buf)
	{
		this.entityId = buf.readVarInt();
        this.motionX = buf.readDouble();
        this.motionZ = buf.readDouble();
        this.motionY = buf.readDouble();
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		if(supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
			supplier.get().enqueueWork(() ->
			{
				/*if(supplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)*/{
					ClientPlayHandler.handleBulletMove(this.entityId,this.motionX, this.motionY, this.motionZ);
				}
			});
			supplier.get().setPacketHandled(true);
		}
	}
}
