package frc.robot;

import java.lang.reflect.Method;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.auto.RotateToAngle;
import frc.robot.commands.auto.MoveToPosition;
import frc.robot.commands.auto.MoveXInches;
import frc.robot.commands.shooter.*;

import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.FieldPositioningSystem;
import frc.robot.subsystems.IntakeSystem;
import frc.robot.subsystems.ShooterSystem;
import frc.robot.subsystems.ClimberSystem;

public class RobotContainer {
  public DriveSystem m_driveSystem = new DriveSystem();
  public FieldPositioningSystem positioningSystem = new FieldPositioningSystem(m_driveSystem);
  public ShooterSystem shooterSystem = new ShooterSystem();
  public IntakeSystem intakeSystem = new IntakeSystem();
  public ClimberSystem climberSystem = new ClimberSystem();

  public XboxController controller = new XboxController(0);

  public double lowerKickerRunSpeed, lowerKickerReverseSpeed;
  public double upperKickerRunSpeed, upperKickerReverseSpeed;
  public double shooterRunSpeed, shooterReverseSpeed;
  public double intakeRunSpeed, intakeReverseSpeed;
  public double autoRotateSpeed;
  public double climberRunnerRunSpeed, climberRunnerReverseSpeed;
  public double climberDeployerRunSpeed, climberDeployerReverseSpeed;

  private int oldLeftTriggerAxis, oldRightTriggerAxis, oldPOV;
  private boolean oldRightStickButton;

  // The container for the robot. Contains subsystems, OI devices, and commands.
  public RobotContainer() {
    configureButtonBindings();

    initializeControlVariables();
    sendControlVariableSettersToShuffleboard();
  }

  /**
   * Initializes the control variables using the constants in ControlConstants
   */
  public void initializeControlVariables() {
    lowerKickerRunSpeed = Constants.ControlConstants.LOWER_KICKER_INITIAL_RUN_SPEED;
    lowerKickerReverseSpeed = Constants.ControlConstants.LOWER_KICKER_INITIAL_REVERSE_SPEED;
    upperKickerRunSpeed = Constants.ControlConstants.UPPER_KICKER_INITIAL_RUN_SPEED;
    upperKickerReverseSpeed = Constants.ControlConstants.UPPER_KICKER_INITIAL_REVERSE_SPEED;
    shooterRunSpeed = Constants.ControlConstants.SHOOTER_INITIAL_RUN_SPEED;
    shooterReverseSpeed = Constants.ControlConstants.SHOOTER_INITIAL_REVERSE_SPEED;
    intakeRunSpeed = Constants.ControlConstants.INTAKE_INITIAL_RUN_SPEED;
    intakeReverseSpeed = Constants.ControlConstants.INTAKE_INITIAL_REVERSE_SPEED;

    autoRotateSpeed = Constants.AutoConstants.AUTO_ROTATE_SPEED;
    climberRunnerRunSpeed = Constants.ControlConstants.CLIMBER_RUNNER_INITIAL_RUN_SPEED;
    climberRunnerReverseSpeed = Constants.ControlConstants.CLIMBER_RUNNER_INITIAL_REVERSE_SPEED;
    climberDeployerRunSpeed = Constants.ControlConstants.CLIMBER_DEPLOYER_INITIAL_RUN_SPEED;
    climberDeployerReverseSpeed = Constants.ControlConstants.INTAKE_INITIAL_REVERSE_SPEED;
    oldLeftTriggerAxis = oldRightTriggerAxis = 0;
    oldPOV = -1;
    oldRightStickButton = false;
  }

  public void sendControlVariableSettersToShuffleboard() {
    try {
      RobotContainer robotContainer = this;

      Method lowerKickerRunSpeedSetter = RobotContainer.class.getMethod("setLowerKickerRunSpeed", Double.class);
      Method lowerKickerReverseSpeedSetter = RobotContainer.class.getMethod("setLowerKickerReverseSpeed", Double.class);
      Method upperKickerRunSpeedSetter = RobotContainer.class.getMethod("setUpperKickerRunSpeed", Double.class);
      Method upperKickerReverseSpeedSetter = RobotContainer.class.getMethod("setUpperKickerReverseSpeed", Double.class);
      Method shooterRunSpeedSetter = RobotContainer.class.getMethod("setShooterRunSpeed", Double.class);
      Method shooterReverseSpeedSetter = RobotContainer.class.getMethod("setShooterReverseSpeed", Double.class);
      Method intakeRunSpeedSetter = RobotContainer.class.getMethod("setIntakeRunSpeed", Double.class);
      Method intakeReverseSpeedSetter = RobotContainer.class.getMethod("setIntakeReverseSpeed", Double.class);
      Method maxDriveInputSetter = DriveSystem.class.getMethod("setMaxInput", Double.class);
      Method autoRotateSpeedSetter = RobotContainer.class.getMethod("setAutoRotateSpeed", Double.class);

      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, lowerKickerRunSpeedSetter, "Control Variables", "lowerKickerRunSpeed", lowerKickerRunSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, lowerKickerReverseSpeedSetter, "Control Variables", "lowerKickerReverseSpeed", lowerKickerReverseSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, upperKickerRunSpeedSetter, "Control Variables", "upperKickerRunSpeed", upperKickerRunSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, upperKickerReverseSpeedSetter, "Control Variables", "upperKickerReverseSpeed", upperKickerReverseSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, shooterRunSpeedSetter, "Control Variables", "shooterRunSpeed", shooterRunSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, shooterReverseSpeedSetter, "Control Variables", "shooterReverseSpeed", shooterReverseSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, intakeRunSpeedSetter, "Control Variables", "intakeRunSpeed", intakeRunSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, intakeReverseSpeedSetter, "Control Variables", "intakeReverseSpeed", intakeReverseSpeed);
      RobotUtils.sendNumberSetterToShuffleboard(m_driveSystem, maxDriveInputSetter, "Control Variables", "maxDriveInput", Constants.DriveConstants.INITIAL_MAX_INPUT);
      RobotUtils.sendNumberSetterToShuffleboard(robotContainer, autoRotateSpeedSetter, "Control Variables", "autoRotateSpeed", autoRotateSpeed);
    } catch (NoSuchMethodException | SecurityException e) {
      SmartDashboard.putString("depressing_error", e.toString());
    }
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //buttons
    //lower kicker reverse (A)
    new JoystickButton(controller, Button.kA.value)
      .whileHeld(() -> shooterSystem.LowerKickerReverse(lowerKickerReverseSpeed))
      .whenReleased(() -> shooterSystem.LowerKickerStop());
    //all kicker reverse (B)
    new JoystickButton(controller, Button.kB.value) 
      .whileHeld(() -> new AllKickerReverse(upperKickerReverseSpeed, lowerKickerReverseSpeed, shooterSystem).schedule())
      .whenReleased(() -> new AllKickerStop(shooterSystem).schedule());
    //shooter reverse (X)
    new JoystickButton(controller, Button.kX.value)
      .whileHeld(() -> shooterSystem.ShooterReverse(shooterReverseSpeed))
      .whenReleased(() -> shooterSystem.ShooterStop());
    //shooter (Y)
    new JoystickButton(controller, Button.kY.value)
      .whileHeld(() -> shooterSystem.ShooterRunVoltage(6))
      .whenReleased(() -> shooterSystem.ShooterStop());

