## <font color="yellow">Simi Init</font>
Init project files by the default config file: .smp/smp.yml.<br/>

Prerequisite:<br/>
1. Create the configuration file in the user directory: ~/.smp/simi-init.yml<br/>
<font color="gray">Tips: The user configuration directory for Windows is "C:\\Users\\\<username>\\\.smp"</font><br/>
2. Add a writing rule to the file ~/.smp/simi-init.yml.<br/>
   <font color="gray">Tips: For the first usage, you can try adding only one rule in the ruleList, and then executing the plugin.</font><br/>
   <font color="gray">Try the simplest java-annotation type to add "//" before each line in your read file.</font><br/>
3. Execute the plugin in the intellij idea menu: <em>Tools / Smp Init</em>, and then you can see the execution result prompt in the lower right corner. 
#### write types:
<table>
    <tr>
        <th>TYPE</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>append-properties-folder </td>   
        <td>Read all properties files in the directory and append them to the writing properties file.</td>
    </tr>
    <tr>
        <td>append-properties</td>   
        <td>Append a read properties file to the writing properties file</td>
    </tr>
    <tr>
        <td>replace-all</td>   
        <td>Replace all content of the writing file with the read file.</td>
    </tr>
    <tr>
        <td>replace-string</td>
        <td>
            Replace matched content with the RP rule list manually defined or rp rule file, <br/>
            Please refer to the introduction of the RP file rule format below.
        </td>
    </tr>
    <tr>
        <td>append-string</td>
        <td>Append all contents of the read file to the write file.</td>
    </tr>
    <tr>
        <td>line-replace</td>
        <td>
            Replace string with the RP file, The left value is the lineNumber,<br/> 
            The right value is the replace content.<br/>
            example: 2%%%String tt="xxx";
        </td>
    </tr>
    <tr>
        <td>line-append</td>
        <td>
            Append string with the RP file, The left value is the lineNumber,<br/> 
            The right value is the append content.<br/>
            example: 2%%%Integer tt=99;
        </td>
    </tr>
    <tr>
        <td>java-annotation</td>
        <td>Each line of the write file will be preceded by '//'</td>
    </tr>
    <tr>
        <td>xml</td>
        <td>Write xml file, Please refer to the introduction of the xml-append.xml file format below.</td>
    </tr>
</table>

#### global env:
You can access the env variable in the <strong>simi-init.yml</strong> file,<br/> 
or in the <strong>xxx.rp</strong> file of the rp rules and the <strong>xxx.xml</strong> file of xml-append rule referenced in the simi-init.yml<br/>
<table>
    <tr>
        <th>ENV</th>
        <th>description</th>
        <th>example</th>
    </tr>
    <tr>
        <td>${project.name}</td>   
        <td>The project name of the current project.</td>
        <td>simi-oracle</td>
    </tr>
    <tr>
        <td>${project.path}</td>   
        <td>The project path of the current project.</td>
        <td>C:\\Users\\&lt;username&gt;\\Desktop\\simi\\simi-service\\simi-oracle</td>
    </tr>
    <tr>
        <td>${project.env} </td>   
        <td>The ENV of the current project.</td>
        <td>UAT</td>
    </tr>
    <tr>
        <td>${smp}         </td>
        <td>The "~/.smp" configuration path.</td>
        <td>C:\\Users\\&lt;username&gt;\\.smp</td>
    </tr>
</table>

