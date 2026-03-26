package wmlib.loader.part;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.util.function.BiFunction;
import java.awt.Color;
import net.minecraft.util.LazyLoadedValue;

import java.util.Map;
import java.util.HashMap;
public class Face {
    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;
	public static boolean isSmoothShade = true;
	
	public static boolean forceQuad = false;
	
    public static int lightmap = 15;
    public static void setLightMap(int value){
        lightmap = value;
    }
    public static void resetLightMap(){
        lightmap = 15;
    }

    public static void setMatrix(PoseStack ms){
        matrix = ms;
    }
    public static void resetMatrix(){
        matrix = null;
    }
	
    private static final LazyLoadedValue<Matrix4f> defaultTransform = new LazyLoadedValue(()->{Matrix4f m = new Matrix4f(); m.identity(); return m;});

	public static PoseStack matrix = null;
    public static final Vector4f uvDefaultOperator = new Vector4f(1,1,0,0);
    public static Vector4f uvOperator = uvDefaultOperator;
	
    public static void setUvOperator(float uScale, float vScale, float uOffset, float vOffset) {
        Face.uvOperator = new Vector4f(uScale, vScale, uOffset, vOffset);
    }
    public static void resetUvOperator(){
        Face.uvOperator = uvDefaultOperator;
    }
	
	public static float movex = 0;
	public static float movey = 0;
	
    @OnlyIn(Dist.CLIENT)
	public void renderMC(int glMode, VertexConsumer tessellator, float red, float green, float blue, float alpha) {
		float textureOffset = 0.0005F;
        if (faceNormal == null)
        {
            faceNormal = this.calculateFaceNormal();
        }
        float averageU = 0;
        float averageV = 0;
        if ((textureCoordinates != null) && (textureCoordinates.length > 0))
        {
            for (int i = 0; i < textureCoordinates.length; ++i)
            {
                averageU += textureCoordinates[i].u * uvOperator.x() + uvOperator.z();
                averageV += textureCoordinates[i].v * uvOperator.y() + uvOperator.w();
            }
            averageU = averageU / textureCoordinates.length;
            averageV = averageV / textureCoordinates.length;
        }
        float offsetU, offsetV;
        VertexConsumer wr = tessellator;
        Matrix4f transform;
        if(matrix != null){
            PoseStack.Pose me = matrix.last();
            transform = me.pose();
        }else{
            transform = defaultTransform.get();
        }
		
		/*if(forceQuad){
            putVertex(wr,0,transform,textureOffset,averageU,averageV, red, green, blue, alpha);
        }*/
        for (int i = 0; i < vertices.length; ++i)
        {
			putVertex(wr,i,transform,textureOffset,averageU,averageV, red, green, blue, alpha);
        }
    }

    void putVertex(VertexConsumer wr, int i, Matrix4f transform, float textureOffset, float averageU, float averageV ,float red, float green, float blue, float alpha){
        float offsetU, offsetV;
        wr.vertex(transform, vertices[i].x, vertices[i].y, vertices[i].z);
        wr.color(red, green, blue, alpha);
        if ((textureCoordinates != null) && (textureCoordinates.length > 0))
        {
            offsetU = textureOffset;
            offsetV = textureOffset;
            float textureU = textureCoordinates[i].u * uvOperator.x() + uvOperator.z()+this.movex;
            float textureV = textureCoordinates[i].v * uvOperator.y() + uvOperator.w()+this.movey;
            if (textureU > averageU)
            {
                offsetU = -offsetU;
            }
            if (textureV > averageV)
            {
                offsetV = -offsetV;
            }
            wr.uv(textureU + offsetU, textureV + offsetV);
        }else{
            wr.uv(0, 0);
        }
        wr.overlayCoords(OverlayTexture.NO_OVERLAY);
        wr.uv2(lightmap);
        Vector3f vector3f;
        if(isSmoothShade && vertexNormals != null) {
            Vertex normal = vertexNormals[i];
            Vec3 nol = new Vec3(normal.x, normal.y, normal.z);
            vector3f = new Vector3f((float)nol.x, (float)nol.y, (float)nol.z);
        }else{
            vector3f = new Vector3f(faceNormal.x, faceNormal.y, faceNormal.z);
        }
        vector3f.mul(new Matrix3f(transform));;
        vector3f.normalize();
        wr.normal(vector3f.x(), vector3f.y(), vector3f.z());
        wr.endVertex();
    }

    public Vertex calculateFaceNormal()
    {
        Vec3 v1 = new Vec3(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y, vertices[1].z - vertices[0].z);
        Vec3 v2 = new Vec3(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y, vertices[2].z - vertices[0].z);
        Vec3 normalVector = null;
        normalVector = v1.cross(v2).normalize();
        return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }

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
        for (int i = 0; i < vertices.length; ++i) {
            if (hasTexture) {
                addFloatToList(list, 
				vertices[i].x, vertices[i].y,vertices[i].z);
                addFloatToList(list, textureCoordinates[i].u+this.movex, textureCoordinates[i].v+this.movey);
                if (this.vertexNormals != null && i < this.vertexNormals.length) {
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
}