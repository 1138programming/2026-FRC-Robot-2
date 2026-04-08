package frc.robot.commands.Fans;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Fans;

public class SetRightFansPower extends Command{

    private Fans fans;
    private double power;

    public SetRightFansPower(Fans fans, double power)
    {
        this.fans = fans;
        this.power = power;
        addRequirements(fans);
    }

    @Override
    public void execute()
    {
        fans.setRightFanPower(power);
    }

     @Override
    public void end(boolean interrupted){
        fans.stopRightFans();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}