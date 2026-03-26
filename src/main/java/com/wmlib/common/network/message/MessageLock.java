package wmlib.common.network.message;
import net.minecraft.world.entity.LivingEntity;
import wmlib.common.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
//import wmlib.common.bullet.EntityMissile;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
public class MessageLock implements IMessage2
{
	int id1;
    int id2;
	
	public MessageLock() {}

	public MessageLock(int i1, int i2) {
        this.id1 = i1;
		this.id2 = i2;
    }

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(id1);
		buf.writeInt(id2);
	}

	@Override
	public void decode(FriendlyByteBuf buf)
	{
		this.id1 = buf.readInt();
		this.id2 = buf.readInt();
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		/*supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				//ServerPlayHandler.handleThrottleMessage(player, message);
				World world = player.level;
				Entity tgtEntity = world.getEntity(this.id1);
				Entity target = world.getEntity(this.id2);
				if(target != null && tgtEntity != null && tgtEntity instanceof EntityMissile) {
					EntityMissile bullet = (EntityMissile) tgtEntity;
					bullet.finalTarget = target;
				}
			}
		});
		supplier.get().setPacketHandled(true);*/
	}
}
