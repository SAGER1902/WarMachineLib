package wmlib.common.network.message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import wmlib.rts.XiangJiEntity;
import wmlib.rts.F9AnXiaAnJianShiProcedure;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;
import wmlib.common.network.play.ServerPlayHandler;
public class MessageRtsCameraMove implements IMessage<MessageRtsCameraMove>{
    private int id;
	private double x;
    private int y;
    private double z;
	private int rote;
	public MessageRtsCameraMove() {}
    public MessageRtsCameraMove(int i, double x, int y, double z, int r) {
		this.id = i;
        this.x = x;
        this.y = y;
        this.z = z;
		this.rote = r;
    }

    public void encode(MessageRtsCameraMove message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
		buf.writeDouble(message.x);
        buf.writeInt(message.y);
        buf.writeDouble(message.z);
		buf.writeInt(message.rote);
    }

	@Override
    public MessageRtsCameraMove decode(FriendlyByteBuf buf) {
        return new MessageRtsCameraMove(
            buf.readInt(),
			buf.readDouble(),
            buf.readInt(),
            buf.readDouble(),
			buf.readInt()
        );
    }

	@Override
	public void handle(MessageRtsCameraMove message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null)
			{
				ServerPlayHandler.handleRtsCameraMessage(player, message);
			}
		});
		supplier.get().setPacketHandled(true);
	}
	
	
	public int getId()
	{
		return this.id;
	}
	public int getRT()
	{
		return this.rote;
	}
	public int getMY()
	{
		return this.y;
	}
	public double getMX()
	{
		return this.x;
	}
	public double getMZ()
	{
		return this.z;
	}
}