package wmlib.client.obj;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.matrix.MatrixStack;
import wmlib.loader.ObjModel;
import wmlib.loader.ObjModelBuilder;
import wmlib.loader.api.ObjModelLoader;
import wmlib.loader.api.model.ObjModelRenderer;
import net.minecraft.util.ResourceLocation;
import java.util.HashMap;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import wmlib.loader.part.Face;
public class SAObjModel
{
	private HashMap<String, ObjModel> modelCache=new HashMap<String, ObjModel>();
	private String modelname;
	public ObjModel model;
	/*private IVertexBuilder buff;
	private int LightIn=-1;
	MatrixStack matrix;*/
	//boolean firstRender = false;
    public SAObjModel(String modelname) {
        this.modelname = modelname;
		/*if(!firstRender){
			firstRender=false;
			ObjModel obj=modelCache.get(this.modelname);
			if(obj==null) {
				modelCache.put(this.modelname, ObjModelLoader.load(new ResourceLocation(this.modelname)));
				obj=modelCache.get(this.modelname);
			}
			obj.renderAll(0);
		}*/
    }
	/*public void setUp(IVertexBuilder buffer, MatrixStack stack, int packedLightIn){
		this.buff=buffer;
		this.LightIn=packedLightIn;
		this.matrix=stack;
	}*/
	
	/*public void setStack(MatrixStack stack){
		this.matrix=stack;
	}*/
	
    public void renderAll() {
        //if(this.buff!=null && this.LightIn!=-1)render_all(buff, LightIn);
		render_all();
    }
    public void renderPart(String part) {
        //if(this.modelname!=null && this.buff!=null && this.LightIn!=-1)render_part(part, buff, LightIn);
		render_part(part);
    }
	
    private void render_part(String name){
		ObjModel obj=modelCache.get(this.modelname);
		if(obj==null) {
			modelCache.put(this.modelname, ObjModelLoader.load(new ResourceLocation(this.modelname)));
			obj=modelCache.get(this.modelname);
		}
		//if(matrix!=null)Face.setMatrix(matrix);
		if(obj.getPart(name) != null)obj.renderPart(0,name);
		//if(matrix!=null)Face.resetMatrix();
    }
    private void render_all(){
		ObjModel obj=modelCache.get(this.modelname);
		if(obj==null) {
			modelCache.put(this.modelname, ObjModelLoader.load(new ResourceLocation(this.modelname)));
			obj=modelCache.get(this.modelname);
		}
		//if(matrix!=null)Face.setMatrix(matrix);
		obj.renderAll(0);
		//if(matrix!=null)Face.resetMatrix();
    }
}