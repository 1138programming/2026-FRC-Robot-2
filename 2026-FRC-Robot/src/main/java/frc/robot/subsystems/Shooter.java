package frc.robot.subsystems;

import com.revrobotics.servohub.ServoChannel;
import com.revrobotics.servohub.ServoHub;
import com.revrobotics.servohub.ServoChannel.ChannelId;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Value;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Constants.TurretConstants.KhoodMinAngle;


public class Shooter extends SubsystemBase{
  private SparkFlex ShooterMotor;
  private ServoHub servoHub;
  private ServoChannel hoodServo;
  private SimpleMotorFeedforward shooterMotorFeedForward;

  private int currentPulse;

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
    currentPulse = hoodServo.getPulseWidth();
    shooterMotorFeedForward = new SimpleMotorFeedforward(shooterkS, shooterkV);

  }

  public void setShooterPower(double power){
    ShooterMotor.set(power);
  }

  public void setShooterVelocity(double rpm){
    double motorOutput = shooterMotorFeedForward.calculate(rpm);
    setShooterPower(-motorOutput);
  }

  public void stopShooter() {
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




  // servo
  public int servoAngleToPulseWidth(double angle){
    if (angle > 270 || angle < 0 ) return 1750;
    return (int) (angle / 270.0) * (kpulseWidthMax - kpulseWidthMin) + kpulseWidthMin;
  }

  public double shooterAngleToServoAngle(double angle) {
    return angle *8;
  }

  public double ServoAngleToShooterAngle(double angle) {
    return angle / 8;
  }

  public double pulseWidthToAngleServo(int pulseWidth){
    return ((pulseWidth - kpulseWidthMin) ); /// (kpulseWidthMax - kpulseWidthMin)) * kHoodServoTravelDeg;
  }

  // public void setHoodAngle(double angle){
  //   final double clamped = MathUtil.clamp(angle, kHoodMinAngle, kHoodMaxAngle);
  //   targetPos = clamped;
  //   hoodServo.setPulseWidth(servoAngleToPulseWidth(angle));
  // }

  public void setcurrentpulse(int pulse) {
    currentPulse = pulse;
  }

  public int getcurrentpulse() {
    return currentPulse;
  }

  public void setHoodPulse(int pulse){
    // final int clamped = MathUtil.clamp(pulse, kpulseWidthMin, kpulseWidthMax);
    // targetPos = clamped;
    hoodServo.setPulseWidth(pulse);
  }

  
  public double getHoodAngle(){
    return pulseWidthToAngleServo(hoodServo.getPulseWidth());
  }


  // public boolean isPosWithinTolerance() {
  //   return MathUtil.isNear(targetPos, currentPos, kHoodTolerance);
  // }

  // private void updateCurrentPosition() {
  //       final Time currentTime = Seconds.of(Timer.getFPGATimestamp());
  //       final Time elapsedTime = currentTime.minus(lastUpdateTime);
  //       lastUpdateTime = currentTime;

  //       if (isPosWithinTolerance()) {
  //           currentPos = targetPos;
  //           return;
  //       }

  //       final Distance maxDistanceTraveled = kMaxServoSpeed.times(elapsedTime);
  //       final double maxPercentageTraveled = maxDistanceTraveled.div(kServoLength).in(Value);
  //       currentPos = targetPos > currentPos
  //           ? Math.min(targetPos, currentPos + maxPercentageTraveled)
  //           : Math.max(targetPos, currentPos - maxPercentageTraveled);
  // } 

  // public void incrementHoodAngle(double amount){ 
  //   setHoodAngle(hoodServo.getPulseWidth());
  // }

  public void incrementHoodPulse(int amount){ 
    currentPulse = hoodServo.getPulseWidth() + amount;
  }

  @Override
  public void periodic() {
    // updateCurrentPosition();
    SmartDashboard.putNumber("servo pulse", hoodServo.getPulseWidth());
    SmartDashboard.putNumber("hood angle", getHoodAngle());
    SmartDashboard.putNumber("flywheel speed",ShooterMotor.getEncoder().getVelocity());
    
    setHoodPulse(currentPulse);
  }


}
