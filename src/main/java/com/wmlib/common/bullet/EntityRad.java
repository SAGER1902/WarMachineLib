package wmlib.common.bullet;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import wmlib.WarMachineLib;
import safx.SagerFX;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.vector.Vector3d;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import wmlib.api.IRadSoldier;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.monster.IMob;

public class EntityRad extends MobEntity implements ITool{
	public EntityRad(EntityType<? extends EntityRad> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  //this.noCulling = true;
	}
	public EntityRad(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(WarMachineLib.ENTITY_RAD, worldIn);
	}
	/*public void checkDespawn() {
	}*/
	public boolean canBeCollidedWith() {//
		return false;
	}
	public boolean isFriendRad = false;
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	public int max_range = 35;
	public int range = 25;
	
    public float iii;
	public float size;
	static boolean glow = true;
	static float shock =0;

	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int stay_time = 0;
	public int max_time = 500;
	public float summontime = 0;
	public float cooltime6 = 0;
	
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			if(team != null && team1 != team && team1 != null){
				return true;
			}else if(entity instanceof IMob && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1)){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
	}
	
	public void aiStep() {
    	/*if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
    	{
			BlockPos blockpos = new BlockPos(this.setx + 0.5,this.sety - 1,this.setz + 0.5);
			BlockState iblockstate = this.level.getBlockState(blockpos);
			if (this.setx != 0 && !iblockstate.isAir(this.level, blockpos)){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
    	}*/
		if(!glow && iii<50F){
			++iii;
		}else{
			glow = true;
		}
		if(glow && iii>0){
			--iii;
		}else{
			glow = false;
		}
		if (this.isAlive()){
			++stay_time;
			size=(max_time*1.5F-stay_time)/(float)max_time;
			range = (int)(max_range*size);
			if(stay_time>max_time)this.remove();
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY());
			int k = MathHelper.floor(this.getZ());
			if(summontime<300)++summontime;
			if(summontime>20){//
				int count = 0;
				int ve = 0;
				int i1 = i;
				int j1 = j + MathHelper.nextInt(this.random, 1, 2);
				int k1 = k;
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(range, range*0.2F, range));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					{
						if((ent instanceof LivingEntity && !this.isAggressive()||NotFriend(ent)) && !(ent instanceof IRadSoldier)){
							LivingEntity living = (LivingEntity)ent;
							if(living.getHealth()>0){
								if(this.level.random.nextInt(2)==1){
									living.hurt(DamageSource.WITHER, size*(2+0.01F*(living.getMaxHealth()-living.getHealth())));
								}else{
									living.hurt(DamageSource.IN_FIRE, 5*size);
								}
							}
						}
					}
				}
				summontime = 0;
			}
      }
      super.aiStep();
   }
}
