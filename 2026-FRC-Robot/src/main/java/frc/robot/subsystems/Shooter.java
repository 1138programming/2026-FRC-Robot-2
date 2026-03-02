package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.ShooterConstants.*;

public class Shooter extends SubsystemBase{
  private SparkFlex ShooterMotor;

  private Servo hoodController;

  public Shooter(){
    ShooterMotor = new SparkFlex(kLeftShooterID, MotorType.kBrushless);

    hoodController = new Servo(khoodControllerID);

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



  public void setHoodAngle(double angle){
    hoodController.set(Math.max(kHoodMaxAngle, Math.min(kHoodMinAngle, angle)));
  }

  public double getHoodAngle(){
    return hoodController.getAngle();
  }

  @Override
  public void periodic() {

  }


}
