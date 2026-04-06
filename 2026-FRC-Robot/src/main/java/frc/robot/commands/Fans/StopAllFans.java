package frc.robot.commands.Fans;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Fans;

public class StopAllFans extends Command {

    private Fans fans;;

    public StopAllFans(Fans fans)
    {
        this.fans = fans;
        addRequirements(fans);
    }

    @Override
    public void execute()
    {
        fans.stopAllFans();
    }

     @Override
    public void end(boolean interrupted){
    
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}