#### simi-init.yml example:
```yaml
project:
  - name: simi-oracle
    enable: true
    path: C:\\Users\\saidake\\Desktop\\DevProject\\simi\\simi-service\\simi-oracle   # Parent project folder
    envList: UAT,DEV,PROD
    defaultEnv: UAT
    pomProjectNameCheck: true         # when executing this path,  check whether Maven project name is project name based on the pom file.
                                      # At this point, then project.path is optional.
    ruleList:
      - write: src/main/resources/application-local.properties    # The relative path to write the file.
        read: /${project.name}/${project.env}                     # Read folder.
         # When the path starts with "/", automatically concatenate the configuration path "~/.smp"
         # Tips: The user configuration directory for Windows is "C:\Users\<username>\.smp"
        type: append-properties-folder                            
        backup: current   # Create a backup file in the current file directory.(The default backup value is "current")
        once: true        # Only write once, It will determine whether it is the first write based on whether the backup file exists.

      - write: src/main/resources/application-dev.properties     # The relative path to write the file.
        read: /${project.name}/test.properties                   # Read property file.
        type: append-properties
        backup: smp             # Create a backup file in the default smp backup folder.(~/.smp/AAAbackup)                                  
        activeEnvList: DEV,UAT             # It takes effect in the DEV and UAT environment and defaults to all environments.

      - write: src/main/resources/logback.xml
        read: /${project.name}/logback.xml
        type: replace-all                                         

      - write: src/main/resources/logback.xml       # The same file can be written multiple times.
        type: replace-string                                      
        rpRuleList:                                 # Use rpRuleList instead of rp file.(it is valid anywhere an RP file is used)
          - fffsfsfd/////%%%ddfsfsfsfsfs
          - fffsfsfd/////%%%ddfsfsfsfsfs

      - write: src/main/resources/logback.xml
        type: replace-string
        read: /${project.name}/logback-replace.rp   # Use the rp rule file instead manually setting one.
        
      - write: src/main/resources/logback.xml
        read: /${project.name}/logback.txt
        type: append-string                                       

      - write: src/main/resources/logback.xml
        read: /${project.name}/logback.rp
        type: line-replace                                        

      - write: src/main/resources/logback.xml
        read: /${project.name}/logback.rp
        type: line-append                                         

      - write: src\main\java\com\saidake\common\core\util\file\SmpTestBackupUtils.java
        type: java-annotation                                     

      - write: src/test/resources/simi-test/pom.xml
        read: /${project.name}/xml-append.xml
        type: xml                                                 

  - name: simi-common-core
    path: C:\Users\saidake\Desktop\DevProject\simi\simi-common\simi-common-core
    envList: UAT,DEV,PROD
    defaultEnv: UAT
    ruleList:
      - write: src\main\java\com\saidake\common\core\util\file\SmpFileBackupUtils.java
        type: java-annotation
        once: true
```

#### RP rul file(.rp) example: 
Key values are separated by '%%%'<br/>
```text
<contextName>logback</contextName>%%%<contextName>logback-replace-content</contextName>
sourceValue%%%ReplaceValue
//source//abc.cert%%%${smp}/abc.cert
```

#### XML rule file(.xml) example: 
```xml
<root>
    <replace xpath="/project/dependencyManagement/dependencies/dependency">    
        <!-- The xpath of the replace tag-->
        <ele xpath="artifactId" xpath-value="maven-compiler-plugin" append-if-not-exists="true"
        custom1="xxx" custom2="xxx" 
        >  
            <!-- Use the artifactId xpath search under dependency, and if the value is equal to 'maven-compiler-plugin', replace it.-->
            <!-- In addition to these three setting attributes, other attributes will be added to the discovered replacement element. -->
            <!-- The attributes custom1, custom2 will be added to the element "dependency"-->
            <dependency>
                <groupId>org.saidake.mmp</groupId>
                <artifactId>smp</artifactId>
                <version>${lombok.version}</version>
                <exclusions>
                    <exclusion>
                        <groupId>org.saidake.mmp</groupId>
                        <artifactId>cc</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
        </ele>
    </replace>
    <append parent-xpath="/project/dependencies">   
        <!-- The xpath of the parent tag of the replace tag-->
        <dependency>
            <groupId>org.saidake.mmp</groupId>
            <artifactId>smp</artifactId>
            <version>baba</version>
        </dependency>
    </append>
</root>
```