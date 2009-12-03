Module description:
The util module consists of a set of utility class used by other modules 
in the ddieditor framework. 

Legal: 
Copyright 2009 Danish Data Archive (http://www.dda.dk) 
 
For all files within util: 

Util is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Util is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser Public License for more details.

You should have received a copy of the GNU Lesser Public License
along with Util. If not, see <http://www.gnu.org/licenses/>.
    
Module Version:
Current module version is given by the property 'xmljar.version' of the
'build.properties' property file.

Building:
Setup:
The project 'lib-java' holding the XmlBeans jars needs to be checked out from
source version control as weel at: ../[project_home]

Build:
    [util_home] ant compile jar
This generates a jar file in the [lib-java_home]/build directory

Change log:
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