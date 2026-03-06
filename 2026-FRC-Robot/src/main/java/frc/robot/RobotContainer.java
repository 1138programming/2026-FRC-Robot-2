

// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import static frc.robot.Constants.IndexerConstants.kIndexerPower;
import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.SwerveConstants.*;
import static frc.robot.Constants.TurretConstants.*;

import java.util.Optional;
import java.util.function.BooleanSupplier;

//subsystem
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterLogic;
import frc.robot.subsystems.Indexer;


import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.toggleLaser;
import frc.robot.commands.Intake.ExtendIntake;
import frc.robot.commands.Intake.IntakeIn;
import frc.robot.commands.Intake.IntakeOut;
import frc.robot.commands.Intake.RetractIntake;
import frc.robot.subsystems.Intake;
import frc.robot.commands.ShooterCommands.IncrementHoodAngle;
import frc.robot.commands.ShooterCommands.SpinShooter;
import frc.robot.commands.ShooterCommands.setHoodAngle;
import frc.robot.commands.SetIndexerPower;
import frc.robot.commands.StopIndexer;

import frc.robot.subsystems.Laser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import frc.robot.Constants.LimelightConstants;
//drive
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;  
  public final Laser m_Laser;
  public final Intake intake;
  public final Limelight limelight;
  public final Shooter shooter;
  public final Indexer indexer;

  public final ShooterLogic logic;


  // Comands

  public final toggleLaser lasertoggle;
  public final IntakeIn intakein;
  public final IntakeOut intakeOut;
  public final ExtendIntake extendIntake;
  public final RetractIntake retractIntake;
  public final SpinShooter spinShooter;
  public final SpinShooter spinShooterReverse;



  public final IncrementHoodAngle hoodUpCommand;
  public final IncrementHoodAngle hoodDownCommand;
  public final SetIndexerPower setIndexerPower;
  public final SetIndexerPower reverseIndexerPower;
  public final StopIndexer stopIndexer;


  // Comands

  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);

   public static Joystick logitech;
  public static Joystick compStreamDeck;
  public static Joystick testStreamDeck;
  public static Joystick autonTestStreamDeck;
  // public final CommandXboxController joystick = new CommandXboxController(1);
  public JoystickButton logitechBtnX,
      logitechBtnA,
      logitechBtnB,
      logitechBtnY,
      logitechBtnLB,
      logitechBtnRB,
      logitechBtnLT,
      logitechBtnRT,
      logitechBtnBack,
      logitechBtnStart; // Logitech Button
  public JoystickButton compStreamDeck1,
      compStreamDeck2,
      compStreamDeck3,
      compStreamDeck4,
      compStreamDeck5,
      compStreamDeck6,
      compStreamDeck7,
      compStreamDeck8,
      compStreamDeck9,
      compStreamDeck10,
      compStreamDeck11,
      compStreamDeck12,
      compStreamDeck13,
      compStreamDeck14,
      compStreamDeck15,
      compStreamDeck16,
      compStreamDeck17,
      compStreamDeck18,
      compStreamDeck19;

  // Top Left SD = 1, numbered from left to right
  public JoystickButton testStreamDeck1,
      testStreamDeck2,
      testStreamDeck3,
      testStreamDeck4,
      testStreamDeck5,
      testStreamDeck6,
      testStreamDeck7,
      testStreamDeck8,
      testStreamDeck9, // Vjoy 2
      testStreamDeck10,
      testStreamDeck11,
      testStreamDeck12,
      testStreamDeck13,
      testStreamDeck14,
      testStreamDeck15;
  public JoystickButton autonTestStreamDeck1,
      autonTestStreamDeck2,
      autonTestStreamDeck3,
      autonTestStreamDeck4,
      autonTestStreamDeck5,
      autonTestStreamDeck6,
      autonTestStreamDeck7,
      autonTestStreamDeck8,
      autonTestStreamDeck9, // Vjoy 2
      autonTestStreamDeck10,
      autonTestStreamDeck11,
      autonTestStreamDeck12,
      autonTestStreamDeck13,
      autonTestStreamDeck14,
      autonTestStreamDeck15;

  private double baseSpeed = KBaseNormalMode;
 

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  // speed
  public double driveSpeed;


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    m_Laser = new Laser();
    intake = new Intake();
    limelight = new Limelight(LimelightConstants.kLimelightName);
    shooter = new Shooter();
  
    //commands
    lasertoggle = new toggleLaser(m_Laser);
    intakein = new IntakeIn(intake);
    intakeOut = new IntakeOut(intake);
    extendIntake = new ExtendIntake(intake);
    retractIntake = new RetractIntake(intake);
    spinShooter = new SpinShooter(shooter, 0.8);
    spinShooterReverse = new SpinShooter(shooter, -0.8);
    hoodUpCommand = new IncrementHoodAngle(shooter, 2100);
    hoodDownCommand = new IncrementHoodAngle(shooter, -900);



    indexer = new Indexer();
    setIndexerPower = new SetIndexerPower(indexer, kIndexerPower);
    reverseIndexerPower = new SetIndexerPower(indexer, -kIndexerPower);
    stopIndexer = new StopIndexer(indexer);

    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        // ModuleIOTalonFX is intended for modules with TalonFX drive, TalonFX turn, and
        // a CANcoder

        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight),
                limelight);

        // The ModuleIOTalonFXS implementation provides an example implementation for
        // TalonFXS controller connected to a CANdi with a PWM encoder. The
        // implementations
        // of ModuleIOTalonFX, ModuleIOTalonFXS, and ModuleIOSpark (from the Spark
        // swerve
        // template) can be freely intermixed to support alternative hardware
        // arrangements.
        // Please see the AdvantageKit template documentation for more information:
        // https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template#custom-module-implementations
        //
        // drive =
        // new Drive(
        // new GyroIOPigeon2(),
        // new ModuleIOTalonFXS(TunerConstants.FrontLeft),
        // new ModuleIOTalonFXS(TunerConstants.FrontRight),
        // new ModuleIOTalonFXS(TunerConstants.BackLeft),
        // new ModuleIOTalonFXS(TunerConstants.BackRight));
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight),
                limelight);
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                limelight);
        break;
    }

    logic = new ShooterLogic(limelight, drive, DriverStation.getAlliance());

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    // Set up SysId routines
    autoChooser.addOption(
        "Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
    autoChooser.addOption(
        "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Forward)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Reverse)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));
    

    logitech = new Joystick(KLogitechPort); // Logitech Dual Action
    compStreamDeck = new Joystick(KCompStreamDeckPort); // Stream Deck + vjoy
    testStreamDeck = new Joystick(KTestingStreamDeckPort); // Stream Deck + vjoy
    autonTestStreamDeck = new Joystick(KAutonTestingStreamDeckPort); // Stream Deck + vjoy

    // Logitch Buttons
    logitechBtnX = new JoystickButton(logitech, KLogitechButtonX);
    logitechBtnA = new JoystickButton(logitech, KLogitechButtonA);
    logitechBtnB = new JoystickButton(logitech, KLogitechButtonB);
    logitechBtnY = new JoystickButton(logitech, KLogitechButtonY);
    logitechBtnLB = new JoystickButton(logitech, KLogitechLeftBumper);
    logitechBtnRB = new JoystickButton(logitech, KLogitechRightBumper);
    logitechBtnLT = new JoystickButton(logitech, KLogitechLeftTrigger);
    logitechBtnRT = new JoystickButton(logitech, KLogitechRightTrigger);
    logitechBtnBack = new JoystickButton(logitech, KLogitechBtnBack);
    logitechBtnStart = new JoystickButton(logitech, KLogitechRightStart);

    // Streamdeck Pages used in match
    compStreamDeck1 = new JoystickButton(compStreamDeck, 1); // -> shoot (Indexer in and activate flywheel)
    compStreamDeck2 = new JoystickButton(compStreamDeck, 2); // -> Indexer (toggle indexer)
    compStreamDeck3 = new JoystickButton(compStreamDeck, 3); 
    compStreamDeck4 = new JoystickButton(compStreamDeck, 4);
    compStreamDeck5 = new JoystickButton(compStreamDeck, 5); // -> Intake (deploy and run intake motors)
    compStreamDeck6 = new JoystickButton(compStreamDeck, 6);
    compStreamDeck7 = new JoystickButton(compStreamDeck, 7); // -> Flywheel (Hold to run flywheel)
    compStreamDeck8 = new JoystickButton(compStreamDeck, 8); // -> Hood Up (Move hood up manual)
    compStreamDeck9 = new JoystickButton(compStreamDeck, 9);
    compStreamDeck10 = new JoystickButton(compStreamDeck, 10); // -> Stow (Stop intake motors and stow intake)
    compStreamDeck11 = new JoystickButton(compStreamDeck, 11);
    compStreamDeck12 = new JoystickButton(compStreamDeck, 12); // -> Hood Bottom (Move hood to lowest position))
    compStreamDeck13 = new JoystickButton(compStreamDeck, 13); // -> Hood Down (Move hood down manual)
    compStreamDeck14 = new JoystickButton(compStreamDeck, 14); // -> Hood Optimum (Set hood to optimum angle for current distance to hub and intake power)
    compStreamDeck15 = new JoystickButton(compStreamDeck, 15); // -> Eject (Run indexer to eject game pieces)
    compStreamDeck16 = new JoystickButton(compStreamDeck, 16);
    compStreamDeck17 = new JoystickButton(compStreamDeck, 17);
    compStreamDeck18 = new JoystickButton(compStreamDeck, 18);
    compStreamDeck19 = new JoystickButton(compStreamDeck, 19);

    // Streamdeck Pages used for testing
    testStreamDeck1 = new JoystickButton(testStreamDeck, 1); 
    testStreamDeck2 = new JoystickButton(testStreamDeck, 2);
    testStreamDeck3 = new JoystickButton(testStreamDeck, 3);
    testStreamDeck4 = new JoystickButton(testStreamDeck, 4);
    testStreamDeck5 = new JoystickButton(testStreamDeck, 5);
    testStreamDeck6 = new JoystickButton(testStreamDeck, 6);
    testStreamDeck7 = new JoystickButton(testStreamDeck, 7);
    testStreamDeck8 = new JoystickButton(testStreamDeck, 8);
    testStreamDeck9 = new JoystickButton(testStreamDeck, 9);
    testStreamDeck10 = new JoystickButton(testStreamDeck, 10);
    testStreamDeck11 = new JoystickButton(testStreamDeck, 11);
    testStreamDeck12 = new JoystickButton(testStreamDeck, 12);
    testStreamDeck13 = new JoystickButton(testStreamDeck, 13);
    testStreamDeck14 = new JoystickButton(testStreamDeck, 14);
    testStreamDeck15 = new JoystickButton(testStreamDeck, 15);

    autonTestStreamDeck1 = new JoystickButton(testStreamDeck, 1); 
    autonTestStreamDeck2 = new JoystickButton(testStreamDeck, 2);
    autonTestStreamDeck3 = new JoystickButton(testStreamDeck, 3);
    autonTestStreamDeck4 = new JoystickButton(testStreamDeck, 4);
    autonTestStreamDeck5 = new JoystickButton(testStreamDeck, 5);
    autonTestStreamDeck6 = new JoystickButton(testStreamDeck, 6);
    autonTestStreamDeck7 = new JoystickButton(testStreamDeck, 7);
    autonTestStreamDeck8 = new JoystickButton(testStreamDeck, 9);
    autonTestStreamDeck9 = new JoystickButton(testStreamDeck, 9);
    autonTestStreamDeck10 = new JoystickButton(testStreamDeck, 10);
    autonTestStreamDeck11 = new JoystickButton(testStreamDeck, 11);
    autonTestStreamDeck12 = new JoystickButton(testStreamDeck, 12);
    autonTestStreamDeck13 = new JoystickButton(testStreamDeck, 13);
    autonTestStreamDeck14 = new JoystickButton(testStreamDeck, 14);
    autonTestStreamDeck15 = new JoystickButton(testStreamDeck, 15);


    driveSpeed = 0.7;
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal field-relative drive
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> getLogiLeftYAxis() * 0.8,
            () -> getLogiLeftXAxis() * 0.8,
            () -> getLogiRightXAxis() * 0.8));

    // Lock to 0° when A button is held
    // logitechBtnA
    //     .whileTrue(
    //         DriveCommands.joystickDriveAtAngle(
    //             drive,
    //             () -> getLogiLeftYAxis(),
    //             () -> getLogiLeftXAxis(),
    //             () -> Rotation2d.kZero));

    // Switch to X pattern when X button is pressed
    // logitechBtnX.onTrue(Commands.runOnce(drive::stopWithX, drive));

    //turret controls
    ;

    //laser controls
    // logitechBtnLB.onTrue(lasertoggle);
    //logitechBtnLB.onTrue(lasertoggle);
    
    logitechBtnRT.whileTrue(setIndexerPower);
    // logitechBtnRT.onFalse(stopIndexer);

    logitechBtnRB.whileTrue(reverseIndexerPower);
    // logitechBtnRB.onFalse(stopIndexer);
    

    // logitechBtnLB.whileTrue(intakein);
    // logitechBtnLT.whileTrue(intakeOut);
    // logitechBtnX.whileTrue(extendIntake);
    // logitechBtnY.whileTrue(retractIntake);
    
  // logitechBtnRB.whileTrue( new FunctionalCommand(
  //   () -> {}, 
  //   () -> {
  //   driveSpeed = 0.5;
  //   }, 
  
  //   interrupted -> {
  //     driveSpeed = 0.7;
  //   }, 
  //   () -> true, 
  //   (SubsystemBase) null) );


    logitechBtnLB.whileTrue(hoodUpCommand);
    logitechBtnLT.whileTrue(hoodDownCommand);




    logitechBtnX.whileTrue(spinShooter);  
    logitechBtnA.whileTrue(spinShooterReverse);  




    // Reset gyro to 0° when B button is pressed
    logitechBtnY
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), Rotation2d.kZero)),
                    drive)
                .ignoringDisable(true));
  }



  
  public double getLogiRightYAxis() {
    final double Y = logitech.getRawAxis(KRightYAxis);
    SmartDashboard.putNumber("getLogiRightYAxis", -Y);
    if (Y > KDeadZone || Y < -KDeadZone) return -Y;
    else return 0;
  }

  public double getLogiLeftYAxis() {
    final double Y = logitech.getY();
    SmartDashboard.putNumber("getLogiLeftYAxis", -Y);
    if (Y > KDeadZone || Y < -KDeadZone) return -Y;
    else return 0;
  }

  public double getLogiRightXAxis() {
    double X = logitech.getZ();
    SmartDashboard.putNumber("getLogiRightXAxis", -X);
    if (X > KDeadZone || X < -KDeadZone) {
      return -X;
    } else {
      return 0;
    }
  }

  public double getLogiLeftXAxis() {
    double X = logitech.getX();
    SmartDashboard.putNumber("getLogiLeftXAxis", -X);
    if (X > KDeadZone || X < -KDeadZone) {

      return -X;
    } else {
      return 0;
    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}
