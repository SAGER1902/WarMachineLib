package wmlib.common.network;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;

import java.util.function.IntSupplier;

/**
 * Author: MrCrayfish
 */
public class HandshakeMessages
{
    static class LoginIndexedMessage implements IntSupplier
    {
        private int loginIndex;

        void setLoginIndex(final int loginIndex)
        {
            this.loginIndex = loginIndex;
        }

        int getLoginIndex()
        {
            return loginIndex;
        }

        @Override
        public int getAsInt()
        {
            return getLoginIndex();
        }
    }

    static class C2SAcknowledge extends LoginIndexedMessage
    {
        void encode(FriendlyByteBuf buf) {}

        static C2SAcknowledge decode(FriendlyByteBuf buf)
        {
            return new C2SAcknowledge();
        }
    }

    public static class S2CVehicleProperties extends LoginIndexedMessage
    {
    }
}