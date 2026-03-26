package wmlib.common.network.message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;
import java.util.List;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.LivingEntity;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IArmy;
import wmlib.common.network.play.ServerPlayHandler;
public class MessageRtsClick implements IMessage<MessageRtsClick>{
    private int x;
	private int y;
	private int z;
    private int entityId;
	private int clickType;
	public MessageRtsClick() {}
    public MessageRtsClick(int t, int x1,int y1,int z1, int entityId) {
		this.clickType = t;
		this.x=x1;
		this.y=y1;
		this.z=z1;
        this.entityId = entityId;
    }
	@Override
    public void encode(MessageRtsClick message, FriendlyByteBuf buf) {
		buf.writeInt(message.clickType);
		buf.writeInt(message.x);
		buf.writeInt(message.y);
		buf.writeInt(message.z);
        buf.writeInt(message.entityId);
    }
	@Override
	public MessageRtsClick decode(FriendlyByteBuf buffer)
	{
		return new MessageRtsClick(buffer.readInt(), buffer.readInt(),buffer.readInt(),buffer.readInt(),buffer.readInt());
	}
	
	@Override
	public void handle(MessageRtsClick message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleRtsClickMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public boolean forceAttack()
	{
		return this.clickType==1;
	}
	public boolean isLink()
	{
		return this.clickType==3;
	}
	
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public int getZ()
	{
		return this.z;
	}
	public int getId()
	{
		return this.entityId;
	}
}