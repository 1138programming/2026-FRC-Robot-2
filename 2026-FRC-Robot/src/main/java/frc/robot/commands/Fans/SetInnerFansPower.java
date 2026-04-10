package frc.robot.commands.Fans;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Fans;

public class SetInnerFansPower extends Command{

    private Fans fans;
    private double power;

    public SetInnerFansPower(Fans fans, double power)
    {
        this.fans = fans;
        this.power = power;
        addRequirements(fans);
    }

    @Override
    public void execute()
    {
        fans.setmidLeftFanMotorPower(power);
        fans.setmidRightFanMotorPower(power);
    }

     @Override
    public void end(boolean interrupted){
        fans.stopAllFans();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}