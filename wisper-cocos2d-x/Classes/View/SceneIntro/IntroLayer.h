#ifndef __IntroLayer_H__
#define __IntroLayer_H__

#include "cocos2d.h"

USING_NS_CC;

class AppDelegate;

class IntroLayer : public cocos2d::CCLayer
{
private:
	/// Rapport de la taille de l'écran par rapport à celle des sprites.
	float _scale;
	/// Label écrivant le nom de la scene.
	CCLabelTTF *_labelName;

public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // implement the "static node()" method manually
    CREATE_FUNC(IntroLayer);
    
    /// Méthode appelé à la fin de la séquence.
    void endScene();
};

#endif // __IntroLayer_H__
