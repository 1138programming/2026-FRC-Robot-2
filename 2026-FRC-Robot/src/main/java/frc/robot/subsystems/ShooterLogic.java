// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Optional;
import java.util.Vector;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.FieldConstants.HubConstants;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.drive.Drive;



import static frc.robot.Constants.FieldConstants.*;
import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Constants.FieldConstants.HubConstants.*;

public class ShooterLogic extends SubsystemBase {
  /** Creates a new ShooterLogic. */

  private Limelight limelight;
  private Drive drive;

  private Boolean readyToShoot; 


  private Pose3d kHubFieldPose3d;
  private Pose2d kHubFieldPose2d;

  
  private Pose3d kAZoneFieldPose3d;
  private Pose2d kAZoneFieldPose2d;

  private double[] shotChangeDataHub;

  public enum Targets {
    HUB,
    AZONE
  }
  private Targets activeTarget;


  public ShooterLogic(Limelight limelight, Drive drive) {
    this.limelight = limelight;
    this.drive = drive;
    readyToShoot = false;
    
    if (DriverStation.getAlliance().isPresent()) {
      if (DriverStation.getAlliance().get() == Alliance.Red) {
        kHubFieldPose3d = HubConstants.red.KhubFieldPose3d;
        kAZoneFieldPose3d = new Pose3d(0.0,0.0,0.0,Rotation3d.kZero);
      }

      if (DriverStation.getAlliance().get() == Alliance.Blue) {
        kHubFieldPose3d = HubConstants.blue.KhubFieldPose3d;
        kAZoneFieldPose3d = new Pose3d(0.0,0.0,0.0,Rotation3d.kZero);

      }
    } else {
      //default red cause thats what the wooden hub we have has
      kHubFieldPose3d = HubConstants.red.KhubFieldPose3d;
      kAZoneFieldPose3d = new Pose3d(0.0,0.0,0.0,Rotation3d.kZero);

    }
    kHubFieldPose2d = kHubFieldPose3d.toPose2d();
    kAZoneFieldPose2d = kAZoneFieldPose3d.toPose2d();


    activeTarget = Targets.HUB;

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run


     if (DriverStation.getAlliance().isPresent()) {
      if (DriverStation.getAlliance().get() == Alliance.Red) {
        kHubFieldPose3d = HubConstants.red.KhubFieldPose3d;
      }

      if (DriverStation.getAlliance().get() == Alliance.Blue) {
        kHubFieldPose3d = HubConstants.blue.KhubFieldPose3d;
      }
    } else {
      //default red cause thats what the wooden hub we have has
      kHubFieldPose3d = HubConstants.red.KhubFieldPose3d;
    }
    kHubFieldPose2d = kHubFieldPose3d.toPose2d();

    // addTurretRotationtoPose();

   

    //shot change math

    // shotChangeDataHub = calculateShotChanges(kHubFieldPose3d);
    // SmartDashboard.putNumberArray("shot changes", shotChangeDataHub);
    //shotChangeDataHub = calculateShotChanges(kHubFieldPose2d);
    //SmartDashboard.putNumberArray("shot changes", shotChangeDataHub);
     

    //SmartDashboard.putNumber("TX Helper", absoluteAngletoAprilTagLimelightDegrees(0));
    SmartDashboard.putNumber("Angle to Hub center", botAngletoPose2d(kHubFieldPose2d));
      SmartDashboard.putNumber("relative Angle to Hub center", relativebaseAngletoPose2d(kHubFieldPose2d));
    // SmartDashboard.putString("Turret Pose 2d", turretPose2d.toString());
    // SmartDashboard.putString("diff translation", kHubFieldPose2d.getTranslation().minus(turretPose2d.getTranslation()).toString());

    SmartDashboard.putNumber("Distance to Hub Center", distancetoPose2d(kHubFieldPose2d));
  }

