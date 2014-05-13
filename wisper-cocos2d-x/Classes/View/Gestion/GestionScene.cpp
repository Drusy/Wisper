#include "GestionScene.h"
#include "View/CustomClasses/CCGestureRecognizer/CCTapGestureRecognizer.h"
#include "View/CustomClasses/CCGestureRecognizer/MyCCSwipeGestureRecognizer.h"
/// Intro Layers
#include "View/SceneIntro/IntroLayer.h"
/// Menu Layers
#include "View/SceneMenu/MenuLayer.h"
/// Game Layers
#include "View/SceneGame/MobileTouchGestion.h"
#include "View/SceneGame/BackgroundLayer.h"
#include "View/SceneGame/couche2Layer.h"
/// Pause Layers
#include "View/ScenePause/PauseLayer.h"
/// EndGame Layers
#include "View/SceneEndGame/EndGameLayer.h"
/// Close Layers
#include "View/SceneClose/CloseLayer.h"

using namespace cocos2d;

GestionScene* GestionScene::_Instance = new GestionScene();

GestionScene::GestionScene()
{
	_currentScene = CCScene::create();
	_indexScene = 1;
	_yAcc = 0;
	_xAcc = 0;
	_currentSwipe = 0;
}

GestionScene* GestionScene::GetInstance()
{
	return _Instance;
}

CCScene* GestionScene::createIntroScene()
{
    /// Création de la scene.
    CCScene *scene = CCScene::create();
    
    /// Création des calques de la scene.
	IntroLayer *introLayer = IntroLayer::create();

	/// Création du label avec le nom de la scene en bas à gauche pour le débogage.
	CCLayer *debugLayer = CCLayer::create();
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCLabelTTF* labelName = CCLabelTTF::create("IntroScene", "Arial", 30 * ((int)winSize.height/720.0f));
	labelName->setPosition(ccp(winSize.width*0.9f, winSize.height*0.05f));
	debugLayer->addChild(labelName);

    /// Ajout des calque à la scene.
	scene->addChild(introLayer, 0);
    scene->addChild(debugLayer, 20);

    /// Retourne la scene.
    return scene;
}

CCScene* GestionScene::createMenuScene()
{
    /// Création de la scene.
    CCScene *scene = CCScene::create();
    
    /// Création des calques de la scene.
	MenuLayer *menuLayer = MenuLayer::create();

	/// Création du label avec le nom de la scene en bas à gauche pour le débogage.
	CCLayer *debugLayer = CCLayer::create();
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCLabelTTF* labelName = CCLabelTTF::create("MenuScene", "Arial", 30 * ((int)winSize.height/720.0f));
	labelName->setPosition(ccp(winSize.width*0.9f, winSize.height*0.05f));
	debugLayer->addChild(labelName);

    /// Ajout des calque à la scene.
	scene->addChild(menuLayer, 0);
    scene->addChild(debugLayer, 20);

    /// Retourne la scene.
    return scene;
}

CCScene* GestionScene::createGameScene()
{
    /// Création de la scene.
	MobileTouchGestion *touchGestion = MobileTouchGestion::create();
    CCScene *scene = CCScene::create();
    
    scene->addChild(touchGestion, 19);

    /// Création des calques de la scene.
	BackgroundLayer *backgroundLayer = BackgroundLayer::create();
	Couche1Layer *_c1Layer = Couche1Layer::create();
	Couche2Layer *c2Layer = Couche2Layer::create();

	/// Création du label avec le nom de la scene en bas à gauche pour le débogage.
	CCLayer *debugLayer = CCLayer::create();
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCLabelTTF* labelName = CCLabelTTF::create("GameScene", "Arial", 30 * ((int)winSize.height/720.0f));
	labelName->setPosition(ccp(winSize.width*0.9f, winSize.height*0.05f));
	debugLayer->addChild(labelName);

    /// Ajout des calque à la scene.
	scene->addChild(backgroundLayer, 0);
	scene->addChild(_c1Layer, 1);
	//scene->addChild(c2Layer, 2);
    scene->addChild(debugLayer, 20);

   	/// On ajoute le listener sur le click
   	/*CCTapGestureRecognizer *tap = CCTapGestureRecognizer::create();
   	tap->setTarget(touchGestion, callfuncO_selector(MobileTouchGestion::didSingleTap));
   	tap->setNumberOfTapsRequired(1);
   	tap->setCancelsTouchesInView(false);
   	scene->addChild(tap);

   	/// Sur le double click
   	CCTapGestureRecognizer *tapDouble = CCTapGestureRecognizer::create();
   	tapDouble->setTarget(touchGestion, callfuncO_selector(MobileTouchGestion::didDoubleTap));
   	tapDouble->setNumberOfTapsRequired(2);
   	tapDouble->setCancelsTouchesInView(false);
   	scene->addChild(tapDouble);*/

   	/// Et sur le swipe
   	MyCCSwipeGestureRecognizer * swipe = MyCCSwipeGestureRecognizer::create();
   	swipe->setTarget(touchGestion, callfuncO_selector(MobileTouchGestion::didSwipe));
   	swipe->setDirection(kSwipeGestureRecognizerDirectionDown | kSwipeGestureRecognizerDirectionUp |
   						kSwipeGestureRecognizerDirectionLeft | kSwipeGestureRecognizerDirectionRight);
   	swipe->setCancelsTouchesInView(false);
   	scene->addChild(swipe);

    /// Retourne la scene.
    return scene;
}

