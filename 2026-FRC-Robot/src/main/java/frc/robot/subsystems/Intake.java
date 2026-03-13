package frc.robot.subsystems;


import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Second;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import static frc.robot.Constants.intakeConstants.*;


public class Intake extends SubsystemBase{
    private SparkFlex intakeMotor;
    private TalonFX intakeDeployMotor;
    private DutyCycleEncoder  deployencoder;
    private PIDController IntakeControler; 

    private static final AngularVelocity maxPivotSpeed = RPM.of(6000).div(kDeployReduction);

    private final VoltageOut deployVoltageRequest = new VoltageOut(0);
    private final MotionMagicVoltage deployMMRequest = new MotionMagicVoltage(0).withSlot(0);
    private final VoltageOut intakeVoltageREquest = new VoltageOut(0);

    private boolean isDeployed = false;

  public Intake() {
      intakeMotor = new SparkFlex(KintakeMotorId,MotorType.kBrushless);
      intakeDeployMotor = new TalonFX(KintakeDeployMotorId);
      deployencoder = new DutyCycleEncoder(KintakeThroughBoreDio,360,315);
      IntakeControler = new PIDController(KintakePIDKp, KintakePIDKi, KintakePIDKd);
      IntakeControler.disableContinuousInput();
      configureDeployMotor();
   // configureIntakeMotor();
  }

  private void configureDeployMotor() {
    final TalonFXConfiguration config = new TalonFXConfiguration()
      .withMotorOutput(
        new MotorOutputConfigs()
          .withInverted(InvertedValue.Clockwise_Positive)
          .withNeutralMode(NeutralModeValue.Brake)
      )
      .withFeedback(
        new FeedbackConfigs()
          .withFeedbackSensorSource(FeedbackSensorSourceValue.RotorSensor)
          .withSensorToMechanismRatio(50) //TODO put in constants maybe
      )
      .withMotionMagic(
        new MotionMagicConfigs()
          .withMotionMagicCruiseVelocity(maxPivotSpeed)
          .withMotionMagicAcceleration(maxPivotSpeed.per(Second))
      )
      .withSlot0(
        new Slot0Configs()
          .withKP(300)
          .withKI(0)
          .withKD(0)
      );
     
      CurrentLimitsConfigs limitConfigs = new CurrentLimitsConfigs();

      limitConfigs.StatorCurrentLimit = 40;
      limitConfigs.StatorCurrentLimitEnable = true;

      limitConfigs.SupplyCurrentLimit = 20;
      limitConfigs.SupplyCurrentLimitEnable = true;
  
      intakeDeployMotor.getConfigurator().apply(config);
      intakeDeployMotor.getConfigurator().apply(limitConfigs);
  }

  private void configureIntakeMotor() {
    // final TalonFXConfiguration config = new TalonFXConfiguration()
    //   .withMotorOutput(
    //     new MotorOutputConfigs()
    //       .withInverted(InvertedValue.Clockwise_Positive)
    //       .withNeutralMode(NeutralModeValue.Brake)
    //   );

    // intakeMotor.getConfigurator().apply(config);
  }
  
  

  public void setIntakePower(double power) {
    intakeMotor.set(power);
  }


  public void setIntakeDeployMotorPower(double power) {
    intakeDeployMotor.set(power);
  }

  public void intakeMoveToPosition(double position) {
    double power = IntakeControler.calculate(getIntakeAngle(),position);
    if (getIntakeAngle() < position + 10) {
      power = -power;
    }
    SmartDashboard.putNumber("pid out", power);
    intakeDeployMotor.set(power);
 
  }

  public void resetIntakePid() {
    IntakeControler.reset();
  }

  public void stopIntakeMotor() {
    intakeMotor.set(0);
  }

  public void 
  stopIntakeDeployMotor() {
    intakeDeployMotor.set(0);
  }
  public double getIntakeThroughBore() {
    return deployencoder.get();
  }

   public double getIntakeAngle() {
    return deployencoder.get() * 360;
  }

  public boolean isDeployed() {
    return isDeployed;
  }

  public void setDeployed(boolean deployed) {
    isDeployed = deployed;
  }


    @Override
    public void periodic() {
      SmartDashboard.putNumber("Intake ThroughBore", getIntakeThroughBore());
      SmartDashboard.putNumber("Intake angle", getIntakeAngle());

      // This method will be called once per scheduler run
    }
}