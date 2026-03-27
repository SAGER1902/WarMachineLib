package wmlib.common.world;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
public class WMExplosionBase {
    public static WMExplosion createExplosion(Entity entityIn, double x, double y, double z,float strength,boolean isFlaming, boolean breakBlock)
    {
        return newExplosion(entityIn, x, y, z, strength, strength, isFlaming, breakBlock, false);
    }
	
	public static WMExplosion ExplosionRad(Entity entityIn, double x, double y, double z,float strength,boolean isFlaming, boolean breakBlock)
    {
        return newExplosion(entityIn, x, y, z, strength, strength, isFlaming, breakBlock, true);
    }
	
    public static WMExplosion createExplosionDamage(Entity entityIn, double x, double y, double z, float damage, float strength,boolean isFlaming, boolean breakBlock)
    {
        return newExplosion(entityIn, x, y, z, damage, strength, isFlaming, breakBlock, false);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public static WMExplosion newExplosion(Entity entityIn, double x, double y, double z, float damage, float strength, boolean isFlaming, boolean breakBlock, boolean isRad)
    {
		WMExplosion explosion = null;
		if(entityIn!=null){
			if (!entityIn.level.isClientSide){
				explosion = new WMExplosion(entityIn.level, entityIn, x, y, z, damage, strength, isFlaming, breakBlock, isRad);
				//if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this, explosion)) return explosion;
				explosion.doExplosionA();
				explosion.doExplosionB();
			}
		}
        return explosion;
    }
}