CCSwipe* GestionScene::currentSwipe()
{
	return _currentSwipe;
}

void GestionScene::setCurrentSwipe(CCSwipe *swipe)
{
	if (_currentSwipe)
		_currentSwipe->release();

	_currentSwipe = swipe;
}

void GestionScene::setYAcceleration(float yAcc)
{
	_yAcc = yAcc;
}

void GestionScene::setXAcceleration(float xAcc)
{
	_xAcc = xAcc;
}

float GestionScene::getYAcceleration()
{
	return _yAcc;
}

float GestionScene::getXAcceleration()
{
	return _xAcc;
}


CCScene* GestionScene::createPauseScene()
{
    /// Création de la scene.
    CCScene *scene = CCScene::create();
    
    /// Création des calques de la scene.
	PauseLayer *pauseLayer = PauseLayer::create();

	/// Création du label avec le nom de la scene en bas à gauche pour le débogage.
	CCLayer *debugLayer = CCLayer::create();
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCLabelTTF* labelName = CCLabelTTF::create("PauseScene", "Arial", 30 * ((int)winSize.height/720.0f));
	labelName->setPosition(ccp(winSize.width*0.9f, winSize.height*0.05f));
	debugLayer->addChild(labelName);

    /// Ajout des calque à la scene.
	scene->addChild(pauseLayer, 0);
    scene->addChild(debugLayer, 20);

    /// Retourne la scene.
    return scene;
}

CCScene* GestionScene::createEndGameScene()
{
    /// Création de la scene.
    CCScene *scene = CCScene::create();
    
    /// Création des calques de la scene.
	EndGameLayer *endGameLayer = EndGameLayer::create();

	/// Création du label avec le nom de la scene en bas à gauche pour le débogage.
	CCLayer *debugLayer = CCLayer::create();
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCLabelTTF* labelName = CCLabelTTF::create("EndGameScene", "Arial", 30 * ((int)winSize.height/720.0f));
	labelName->setPosition(ccp(winSize.width*0.9f, winSize.height*0.05f));
	debugLayer->addChild(labelName);

    /// Ajout des calque à la scene.
	scene->addChild(endGameLayer, 0);
    scene->addChild(debugLayer, 20);

    /// Retourne la scene.
    return scene;
}

CCScene* GestionScene::createCloseScene()
{
    /// Création de la scene.
    CCScene *scene = CCScene::create();
    
    /// Création des calques de la scene.
	CloseLayer *closeLayer = CloseLayer::create();

	/// Création du label avec le nom de la scene en bas à gauche pour le débogage.
	CCLayer *debugLayer = CCLayer::create();
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCLabelTTF* labelName = CCLabelTTF::create("CloseScene", "Arial", 30 * ((int)winSize.height/720.0f));
	labelName->setPosition(ccp(winSize.width*0.9f, winSize.height*0.05f));
	debugLayer->addChild(labelName);

    /// Ajout des calque à la scene.
	scene->addChild(closeLayer, 0);
    scene->addChild(debugLayer, 20);

    /// Retourne la scene.
    return scene;
}

void GestionScene::changeScene(int action)
{
	_indexScene += action;
	CCTransitionScene *newScene;

	switch (_indexScene)
	{
	case 1:
		_currentScene = createIntroScene();
		newScene = CCTransitionFade::create(0.0f, _currentScene);
		break;
	case 2:
		_currentScene = createMenuScene();
		newScene = CCTransitionFade::create(0.0f, _currentScene);
		break;
	case 3:
		_currentScene = createGameScene();
		newScene = CCTransitionFade::create(0.0f, _currentScene);
		break;
	case 4:
		_currentScene = createPauseScene();
		newScene = CCTransitionFade::create(0.0f, _currentScene);
		break;
	case 5:
		_currentScene = createEndGameScene();
		newScene = CCTransitionFade::create(0.0f, _currentScene);
		break;
	case 6:
		_currentScene = createCloseScene();
		newScene = CCTransitionFade::create(0.0f, _currentScene);
		break;
	default:
		/// On quitte
		CCDirector::sharedDirector()->end();
		return;
	}

	CCDirector::sharedDirector()->replaceScene(newScene);
}
