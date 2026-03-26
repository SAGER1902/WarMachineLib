package wmlib.api;
import net.minecraft.world.entity.LivingEntity;
public interface IHealthBar
{
	public LivingEntity getBarOwner();
	public boolean isShow();
	public int getBarType();
}