LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := game_shared
LOCAL_MODULE_FILENAME := libgame

LOCAL_SRC_FILES := hellocpp/main.cpp \
				../../Classes/Model/Item.cpp \
				../../Classes/Model/Missile.cpp \
				../../Classes/Model/Strategy.cpp \
				../../Classes/View/CustomClasses/CCGestureRecognizer/CCGestureRecognizer.cpp \
				../../Classes/View/CustomClasses/CCGestureRecognizer/CCTapGestureRecognizer.cpp \
				../../Classes/View/CustomClasses/CCGestureRecognizer/MyCCSwipeGestureRecognizer.cpp \
				../../Classes/View/Gestion/AppDelegate.cpp \
				../../Classes/View/Gestion/GestionScene.cpp \
				../../Classes/View/SceneClose/CloseLayer.cpp \
				../../Classes/View/SceneEndGame/EndGameLayer.cpp \
				../../Classes/View/SceneGame/BackgroundLayer.cpp \
				../../Classes/View/SceneGame/Couche1Layer.cpp \
				../../Classes/View/SceneGame/Couche2Layer.cpp \
				../../Classes/View/SceneGame/MobileTouchGestion.cpp \
				../../Classes/View/SceneIntro/IntroLayer.cpp \
				../../Classes/View/SceneMenu/MenuLayer.cpp \
				../../Classes/View/ScenePause/PauseLayer.cpp \

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../Classes
LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static cocosdenshion_static cocos_extension_static

include $(BUILD_SHARED_LIBRARY)

$(call import-module,CocosDenshion/android) \
$(call import-module,cocos2dx) \
$(call import-module,extensions)
