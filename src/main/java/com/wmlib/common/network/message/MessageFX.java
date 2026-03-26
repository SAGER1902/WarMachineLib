package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import wmlib.util.vec.Vector3f;

import safx.SagerFX;
public class MessageFX implements IMessage2
{
	double posX;
	double posY;
	double posZ;
	
	double motionX=0.0D;
	double motionY=0.0D;
	double motionZ=0.0D;
	
	float scale = 1.0f;
	String name;
	public MessageFX() {
	}	
	public MessageFX(String name, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float size) {
		this.name=name;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.scale = size;
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeDouble(motionX);
        buf.writeDouble(motionZ);
        buf.writeDouble(motionY);
        buf.writeFloat(scale);
        buf.writeUtf(name);
	}

	@Override
	public void decode(FriendlyByteBuf buf)
	{
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.motionX = buf.readDouble();
        this.motionZ = buf.readDouble();
        this.motionY = buf.readDouble();
        this.scale = buf.readFloat();
        this.name = buf.readUtf(Short.MAX_VALUE);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			SagerFX.proxy.createFX(this.name, null, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, this.scale);
		});
		supplier.get().setPacketHandled(true);
	}
}
