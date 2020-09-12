# define installer name
Name "Novel Insight Hypercube VST"
OutFile "Novel Insight Hypercube VST Setup.exe"

Unicode True
RequestExecutionLevel highest

# set desktop as install directory
InstallDir "$PROGRAMFILES64\Novel Insight Hypercube VST"
 
# default section start
Section

MessageBox MB_YESNO "Do you want to install Novel Insight Hypercube VST?" IDYES continue IDNO abort_installer
abort_installer:
Quit
continue:
 
# define output path
CreateDirectory "$PROGRAMFILES64\Novel Insight Hypercube VST"
SetOutPath $INSTDIR
 
# specify file to go in output path
File *.dll
File ni-hypercube.exe
File *.jar
File launch4j.*
File *.ico
File cube-icon-small.jpg
File LICENSES.txt
File /r help

CreateShortCut "$DESKTOP\NI Hypercube VST.lnk" "$INSTDIR\ni-hypercube.exe"

 
# define uninstaller name
WriteUninstaller $INSTDIR\uninstaller.exe

# write uninstall information to windows registery

WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Novel Insight Hypercube VST" \
                 "DisplayName" "Novel Insight Hypercube VST"
WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Novel Insight Hypercube VST" \
                 "UninstallString" "$\"$INSTDIR\uninstaller.exe$\""
WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Novel Insight Hypercube VST" \
                 "QuietUninstallString" "$\"$INSTDIR\uninstaller.exe$\" /S"
WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Novel Insight Hypercube VST" \
                 "Publisher" "Novel Insight"


# set PATH
## EnVar::SetHKLM ; if SetHKLM is set attempt to modify PATH just hangs..
DetailPrint "Updating PATH enviroment variable.."
EnVar::AddValue    "Path" "$INSTDIR"

 
#-------
# default section end
SectionEnd
 
# create a section to define what the uninstaller does.
# the section will always be named "Uninstall"
Section "Uninstall"
 
# Always delete uninstaller first
Delete $INSTDIR\uninstaller.exe
 
# now delete installed files
Delete $INSTDIR\*.dll
Delete $INSTDIR\ni-hypercube.exe
Delete $INSTDIR\*.jar
Delete $INSTDIR\launch4j.*
Delete $INSTDIR\*.ico
Delete $INSTDIR\cube-icon-small.jpg
Delete $INSTDIR\LICENSES.txt
RMDir /r $INSTDIR\help
RMDir $INSTDIR
Delete "$DESKTOP\NI Hypercube VST.lnk"

DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Novel Insight Hypercube VST"

DetailPrint "Removing environment variables.."
## EnVar::SetHKLM ; if SetHKLM is set EnVar call to modify Path just hangs..
EnVar::DeleteValue    "Path" "$INSTDIR"

SectionEnd
