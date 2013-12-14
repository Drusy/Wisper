#ifndef __BackgroundLayer_H__
#define __BackgroundLayer_H__

#include "cocos2d.h"

USING_NS_CC;

class AppDelegate;

class BackgroundLayer : public cocos2d::CCLayer
{
private:
	/// Rapport de la taille de l'�cran par rapport � celle des sprites.
	float _scale;
	/// Label �crivant le nom de la scene.
	CCLabelTTF *_labelName;
	CCParallaxNode *_backgroundNode;  
	CCSprite *_galaxy;
	CCSprite *_spacedust1;
	CCSprite *_spacedust2;

public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // implement the "static node()" method manually
    CREATE_FUNC(BackgroundLayer);
    
    /// M�thode appel� � la fin de la s�quence.
    void endScene();
	void update(float dt);
};

#endif // __BackgroundLayer_H__
