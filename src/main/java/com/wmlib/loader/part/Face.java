package wmlib.loader.part;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import java.util.List;
import net.minecraft.util.LazyValue;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.texture.OverlayTexture;
import java.awt.Color;
public class Face {
    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
	public static boolean isSmoothShade = true;
    public TextureCoordinate[] textureCoordinates;
    /*public static int lightmap = 15;
    public static void setLightMap(int value){
        lightmap = value;
    }
    public static void resetLightMap(){
        lightmap = 15;
    }
	public static MatrixStack matrix = null;
    public static void setMatrix(MatrixStack ms){
        matrix = ms;
    }
    public static void resetMatrix(){
        matrix = null;
    }
	private static final LazyValue<Matrix4f> defaultTransform = new LazyValue(()->{Matrix4f m = new Matrix4f(); m.setIdentity(); return m;});
	*/
    private void addFloatToList(List<Float> list, double... f) {
        for (int i = 0; i < f.length; i++) {
            list.add((float) f[i]);
        }
    }
    private void addFloatToList(List<Float> list, float... f) {
        for (int i = 0; i < f.length; i++) {
            list.add(f[i]);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public int renderByVAO(List<Float> list) {
        if (faceNormal == null) {
            faceNormal = this.calculateFaceNormal();
        }
        boolean hasTexture = (textureCoordinates != null) && (textureCoordinates.length > 0);
		
		/*Matrix4f transform;
        if(matrix != null){
            MatrixStack.Entry me = matrix.last();
            transform = me.pose();
        }else{
            transform = defaultTransform.get();
        }*/
		
        for (int i = 0; i < vertices.length; ++i) {
            if (hasTexture) {
				/*Vector4f vector4f = new Vector4f(vertices[i].x, vertices[i].y, vertices[i].z, 1.0F);
				vector4f.transform(transform);
                addFloatToList(list, 
				vector4f.x(),vector4f.y(),vector4f.z());*/
				addFloatToList(list, vertices[i].x, vertices[i].y, vertices[i].z);
                addFloatToList(list, textureCoordinates[i].u, textureCoordinates[i].v);
				//Vector3f vector3f;
                if (this.vertexNormals != null && i < this.vertexNormals.length) {
					/*Vertex normal = vertexNormals[i];
					Vector3d nol = new Vector3d(normal.x, normal.y, normal.z);
					vector3f = new Vector3f((float)nol.x, (float)nol.y, (float)nol.z);
                    addFloatToList(list, vector3f.x(), vector3f.y(), vector3f.z());*/
					addFloatToList(list, vertexNormals[i].x, vertexNormals[i].y, vertexNormals[i].z);
                } else {
                    addFloatToList(list, faceNormal.x, faceNormal.y, faceNormal.z);
                }
            } else {
                addFloatToList(list, 
				vertices[i].x, vertices[i].y,vertices[i].z);
				
                addFloatToList(list, faceNormal.x, faceNormal.y, faceNormal.z);
            }
        }
        if (hasTexture) {
            return 8;
        } else {
            return 6;
        }
    }

    public Vertex calculateFaceNormal() {
        Vector3d v1 = new Vector3d(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y,
                vertices[1].z - vertices[0].z);
        Vector3d v2 = new Vector3d(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y,
                vertices[2].z - vertices[0].z);
        Vector3d normalVector = v1.cross(v2).normalize();//crossProduct

        return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }
}