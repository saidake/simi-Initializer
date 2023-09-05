package com.simi.plugin.init;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.simi.common.util.file.SmpInit;
import com.simi.common.util.file.support.yaml.ProjectInfo;
import com.simi.common.util.file.support.yaml.SmpYmlProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class InitGroup extends DefaultActionGroup {

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        try {
            SmpYmlProperties smpYmlProperties = SmpInit.readYmlProperties();
            int collect = (int)smpYmlProperties.getProject().stream().mapToLong(projectInfo -> projectInfo.getEnvList().size()).sum();
            EnvAction[] envActionList=new EnvAction[collect];
            int envActionIndex=0;
            for (int i = 0; i < smpYmlProperties.getProject().size(); i++) {
                ProjectInfo projectInfo = smpYmlProperties.getProject().get(i);
                Iterator<String> iterator = projectInfo.getEnvList().iterator();
                for (int j = 0; iterator.hasNext(); j++) {
                    String currentEnv = iterator.next();
                    EnvAction envAction = new EnvAction( projectInfo.getName()+ " - "+currentEnv,null, null,
                            currentEnv, projectInfo.getName(),projectInfo.getPomProjectNameCheck());
                    envActionList[envActionIndex]=envAction;
                    envActionIndex++;
                }
            }
            return envActionList;
        } catch (Exception ex) {
            NotificationGroupManager.getInstance().getNotificationGroup("Smp Notification")
                    .createNotification(ex.getMessage(), NotificationType.ERROR)
                    .setTitle("Init project failed")
                    .notify(e.getProject());
            return new AnAction[0];
        }
    }
}
