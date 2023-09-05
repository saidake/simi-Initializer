package com.simi.plugin.init.core;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.simi.common.util.file.SmpXmlUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@UtilityClass
@Slf4j
public class SmpIntellijUtils {

    /**
     * Get current project root path.
     *
     * @param project intellij project
     * @param projectName project name
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Optional<String> getPomProjectName(Project project, String projectName) throws IOException, DocumentException {
        VirtualFile virtualFile = ProjectUtil.guessProjectDir(project);
        return Optional.ofNullable(deepCheckDirectory(true,projectName, virtualFile));
    }

    private static String deepCheckDirectory(boolean hasUpperPom,String projectName, VirtualFile virtualFile) throws DocumentException, IOException {
        if(!hasUpperPom)return null;
        if(virtualFile ==null|| virtualFile.getChildren()==null) return null;
        boolean currentPom= Arrays.stream(virtualFile.getChildren()).anyMatch(item->"pom.xml".equals(item.getName()));
        for (VirtualFile child : virtualFile.getChildren()) {
            if(child.isDirectory()){
                String getReturn = deepCheckDirectory(currentPom, projectName, child);
                if(getReturn!=null)return getReturn;
            } else if(child.getName().equals("pom.xml")){
                SAXReader saxReader=new SAXReader();
                Document readPomDocument = saxReader.read(child.getInputStream());
                Optional<HashMap<String, String>> pomNameSpaceMap = SmpXmlUtils.createNamespaceMap(readPomDocument);
                Node node = SmpXmlUtils.selectSingleNodeListWithNSXpath(pomNameSpaceMap.orElse(null), readPomDocument, "/project/artifactId");
                String text = node.getText();
                log.info("check pom project: {}",text);
                if(projectName.equalsIgnoreCase(text)){
                    log.info("valid pom project: {}, path: {}",text,virtualFile.getPath());
                    return virtualFile.getPath();
                }
            }
        }
        return null;
    }
}
