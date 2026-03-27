package wmlib.common.network.message;

import wmlib.common.network.play.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
/**
 * Author: MrCrayfish
 */
public class MessageHit implements IMessage<MessageHit>
{
    private int entityId;
	private int animId;
    private int hit_name;
    private float countId;
    public MessageHit() {}

    public MessageHit(int entityId, int animId, int hit_name, float countId)
    {
        this.entityId = entityId;
		this.animId= animId;
        this.hit_name = hit_name;
		this.countId = countId;
    }

    @Override
    public void encode(MessageHit message, PacketBuffer buffer)
    {
        buffer.writeVarInt(message.entityId);
		buffer.writeVarInt(message.animId);
        buffer.writeVarInt(message.hit_name);
		buffer.writeFloat(message.countId);
    }

    @Override
    public MessageHit decode(PacketBuffer buffer)
    {
        return new MessageHit(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readFloat());
    }

    @Override
    public void handle(MessageHit message, Supplier<NetworkEvent.Context> supplier)
    {
        if(supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            IMessage.enqueueTask(supplier, () -> ClientPlayHandler.handleHit(message));
        }
    }

    public int getAnimId()
    {
        return this.animId;
    }
    public int getEntityId()
    {
        return this.entityId;
    }

    public int getNameId()
    {
        return this.hit_name;
    }
    public float getCountId()
    {
        return this.countId;
    }
}
