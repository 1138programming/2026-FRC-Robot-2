// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;
import static frc.robot.generated.TunerConstants.kSpeedAt12Volts;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final Mode simMode = Mode.SIM;

  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }

  public static class standardConstants {
    public static final double gravity = 9.81; // m/s^2
  }

  public static class SwerveConstants {

    // public static final double KMaxSpeed = kSpeedAt12Volts.in(MetersPerSecond);
    // // kSpeedAt12Volts desired
    public static final double KMaxSpeed =
        kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired
    // top speed

    public static final double KMaxAngularRate =
        RotationsPerSecond.of(1.8).in(RadiansPerSecond); // 3/4 of
    // a
    // rotation
    // per
    // second
    // max angular velocity

    public static final double KBaseTurboMode = 1;
    public static final double KBaseNormalMode = 0.5;
    public static final double KBaseSlowMode = 0.25;
  }

  public static class OperatorConstants {
        // Controller Ports (check in Driver Station, IDs may be different for each
    // compStreamDeckuter)
    public static final int KLogitechPort = 0;
    public static final int KXboxPort = 1;
    public static final int KCompStreamDeckPort = 2;
    public static final int KTestingStreamDeckPort = 3;
    public static final int KAutonTestingStreamDeckPort = 4;

    // Deadzone
    public static final double KDeadZone = 0.05;

    // Joystick Axis IDs
    public static final int KLeftXAxis = 0;
    public static final int KLeftYAxis = 1;
    public static final int KRightXAxis = 2;
    public static final int KRightYAxis = 3;

    // Joystick Axis IDs
    public static final int KXboxLeftYAxis = 1;
    public static final int KXboxRightYAxis = 5;
    public static final int KXboxLeftXAxis = 0;
    public static final int KXboxRightXAxis = 4;

    // Logitech Button Constants
    public static final int KLogitechButtonX = 1;
    public static final int KLogitechButtonA = 2;
    public static final int KLogitechButtonB = 3;
    public static final int KLogitechButtonY = 4;
    public static final int KLogitechLeftBumper = 5;
    public static final int KLogitechRightBumper = 6;
    public static final int KLogitechLeftTrigger = 7;
    public static final int KLogitechRightTrigger = 8;
    public static final int KLogitechBtnBack = 9;
    public static final int KLogitechRightStart = 10;

    // Xbox Button Constants
    public static final int KXboxButtonA = 1;
    public static final int KXboxButtonB = 2;
    public static final int KXboxButtonX = 3;
    public static final int KXboxButtonY = 4;
    public static final int KXboxLeftBumper = 5;
    public static final int KXboxRightBumper = 6;
    public static final int KXboxSelectButton = 7;
    public static final int KXboxStartButton = 8;
    public static final int KXboxLeftTrigger = 2;
    public static final int KXboxRightTrigger = 3;
    public static final int kDriverControllerPort = 0;

    public static final double khoodDecrement = -0.1;
    public static final double khoodIncrement = 0.1;
  }

 

  public static class intakeConstants {
    //change
    public static final int KintakeMotorId = 2;
    public static final double KintakeMotorInSpeed = -0.7;
    public static final double KintakeMotorOutSpeed = 0.7;

    
    public static final int KintakeDeployMotorId = 1;
    public static final int KintakeThroughBoreDio = 9;
    public static final double KintakeDeployMotorSpeed = -0.3;
    public static final double KintakeStowMotorSpeed = 0.2;


    public static final double KintakePIDKp = 0.0015;
    public static final double KintakePIDKi = 0;
    public static final double KintakePIDKd = 0;


    public static final double kDeployReduction = 50.0;

    public static final double kDeployAngle = 23;
    public static final double kStowAngle = 270;

    public static final double kStowed = 100;


  }
  

  public static class LimelightConstants {

    public static final String kLimelightName = "limelight-down";
    public static class TagConstants {
      

      public static class OffsetConstants {
        public static final double kX_HubCenterTagtoHubCenterMeters = -0.5969;
        public static final double kY_HubCenterTagtoHubScoreHeightMeters = 0.70485;
      }
    }
  }
  public static class turretSpeedConstants {
    public static final double KHoodMotorPower = 1.0;
    public static final double KRotationMotorPower = 1.0;
    public static final double KFlyWheelPower = 1.0;

  }

  public static class LaserConstants {
    public static final int klaserDIOPort = 3;
  }

  

  public static class FieldConstants {
    public static final double kHubHeight = 1.8288; //meters
    public static final double kScoreHeight = kHubHeight - 0.5588; //meters (shooter height is 0.5588m temporary)
    public static final double kScoreAngle = Math.toRadians(35); //radians
    
    public static class TagIDConstants {
      public static final int kHubCenterTagRed = 10;
      public static final int kHubCenterTagBlue = 26;
    }

    public static class HubConstants {

      public static class red {
        public static final double kPoseX = 11.887454;
        public static final double kPoseY = 4.034536;
        public static final double kPoseZ = 1.8288;
        public static final Pose2d kHubFieldPose2d = new Pose2d(kPoseX, kPoseY, new Rotation2d());
        public static final Pose3d KhubFieldPose3d = new Pose3d(kPoseX, kPoseY, kPoseZ, new Rotation3d());
      }

      public static class blue {
        public static final double kPoseX = 4.625594;
        public static final double kPoseY = 4.034536;
        public static final double kPoseZ = 1.8288;
        public static final Pose2d kHubFieldPose2d = new Pose2d(kPoseX, kPoseY, new Rotation2d());
        public static final Pose3d KhubFieldPose3d = new Pose3d(kPoseX, kPoseY, kPoseZ, new Rotation3d());
      }

      public static final double kPassThroughPointRadius = 0; // 0.6096 meters

    }

    //ferry constants: tosses balls into alliance zone. approximately 3/4 between wall and alliance boarder and halfway beteen alliance mid line and alliance wall up down per specified target location.
    public static class FerryConstants {
      public static class blue {
          public static class left {
              public static final double kPoseX = 2.9834;
              public static final double kPoseY = 6.0520;
              public static final Pose2d kFerryFieldPose2d = new Pose2d(kPoseX, kPoseY, new Rotation2d());
            
          }

          public static class right {
              public static final double kPoseX = 2.9834;
              public static final double kPoseY = 2.0173;
              public static final Pose2d kFerryFieldPose2d = new Pose2d(kPoseX, kPoseY, new Rotation2d());
          }
      }

      public static class red {
        public static class left {
            public static final double kPoseX = 13.5576;
            public static final double kPoseY = 6.0520;
            public static final Pose2d kFerryFieldPose2d = new Pose2d(kPoseX, kPoseY, new Rotation2d());
          
        }

        public static class right {
            public static final double kPoseX = 13.5576;
            public static final double kPoseY = 2.0173;
            public static final Pose2d kFerryFieldPose2d = new Pose2d(kPoseX, kPoseY, new Rotation2d());
        }
      }
    }
  }

  public static class ShooterConstants{
    public static final int kLeftShooterID = 3;
    public static final int kRightShooterID = -1;


    public static final double kHoodMaxAngleDeg = 70; 
    public static final double kHoodMinAngleDeg = 20;
    public static final double kHoodDefaultAngleDeg = 60;

    public static final double kHoodMaxAngleRadians = Math.toRadians(kHoodMaxAngleDeg); 
    public static final double kHoodMinAngleRadians = Math.toRadians(kHoodMinAngleDeg);
    public static final double kHoodDefaultAngleRadians = Math.toRadians(kHoodDefaultAngleDeg);

    public static final double kHoodTolerance = 1.0; //degrees

    public static final int KHoodMotorID = 5;
    public static final int KHoodThroughBoreDIO = 1;

    public static final double KHoodGearRatio = 0;

    public static final int KHoodMotorKp= 0;
    public static final int KHoodMotorKI= 0;
    public static final int KHoodMotorKD= 0;




    public static final double KShooterFlywheelkS = 0.1;
    public static final double KShooterFlywheelkV = 0.0001315; // 0.0001315
    public static final double KShooterFlywheelkP = 0.0005; // 0.0002 - 0.001
    public static final double KShooterFlywheelkI = 0; 

    public static final double KShooterFlywheelPITolerance =10;

    public static final double KFlyWheelDefaultSpeed = 4500;
    public static final double KFlyWheelautoSpeed = 3600;

    public static final double KFlyWheelRPMMultiplier = 2.1;



    public static final double kShooterHeightInches = 19.39;
    public static final double kShooterHeightMeters = 0.482506;
    public static final double kShooterWheelRadiusInches = 3; // 3 inches in meters PLACEHOLDER PLACEHOLDER
    public static final double kShooterWheelRadiusMeters = 0.0381;

  }

  public static class IndexerConstants{
    public static final int kIndexerID = 4;
    
    public static final double kIndexerPower = 0.75;

    public static final double kIndexerPowerAuto = 0.4;
    
  }
}
