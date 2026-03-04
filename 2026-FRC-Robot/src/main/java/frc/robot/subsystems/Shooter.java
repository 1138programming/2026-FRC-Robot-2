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


  public void setFullShooterPower(double power) {
    setLeftShooterPower(power);
  }



  public void setHoodAngle(double angle){
    hoodController.set(Math.max(kHoodMaxAngle, Math.min(kHoodMinAngle, angle)));
  }

  public double getHoodAngle(){
    return hoodController.getAngle();
  }

  public void incrementHoodAngle(int amount){ 
    setHoodAngle(getHoodAngle() + amount);
  }

  @Override
  public void periodic() {

  }


}
