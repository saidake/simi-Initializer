package com.simi.plugin.init;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.simi.common.util.file.SimiInitializer;
import com.simi.common.util.file.support.yaml.SimiYmlProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class InitGroup extends DefaultActionGroup {

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        try {
            ArrayList<EnvAction> envActions = new ArrayList<>();
            SimiYmlProperties simiYmlProperties = SimiInitializer.readYmlProperties();
            SimiInitializer.renderToolAction(simiYmlProperties,(projectInfo, currentEnv) -> {
                EnvAction envAction = new EnvAction( projectInfo.getName()+ " - "+currentEnv,null, null,
                        simiYmlProperties, currentEnv, projectInfo.getName());
                envActions.add(envAction);
            });
            return envActions.toArray(new EnvAction[envActions.size()]);
        } catch (Exception ex) {
            NotificationGroupManager.getInstance().getNotificationGroup("Smp Notification")
                    .createNotification(ex.getMessage(), NotificationType.ERROR)
                    .setTitle("Init project failed")
                    .notify(e.getProject());
            return new AnAction[0];
        }
    }
}
