package frc.robot.subsystems;
import static frc.robot.Constants.ShooterConstants.*;
import com.revrobotics.spark.SparkFlex;

public class DuoShooter {
    SparkFlex bottomMotor;
    SparkFlex topMotor;

    public DuoShooter ()
    {
        bottomMotor = new SparkFlex(kLeftShooterID, null)
    }
