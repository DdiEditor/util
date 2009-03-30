Util
A set of utility class to be used by other projects

Building:
Setup:
    Edit the [util_home]/build.properties for location of ddi xmlbeans jar
Build:
    [util_home] ant compile jar
This generates a jar file in the [lib-java_home]/build directory

Change log
Version 0.5
 - Added file utils
 
Version 0.5
 - Updated to use ddi-3.0-20080428
 - Added support for xml line number identification on xml objects on XmlBeansUtil
 - Added support for NCube in parser
 - Added centralized configuration 

Version 0.4
Added:
 - Support for ResourcePackets
 - Added GroupParserDDI3 to extract derived study units

Version 0.3
Added:
 - Support for multiple message files for i18n
 - DDI3 parser
 
Version 0.2
Added: 
 - I18n and l10n added, See Translator
 - DDIFtpException added
 - UrnUtil added, generate/ retrieve for XML objects 
 - UrnUtilDDI3, DDI3 specific URN utility implementation
 - ReflectionUtil added method for static method invocation