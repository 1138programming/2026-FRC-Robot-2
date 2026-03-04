package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Value;

import static frc.robot.Constants.ShooterConstants.*;

public class Shooter extends SubsystemBase{
  private SparkFlex ShooterMotor;

  private Servo hoodController;

  //PID info
  private double currentPos = 0.5;
  private double targetPos = 0.5;

  private Time lastUpdateTime = Seconds.of(0);

  public Shooter(){
    ShooterMotor = new SparkFlex(kLeftShooterID, MotorType.kBrushless);

    hoodController = new Servo(khoodControllerID);
    hoodController.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);


  }

  public void setLeftShooterPower(double power){
    ShooterMotor.set(power);
  }

  public void stopLeftShooter() {
    ShooterMotor.set(0.0);
  }


  public void setFullShooterPower(double power) {
    setLeftShooterPower(power);
  }



  public void setHoodAngle(double angle){
    final double clamped = MathUtil.clamp(angle, kHoodMinAngle, kHoodMaxAngle);
    hoodController.set(clamped);
    targetPos = clamped;
  }

  public double getHoodAngle(){
    return hoodController.getAngle();
  }

  public boolean isPosWithinTolerance() {
    return MathUtil.isNear(targetPos, currentPos, kHoodTolerance);
  }

  private void updateCurrentPosition() {
        final Time currentTime = Seconds.of(Timer.getFPGATimestamp());
        final Time elapsedTime = currentTime.minus(lastUpdateTime);
        lastUpdateTime = currentTime;

        if (isPosWithinTolerance()) {
            currentPos = targetPos;
            return;
        }

        final Distance maxDistanceTraveled = kMaxServoSpeed.times(elapsedTime);
        final double maxPercentageTraveled = maxDistanceTraveled.div(kServoLength).in(Value);
        currentPos = targetPos > currentPos
            ? Math.min(targetPos, currentPos + maxPercentageTraveled)
            : Math.max(targetPos, currentPos - maxPercentageTraveled);
    } 

  @Override
  public void periodic() {
    updateCurrentPosition();
  }


}
