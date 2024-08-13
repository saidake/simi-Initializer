## <font color="yellow">Simi Initializer</font>
Init project files by the default config file: .simi/simi-initializer.yml.<br/>

Prerequisites:<br/>
1. Create the configuration file in the user directory: ~/.simi/simi-initializer.yml<br/>
<font color="gray">Tips: The user configuration directory for Windows is "C:\\Users\\\<username>\\\.simi"</font><br/>
2. Add a write rule to the file ~/.simi/simi-initializer.yml.<br/>
   <font color="gray">Tips: For the first usage, you can try adding only one rule in the ruleList, and then executing the plugin.</font><br/>
   <font color="gray">Try the simplest java-annotation type to add "//" before each line in your read file.</font><br/>
3. Execute the plugin in the intellij idea menu: <em>Tools / Simi Initializer</em>, and then you can see the execution result prompt in the lower right corner. 
#### write types:
<table>
    <tr>
        <th>TYPE</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>append-properties-folder </td>   
        <td>Read all properties files in the directory and append them to the write properties file.</td>
    </tr>
    <tr>
        <td>append-properties</td>   
        <td>Append a read properties file to the write properties file</td>
    </tr>
    <tr>
        <td>replace-all</td>   
        <td>Replace all content of the write file with the read file.</td>
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
            Replace string with the RP file,  <br/>
            Please refer to the introduction of the RP file rule format below. <br/>
            example: 2%%%String tt="xxx";
        </td>
    </tr>
    <tr>
        <td>line-append</td>
        <td>
            Append string with the RP file,  <br/>
            Please refer to the introduction of the RP file rule format below. <br/>
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
You can access the env variables in the <strong>simi-initializer.yml</strong> file,<br/> 
or in the <strong>xxx.rp</strong> file of the rp rules and the <strong>xxx.xml</strong> file of xml-append rule referenced in the simi-initializer.yml<br/>
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
        <td>${simi}         </td>
        <td>The "~/.simi" configuration path.</td>
        <td>C:\\Users\\&lt;username&gt;\\.simi</td>
    </tr>
</table>

#### simi-initializer.yml example:
```yaml
project:
  - name: simi-oracle
    enable: true
    # Parent project folder
    path: C:\\Users\\saidake\\Desktop\\DevProject\\simi\\simi-service\\simi-oracle   
    envList: UAT,DEV,PROD
    defaultEnv: UAT
     # when executing this path, 
     # check whether Maven project name is project name based on the pom file (Optional).
    pomProjectNameCheck: true        
    ruleList:
      # Read all properties files in the directory and append them to the write properties file.  
      - type: append-properties-folder
        # The relative path to write the file.
        write: src/main/resources/application-local.properties
        # Read folder.
        # When the path starts with "/", automatically concatenate the configuration path "~/.simi"
        # Tips: The user configuration directory for Windows is "C:\Users\<username>\.simi"
        read: /${project.name}/${project.env}                     
        # Create a backup file in the current file directory.(The default backup value is "current")
        backup: current
        # Only write once, 
        # It will determine whether it is the first write based on whether the backup file exists.
        once: true        

      # Append a read properties file to the write properties file 
      - type: append-properties
        # The relative path to write the file.
        write: src/main/resources/application-dev.properties
        # Read property file.
        read: /${project.name}/test.properties                   
        # Create a backup file in the default simi backup folder.(~/.simi/AAAbackup)   
        backup: simi
        # It takes effect in the DEV and UAT environment and defaults to all environments.
        activeEnvList: DEV,UAT             
        
      # Replace all content of the write file with the read file.
      - type: replace-all 
        write: src/main/resources/logback.xml
        read: /${project.name}/logback.xml
                                                

      # Replace matched content with the RP rule list manually defined or rp rule file, 
      # Please refer to the introduction of the RP file rule format below.
      - type: replace-string
        # The same file can be written multiple times.
        write: src/main/resources/logback.xml
        # Use rpRuleList instead of rp file.(it is valid anywhere an RP file is used)
        rpRuleList:                                 
          - fffsfsfd/////%%%ddfsfsfsfsfs
          - fffsfsfd/////%%%ddfsfsfsfsfs

      - type: replace-string
        write: src/main/resources/logback.xml
         # Use the rp rule file instead manually setting one.
        read: /${project.name}/logback-replace.rp   
        
      # Append all contents of the read file to the write file.
      - type: append-string  
        write: src/main/resources/logback.xml
        read: /${project.name}/logback.txt
      #  Replace string with the RP file
       # Please refer to the introduction of the RP file rule format below.
      - type: line-replace
        write: src/main/resources/logback.xml
        read: /${project.name}/logback.rp
                                                
      # Append string with the RP file, 
      # Please refer to the introduction of the RP file rule format below.
      - type: line-append
        write: src/main/resources/logback.xml
        read: /${project.name}/logback.rp
                                                 
      # Each line of the write file will be preceded by '//'
      - type: java-annotation
        write: src\main\java\com\saidake\common\core\util\file\SmpTestBackupUtils.java
      # Write xml file, Please refer to the introduction of the xml-append.xml file format below.
      - type: xml  
        write: src/test/resources/simi-test/pom.xml
        read: /${project.name}/xml-append.xml
                                                       
  - name: simi-common-core
    path: C:\Users\saidake\Desktop\DevProject\simi\simi-common\simi-common-core
    envList: UAT,DEV,PROD
    defaultEnv: UAT
    ruleList:
      - write: src\main\java\com\saidake\common\core\util\file\SmpFileBackupUtils.java
        type: java-annotation
        once: true
```

#### RP rule file(.rp) example: 
Key values are separated by '%%%'<br/>
```text
<contextName>logback</contextName>%%%<contextName>logback-replace-content</contextName>
sourceValue%%%ReplaceValue
//source//abc.cert%%%${simi}/abc.cert
```

#### XML rule file(.xml) example: 
```xml
<root>
    <replace xpath="/project/dependencyManagement/dependencies/dependency">    
        <!-- 
          xpath:  The xpath of the "replace" tag
        -->
        <ele 
                xpath="artifactId" 
                xpath-value="maven-compiler-plugin" 
                append-if-not-exists="true" 
                custom1="xxx"  
                custom2="xxx" 
        > 
            <!-- 
              xpath:                  tag name to use for matching 
              xpath-value:            matched tag value 
                  (and if the value is equal to 'maven-compiler-plugin', replace whole dependency tag.)
              append-if-not-exists:   Change replace to append
              custom1 / custom2...:   Other attributes will be appended to the tag matching the xpath of the "replace" tag
            -->
            <dependency>
                <groupId>org.saidake.simi</groupId>
                <artifactId>simi</artifactId>
                <version>1.0</version>
            </dependency>
        </ele>
    </replace>
    <append 
            parent-xpath="/project/dependencies" 
            position="top"
    >   
        <!-- 
          parent-xpath: The xpath of the parent tag.
          position:     The position to be appended.
        -->
       <dependency>
          <groupId>org.saidake.simi</groupId>
          <artifactId>simi</artifactId>
          <version>1.0</version>
       </dependency>
    </append>
</root>
```