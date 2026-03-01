package frc.robot.subsystems;


import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Second;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import static frc.robot.Constants.intakeConstants.*;


public class Intake extends SubsystemBase{
    private SparkFlex intakeMotor;
    private TalonFX intakeDeployMotor;

    private static final AngularVelocity maxPivotSpeed = RPM.of(6000).div(kDeployReduction);

    private final VoltageOut deployVoltageRequest = new VoltageOut(0);
    private final MotionMagicVoltage deployMMRequest = new MotionMagicVoltage(0).withSlot(0);
    private final VoltageOut intakeVoltageREquest = new VoltageOut(0);

  public Intake() {
      intakeMotor = new SparkFlex(KintakeMotorId,MotorType.kBrushless);
      intakeDeployMotor = new TalonFX(KintakeDeployMotorId);
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
      intakeDeployMotor.getConfigurator().apply(config);
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

  public void setAngle(double angle) {
    intakeDeployMotor.setControl(
      deployMMRequest
        .withPosition(angle)
    );
  }

  public void setIntakeDeployMotorPower(double power) {
    intakeDeployMotor.set(power);
  }

  public void stopIntakeMotor() {
    intakeMotor.set(0);
  }

  public void stopIntakeDeployMotor() {
    intakeDeployMotor.set(0);
  }



    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
}