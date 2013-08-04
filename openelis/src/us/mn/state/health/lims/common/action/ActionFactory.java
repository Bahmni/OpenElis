package us.mn.state.health.lims.common.action;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActionFactory {
    public static BaseAction newAction(BaseAction baseAction) {
        try {
            Class<? extends BaseAction> aClass = baseAction.getClass();
            Constructor<? extends BaseAction> constructor = aClass.getConstructor();
            BaseAction newBaseAction = constructor.newInstance();
            newBaseAction.setState(baseAction);
            return newBaseAction;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new LIMSRuntimeException(e);
        }
    }
}