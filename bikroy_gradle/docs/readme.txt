


== Description ==

This framework is a light weight hybrid application which enables efficient design and development of automated test 
scripts and reliable analysis of issues for the system under test. This framework is designed as a conceptual structure 
created to provide support or guidance to each and every software entity that could expand in future. 


== What Is New ==

Common practices say in general that; test scripts interact with framework component. This component interacts with
specific tool component(selenium here). This tool further establishes communication with Browser(Web App). 
This bikroy framework implements an additional layer between tool and framework component to abstract away particular
automation tool. This is a successful attempt to support bikroy framework with Tool Independent Capabilities.
For architectural analysis, please refer to the attached class diagram(/bikroy_gradle/docs/classdiagrams/Framework_ClassDiagram.png)
and have a look at following entities: 

-- SeleniumBrowser class
-- IBrowser interface
-- Element Field class
-- @LocateBy custom annotation


== After Test Produced Deliverables == 

-- Logs (/bikroy_gradle/logs/AUTO_EXEC_LOG.log)
-- Screenshots(/bikroy_gradle/results/screenshots)
-- Test Report(/bikroy_gradle/build/reports/tests/index.html)


== Requirements ==

-- Install and add following plug-in to eclipse IDE to automate gradle build synchronization at initial local setup:
http://download.eclipse.org/buildship/updates/e43/releases/1.0

-- Change project and jre compliance to (1.7) if you see errors in your gradle project at initial setup


== Command Line Execution ==

-- Navigate to project root folder
-- Enter command: gradle clean test -Pbikroy=.xml File Name
-- Example-1: gradle clean test -Pbikroy=debug
-- Example-2: gradle clean test -Pbikroy=ValidateFlowForAdPostFunctionality




@author-Aishwarya D.

 




