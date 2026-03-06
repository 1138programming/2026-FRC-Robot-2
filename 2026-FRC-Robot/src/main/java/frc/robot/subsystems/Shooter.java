package frc.robot.subsystems;

import com.revrobotics.servohub.ServoChannel;
import com.revrobotics.servohub.ServoHub;
import com.revrobotics.servohub.ServoChannel.ChannelId;
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
import static frc.robot.Constants.TurretConstants.KhoodMinAngle;


public class Shooter extends SubsystemBase{
  private SparkFlex ShooterMotor;
  private ServoHub servoHub;
  private ServoChannel hoodServo;



  //PID info
  private double currentPos = 0.5;
  private double targetPos = 0.5;

  private Time lastUpdateTime = Seconds.of(0);

  public Shooter(){


    ShooterMotor = new SparkFlex(kShooterID, MotorType.kBrushless);
    servoHub = new ServoHub(kServoHubID);
    hoodServo = servoHub.getServoChannel(ChannelId.kChannelId0);
    hoodServo.setEnabled(true);
    hoodServo.setPowered(true);


    hoodServo.setEnabled(true);
    hoodServo.setPowered(true);
  }

  public void setLeftShooterPower(double power){
    ShooterMotor.set(power);
  }

  public void stopLeftShooter() {
    ShooterMotor.set(0.0);
  }

  /**
   * @param power
   * @return Linear speed meters per second
   */
  public double ShooterPowertoLinearSpeed(double power) {
    double speed = (power * 6784) / 60; // Convert power to RPM, then to RPS. 6784 is the free speed of the motor at 12V
    return speed * (2 * Math.PI * kShooterWheelRadiusMeters); // Convert RPS to linear speed
  }

  public double LinearSpeedToShooterPower(double linearSpeed) {
    double rps = linearSpeed / (2 * Math.PI * kShooterWheelRadiusMeters); // Convert linear speed to RPS
    double rpm = rps * 60; // Convert RPS to RPM
    return rpm / 6784; // Convert RPM to power
  }


  public void setFullShooterPower(double power) {
    setLeftShooterPower(power);
  }

  // servo
  public int angleToPulseWidth(double angle){
    if (angle > 270 || angle < 0 ) return 1750;
    return (int) (angle / 270.0) * (kpulseWidthMax - kpulseWidthMin) + kpulseWidthMin;
  }


  public double pulseWidthToAngle(int pulseWidth){
    return ((pulseWidth - kpulseWidthMin) / (kpulseWidthMax - kpulseWidthMin)) * 270;
  }

  public void setPulseWidthModulation(int pWidthModulation){
    hoodServo.setPulseWidth(pWidthModulation);
  }

  public void setHoodAngle(double angle){
    final double clamped = MathUtil.clamp(angle, kHoodMinAngle, kHoodMaxAngle);
    targetPos = clamped;
    hoodServo.setPulseWidth(angleToPulseWidth(clamped));
  }

  
  public double getHoodAngle(){
    return angleToPulseWidth(hoodServo.getPulseWidth());
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

  public void incrementHoodAngle(double amount){ 
    setHoodAngle(getHoodAngle() + amount);
  }

  @Override
  public void periodic() {
    updateCurrentPosition();
  }


}
