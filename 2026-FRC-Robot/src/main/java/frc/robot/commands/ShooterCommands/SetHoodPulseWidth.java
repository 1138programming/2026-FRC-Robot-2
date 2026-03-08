package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class SetHoodPulseWidth extends Command {
    private Shooter shooter;
    private int amount;

    public SetHoodPulseWidth(Shooter shooter, int amount){
        this.shooter = shooter;
        this.amount = amount;
        addRequirements(shooter);

    }

    @Override
    public void execute(){
        shooter.setcurrentpulse(amount);
    }

     @Override
    public void end(boolean interrupted) {
        
    }

    @Override
    public boolean isFinished(){
        return false;
    }

    
}