  /**
   * Calculates the flywheel speed, hood angle, and turret angle based on robot position in accordance to the hub center with velocity compensation.
   * 
   * @return double[] {flywheelSpeed (meters per second), hoodAngle (radians), turretAngle (radians)}
   */
  @Deprecated
  public double[] calculateShotChanges(Pose3d target) {

    final double g = 9.81;
    double x =  distancetoPose2d(target.toPose2d()) - kPassThroughPointRadius; //could be alternatively used using Pose
    double y = target.getZ() - kShooterHeightMeters; //could be alternatively used using Pose
    double a = kScoreAngle;
    double robotAngle = drive.getRotation().getRadians(); //robot angle in reference to field
    double robottoGoalAngle = botAngletoPose2d(target.toPose2d()); //angle from robot to goal in reference to field

    //initial launch components
    double hoodAngle = Math.max(kHoodMinAngleRadians, Math.min(kHoodMaxAngleRadians, (Math.atan(2 * y / x - Math.tan(a))))); //this clamps the hood angle to constraints
    double flywheelSpeed = Math.sqrt(Math.abs(g * x * x / (2 * Math.pow(Math.cos(hoodAngle), 2) * (x * Math.tan(hoodAngle) - y))));

    //robot velocity components
    double robotVelocityXComponent = drive.getHorizontalVelocityMetersPerSecond();
    double robotVelocityYComponent = drive.getVerticalVelocityMetersPerSecond();

    // //velocity compensation variables
    double vz = flywheelSpeed * Math.sin(hoodAngle); //velocity of the projectile in z direction (vertical)
    double time = x / (flywheelSpeed * Math.cos(hoodAngle)); //projectile air time
    double ivr = x / time + robotVelocityXComponent; //initial radial velocity of of the projectile
    double nvr = Math.sqrt(ivr * ivr + robotVelocityYComponent * robotVelocityYComponent); //compensating launch velocity using perpendicular moevement 
    double ndr = nvr * time; //convert to distance

    //final launch components with compensation
    hoodAngle = Math.max(kHoodMaxAngleRadians, Math.min(kHoodMinAngleRadians, (Math.atan(vz / nvr))));
    flywheelSpeed = Math.sqrt(g * ndr * ndr / (2 * Math.pow(Math.cos(hoodAngle), 2) * (ndr * Math.tan(hoodAngle) - y)));

    // //updating turret
    double turretVelCompensation = Math.atan(robotVelocityYComponent / ivr);
    double newAngle = robottoGoalAngle + turretVelCompensation;

  
  

    return new double[] {flywheelSpeed, hoodAngle, newAngle};
  }

  
  /**
   * Calculates the  hood angle, based on robot position in accordance to the hub center.
   * 
   * @return double[] {flywheelSpeed (meters per second), hoodAngle (radians), turretAngle (radians)}
   */
  @Deprecated
  public double getHoodAimAngleforStaticBase(Pose3d target, double flywheelSpeed) {
        double y = target.getZ();
        double h = kShooterHeightMeters; //should be constant
        double x2 = distancetoPose2d(target.toPose2d()) * distancetoPose2d(target.toPose2d());
        double x4 = x2 * x2;
        double y2 = y * y;
        double v2 = flywheelSpeed * flywheelSpeed;
        double v4 = v2 * v2;
        double h2 = h * h;
        double g = Constants.standardConstants.gravity;
        double g2 = g * g;

        double innerDiscriminant = Math.sqrt(
          Math.pow((g * y * x2 - g * h * x2)/(v2),2)
          - (g2 * x4)/(v4) *
          (x2 + y2 + h2 - (2 * h * y))
        );

        double innnerb = 
          x2 
          - ((g * y * x2 - g * h * x2)/(v2));

        double denominator = 2 * (x2 + y2 + h2 - (2 * h * y));

        if (denominator == 0) { 
          return -1; //invalid shot, target is too close or at the same height as the shooter
        }

        //using plus because - will produce a more direct angle likly hitting the hoop of the hub
        // + produces higher angle which is more likely to enter the hub with no collision.
        double hoodAngle = Math.acos(Math.sqrt((innnerb + Math.sqrt(innerDiscriminant))/denominator));
        return hoodAngle;
  }

  public double getFlywheelExitVelocity(double hoodAngle) {
    
    final double g = 9.81;
    double x =  distancetoPose2d(getTargetPose3d().toPose2d()) - kPassThroughPointRadius; //could be alternatively used using Pose
    double y = getTargetPose3d().getZ() - kShooterHeightMeters; //could be alternatively used using Pose
    double flywheelSpeed = Math.sqrt(Math.abs(g * x * x / (2 * Math.pow(Math.cos(hoodAngle), 2) * (x * Math.tan(hoodAngle) - y))));

    return flywheelSpeed;
  }
  

  public void setTargetHUB() {
    activeTarget = Targets.HUB;
  }
   
  public void setTargetAZONE(){
    activeTarget = Targets.AZONE;
  }

  public Pose3d getTargetPose3d() {
    switch (activeTarget) {
      case HUB:
        return kHubFieldPose3d;
      case AZONE:
        return kHubFieldPose3d;
      default:
        return null;
    }
  }

  //-----------------------//
  //----- Odom Aiming -----//
  //-----------------------//

/////////////////
//     //     ///
/////// /////////
/////////////////
//             //
/////////////////

  
  /**
   * @param pose2d
   * @return The relative yaw angle of the bot to directly face the Pose2d position (in degrees)
   * Values range from -180 to 180
   */

  public double relativebaseAngletoPose2d(Pose2d pose) {
    // double angle = Math.abs(Math.abs(botAngletoPose2d(pose)) - 180);
     double angle = (drive.getRotation().getDegrees() - botAngletoPose2d(pose))- 180;

  
    if(angle > 180) {
      angle -= 360;
    } 
    if(angle < -180) {
      angle += 360;
    }

    return angle;
  }

  //in shooter logic as it requires continual adjustment by drive for the robot's position
  //Review if this is alright here
  private double distancetoPose2d(Pose2d pose2d) {
    return drive.getPose().getTranslation().getDistance(pose2d.getTranslation());
  }



  /**
   *
   * @param pose2d
   * @return Returns the yaw angle of the bot to the Pose2d relative to the field in degrees
   */
  public double botAngletoPose2d(Pose2d pose2d) {
    Translation2d diffTranslation = pose2d.getTranslation().minus(drive.getPose().getTranslation());
    return diffTranslation.getAngle().getDegrees();
  } 

  public Double getShotChangeFlywheelVelocity(Targets target) {
    switch (target) {
      case HUB:
        return shotChangeDataHub[0];
      default:
        return null;
    }
  }

  public Double getShotChangeHoodAngle(Targets target) {
        switch (target) {
      case HUB:
        return shotChangeDataHub[1];
      default:
        return null;
    }
  }

  public Double getShotChangeTurretAngle(Targets target) {
        switch (target) {
      case HUB:
        return shotChangeDataHub[2];
      default:
        return null;
    }
  }

}



