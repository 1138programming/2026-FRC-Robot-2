package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.ShooterConstants.*;

public class DuoShooter extends SubsystemBase{
  private SparkFlex leftShooterMotor;
  private SparkFlex rightShooterMotor;

  private Servo hoodController;

  public DuoShooter(){
    leftShooterMotor = new SparkFlex(kLeftShooterID, MotorType.kBrushless);
    rightShooterMotor = new SparkFlex(kRightShooterID, MotorType.kBrushless);

    hoodController = new Servo(khoodControllerID);

  }

  public void setLeftShooterPower(double power){
    leftShooterMotor.set(power);
  }

  public void stopLeftShooter() {
    leftShooterMotor.set(0.0);
  }

  public void stopRightShooter() {
    leftShooterMotor.set(0.0);
  }

  public void setRightShooterPower(double power){
    rightShooterMotor.set(power);
  }

  public void setFullShooterPower(double power) {
    setLeftShooterPower(power);
    setRightShooterPower(power);
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
