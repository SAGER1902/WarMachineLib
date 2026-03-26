package wmlib.client.obj;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.VertexConsumer;
import wmlib.loader.ObjModel;
import wmlib.loader.ObjModelBuilder;
import wmlib.loader.api.ObjModelLoader;
import wmlib.loader.api.model.ObjModelRenderer;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import wmlib.loader.part.Face;
import java.awt.Color;
public class SAObjModel
{
	private HashMap<String, ObjModel> modelCache=new HashMap<String, ObjModel>();
	private String modelname;
	public ObjModel model;
	private VertexConsumer buff;
	private RenderType rtype;
	
	private PoseStack matrix;
	private int LightIn=-1;
	
	boolean useMC = false;
	private static float movex =0;
	private static float movey =0;
    public SAObjModel(String modelname) {
        this.modelname = modelname;
    }
	public void setRender(RenderType type, VertexConsumer buffer, PoseStack stack,int packedLight){
		if(type!=null)this.rtype=type;
		if(buffer!=null)this.buff=buffer;
		if(stack!=null)this.matrix=stack;
		this.LightIn=packedLight;
	}
	
	public void setRenderLight(int packedLight){
		this.LightIn=packedLight;
	}
	
	public void setRenderType(RenderType type){
		if(type!=null)this.rtype=type;
	}
	
	public void setUseMC(boolean use){
		useMC=use;
	}

	float r=1F;
	float g=1F;
	float b=1F;
	float a=1F;
	public void setColor(float red, float green, float blue, float alpha){
		r=red;
		g=green;
		b=blue;
		if(alpha<0)alpha=0;
		a=alpha;
	}
	
	public void setMoveTex(float x, float y){
		this.movex=x;
		this.movey=y;
	}
	
    public void renderAll() {
        if(this.buff!=null && this.matrix!=null && this.LightIn!=-1||this.rtype!=null)render_all(buff, matrix, LightIn);
    }
    public void renderPart(String part) {
        if(this.modelname!=null && this.buff!=null && this.matrix!=null && this.LightIn!=-1 ||this.rtype!=null)render_part(part, buff, matrix, LightIn);
    }
	
    private void render_part(String name,VertexConsumer buffer, PoseStack stack,int packedLight){
		ObjModel obj=modelCache.get(this.modelname);
		if(obj==null) {
			modelCache.put(this.modelname, ObjModelLoader.load(ResourceLocation.tryParse(this.modelname)));
			obj=modelCache.get(this.modelname);
		}
		if(obj.getPart(name) != null){
			if(rtype==null && buffer!=null){
				Face.movex=this.movex;
				Face.movey=this.movey;
				Face.setLightMap(packedLight);
				Face.setMatrix(stack);
			}
			obj.renderPart(rtype, name, buffer, stack, packedLight, r, g, b, a);
			if(rtype==null && buffer!=null){
				Face.resetMatrix();
				Face.resetLightMap();
			}
		}
    }
    private void render_all(VertexConsumer buffer, PoseStack stack,int packedLight){
		ObjModel obj=modelCache.get(this.modelname);
		if(obj==null) {
			modelCache.put(this.modelname, ObjModelLoader.load(ResourceLocation.tryParse(this.modelname)));
			obj=modelCache.get(this.modelname);
		}
		if(rtype==null && buffer!=null){
			Face.movex=this.movex;
			Face.movey=this.movey;
			Face.setLightMap(packedLight);
			Face.setMatrix(stack);
		}
		obj.renderAll(rtype, buffer, stack, packedLight, r, g, b, a);
		if(rtype==null && buffer!=null){
			Face.resetMatrix();
			Face.resetLightMap();
		}
    }
}