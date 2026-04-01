// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Optional;
import java.util.Vector;
import java.util.function.Supplier;

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
import frc.robot.subsystems.drive.Drive;



import static frc.robot.Constants.FieldConstants.*;
import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Constants.FieldConstants.HubConstants.*;

public class ShooterLogic extends SubsystemBase {
  /** Creates a new ShooterLogic. */

  private Drive drive;

  private Pose2d kAZoneFieldPose2d;

  private double[] shotChangeDataHub;



  public ShooterLogic(Drive drive) {
    this.drive = drive;
    
    

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //SmartDashboard.putNumber("TX Helper", absoluteAngletoAprilTagLimelightDegrees(0));
    SmartDashboard.putNumber("Angle to Hub center", botAngletoPose2d(getHubPose3d().toPose2d()));
      SmartDashboard.putNumber("relative Angle to Hub center", relativebaseAngletoPose2d(getHubPose3d().toPose2d()));
    // SmartDashboard.putString("Turret Pose 2d", turretPose2d.toString());
    // SmartDashboard.putString("diff translation", kHubFieldPose2d.getTranslation().minus(turretPose2d.getTranslation()).toString());

    SmartDashboard.putNumber("Distance to Hub Center", distancetoPose2d(getHubPose3d().toPose2d()));
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

  public double getFlywheelExitVelocity(double hoodAngle,Pose3d target ) {
    final double g = 9.81;
    double x =  distancetoPose2d(target.toPose2d()) - kPassThroughPointRadius; //could be alternatively used using Pose
    double y = 2.0 - kShooterHeightMeters; //could be alternatively used using Pose
    double flywheelSpeed = Math.sqrt(Math.abs(g * (x * x) / ( 
      Math.pow(Math.cos(hoodAngle), 2) * (y- (x * Math.tan(hoodAngle)))*2)));

    return flywheelSpeed;
  }

  public double getFlywheelExitVelocity(double hoodAngle, double distance) {
    final double g = 9.81;
    double x =  distance - kPassThroughPointRadius; //could be alternatively used using Pose
    double y = 2.0 - kShooterHeightMeters; //could be alternatively used using Pose
    double flywheelSpeed = Math.sqrt(Math.abs(g * (x * x) / ( 
      Math.pow(Math.cos(hoodAngle), 2) * (y- (x * Math.tan(hoodAngle)))*2)));

    return flywheelSpeed;
  }
  
  public Pose3d getHubPose3d() {
    if (DriverStation.getAlliance().isPresent()) {
      if (DriverStation.getAlliance().get() == Alliance.Red) {
        return HubConstants.red.KhubFieldPose3d;
      }

      if (DriverStation.getAlliance().get() == Alliance.Blue) {
       return HubConstants.blue.KhubFieldPose3d;
      }
    } 

      //default red cause thats what the wooden hub we have has
      return HubConstants.red.KhubFieldPose3d;

  }

  public Pose3d getAzonePose3d() {
    if (DriverStation.getAlliance().isPresent()) {
      if (DriverStation.getAlliance().get() == Alliance.Red) {
        return new Pose3d(0.0,0.0,0.0,Rotation3d.kZero);
      }

      if (DriverStation.getAlliance().get() == Alliance.Blue) {
       return new Pose3d(0.0,0.0,0.0,Rotation3d.kZero);
      }
    } 
    //default red cause thats what the wooden hub we have has
    return new Pose3d(0.0,0.0,0.0,Rotation3d.kZero);
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

  /**
   *
   * @param pose2d
   * @return Returns the distance of the bot to the Pose2d
   */
  public double distancetoPose2d(Pose2d pose2d) {
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

  

}