    //bumpers
    //intake reverse (LB)
    new JoystickButton(controller, Button.kLeftBumper.value)
      .whileHeld(() -> intakeSystem.IntakeReverse(intakeReverseSpeed))
      .whenReleased(() -> intakeSystem.IntakeStop());

  }

  public XboxController getXboxController() {
    return controller;
  }

  public DriveSystem getDriveSystem() {
    return m_driveSystem;
  }

  public void periodic() {
    //triggers
    //intake and kickers (LT)
    if(controller.getLeftTriggerAxis() == 1) {
      intakeSystem.IntakeRun(intakeRunSpeed);
      shooterSystem.UpperKickerRun(upperKickerRunSpeed);
      shooterSystem.LowerKickerReverse(lowerKickerReverseSpeed);

    }
    else if (oldLeftTriggerAxis == 1) {
      intakeSystem.IntakeStop();
      shooterSystem.UpperKickerStop();
      shooterSystem.LowerKickerStop();

    }
    //auto shoot (RT)
    if(controller.getRightTriggerAxis() == 1)
      new TeleOpShoot(4000, lowerKickerRunSpeed, upperKickerRunSpeed, shooterSystem).schedule();
    else if (oldRightTriggerAxis == 1) {
      shooterSystem.ShooterStop();
      shooterSystem.LowerKickerStop();
      shooterSystem.UpperKickerStop();

    }

    //d-pad
    //climber runner up (Up)
    if (controller.getPOV() == 0)
      climberSystem.ClimberRunnerUp(climberRunnerRunSpeed);
    //climber runner down (Down)
    else if (controller.getPOV() == 180)
      climberSystem.ClimberRunnerDown(climberRunnerReverseSpeed);
    //climber deployer up (Left)
    else if (controller.getPOV() == 270)
      climberSystem.ClimberDeployerUp(climberDeployerRunSpeed);
    //climber deployer down (Right)
    else if (controller.getPOV() == 90)
      climberSystem.ClimberDeployerDown(climberDeployerReverseSpeed);
    else if (controller.getPOV() < 0 && oldPOV >= 0) {
      climberSystem.ClimberStop();
      climberSystem.ClimberDeployerStop();

    }

    //sticks
    //reverse drive (RS)
    if (!oldRightStickButton && controller.getRightStickButton())
      m_driveSystem.switchMotorPorts();
    
    //old inputs
    oldLeftTriggerAxis = (int)controller.getLeftTriggerAxis();
    oldRightTriggerAxis = (int)controller.getRightTriggerAxis();
    oldRightStickButton = controller.getRightStickButton();
    oldPOV = ((int)controller.getPOV());

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    positioningSystem.zeroPosition();
    //return new MoveToPosition(m_driveSystem, positioningSystem, new double[] {initialPos[0]+12,initialPos[1]+12});
    return new SequentialCommandGroup(
      new MoveToPosition(m_driveSystem, positioningSystem, new double[] {-36,0}),
      new MoveToPosition(m_driveSystem, positioningSystem, new double[] {12,0}));
  }

  /**
   * Setters
   */

  public void setLowerKickerRunSpeed(Double speed) {
    lowerKickerRunSpeed = speed;
  }

  public void setLowerKickerReverseSpeed(Double speed) {
    lowerKickerReverseSpeed = speed;
  }

  public void setUpperKickerRunSpeed(Double speed) {
    upperKickerRunSpeed = speed;
  }

  public void setUpperKickerReverseSpeed(Double speed) {
    upperKickerReverseSpeed = speed;
  }

  public void setShooterRunSpeed(Double speed) {
    shooterRunSpeed = speed;
  }

  public void setShooterReverseSpeed(Double speed) {
    shooterReverseSpeed = speed;
  }

  public void setIntakeRunSpeed(Double speed) {
    intakeRunSpeed = speed;
  }

  public void setIntakeReverseSpeed(Double speed) {
    intakeReverseSpeed = speed;
  }

  public void setAutoRotateSpeed(Double speed) {
    autoRotateSpeed = speed;
  }
}
