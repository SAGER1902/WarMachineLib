package wmlib.common.network.message;

import wmlib.common.network.play.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.client.Minecraft;
import java.util.function.Supplier;
import wmlib.util.vec.Vector3f;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.fml.LogicalSide;
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
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeVarInt(entityId);
        buf.writeDouble(motionX);
        buf.writeDouble(motionZ);
        buf.writeDouble(motionY);
	}

	@Override
	public void decode(FriendlyByteBuf buf)
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
