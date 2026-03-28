package frc.robot.subsystems;

import com.revrobotics.servohub.ServoChannel;
import com.revrobotics.servohub.ServoHub;
import com.revrobotics.servohub.ServoChannel.ChannelId;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Value;

import static frc.robot.Constants.ShooterConstants.*;

import org.littletonrobotics.junction.Logger;


public class Shooter extends SubsystemBase{
  private SparkFlex ShooterMotorLeft;
  private SparkFlex ShooterMotorRight;

  private SimpleMotorFeedforward shooterMotorFeedForward;
  private PIDController shooterMotorPI;
  private PIDController hoodPidController;
  private DutyCycleEncoder HoodThroughBore; 


  private SparkMax HoodMotor;


  private int currentPulse;

  //PID info
  private double currentPos = 0.5;
  private double targetPos = 0.5;

  private Time lastUpdateTime = Seconds.of(0);
  

  public Shooter(){


    ShooterMotorLeft = new SparkFlex(kLeftShooterID, MotorType.kBrushless);
    ShooterMotorRight = new SparkFlex(kRightShooterID, MotorType.kBrushless);
    shooterMotorFeedForward = new SimpleMotorFeedforward(KShooterFlywheelkS, KShooterFlywheelkV);
    shooterMotorPI = new PIDController(KShooterFlywheelkP, KShooterFlywheelkI, 0);
    shooterMotorPI.setTolerance(KShooterFlywheelPITolerance);

    HoodMotor = new SparkMax(KHoodMotorID, MotorType.kBrushless);
    hoodPidController = new PIDController(KHoodMotorKp, KHoodMotorKI, KHoodMotorKD);
    HoodThroughBore = new DutyCycleEncoder(KHoodThroughBoreDIO,0,0);

  }

  /// Flywheel

  public void setShooterPower(double power){
    ShooterMotorLeft.set(power);
    ShooterMotorRight.set(power);
  }

  public void setShooterVelocity(double rpm){
    Logger.recordOutput("Flywheel/DesiredRPM", rpm);
    double motorOutput = shooterMotorFeedForward.calculate(rpm) + shooterMotorPI.calculate(getflywheelVelocity(), rpm);
    setShooterPower(motorOutput);
  }

  public void stopShooter() {
    ShooterMotorLeft.set(0.0);
    ShooterMotorRight.set(0.0);
  }

  public double getflywheelVelocity() {
    return (ShooterMotorLeft.getEncoder().getVelocity() + 
    ShooterMotorRight.getEncoder().getVelocity())/(double) 2;
  }




  /**
   * @param power
   * @return Linear speed meters per second
   */
  public double ShooterRPMtoLinearSpeed(double rpm) {
    // Convert RPM to RPS.
    double rps =  rpm / 60;; // Convert linear speed to RPS
    return rps * (2 * Math.PI * kShooterWheelRadiusMeters); // Convert RPS to linear speed
  }

  public double LinearSpeedToRPM(double linearSpeed) {
    double rps = linearSpeed / (2 * Math.PI * kShooterWheelRadiusMeters); // Convert linear speed to RPS
    double rpm = rps * 60; // Convert RPS to RPM
    return rpm * KFlyWheelRPMMultiplier; // Convert RPM to power
  }

  public void spinHoodMotor(double power){
    if (getHoodAngle() > (kHoodMinAngleDeg + kHoodTolerance)) {
      power = Math.max(0, power);
    }
    else if (getHoodAngle() < (kHoodMaxAngleDeg - kHoodTolerance)) {
      power = Math.min(0, power);
    }
    HoodMotor.set(power);
  }

  public void setHoodAngle(double angle){
    spinHoodMotor(hoodPidController.calculate(getHoodAngle(),angle));

  }
  
  public double getHoodAngle(){
    return HoodThroughBore.get() * KHoodGearRatio;
  }

  public void stopHoodMotor(){
    HoodMotor.set(0);
  }
  

  
  @Override
  public void periodic() {

    SmartDashboard.putNumber("hood angle", getHoodAngle());
    SmartDashboard.putNumber("flywheel speed",getflywheelVelocity());
    Logger.recordOutput("Flywheel/RPM", getflywheelVelocity());

    
  }


}
