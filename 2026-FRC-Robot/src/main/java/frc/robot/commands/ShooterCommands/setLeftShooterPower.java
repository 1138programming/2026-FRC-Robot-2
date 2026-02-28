package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class setLeftShooterPower extends Command{
    private double power;
    private Shooter duoShooter;

    public setLeftShooterPower(Shooter duoShooter, double power){
        this.duoShooter = duoShooter;
        this.power = power;
        addRequirements(duoShooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    @Override
    public void execute(){
        duoShooter.setLeftShooterPower(power);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    
    
}
