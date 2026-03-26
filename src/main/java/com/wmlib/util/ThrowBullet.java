package wmlib.util;
public class ThrowBullet{//from SuperbWarfare
    public static boolean canReachTarget(double initialVelocity, double gravity, double velocityDecay, int startX,int startY,int startZ,int  targetX,int targetY,int targetZ, double[] angles, boolean canStraight) {
        double distanceXZ = Math.sqrt(Math.pow(targetX - startX, 2) + Math.pow(targetZ - startZ, 2));
        double theta = calculateLaunchAngle(initialVelocity, gravity, velocityDecay, distanceXZ, targetY - startY, canStraight);
        if (theta > 90) {
            return false;
        }
        //angles[0] = Math.atan2(targetZ, targetX);
        angles[1] = theta;
        return true;
    }
    public static double calculateLaunchAngle(double initialVelocity, double gravity, double velocityDecay, double distanceXZ, double targetY, boolean canStraight) {
        double left = 20;
        double right = 30;
        double tolerance = 0.5;
		if(canStraight)left = -90;
        while (right - left > tolerance) {
            double mid = (left + right) / 2;
            double radian = Math.toRadians(mid);
            double range = calculateRangeWithDeltaY(initialVelocity, radian, gravity, velocityDecay, targetY);
            if (Math.abs(range - distanceXZ) < tolerance * 8) {
                return mid;
            } else if (range < distanceXZ) {
                left = mid;
            } else {
                right = mid;
            }
        }
        left = 30;
        right = 90;
        while (right - left > tolerance) {
            double mid = (left + right) / 2;
            double radian = Math.toRadians(mid);
            double range = calculateRangeWithDeltaY(initialVelocity, radian, gravity, velocityDecay, targetY);
            if (Math.abs(range - distanceXZ) < tolerance * 8) {
                return mid;
            } else if (range < distanceXZ) {
                right = mid;
            } else {
                left = mid;
            }
        }
        return -1;
    }
    public static double calculateRangeWithDeltaY(double initialVelocity, double theta, double gravity, double velocityDecay, double deltaY) {
        double vx = initialVelocity * Math.cos(theta);
        double vy = initialVelocity * Math.sin(theta);
        double range = 0.0;
        double y = 1.0;
        double commonRange = calculateRange(initialVelocity, theta, gravity, velocityDecay);
        while (range < commonRange / 2 || (range >= commonRange / 2 && y >= deltaY)) {
            range += vx;
            y += vy;
            vx *= velocityDecay;
            vy = vy * velocityDecay - gravity;
            if (range >= commonRange / 2 && y < deltaY) {
                break;
            }
        }
        return range;
    }
    public static double calculateRange(double initialVelocity, double theta, double gravity, double velocityDecay) {
        double vx = initialVelocity * Math.cos(theta);
        double vy = initialVelocity * Math.sin(theta);
        double x = 0.0;
        double y = 1.0; 
        while (y >= 0) {
            x += vx;
            y += vy;
            vx *= velocityDecay;
            vy = vy * velocityDecay - gravity;
            if (y < 0) {
                break;
            }
        }
        return x;
    }
}