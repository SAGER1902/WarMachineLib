package wmlib.api;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
public interface IArmy
{
	public ResourceLocation getIcon1();
	public ResourceLocation getIcon2();
	public void setSelect(boolean stack);
	public void setMove(int id, int x,int y,int z);
	public void setAttack(LivingEntity ent);
	public boolean getSelect();
	public boolean isDrive();
	//public boolean isCommander(LivingEntity owner);
	public LivingEntity getArmyOwner();
	public int getTeamCount();
	public void setTeamCount(int id);
	public int getArmyMoveT();
	public int getArmyMoveX();
	public int getArmyMoveY();
	public int getArmyMoveZ();
	public default int getUnitType(){//air = 1
		return 0;
	}
	public default void stopUnitPassenger(){
	}
}