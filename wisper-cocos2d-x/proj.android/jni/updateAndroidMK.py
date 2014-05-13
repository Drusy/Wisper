#!/usr/bin/python
import os

def listClasses():
	cpps = []
	paths = []
	for root, dirs, files in os.walk("../../Classes"):
		for f in files:
			if not f.endswith('.cpp'): continue
			if "Unused" in root: continue
			full = root.replace("\\", "/") + "/" + f
			cpps.append(full)

	return sorted(cpps)


if __name__ == '__main__':
	f = open('Android.mk','w')
	f.write("LOCAL_PATH := $(call my-dir)\n\n")
	f.write("include $(CLEAR_VARS)\n\n")
	f.write("LOCAL_MODULE := game_shared\n")
	f.write("LOCAL_MODULE_FILENAME := libgame\n\n")

	f.write("LOCAL_SRC_FILES := hellocpp/main.cpp \\\n")
	for line in listClasses():
		f.write("\t\t\t\t" + line + " \\\n")
                   
	f.write("\nLOCAL_C_INCLUDES := $(LOCAL_PATH)/../../Classes\n")
	f.write("LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static cocosdenshion_static cocos_extension_static\n\n")
	f.write("include $(BUILD_SHARED_LIBRARY)\n\n")
	f.write("$(call import-module,CocosDenshion/android) \\\n")
	f.write("$(call import-module,cocos2dx) \\\n")
	f.write("$(call import-module,extensions)\n")
	f.close() 