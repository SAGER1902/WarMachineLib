package wmlib.util;
import net.minecraft.world.entity.Entity;
public class TurretAngle{
	public static boolean targetRange(Entity attacker, Entity target, double height_max, double height_min, float yawbase,  float minyaw, float maxyaw, float range_min, float pitch_max, float pitch_min) {
		boolean task = false;
		if(target!=null){
			double disY = target.getY() - attacker.getY();
			double d5 = target.getX() - attacker.getX();
			double d7 = target.getZ() - attacker.getZ();
			double d3 = (double)Math.sqrt(d5 * d5 + d7 * d7);
			double d1 = attacker.getY() - (target.getY());
			float f11 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			float yaw= -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			float rotep_offset = -f11 + 10;
			if(rotep_offset <= pitch_min && rotep_offset >= pitch_max){
				if (attacker.distanceTo(target)>range_min && disY >height_min && disY <height_max){
					if(isAngleInRange(yawbase,yaw, minyaw, maxyaw)){
						task = true;
					}
				}
			}
		}
		return task;
	}
	/**
	 * 判断角度 y2 是否在以 y1 为中心，偏移区间 [x1, x2] 所确定的范围内。
	 * 区间定义为 [y1 + x1, y1 + x2]（模 360°），且假设 x1 ≤ x2。
	 *
	 * @param y1 参考角度，范围 [-180, 180]
	 * @param y2 待测角度，范围 [-180, 180]
	 * @param x1 区间左偏移量，范围 [-360, 360]（应 ≤ x2）
	 * @param x2 区间右偏移量，范围 [-360, 360]（应 ≥ x1）
	 * @return 如果 y2 在区间内返回 true，否则 false
	 */
	public static boolean isAngleInRange(double y1, double y2, double x1, double x2) {
		// 计算区间原始长度
		double length = x2 - x1;

		// 如果长度 ≥ 360°，则区间覆盖整个圆周，任何角度都在范围内
		if (length >= 360.0) {
			return true;
		}

		// 将所有角度归一化到 [0, 360) 范围
		double y1Norm = normalizeAngle(y1);
		double y2Norm = normalizeAngle(y2);

		// 计算平移后的区间端点（模 360）
		double start = normalizeAngle(y1Norm + x1);
		double end = normalizeAngle(y1Norm + x2);

		// 判断 y2Norm 是否在区间内，考虑跨越 0° 的情况
		if (start <= end) {
			// 区间不跨越 0°：直接比较
			return y2Norm >= start && y2Norm <= end;
		} else {
			// 区间跨越 0°：实际为 [start, 360) ∪ [0, end]
			return y2Norm >= start || y2Norm <= end;
		}
	}
	/**
	 * 将任意角度归一化到 [0, 360) 范围。
	 *
	 * @param angle 输入角度（度）
	 * @return 归一化后的角度
	 */
	private static double normalizeAngle(double angle) {
		angle = angle % 360.0;
		if (angle < 0) {
			angle += 360.0;
		}
		return angle;
	}
}