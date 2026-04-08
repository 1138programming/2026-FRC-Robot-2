package frc.robot.commands.Fans;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Fans;

public class SetLeftFansPower extends Command{

    private Fans fans;
    private double power;

    public SetLeftFansPower(Fans fans, double power)
    {
        this.fans = fans;
        this.power = power;
        addRequirements(fans);
    }

    @Override
    public void execute()
    {
        fans.setLeftFanPower(power);
    }

     @Override
    public void end(boolean interrupted){
        fans.stopLeftFans();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}