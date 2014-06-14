#ifndef __GestionScene_H__
#define __GestionScene_H__

#include "cocos2d.h"
#include "View/SceneGame/couche1Layer.h"
#include "View/CustomClasses/CCGestureRecognizer/MyCCSwipeGestureRecognizer.h"

USING_NS_CC;

class GestionScene
{    
private:
	/// Scene courante.
	CCScene *_currentScene;
	int _indexScene;

	static GestionScene *_Instance;
	Couche1Layer *_c1Layer;

	GestionScene();
	float _yAcc;
	float _xAcc;
	CCSwipe *_currentSwipe;

public:
	static GestionScene* GetInstance();

	CCSwipe* currentSwipe();
	void setCurrentSwipe(CCSwipe *swipe);

	void setYAcceleration(float yAcc);
	float getYAcceleration();

	void setXAcceleration(float xAcc);
	float getXAcceleration();

	CCScene* createIntroScene();

	CCScene* createMenuScene();
	
	CCScene* createGameScene();
	
	CCScene* createPauseScene();
	
	CCScene* createEndGameScene();

	CCScene* createCloseScene();

	void changeScene(int action = 1);
};

#endif // __GestionScene_H__
