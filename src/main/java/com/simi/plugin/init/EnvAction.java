package com.simi.plugin.init;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.simi.common.util.file.SimiInitializer;
import com.simi.common.util.file.support.InitException;
import com.simi.common.util.file.support.yaml.SimiYmlProperties;
import com.simi.plugin.init.core.SimiIntellijUtils;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class EnvAction extends AnAction {
    private SimiYmlProperties simiYmlProperties;
    private String env;
    private String projectName;

    public EnvAction(@Nullable String text,
                     @Nullable  String description,
                     @Nullable Icon icon,
                     SimiYmlProperties simiYmlProperties,
                     String env,
                     String projectName) {
        super(text,description,icon);
        this.simiYmlProperties=simiYmlProperties;
        this.env=env;
        this.projectName=projectName;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            Map<String, Set<String>> init = SimiInitializer.init(this.simiYmlProperties, this.projectName, this.env, () -> {
                    try {
                        return SimiIntellijUtils.getPomProjectName(e.getProject(), projectName);
                    } catch (Exception ex) {
                        throw new InitException(ex.getMessage());
                    }
            });
            StringBuilder stringBuilder=new StringBuilder();
            init.forEach((key,val)->{
                stringBuilder.append("<span style=\"color:#4fc3f7;\">").append(key).append(" :").append(
                        val.isEmpty()?" empty":""
                ).append("</span>").append("<br/>");
                for (String fileName : val) {
                    stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<span style=\"color:#82aaff;\">").append(fileName).append("</span><br/>");
                }
            });
            NotificationGroupManager.getInstance().getNotificationGroup("Simi Notification")
                    .createNotification(stringBuilder.toString(), NotificationType.INFORMATION)
                    .setTitle("Init project success")
                    .notify(e.getProject());
        } catch (Exception ex) {
            NotificationGroupManager.getInstance().getNotificationGroup("Simi Notification")
                    .createNotification(ex.getMessage(), NotificationType.ERROR)
                    .setTitle("Init project failed")
                    .notify(e.getProject());
        }
    }
}
