package wmlib.common.network.message;

import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
import wmlib.util.Vector3f;
import wmlib.client.render.InstantBulletRenderer;
public class MessageTrail implements IMessage2
{
    double posX;
    double posY;
    double posZ;
	boolean isConnect;
	int id;
    double motionX;
    double motionZ;

    double dirX;
    double dirY;
    double dirZ;
    double range;
    float bulletspeed;
	private String modelname;
	
	public MessageTrail() {}

	public MessageTrail(boolean connect, int i,String name,double X, double Y, double Z, double motionX, double motionZ, double x, double y, double z, double range, float bulletspeed) {
        this.posX = X;
        this.posY = Y;
        this.posZ = Z;
		this.id = i;
		this.isConnect = connect;
        this.motionX = motionX;
        this.motionZ = motionZ;
        this.dirX = x;
        this.dirY = y;
        this.dirZ = z;
        this.range = range;
        this.bulletspeed = bulletspeed;
        this.modelname=name;
    }

	@Override
	public void encode(PacketBuffer buf)
	{
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
		buf.writeInt(id);
		buf.writeBoolean(isConnect);
        buf.writeDouble(motionX);
        buf.writeDouble(motionZ);
        buf.writeDouble(dirX);
        buf.writeDouble(dirY);
        buf.writeDouble(dirZ);
        buf.writeDouble(range);
        buf.writeFloat(bulletspeed);
        buf.writeUtf(modelname);
	}

	@Override
	public void decode(PacketBuffer buf)
	{
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
		this.id = buf.readInt();
		this.isConnect = buf.readBoolean();
        this.motionX = buf.readDouble();
        this.motionZ = buf.readDouble();
        this.dirX = buf.readDouble();
        this.dirY = buf.readDouble();
        this.dirZ = buf.readDouble();
        this.range = buf.readDouble();
        this.bulletspeed = buf.readFloat();
        this.modelname = buf.readUtf(Short.MAX_VALUE);
		/*return new MessageTrail(buf.readBoolean(), buf.readInt(), buf.readUtf(Short.MAX_VALUE), 
		buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(), buf.readFloat());*/
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			/*ServerPlayerEntity player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleThrottleMessage(player, message);
			}*/
			//System.out.println("");
			double dx = this.dirX * this.range;
			double dy = this.dirY * this.range;
			double dz = this.dirZ * this.range;
			int time = (int)this.range;
			final Vector3f vec = new Vector3f((float) this.posX, (float) this.posY, (float) this.posZ);
			Vector3f vec1 = new Vector3f((float) (vec.x + dx + this.motionX), (float) (vec.y + dy), (float) (vec.z + dz + this.motionZ));
			if(this.id>1 && this.isConnect){
				vec1 = new Vector3f((float) this.dirX, (float) this.dirY, (float) this.dirZ);
			}
			InstantBulletRenderer.AddTrail(new InstantBulletRenderer.InstantShotTrail(vec, vec1, this.bulletspeed, this.modelname, this.id, time));
		});
		supplier.get().setPacketHandled(true);
	}
}
